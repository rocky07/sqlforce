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
package com.aslan.sfdc.sqlforce.command;

import java.util.ArrayList;
import java.util.List;

import org.apache.axis.message.MessageElement;
import org.w3c.dom.Element;

import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.partner.SObjectQueryHelper;
import com.aslan.sfdc.sqlforce.AbstractSQLForceCommand;
import com.aslan.sfdc.sqlforce.LexicalAnalyzer;
import com.aslan.sfdc.sqlforce.LexicalToken;
import com.aslan.sfdc.sqlforce.SQLForceEnvironment;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.sobject.SObject;

/**
 * SQLForce INSERT command -- simulate an ANSI/SQL INSERT command.
 * 
 * Syntax:
 * <pre>
 * INSERT INTO tableName (col1 [,col2]*) VALUES (v1 [,v2]*) [,(v3[,v4]*)]*;
 * INSERT INTO tableName (col1, [,col2]*) SELECT v1 [,v2]* FROM ...;
 * 
 * </pre>
 * @author snort
 *
 */
public class Insert extends AbstractSQLForceCommand {


	private static final String EOL = ";";
	private static int CREATE_CHUNK_SIZE = 200;

	
	@Override
	public String getOneLineHelp() {
		
		return "INSERT INTO tableName (col1 [,col2]*) VALUES (v1 [,v2]*) [,(v3[,v4]*)]*;"
				+ "\nINSERT INTO tableName (col1 [,col2]*) SELECT v1 [,v2]* FROM ...;";
	}

	@Override
	public String getHelp() {
		return getHelp( Insert.class, "Insert.help");
	}

	private MessageElement newMessageElement(String name, Object value)
	throws Exception {

		MessageElement me = new MessageElement("", name); // , value);
		me.setObjectValue(value);
		Element e = me.getAsDOM();
		e.removeAttribute("xsi:type");
		e.removeAttribute("xmlns:ns1");
		e.removeAttribute("xmlns:xsd");
		e.removeAttribute("xmlns:xsi");

		me = new MessageElement(e);
		return me;
	}
	
	private SObject makeSObject( String table, List<String> columns, List<String> values ) throws Exception {
		List<MessageElement> elementList = new ArrayList<MessageElement>();
		List<String> fieldsToNull = new ArrayList<String>();
		
		for( int n = 0; n < columns.size(); n++ ) {
			String value = values.get(n);
			if( null != value ) {
				elementList.add( newMessageElement(columns.get(n), value));
			}
		}
		
		
		return new SObject(
				table,
				fieldsToNull.toArray(new String[0]),
				(String) null, // id
				elementList.toArray( new MessageElement[0])
				);
	}
	
	/**
	 * Return values from an INSERT statement immediately after the VALUES keyword has been read.
	 * 
	 * @param lex grab tokens from this source.
	 * @param nValuesPerRow number of values required for each row.
	 * @return array of values found.
	 * @throws Exception if anything goes wrong.
	 */
	private IRowCache readFromValues(LexicalAnalyzer lex, SQLForceEnvironment env, int nValuesPerRow ) throws Exception {
		boolean firstRow = true;
		MemoryRowCache cache = new MemoryRowCache();
		
		while( true ) {
			if( !firstRow ) { // After the first row of values either a , or ; must appear.
				LexicalToken valuesToken = lex.getToken( true );
				
				if( null == valuesToken  ) { break; } // Read all values - end of file.
				if( ";".equals( valuesToken.getValue())) { break; } // Read all values
				if( ! ",".equalsIgnoreCase(valuesToken.getValue())) {
					throw new Exception("Expected ','  but found '" + valuesToken.getValue() + "'");
				}
				
			} else {
				firstRow = false;
			}
			
			List<String> values = Parser.parseSQLParenData(lex, env);
			
			if( values.size() != nValuesPerRow) {
				throw new Exception("Expected " + nValuesPerRow + " values but " + values.size() + " are specified in value set " + (cache.getNumColumns()));
			}
			
			cache.addRow( values );
		}
		
		return cache;
	}
	
