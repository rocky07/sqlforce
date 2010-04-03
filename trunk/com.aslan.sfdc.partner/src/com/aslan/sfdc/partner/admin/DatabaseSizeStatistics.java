/*******************************************************************************
 * Copyright (c) 2010 Gregory Smith (gsmithfarmer@gmail.com).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Gregory Smith (gsmithfarmer@gmail.com) - initial API and implementation
 ******************************************************************************/
package com.aslan.sfdc.partner.admin;

import java.util.ArrayList;
import java.util.List;

import com.aslan.sfdc.partner.LoginManager;
import com.sforce.soap.partner.DescribeGlobalResult;
import com.sforce.soap.partner.DescribeGlobalSObjectResult;
import com.sforce.soap.partner.DescribeSObjectResult;
import com.sforce.soap.partner.Field;

/**
 * Compute various statistics about a database.
 * 
 * This was originally developed to help Greg produce a RFP for vendors to help
 * migrate the TRPS instance into the PHST instance.
 * 
 * @author gsmithfarmer@gmail.com
 *
 */
public class DatabaseSizeStatistics {

	private LoginManager.Session session;
	
	public class TableStatistics {
		
		private String name;
		private int recordCount = 0;
		private int customFieldCount = 0;
		private DescribeSObjectResult describeSObjectResult;
		
		private TableStatistics( String name ) {
			this.name = name;
		}
		
		public String getName() { return name; }
		public int getRecordCount() { return recordCount; }
		public int getCustomFieldCount() { return customFieldCount; }
		public boolean isCustom() { return describeSObjectResult.isCustom();
		
		}
		public DescribeSObjectResult getDescribeSObjectResult() { return describeSObjectResult; }
		
		
	}
	
	public DatabaseSizeStatistics(LoginManager.Session session ) {
		this.session = session;
	}
	
	/**
	 * Find a bunch of statistics about a table.
	 * 
	 * @param tableName look at this table.
	 * @return statistics about the table.
	 * @throws Exception if anything goes wrong.
	 */
	public TableStatistics getTableStatistics( String tableName ) throws Exception {
		
		TableStatistics stats = new TableStatistics( tableName );
		
		stats.recordCount = new RecordCounter(session).findCount(tableName);
		
		DescribeSObjectResult describeSObjectResult = session.getBinding().describeSObject(tableName);
		
		if( null == describeSObjectResult) {
			throw new Exception("Table not found: " + tableName );
		}
		
		Field fields[] = describeSObjectResult.getFields();
		int nCustomFields = 0;
		for( Field field : fields ) {
			if( field.isCustom()) { nCustomFields++; }
		}
		stats.customFieldCount = nCustomFields;
		stats.describeSObjectResult = describeSObjectResult;
		
		return stats;
	}
	
	/**
	 * Return statistics for all tables in this SFDC instance.
	 * 
	 * @return stats for all tables.
	 * 
	 * @throws Exception if anything goes wrong.
	 */
	public List<TableStatistics> getAllTableStatistics() throws Exception {
		List<TableStatistics> statList = new ArrayList<TableStatistics>();

		DescribeGlobalResult describeGlobalResult = session.getBinding().describeGlobal();
		if( null == describeGlobalResult ) {
			throw new Exception("Failed to retrieve any descriptions from SalesForce");
		}

		DescribeGlobalSObjectResult sList[] = describeGlobalResult.getSobjects();
		for( DescribeGlobalSObjectResult result : sList ) {
			String name = result.getName();
			try {
				TableStatistics stat = getTableStatistics(name);
				statList.add(stat);
			} catch( Exception e ) {
				;
			}
		}
//		String nameList[] = describeGlobalResult.getTypes();
//		if( null == nameList ) {
//			throw new Exception("Failed to retrieve any types from SalesForce");
//		}
//
//		for (String name : nameList) {
//			//DescribeSObjectResult describeSObjectResult = session.getPort().describeSObject(name);
//			
//			try {
//				TableStatistics stat = getTableStatistics(name);
//				statList.add(stat);
//			} catch( Exception e ) {
//				;
//			}
//		}


		return statList;
	}
}
