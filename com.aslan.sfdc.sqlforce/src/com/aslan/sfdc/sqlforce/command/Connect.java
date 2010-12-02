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

import com.aslan.sfdc.partner.LoginCredentials;
import com.aslan.sfdc.partner.LoginCredentialsRegistry;
import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.sqlforce.AbstractSQLForceCommand;
import com.aslan.sfdc.sqlforce.LexicalAnalyzer;
import com.aslan.sfdc.sqlforce.LexicalToken;
import com.aslan.sfdc.sqlforce.SQLForceEnvironment;

/**
 * Establish a connection to a Salesforce database.
 * 
 * The form of CONNECT is:
 *  is:
 * <pre>
 * CONNECT [PRODUCTION|SANDBOX] username password activationKey
 * CONENCT PROFILE profileName
 * </pre>
 * @author snort
 *
 */
public class Connect extends AbstractSQLForceCommand {

	private final String helpMessage = "CONNECT [PRODUCTION|SANDBOX] username password [activationKey]";
	private final String usageMessage = "Usage: " + helpMessage;
	private final String PRODUCTION = "PRODUCTION";
	private final String SANDBOX = "SANDBOX";
	private final String PROFILE = "PROFILE";
	public static int DEFAULT_TIMEOUT = 100000;

	private String getToken( LexicalAnalyzer lex ) throws Exception {
		LexicalToken token = lex.getNonBlankToken();
		
		if( null == token || LexicalToken.Type.END_OF_LINE==token.getType()) {
			throw new Exception(usageMessage);
		}
		
		return lex.stripQuotes(token.getValue());
	}

	private String getOptionalToken( LexicalAnalyzer lex ) throws Exception {
		LexicalToken token = lex.getNonBlankToken();
		
		if( null == token || LexicalToken.Type.END_OF_LINE==token.getType()) {
			return "";
		}
		
		return lex.stripQuotes(token.getValue());
	}
	
	@Override
	public String getOneLineHelp() {
		return helpMessage;
	}

	

	@Override
	public String getHelp() {
		return getHelp( Connect.class, "Connect.help");
	}


	public void execute(LexicalToken token, LexicalAnalyzer lex,
			SQLForceEnvironment env) throws Exception {


		String connectionType = lex.getToken(new String[] {PRODUCTION, SANDBOX, PROFILE}).getValue();
		LoginManager mgr = new LoginManager();
		LoginCredentials credentials = null;
		
		if( PROFILE.equalsIgnoreCase( connectionType )) {
			String profile = getToken(lex);
			
			credentials = LoginCredentialsRegistry.getInstance().getCredentials(profile);
			if( null == credentials ) {
				env.logError("A Profile with the name '" + profile + "' was not found in the credentials registry");
				return;
			}
		} else {
			String username = getToken(lex);
			String password = getToken(lex);
			String securityKey = getOptionalToken(lex);
			
			LoginCredentials.ConnectionType cType = null;
			if( PRODUCTION.equalsIgnoreCase(connectionType)) {
				cType = LoginCredentials.ConnectionType.Production;
			} else if( SANDBOX.equalsIgnoreCase(connectionType)) {
				cType = LoginCredentials.ConnectionType.Sandbox;
			} 
			
			credentials = new LoginCredentials( cType, username, password, securityKey);
		}
		
		
		try {
			LoginManager.Session session = mgr.login( credentials, DEFAULT_TIMEOUT );
			env.setSession(session);
			env.log("Connected");
		} catch( Exception e ) {
			
			env.logError("CONNECT Failed: " + e.toString());
			
		}
		
		
	}

}