	/**
	 * Return values from an INSERT statement immediately after the SELECT keyword has been read.
	 * 
	 * @param lex grab tokens from this source.
	 * @param nValuesPerRow number of values required for each row.
	 * @return array of values found.
	 * @throws Exception if anything goes wrong.
	 */
	private IRowCache readFromSelect(LexicalAnalyzer lex, SQLForceEnvironment env, int nValuesPerRow ) throws Exception {
		DiskRowCache cache = new DiskRowCache();
		
		String sql = "SELECT " + readSQL(env, lex);
		
		env.checkSession();
		LoginManager.Session session = env.getSession();
		
		List<String[]> rows = null;
		try {
			rows = (new SObjectQueryHelper()).runQuery2(session, sql);
		} catch( Exception e ) {
			throw new Exception( e.toString());
		}

		if( rows.size() > 0 ) {
			if( rows.get(0).length != nValuesPerRow ) {
				throw new Exception("SELECT statement returned " + rows.get(0).length + " value per row but INSERT requires " + nValuesPerRow );
			}
			
			for( String rowArray[] : rows ) {
				List<String> thisRow = new ArrayList<String>(nValuesPerRow);
				for( String v : rowArray ) {
					thisRow.add(v);
				}
				cache.addRow(thisRow);
			}
		}
		
		return cache;
	}
	public void execute(LexicalToken token, LexicalAnalyzer lex,
			SQLForceEnvironment env) throws Exception {
		
		List<String> columnNames = new ArrayList<String>();
		IRowCache allValues;
		String table;
		
		try {
			lex.getKeyword(true, "INTO");
			table = lex.getToken(true, LexicalToken.Type.IDENTIFIER).getValue();
			lex.getToken(true, "(");

			//
			// Grab the name of each column to insert
			//
			while( true ) {
				LexicalToken columnToken = lex.getToken(true);

				if( LexicalToken.Type.IDENTIFIER != columnToken.getType()) {
					throw new Exception("Expected a table column name but found '" + columnToken.getValue() + "'");
				}

				columnNames.add( columnToken.getValue());
				
				LexicalToken delim = lex.getToken(true );
				if( ")".equals( delim.getValue())) { break; } // End of column names
				if( !",".equals( delim.getValue())) {
					throw new Exception("Expected a comma after " + columnToken.getValue() + " but found " + delim.getValue());
				}
			}
			
			//
			// Determine if this is a INSERT VALUES or INSERT SELECT
			// and grab the values to insert.
			//
			LexicalToken typeToken = lex.getToken(true, LexicalToken.Type.IDENTIFIER);
			
			if( "VALUES".equalsIgnoreCase(typeToken.getValue())) {
				allValues = readFromValues( lex, env, columnNames.size());
			} else if( "SELECT".equalsIgnoreCase(typeToken.getValue())) {
				allValues = readFromSelect( lex, env, columnNames.size());
			} else {
				throw new Exception("Expected VALUES or SELECT but found '" + typeToken.getValue() + "'" );
			}
			
		} finally {
			lex.consume(EOL);
		}
		
		//
		// At this point we have all of the data we want to insert.
		// The next step is to convert it into the form required by the SFDC interface.
		//
		
		env.checkSession();
		LoginManager.Session session = env.getSession();
		
		List<SObject> sObjList = new ArrayList<SObject>();
		int nInserted = 0;
		int nErrors = 0;
		int reportInterval = CREATE_CHUNK_SIZE;
		List<String> nextRow;
		List<String> insertedIds = new ArrayList<String>();
		
		while( null != (nextRow=allValues.nextRow())) {
			sObjList.clear();
			do {
				SObject sObj = makeSObject( table, columnNames, nextRow );
				sObjList.add( sObj );
			} while( sObjList.size()<CREATE_CHUNK_SIZE && null != (nextRow=allValues.nextRow()));
			
			
			SObject sObjectBatch[] = sObjList.toArray( new SObject[0] );
			
			SaveResult batchResults[];
			try {
				batchResults = session.getBinding().create( sObjectBatch );
			} catch( Exception e ) {
				throw new Exception( e.toString());
			}
			
			for( SaveResult rr : batchResults ) {
				
				
				if( rr.isSuccess()) {
					insertedIds.add( rr.getId());
					
					nInserted++;
					continue; 
				} else {
					insertedIds.add( null );
					nErrors++;
					env.logError( rr.getErrors()[0].getMessage());
				}
			}
			
			env.logInfo("Inserted Records " + Math.max(1,(1 + nInserted-reportInterval)) + " thru " + nInserted);
		}
		
		env.setInsertIds(insertedIds);
		env.setSQLRowResultCounts(nInserted, nErrors);
		env.log("Finished: Inserted " + nInserted + " rows");
	}

	
}
