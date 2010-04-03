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

import com.aslan.sfdc.partner.DefaultSObjectDeleteCallback;
import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.partner.SObjectDeleteHelper;
import com.aslan.sfdc.sqlforce.AbstractSQLForceCommand;
import com.aslan.sfdc.sqlforce.LexicalAnalyzer;
import com.aslan.sfdc.sqlforce.LexicalToken;
import com.aslan.sfdc.sqlforce.SQLForceEnvironment;
import com.sforce.soap.partner.DeleteResult;

/**
 * SQLForce DELETE command (simulates a regular SQL Delete command.
 * 
 * The form of DELETE supported
 *  is:
 * <pre>
 * DELETE FROM table WHERE predicate;
 * </pre>
 * @author snort
 *
 */
public class Delete extends AbstractSQLForceCommand {

	private final String EOL = ";";

	class DeleteCallback extends DefaultSObjectDeleteCallback {
		SQLForceEnvironment env;
		int deleteCount = 0;
		int errorCount = 0;
		int reportInterval = 200;
		
		DeleteCallback( SQLForceEnvironment env ) {
			this.env = env;
		}
		
		public int getDeleteCount() { return deleteCount; }
		public int getErrorCount() { return errorCount; }
		
		@Override
		public void delete(int rowNumber, String id,
				DeleteResult deleteResult) {
			
			deleteCount++;
			
			if( 0==(deleteCount%reportInterval)) {
				env.logInfo("Deleted Records " + (1 + deleteCount-reportInterval) + " thru " + deleteCount);
			}
		}

		@Override
		public void finish() {
			if( 0 != (deleteCount%reportInterval)) {
				env.logInfo("Deleted Records " + Math.max(1,(1 + deleteCount-reportInterval)) + " thru " + deleteCount);
			}
		}

		@Override
		public void error(int rowNumber, String id,
				DeleteResult deleteResult, String message) {
			
			errorCount++;
			env.logError(message );
		}
	}


	@Override
	public String getOneLineHelp() {
		
		return "DELETE FROM table WHERE predicate;";
	}

	@Override
	public String getHelp() {
		return getHelp( Delete.class, "Delete.help");
	}


	public void execute(LexicalToken token, LexicalAnalyzer lex,
			final SQLForceEnvironment env) throws Exception {

		String sqlWHERE;
		LexicalToken table;

		try {

			lex.getKeyword(true, "FROM");
			table = lex.getToken(true, LexicalToken.Type.IDENTIFIER );
			lex.getKeyword(true, "WHERE");
			sqlWHERE = readSQL( env, lex );
			
		} catch( Exception e ) { // Eat the remaining SQL line up to the EOL character.
			lex.consume(EOL);
			
			throw e;
		}

		if( 0==sqlWHERE.length()) {
			throw new Exception("WHERE clause cannot be empty");
		}
		
		//
		// Select the records to be deleted.
		//
		env.checkSession();
		LoginManager.Session session = env.getSession();
		
		DeleteCallback callback = new DeleteCallback( env );
		int nDeleted = (new SObjectDeleteHelper()).deleteWhere( session, table.getValue(), sqlWHERE, callback);
		
		env.setSQLRowResultCounts(callback.getDeleteCount(), callback.getErrorCount());
		
		env.log("Finished: Deleted " + nDeleted + " " + table.getValue() + " records");
	
	}

}
