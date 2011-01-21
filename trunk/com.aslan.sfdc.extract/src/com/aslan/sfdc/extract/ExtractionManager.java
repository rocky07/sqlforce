/*******************************************************************************
 * Copyright (c) 2010 Gregory Smith (gsmithfarmer@gmail.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/

package com.aslan.sfdc.extract;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

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
		private Field lastModifiedDateField;
		
		TableRuleInstance( ExtractionRuleset ruleSet, TableRule tableRule, String tableName, boolean byReference ) {
			this.ruleSet = ruleSet;
			this.tableRule = tableRule;
			this.tableName = tableName;
			this.byReference = byReference;
			
			try {
				lastModifiedDateField = session.getLastModifiedField(tableName);
			} catch (Exception e) {
				lastModifiedDateField = null;
			}
		}
		
		@SuppressWarnings("unused")
		public ExtractionRuleset getRuleSet() { return ruleSet; }
		public TableRule getTableRule() { return tableRule; }
		public String getTableName() { return tableName; }
		@SuppressWarnings("unused")
		public boolean isByReference() { return byReference; }
		public Field getLastModifiedDateField() { return lastModifiedDateField; }
		
		
	}
	
	/**
	 * SAX Parser handler for picking up local configuration data.
	 * 
	 */
	private class ConfigHandler extends DefaultHandler
	{
		private boolean isAdminUser;
		
		ConfigHandler( boolean isAdminUser) {
			this.isAdminUser = isAdminUser;
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			
			if( "NoDataExport".equals(qName)) {
				String name = attributes.getValue("name");
				String adminOK = attributes.getValue("adminOK");
				boolean canAdminExport = (isAdminUser && (null!=adminOK && "true".equals(adminOK)));
				if( !canAdminExport ) {
					noExportTables.add(name.toUpperCase());
				}
			}
			
		}
		
		
	}
	private LoginManager.Session session;
	private IDatabaseBuilder builder;
	private List<String> allTableNames = new ArrayList<String>();
	private SchemaAnalyzer schemaAnalyzer;
	private int maxBytesToBuffer = 1*(1024*1024);
	private int maxRowsToBuffer = 100;
	private String copyRecordsSince = null;
	
	
	private Map<String,TableDescriptor> tableNameMap = new HashMap<String,TableDescriptor>();
	private Set<String> noExportTables = new HashSet<String>();
	
	public ExtractionManager( LoginManager.Session session, IDatabaseBuilder builder ) throws Exception {
		
		//
		// Load configuration data
		//
		
		InputStream inStream = null;
		try {
			inStream = ExtractionManager.class.getResourceAsStream("ExtractionManagerConfig.xml");
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			ConfigHandler handler = new ConfigHandler(session.isAdministrator());
			parser.parse(inStream, handler );
			
		} finally {
			if( null != inStream ) {
				inStream.close();
			}
		}
		
		//
		// Start the process
		//
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
	 * Set the maximum of rows to buffer before writing out rows to the destination.
	 * 
	 * @param nRows number of rows to buffer.
	 */
	public void setMaxRowsToBuffer( int nRows ) {
		maxRowsToBuffer = nRows;
	}
	
	/**
	 * Set an SFDC expression that will limit the records retrieved from salesforce based on a datetime comparison.
	 * 
	 * The value MUST be recognized as a datetime by SOQL. Examples:
	 * <ul>
	 * <li>yesterday
	 * <li>last_quarter
	 * <li>last_week
	 * <li>this_quarter
	 * <li>2010-12-31T00:00:15.000Z
	 * </ul>
	 * 
	 * @param copySince grab records modified after this datetime.
	 */
	public void setCopyRecordsSince( String copySince ) {
		copyRecordsSince = copySince;
		if(null!=copyRecordsSince && 0==copyRecordsSince.trim().length()) {
			copyRecordsSince = null;
		}
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
	private void extractData(final TableRuleInstance rule, final IExtractionMonitor monitor ) throws Exception {
		

		final String sObjectName = rule.getTableName();
		final TableDescriptor tableDesc = getTableDescriptor( sObjectName );

		
		if( noExportTables.contains(sObjectName.toUpperCase()) || !tableDesc.describeResult.isQueryable()) {
			monitor.startCopyData(sObjectName);
			monitor.endCopyData( sObjectName, 0, 0, 0 );
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
		
		StringBuffer sqlWHERE = new StringBuffer();
		Field dateField = rule.getLastModifiedDateField();
		if( null!=dateField && null != copyRecordsSince ) {
			sqlWHERE.append(dateField.getName()+ " >= " + copyRecordsSince);
			
		}
		if( null != rule.getTableRule().getPredicate()) {
			if( 0 != sqlWHERE.length()) { sqlWHERE.append(" AND "); }
			sqlWHERE.append("(" + rule.getTableRule().getPredicate() + ")");
		}
		
		if( 0 != sqlWHERE.length() ) {
			sql.append( " WHERE " + sqlWHERE.toString());
		}
		
		class MyCallback extends DefaultSObjectQuery2Callback {
			List<String[]> rowBuffer = new ArrayList<String[]>();
			
			int nWritten = 0;
			int nBytesPending = 0;
			int nRead = 0;
			int nSkipped = 0;
			
			private void flush() {
				if( 0 == rowBuffer.size()) { return; }
				int skippedThisTime = 0;
				int writtenThisTime = 0;
				try {
					skippedThisTime = builder.insertData( tableDesc.describeResult, rule.getLastModifiedDateField(), fields, rowBuffer);
					writtenThisTime = rowBuffer.size() - skippedThisTime;
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				rowBuffer.clear();
				nBytesPending = 0;
				
				if( writtenThisTime > 0 ) {
					nWritten += writtenThisTime;
					monitor.copyData( sObjectName, nWritten);
				}
				
				if( skippedThisTime > 0 ) {
					nSkipped += skippedThisTime;
					monitor.skipData( sObjectName, nSkipped );
				}
				System.gc();
			}
			@Override
			public void addRow(int rowNumber, String[] data) {
				nRead++;
				rowBuffer.add(data);
				for( String s : data ) {
					if( null != s ) {
						nBytesPending += s.length();
					}
				}
				
				monitor.readData( sObjectName, nRead );

				if( (nBytesPending >= maxBytesToBuffer) || monitor.isCancel() || rowBuffer.size() >= maxRowsToBuffer) {
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
		
		(new SObjectQueryHelper()).findRows( session, sql.toString(), callback );
		
		monitor.endCopyData( sObjectName, callback.nRead, callback.nSkipped, callback.nWritten);
		System.gc();
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
		monitor.startTables();
		monitor.reportMessage("Calculating the order in which data will be extracted");
		List<TableRuleInstance> tables = calculateExtractionList(ruleSet, monitor );
		
		for( TableRuleInstance table : tables ) {
			extractData( table, monitor );
			if( monitor.isCancel()) { break; }
		}
		monitor.endTables();
		monitor.reportMessage("Finished copying data" + (monitor.isCancel()?": CANCELLED":""));
	}
	/**
	 * Extract the schema for a single object from Salesforce.
	 * 
	 * @param sObjectName name of the object to extract
	 * @param monitor report progress as the schema extraction happens.
	 * @throws Exception if anything goes wrong.
	 */
	private void extractSchema(String sObjectName, IExtractionMonitor monitor ) throws Exception {
		
		TableDescriptor tableDesc = getTableDescriptor( sObjectName );
		if( builder.isTableNew( tableDesc.describeResult)) {
			builder.createTable( tableDesc.describeResult);
			monitor.createTable(sObjectName);
			Thread.yield();
		} else {
			monitor.skipTable(sObjectName);
			Thread.yield();
		}
		System.gc();
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
		
		monitor.startSchema();
		monitor.reportMessage("Calculating the order in which schema will be extracted"); 
		List<TableRuleInstance> tables = calculateExtractionList(ruleSet, monitor );
		
		monitor.reportMessage("Start Copy of Schema");
		for( TableRuleInstance rule : tables ) {
			extractSchema( rule.getTableName(), monitor );
			if( monitor.isCancel()) { break; }
		}
		monitor.endSchema();
		monitor.reportMessage("Finished copying schema" + (monitor.isCancel()?": CANCELLED":"")); 
		System.gc();
	}
}
