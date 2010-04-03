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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.axis.message.MessageElement;
import org.w3c.dom.Element;

import com.aslan.sfdc.partner.DefaultSObjectQueryCallback;
import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.partner.SObjectQueryHelper;
import com.aslan.sfdc.sqlforce.AbstractSQLForceCommand;
import com.aslan.sfdc.sqlforce.LexicalAnalyzer;
import com.aslan.sfdc.sqlforce.LexicalToken;
import com.aslan.sfdc.sqlforce.SQLForceEnvironment;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.sobject.SObject;

/**
 * SQLForce UPDATE command (simulates a regular SQL Update command.
 * 
 * Two forms are UPDATE supported
 *  is:
 * <pre>
 * UPDATE table SET col=value [,col=value]* [WHERE predicate];
 * 
 * UPDATE table(col1 [,col]*) id=(value[,value]*) [,id=(value[,value]*)]*
 * </pre>
 * <p>
 * The first form of UPDATE is the regular ANSI/SQL form. The second form is designed
 * to support efficient updates of many records by using a SQL INSERT type syntax.
 * 
 * @author snort
 *
 */
public class Update extends AbstractSQLForceCommand {

	private final String EOL = ";";
	private static int UPDATE_CHUNK_SIZE = 200;

	//
	// This private class updates records in batches as they
	// are found. It prevents large amounts of memory being required
	// for very large update statements (e.g. those that effect a lot of records).
	//
	class SelectForUpdateCallback extends DefaultSObjectQueryCallback {
		
		private LoginManager.Session session;
		private SQLForceEnvironment env;
		private String tableName;
		private Map<String,String> setVariableMap;
		
		private List<String> pendingIds = new ArrayList<String>();
		private int rowCounter = 0;
		private int nUpdated = 0;
		private int nErrors = 0;
		private int reportInterval = 200;
		
	
		SelectForUpdateCallback( LoginManager.Session session, SQLForceEnvironment env, String tableName, Map<String,String> setVariableMap) {
			this.session = session;
			this.env = env;
			this.tableName = tableName;
			this.setVariableMap = setVariableMap;
		}
		
		private void updatePending() {
			if( 0 == pendingIds.size() ) { return; }
			
			try {
				SObject sObjects[] = new SObject[pendingIds.size()];
				int k = 0;
				for( String id : pendingIds ) {
					sObjects[k] = makeSObject( tableName, id, setVariableMap);
					k++;
				}
				
				SaveResult batchResults[];
				try {
					batchResults = session.getBinding().update( sObjects );
				} catch( Exception e ) {
					throw new Exception( e.toString());
				}
				
				for( SaveResult rr : batchResults ) {
					rowCounter++;
					
					if( rr.isSuccess()) { 
						nUpdated++;
					} else {
						nErrors++;
						env.logError( rr.getErrors()[0].getMessage());
					}
				}
				
				env.logInfo("Updated Records " + Math.max(1,(1 + nUpdated-reportInterval)) + " thru " + nUpdated);

			} catch (Exception e) {
				//
				// Something bad happened with SalesForce. We will not be able to recover.
				//
				throw new Error( e.getMessage());
			}
			pendingIds.clear();
		}
		
		public int getNUpdated() { return nUpdated; }
		public int getNErrors() { return nErrors; }
		@Override
		public void addRow(int rowNumber, SObject sObject) {

			pendingIds.add( sObject.getId());
			
			if( UPDATE_CHUNK_SIZE == pendingIds.size()) { 
				updatePending(); 
			
			}
		}

		@Override
		public void finish() {
			updatePending();
			super.finish();
		}

		@Override
		public void start() {
			super.start();
			
			rowCounter = 0;
			nUpdated = 0;
		}
		
	}

	/**
	 * Record that needs to be updated in salesforce.
	 * 
	 * 
	 *
	 */
	class PendingRecord {
		String id;
		List<String> values;
		
		PendingRecord( String id, List<String> values ) {
			this.id = id;
			this.values = values;
		}
	}
	
	/**
	 * Result of updating a chunk of records in salesforce.
	 * 
	 *
	 */
	class UpdateStatus {
		int nUpdated;
		int nErrors;
		UpdateStatus( int nUpdated, int nErrors ) {
			this.nUpdated = nUpdated;
			this.nErrors = nErrors;
		}
	}
	@Override
	public String getOneLineHelp() {
		
		return "UPDATE table SET col=value [,col=value]* [WHERE predicate];"
			+ "\nUPDATE table(col[,col]*) id=(value[,value]*) [,id=(value[,value]*)]*";
	}

