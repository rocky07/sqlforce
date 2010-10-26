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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aslan.sfdc.extract.ExtractionRuleset.TableRule;
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
	
	private class TableDescriptor {
		private DescribeSObjectResult describeResult;
		
		TableDescriptor( DescribeSObjectResult describeResult ) {
			this.describeResult = describeResult;
		}
	}
	
	private class TableRuleInstance {
		private ExtractionRuleset ruleSet;
		private TableRule tableRule;
		private String tableName;
		private boolean byReference;
		
		TableRuleInstance( ExtractionRuleset ruleSet, TableRule tableRule, String tableName, boolean byReference ) {
			this.ruleSet = ruleSet;
			this.tableRule = tableRule;
			this.tableName = tableName;
			this.byReference = byReference;
		}
		
		public ExtractionRuleset getRuleSet() { return ruleSet; }
		public TableRule getTableRule() { return tableRule; }
		public String getTableName() { return tableName; }
		public boolean isByReference() { return byReference; }
		
		
	}
	private LoginManager.Session session;
	private IDatabaseBuilder builder;
	private List<String> allTableNames = new ArrayList<String>();
	private SchemaAnalyzer schemaAnalyzer;
	private boolean reportRowExtraction = true;
	private int maxBytesToBuffer = 1*(1024*1024);
	
	
	private Map<String,TableDescriptor> tableNameMap = new HashMap<String,TableDescriptor>();
	private Set<String> noExportTables = new HashSet<String>();
	
	public ExtractionManager( LoginManager.Session session, IDatabaseBuilder builder ) throws Exception {
		
		for( String table : new String[] {"UserProfileFeed", "Vote", "EntitySubscription"} ) {
			noExportTables.add(table.toUpperCase());
		}
		this.session = session;
		this.builder = builder;
		this.schemaAnalyzer = new SchemaAnalyzer( session );
		
	}
	
	/**
	 * Set the maximum number of bytes that will be cached by the system until flushing rows to
	 * the destination database.
	 * 
	 * @param nBytes number of bytes to buffer.
	 */
	public void setMaxBytesToBuffer( int nBytes ) {
		maxBytesToBuffer = nBytes;
	}

	/**
	 * Return a list of all defined tables in the Salesforce.
	 * 
	 * @return all defined tables.
	 * @throws Exception if the connection to salesforce fails.
	 */
	private List<String> getAllTableNames() throws Exception {
		if( 0 == allTableNames.size()) {
			DescribeGlobalResult describeGlobalResult = session.getBinding().describeGlobal();
			
			DescribeGlobalSObjectResult resultList[] = describeGlobalResult.getSobjects();
			for( DescribeGlobalSObjectResult result : resultList ) {
				allTableNames.add( result.getName());

			}
		}
		
		return allTableNames;
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
	 * Build a list of all tables referenced by the specific table -- down to the roots.
	 * 
	 * @param rootTable start looking at this table.
	 * @param refList list of all tables referenced.
	 * @param excludedTables table to ignore.
	 */
	private void calculateTableReferences( SchemaAnalyzer.Table rootTable, List<SchemaAnalyzer.Table> refList, Set<String> excludedTables ) {
		
		for( SchemaAnalyzer.Table refTable : rootTable.getTableReferences()) {
			if( refList.contains(refTable)) { continue; }
			if( excludedTables.contains( refTable.getName())) { continue; }
			
			refList.add( 0, refTable );
			calculateTableReferences( refTable, refList, excludedTables );
			refList.remove(refTable);
			refList.add( refTable);
			
		}
	}
	/**
	 * Determine the order in which tables should be extracted from Salesforce.
	 * 
	 * @param ruleSet rules that govern what should be extracted.
	 * @param monitor progress reporter.
	 * @return order list of tables.
	 * @throws Exception if salesforce fails.
	 */
	

	private List<TableRuleInstance> calculateExtractionList(ExtractionRuleset ruleSet, IExtractionMonitor monitor ) throws Exception {
		List<String> tableNames = getAllTableNames();
		Set<String> extractedTables = new HashSet<String>();
		List<TableRuleInstance> tableList = new ArrayList<TableRuleInstance>();
		Set<String> excludedTables = new HashSet<String>();
		
		for( String name : tableNames ) {
			if( ruleSet.isTableExcluded(name)) {
				excludedTables.add(name);
			}
		}
		
		for( TableRule rule : ruleSet.getIncludedTableRules()) {
			for( String name : tableNames ) {
				if( extractedTables.contains(name)) { continue; }
				if( excludedTables.contains(name)) { continue; }
				if( !rule.isMatch(name)) { continue; }
				
				if( !rule.isIncludeReferencedTables()) {
					tableList.add(new TableRuleInstance( ruleSet, rule, name, false));
					extractedTables.add(name);
					continue;
				}
				
				if( !rule.isIncludeReferencedTables()) { continue; }
				
				
				SchemaAnalyzer.Table table = schemaAnalyzer.getTable(name);
				List<SchemaAnalyzer.Table> refTables = new ArrayList<SchemaAnalyzer.Table>();
				calculateTableReferences(table, refTables, excludedTables);
				
				for(SchemaAnalyzer.Table refTable : refTables ) {
					if( extractedTables.contains(refTable.getName())) { continue; }
					tableList.add(new TableRuleInstance( ruleSet, rule, refTable.getName(), true));
					extractedTables.add(refTable.getName());
				}
				if( !extractedTables.contains(name)) { // A self referential table may already be included
					tableList.add(new TableRuleInstance( ruleSet, rule, name, false));
					extractedTables.add(name);
				}
			}
		}
		
		return tableList;
	}
	/**
	 * Extract the data for a single object from Salesforce.
	 * 
	 * @param sObjectName name of the object to extract
	 * @param monitor
	 * @throws Exception if anything goes wrong.
	 */
	private void extractData(TableRuleInstance rule, final IExtractionMonitor monitor ) throws Exception {
		

		final String sObjectName = rule.getTableName();
		final TableDescriptor tableDesc = getTableDescriptor( sObjectName );
		
		if( noExportTables.contains(sObjectName.toUpperCase()) || !tableDesc.describeResult.isQueryable()) {
			monitor.startCopyData(sObjectName);
			monitor.endCopyData( sObjectName, 0 );
			return;
		}
		
		final Field fields[] = tableDesc.describeResult.getFields();
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ID"); // ALWAYS make the ID be the first column selected.
		for( int n = 0; n < fields.length; n++ ) {
			if( "id".equalsIgnoreCase(fields[n].getName())) { continue; }
			sql.append( "," + fields[n].getName());
		}
		sql.append( " FROM " + tableDesc.describeResult.getName());
		
		if( null != rule.getTableRule().getPredicate()) {
			sql.append( " WHERE " + rule.getTableRule().getPredicate());
		}
		
		class MyCallback extends DefaultSObjectQuery2Callback {
			List<String[]> rowBuffer = new ArrayList<String[]>();
			
			int nWritten = 0;
			int nBytesPending = 0;
			
			private void flush() {
				if( 0 == rowBuffer.size()) { return; }
				
				try {
					builder.insertData( tableDesc.describeResult, fields, rowBuffer);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				if( reportRowExtraction ) {
					monitor.reportMessage("....Rows " + (1+nWritten) + " to " + (nWritten + rowBuffer.size()));
				}
				nWritten += rowBuffer.size();
				rowBuffer.clear();
				nBytesPending = 0;
				
				monitor.copyData( sObjectName, nWritten);
			}
			@Override
			public void addRow(int rowNumber, String[] data) {
				rowBuffer.add(data);
				for( String s : data ) {
					if( null != s ) {
						nBytesPending += s.length();
					}
				}
				
				monitor.readData( sObjectName, rowBuffer.size() + nWritten );
				if( nBytesPending >= maxBytesToBuffer || monitor.isCancel()) {
					flush();
				}
				
				if( monitor.isCancel()) { 
					cancel();
				}
			}

			@Override
			public void finish() {
				flush();
			}
			
		}
		MyCallback callback = new MyCallback();
		
		monitor.startCopyData(sObjectName);
		monitor.reportMessage("Start Extract Data: " + sObjectName 
				+ (null==rule.getTableRule().getPredicate()?
						" (All Rows)"
						:(" (WHERE " + rule.getTableRule().getPredicate() + ")")));
		
		(new SObjectQueryHelper()).findRows( session, sql.toString(), callback );
		
		monitor.endCopyData( sObjectName, callback.nWritten);
		monitor.reportMessage("End Extract Data: " + sObjectName + ", " + callback.nWritten + " rows extracted");
	}
	
	/**
	 * Extract specified data from a salesforce database.
	 * 
	 * @param ruleSet rules that determine what data will be extracted.
	 * @param monitor callback for reporting progress.
	 * 
	 * @throws Exception if anything goes wrong.
	 */
	public void extractData(ExtractionRuleset ruleSet,  IExtractionMonitor monitor ) throws Exception {
		monitor.reportMessage("Calculating the order in which data will be extracted");
		List<TableRuleInstance> tables = calculateExtractionList(ruleSet, monitor );
		
		for( TableRuleInstance table : tables ) {
			extractData( table, monitor );
			if( monitor.isCancel()) { break; }
		}
		monitor.reportMessage("Finished extracting data" + (monitor.isCancel()?": CANCELLED":""));
	}
	/**
	 * Extract the schema for a single object from Salesforce.
	 * 
	 * @param sObjectName name of the object to extract
	 * @param monitor TODO
	 * @throws Exception if anything goes wrong.
	 */
	private void extractSchema(String sObjectName, IExtractionMonitor monitor ) throws Exception {
		
		TableDescriptor tableDesc = getTableDescriptor( sObjectName );
		if( builder.isTableNew( tableDesc.describeResult)) {
			builder.createTable( tableDesc.describeResult);
			monitor.createTable(sObjectName);
			Thread.yield();
		}
	}
	
	/**
	 * Extract the specified schema from Salesforce.
	 * 
	 * @param ruleSet rules that determine what schema objects will be extracted.
	 * @param monitor callback for reporting progress.
	 * 
	 * @throws Exception if anything goes wrong.
	 */
	public void extractSchema(ExtractionRuleset ruleSet, IExtractionMonitor monitor) throws Exception {
		
		monitor.reportMessage("Calculating the order in which schema will be extracted"); 
		List<TableRuleInstance> tables = calculateExtractionList(ruleSet, monitor );
		
		for( TableRuleInstance rule : tables ) {
			extractSchema( rule.getTableName(), monitor );
			if( monitor.isCancel()) { break; }
		}
		monitor.reportMessage("Finished extracting schema" + (monitor.isCancel()?": CANCELLED":"")); 
		
	}
}
