/*******************************************************************************
 * Copyright (c) 2010 Gregory Smith (gsmithfarmer@gmail.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/

package com.aslan.sfdc.extract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aslan.sfdc.partner.DefaultSObjectQuery2Callback;
import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.partner.SObjectQueryHelper;
import com.sforce.soap.partner.DescribeGlobalResult;
import com.sforce.soap.partner.DescribeGlobalSObjectResult;
import com.sforce.soap.partner.DescribeSObjectResult;
import com.sforce.soap.partner.Field;

/**
 * Extract everything from a Salesforce instance into a database.
 * 
 * @author greg
 *
 */
public class ExtractionManager {

	private LoginManager.Session session;
	private IDatabaseBuilder builder;
	
	private class TableDescriptor {
		private DescribeSObjectResult describeResult;
		
		TableDescriptor( DescribeSObjectResult describeResult ) {
			this.describeResult = describeResult;
		}
	}
	
	private Map<String,TableDescriptor> tableNameMap = new HashMap<String,TableDescriptor>();
	
	public ExtractionManager( LoginManager.Session session, IDatabaseBuilder builder ) throws Exception {
		this.session = session;
		this.builder = builder;
		
	}
	
	private int getMaxRowsToBuffer( String sObjectName ) {
		if( "Attachment".equalsIgnoreCase(sObjectName)) {
			return 25;
		} else {
			return 500;
		}
	}
	private TableDescriptor getTableDescriptor( String name ) throws Exception {
		if( !tableNameMap.containsKey(name.toUpperCase()) ) {
			DescribeSObjectResult describeSObjectResult = session.getBinding().describeSObject(name);
			if( null != describeSObjectResult) {
				TableDescriptor table = new TableDescriptor(describeSObjectResult );
				tableNameMap.put( name.toUpperCase(), table );
			} else {
				throw new Exception("Salesforce object " + name + " was not found");
			}
		}
		
		return tableNameMap.get(name.toUpperCase());
	}
	
	/**
	 * Extract the data for a single object from Salesforce.
	 * 
	 * @param sObjectName name of the object to extract
	 * @param monitor
	 * @throws Exception if anything goes wrong.
	 */
	public void extractData(String sObjectName, final IExtractionMonitor monitor ) throws Exception {
		
		final TableDescriptor tableDesc = getTableDescriptor( sObjectName );
		if( !tableDesc.describeResult.isQueryable()) {
			monitor.reportMessage("Skip " + sObjectName + ". SELECT Operation not supported by Salesforce");
			return;
		}
		final Field fields[] = tableDesc.describeResult.getFields();
		final int maxRowsToBuffer = getMaxRowsToBuffer(sObjectName);
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ");
		for( int n = 0; n < fields.length; n++ ) {
			sql.append( (n>0?",":"") + fields[n].getName());
		}
		sql.append( " FROM " + tableDesc.describeResult.getName());
		
		DefaultSObjectQuery2Callback callback = new DefaultSObjectQuery2Callback() {
			
			List<String[]> rowBuffer = new ArrayList<String[]>();
			int nWritten = 0;
			
			private void flush() {
				if( 0 == rowBuffer.size()) { return; }
				
				try {
					builder.insertData( tableDesc.describeResult, fields, rowBuffer);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				
				monitor.reportMessage("....Rows " + (1+nWritten) + " to " + (nWritten + rowBuffer.size()));
				nWritten += rowBuffer.size();
				rowBuffer.clear();
			}
			@Override
			public void addRow(int rowNumber, String[] data) {
				rowBuffer.add(data);
				if( rowBuffer.size() >= maxRowsToBuffer ) {
					flush();
				}
			}

			@Override
			public void finish() {
				flush();
			}
			
		};
		
		monitor.reportMessage("Extract Data: " + sObjectName );
		(new SObjectQueryHelper()).findRows( session, sql.toString(), callback );
	}
	
	/**
	 * Extract all data in a salesforce database.
	 * 
	 */
	public void extractData( IExtractionMonitor monitor ) throws Exception {
		DescribeGlobalResult describeGlobalResult = session.getBinding().describeGlobal();
		
		DescribeGlobalSObjectResult resultList[] = describeGlobalResult.getSobjects();
		for( DescribeGlobalSObjectResult result : resultList ) {
			String name = result.getName();
			extractData( name, monitor );

		}
	}
	/**
	 * Extract the schema for a single object from Salesforce.
	 * 
	 * @param sObjectName name of the object to extract
	 * @param monitor TODO
	 * @throws Exception if anything goes wrong.
	 */
	public void extractSchema(String sObjectName, IExtractionMonitor monitor ) throws Exception {
		
		monitor.reportMessage("Extract Schema: " + sObjectName );
		TableDescriptor tableDesc = getTableDescriptor( sObjectName );
		builder.createTable( tableDesc.describeResult);
	}
	
	/**
	 * Extract the schema for all SObjects in Salesforce.
	 * @param monitor TODO
	 * 
	 * @throws Exception if anything goes wrong.
	 */
	public void extractSchema(IExtractionMonitor monitor) throws Exception {
		DescribeGlobalResult describeGlobalResult = session.getBinding().describeGlobal();
		
		DescribeGlobalSObjectResult resultList[] = describeGlobalResult.getSobjects();
		for( DescribeGlobalSObjectResult result : resultList ) {
			String name = result.getName();
			extractSchema( name, monitor );

		}
	}
}