	@Override
	public String getHelp() {
		return getHelp( Update.class, "Update.help");
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
	
	private SObject makeSObject( String table, String id, Map<String,String> values ) throws Exception {
		List<MessageElement> elementList = new ArrayList<MessageElement>();
		List<String> fieldsToNull = new ArrayList<String>();
		
		for( String colName : values.keySet()) {
			String value = values.get(colName);
			if( null != value ) {
				elementList.add( newMessageElement(colName, value));
			} else {
				fieldsToNull.add(colName);
			}
		}
		
		return new SObject(
				table,
				fieldsToNull.toArray(new String[0]),
				id, // id
				elementList.toArray( new MessageElement[0])
				);
	}
	
	private SObject makeSObject( String table, List<String> columnNames, PendingRecord record ) throws Exception {
		List<MessageElement> elementList = new ArrayList<MessageElement>();
		List<String> fieldsToNull = new ArrayList<String>();
		
		for( int n = 0; n < columnNames.size(); n++ ) {
			String name = columnNames.get(n);
			String value = record.values.get(n);
			if( null != value ) {
				elementList.add( newMessageElement(name, value));
			} else {
				fieldsToNull.add(name);
			}
		}
	
		
		return new SObject(
				table,
				fieldsToNull.toArray(new String[0]),
				record.id, // id
				elementList.toArray( new MessageElement[0])
				);
	}
	/**
	 * Update a chunk of values -- chunk will never be more than salesforce can handle in a batch.
	 * 
	 * @param env
	 * @param table
	 * @param columnNames
	 * @param values
	 * @throws Exception
	 */
	private UpdateStatus updateInlineCommandChunk(SQLForceEnvironment env, String table, List<String> columnNames, List<PendingRecord> values ) throws Exception {
		
		int nUpdated  = 0;
		int nErrors = 0;
		
		env.checkSession();
		LoginManager.Session session = env.getSession();
		
		SObject sObjects[] = new SObject[values.size()];
		int k = 0;
		for( PendingRecord record : values ) {
			sObjects[k] = makeSObject( table, columnNames, record);
			k++;
		}
		
		SaveResult batchResults[];
		try {
			batchResults = session.getBinding().update( sObjects );
		} catch( Exception e ) {
			throw new Exception( e.toString());
		}
		
		for( SaveResult rr : batchResults ) {
			
			if( rr.isSuccess()) { 
				nUpdated++;
			} else {
				nErrors++;
				env.logError( rr.getErrors()[0].getMessage());
			}
		}
		
		return new UpdateStatus( nUpdated, nErrors );
	}
	/**
	 * Execute the UPDATE table(col,[col]*) id=(value[,value]*) form of the update command.
	 * 
	 * When this method is called "UPDATE table(" has already been read from
	 * the lexical analyzer.
	 * 
	 * @param table table to update.
	 * @param lex read tokens from this guy
	 * @param env the SQLForce environment
	 * @throws Exception if anything goes wrong.
	 */
	private void executeUpdateInlineCommand( String table, LexicalAnalyzer lex,
			SQLForceEnvironment env) throws Exception {
		
		List<String> columnNames = new ArrayList<String>();
		//
		// Find the column names to update.
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
		// Grab records of the form id=(value[,value]*) until we run out of records
		// or a syntax error is encountered.
		//
		
		List<PendingRecord> pendingValues = new ArrayList<PendingRecord>();
		int nErrors = 0;
		int nUpdated = 0;
		
		while( true ) {
			LexicalToken idToken = lex.getLetterDigitToken(true);
			
			if( null == idToken ) { 
				throw new Exception("Unexpected end of data while reading UPDATE value records");
			}
			
			lex.getToken( true, "=");
			
			List<String> values = Parser.parseSQLParenData( lex, env );
			
			pendingValues.add(new PendingRecord( idToken.getValue(), values));
			if( UPDATE_CHUNK_SIZE == pendingValues.size()) {
				UpdateStatus status = updateInlineCommandChunk(env, table, columnNames, pendingValues );
				nErrors += status.nErrors;
				nUpdated += status.nUpdated;
				pendingValues.clear();
				
				env.logInfo("Updated Records " + Math.max(1,(1 + nUpdated-UPDATE_CHUNK_SIZE)) + " thru " + nUpdated);
			}
			
			//
			// See if we have additional tokens
			//
			LexicalToken eolToken = lex.getToken(true);
			if( null==eolToken || ";".equals( eolToken.getValue())) { break; } // End of command
			if( ! ",".equals( eolToken.getValue())) {
				throw new Exception("Expected ',' but found " + eolToken.getValue());
			}
		}
		
		if( 0 != pendingValues.size()) {
			UpdateStatus status = updateInlineCommandChunk(env, table, columnNames, pendingValues );
			nErrors += status.nErrors;
			nUpdated += status.nUpdated;
		}
		
		env.setSQLRowResultCounts(nUpdated, nErrors);

		env.log("Finished: Updated " + nUpdated + " records");
	}
	/**
	 * Execute the UPDATE table SET form of update.
	 * 
	 * When this method is called, the "UPDATE table SET" tokens have already been read
	 * by the lexical analyzier.
	 * 
	 * @param table table to update
	 * @param lex source of new tokens
	 * @param env the SQLForce environment
	 * @throws Exception if anything goes wrong.
	 */
	private void executeUpdateSetCommand(String table, LexicalAnalyzer lex,
			SQLForceEnvironment env) throws Exception {
		Map<String,String> setVariableMap = new HashMap<String,String>();
		StringBuffer sqlWHERE = new StringBuffer();




		//
		// Scan for column=value [,column=value]* pairs until a WHERE, EOF, or semicolon.
		//


		boolean foundWHERE = false;
		while( true ) {
			LexicalToken var = lex.getToken(true, LexicalToken.Type.IDENTIFIER );
			lex.getToken(true, "=");
			LexicalToken value = lex.getToken( true );

			if( LexicalToken.Type.IDENTIFIER == value.getType() && "null".equalsIgnoreCase(value.getValue())) {
				setVariableMap.put( var.getValue(), null );
			} else {
				String finalValue = env.replaceEnv(value.getValue());
				if( 0==finalValue.length() ) {
					setVariableMap.put( var.getValue(), null );
				} else {
					setVariableMap.put( var.getValue(),  finalValue );
				}
			}



			LexicalToken delim = lex.getToken(true);

			if( null == delim ) { break; } // end of file.

			if( ",".equals( delim.getValue())) {
				continue;
			}

			if( EOL.equals( delim.getValue())) {
				break;
			}

			if( "WHERE".equalsIgnoreCase( delim.getValue())) {
				foundWHERE = true;
				break;
			}
			break;
		}

		//
		// If the keyword WHERE was found, then the rest of the line is used to select records.
		//
		if( foundWHERE ) {
			String predicate = readSQL( env, lex);
			if( null==predicate || 0==predicate.trim().length()) {
				throw new Exception("No predicate specified after the keyword WHERE");
			}
			sqlWHERE.append( predicate );
		}


		//
		// Select and update records.
		//
		env.checkSession();
		LoginManager.Session session = env.getSession();

		SelectForUpdateCallback callback = new SelectForUpdateCallback( session, env, table, setVariableMap);

		//
		// Note that the "callback" will update chunks of records as they are found.
		//
		(new SObjectQueryHelper()).findSObjectIds(session, table, sqlWHERE.toString(), callback);

		env.setSQLRowResultCounts(callback.getNUpdated(), callback.getNErrors());

		env.log("Finished: Updated " + callback.getNUpdated() + " records");

	}

	public void execute(LexicalToken token, LexicalAnalyzer lex,
			SQLForceEnvironment env) throws Exception {

		String table;

		try {
			table = lex.getToken(true, LexicalToken.Type.IDENTIFIER ).getValue();
			
			//
			// Figure out which form of the UPDATE statement is being used.
			//
			LexicalToken updateTypeToken = lex.getToken(true);

			switch( updateTypeToken.getType()) {
			case IDENTIFIER: {
				if( "SET".equalsIgnoreCase(updateTypeToken.getValue())) {
					executeUpdateSetCommand( table, lex, env );
					return;
				} 
			} break;
			
			case PUNCTUATION: {
				if( "(".equalsIgnoreCase(updateTypeToken.getValue())) {
					executeUpdateInlineCommand( table, lex, env);
					return;
				} 
			} break;
			
			}
			throw new Exception("Unexpected token '" + updateTypeToken.getValue() + "'. Expected ( or the keyword SET");
		} catch( Exception e ) { // Eat the remaining SQL line up to the EOL character.
			lex.consume(EOL);
			
			throw e;
		}
	}

}
