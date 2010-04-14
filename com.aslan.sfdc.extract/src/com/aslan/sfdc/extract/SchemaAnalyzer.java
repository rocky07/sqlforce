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

import com.aslan.sfdc.partner.LoginManager;
import com.sforce.soap.partner.DescribeGlobalResult;
import com.sforce.soap.partner.DescribeGlobalSObjectResult;
import com.sforce.soap.partner.DescribeSObjectResult;
import com.sforce.soap.partner.Field;

/**
 * Explore a Salesforce schema to find stuff like sobject dependencies.
 * @author greg
 *
 */
public class SchemaAnalyzer {

	public class Table {
		private DescribeSObjectResult describeResult;
		private List<Table> tableReferences = new ArrayList<Table>();
		
		Table( DescribeSObjectResult describeResult ) {
			this.describeResult = describeResult;
		}
		
		private void addTableReference( Table ref ) {
			if( !tableReferences.contains(ref)) {
				tableReferences.add(ref);
			}
		}
		
		public List<Table> getTableReferences() {
			return tableReferences;
		}
		
		public String getName() {
			return describeResult.getName();
		}
		
		public Field[] getFields() {
			return describeResult.getFields();
		}
	}
	
	private LoginManager.Session session;
	private boolean allTablesLoaded = false;
	private Map<String, Table> allTables = new HashMap<String, Table >();
	
	public SchemaAnalyzer( LoginManager.Session session ) {
		this.session = session;
	}
	
	private void loadAllTables() throws Exception {
		if( allTablesLoaded ) { return; }
		DescribeGlobalResult describeGlobalResult = session.getBinding().describeGlobal();
		
		DescribeGlobalSObjectResult resultList[] = describeGlobalResult.getSobjects();
		for( DescribeGlobalSObjectResult result : resultList ) {
			String name = result.getName();
			getTable( name );
		}
		allTablesLoaded = true;
	}

	private void analyze( Table table ) throws Exception {
		//
		// Find all tables which this table references.
		//
	
		for( Field field : table.getFields()) {
			String fieldTypeName = field.getType().getValue();
			
			if( IDatabaseBuilder.FIELDTYPE_REFERENCE.equals(fieldTypeName)) {
				for( String name : field.getReferenceTo() ) {
					Table refTable = getTable( name );
					table.addTableReference( refTable );
				}
			}
			
		}
	}
	
	public Table getTable( String name ) throws Exception {
		if( !allTables.containsKey(name.toUpperCase()) ) {
			DescribeSObjectResult describeSObjectResult = session.getBinding().describeSObject(name);
			if( null != describeSObjectResult) {
				Table table = new Table(describeSObjectResult );
				allTables.put( name.toUpperCase(), table );
				analyze(table);
			} else {
				throw new Exception("Salesforce object " + name + " was not found");
			}
		}
		
		return allTables.get(name.toUpperCase());
	}
	
	
	public List<Table> getTables() throws Exception {
		loadAllTables();
		
		return new ArrayList<Table>( allTables.values());
	}
}
