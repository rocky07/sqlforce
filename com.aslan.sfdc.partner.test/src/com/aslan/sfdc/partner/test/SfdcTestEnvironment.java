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
/**
 * 
 */
package com.aslan.sfdc.partner.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.aslan.sfdc.partner.LoginCredentials;
import com.aslan.sfdc.partner.LoginCredentialsRegistry;
import com.aslan.sfdc.partner.LoginManager;

/**
 * Environment data used to set up all SFDC unit tests.
 * 
 * @author snort
 *
 */
public class SfdcTestEnvironment {
	
	private static final Properties testEnvironment;
	private static Map<String,LoginManager.Session> sessionMap = new HashMap<String, LoginManager.Session>();
	private static final String defaultKey;
	private static final String secondaryKey;
	private static final String testKey;
	private static final String readOnlyKey; // A database safe for reading that has a lot of records.
	
	static {
		InputStream inStream = null;
		testEnvironment  = new Properties();
		try {
			inStream = SfdcTestEnvironment.class.getResourceAsStream("SfdcTestEnvironment.properties");
			testEnvironment.load(inStream );
			String tmpStr = testEnvironment.getProperty("sfdc.default");
			defaultKey = (null==tmpStr||tmpStr.trim().length()==0)?null:tmpStr;
			
			tmpStr = testEnvironment.getProperty("sfdc.secondary");
			secondaryKey = (null==tmpStr||tmpStr.trim().length()==0)?null:tmpStr;
			
			tmpStr = testEnvironment.getProperty("sfdc.test");
			testKey = (null==tmpStr||tmpStr.trim().length()==0)?null:tmpStr;
			
			tmpStr = testEnvironment.getProperty("sfdc.readonly");
			readOnlyKey = (null==tmpStr||tmpStr.trim().length()==0)?null:tmpStr;
			
		} catch (IOException e) {
			 throw new java.lang.Error("Error creating SFDC Test Environment, ", e);
		} finally {
			try {
				inStream.close();
			} catch (IOException e) {
				;
			}
		}
	}
	
	public LoginCredentials.ConnectionType getConnectionType( String instanceName ) {
		
		LoginCredentials cred =  LoginCredentialsRegistry.getInstance().getCredentials( instanceName );
		
		if( null == cred ) {return null; }
		return cred.getConnectionType();
	}
	
	public LoginCredentials.ConnectionType getConnectionType() {
		return getConnectionType(defaultKey);
	}
	
	public String getUsername( String instanceName) {
		LoginCredentials cred =  LoginCredentialsRegistry.getInstance().getCredentials( instanceName );
		
		if( null == cred ) {return null; }
		return cred.getUsername();
	}
	
	public String getUsername() {
		return getUsername( defaultKey );
	}
	
	public String getPassword( String instanceName) {
		LoginCredentials cred =  LoginCredentialsRegistry.getInstance().getCredentials( instanceName );
		
		if( null == cred ) {return null; }
		return cred.getPassword();
	}
	
	public String getPassword() {
		return getPassword(defaultKey);
	}
	
	public String getActivationKey( String instanceName) {
		LoginCredentials cred =  LoginCredentialsRegistry.getInstance().getCredentials( instanceName );
		
		if( null == cred ) {return null; }
		return cred.getSecurityToken();
	}
	
	public String getActivationKey() {
		return getActivationKey(defaultKey);
	}
	
	/**
	 * Return an open session to SFDC (logging in if necessary).
	 * 
	 * @return active SFDC session using the default credentials for testing.
	 */
	public static LoginManager.Session getSession(String instanceName) {
		
		LoginManager.Session session;
		
		if( !sessionMap.containsKey(instanceName )) {
			
			LoginCredentials cred =  LoginCredentialsRegistry.getInstance().getCredentials( instanceName );
			if( null == cred ) {
				throw new Error( "Unrecognized credentials: " + instanceName );
			}

			LoginManager mgr = new LoginManager();
			
			try {
				session = mgr.login( cred );
				
				sessionMap.put( instanceName, session );
			} catch (Exception e) {
				throw new java.lang.Error("Failed to connection to SalesForce, ", e);
			}
		}
		
		return sessionMap.get( instanceName );
	}
	
	public static LoginCredentials getLoginCredentials( String instanceName ) {
		
		LoginCredentials cred =  LoginCredentialsRegistry.getInstance().getCredentials( instanceName );
		
		if( null == cred ) {
			throw new java.lang.Error("No parameters for instance " + instanceName );
		}
		
		return cred;
	}
	
	public static LoginCredentials getLoginCredentials() {
		return getLoginCredentials( defaultKey );
	}
	/**
	 * Return an open session to SFDC (logging in if necessary).
	 * 
	 * @return active SFDC session using the default credentials for testing.
	 */
	public static LoginManager.Session getSession() {
		
		return getSession( defaultKey );
		
	}
	
	/**
	 * Return an open session to the secondary SFDC (logging in if necessary).
	 * 
	 * This instance is used by tests that are comparing records between SFDC database.
	 * It may be the same as the value returned by getSession();
	 * 
	 * @return active SFDC session using the default credentials for testing.
	 */
	public static LoginManager.Session getSecondarySession() {
		return getSession( secondaryKey );
	}
	
	public static LoginCredentials getSecondaryLoginCredentials() {
		return getLoginCredentials( secondaryKey );
	}
	
	/**
	 * Return an open session to the test SFDC (logging in if necessary).
	 * 
	 * This instance is used by most tests. Tests may both read and write.
	 * It may be the same as the value returned by getSession();
	 * 
	 * @return active SFDC session using the default credentials for testing.
	 */
	public static LoginManager.Session getTestSession() {
		
		if(  null == testKey ) {
			throw new Error("No TEST database has been defined in the configuration file");
		}
		return getSession( testKey );
	}
	
	/**
	 * Return an open session to an SFDC database that has a lot of data.
	 * 
	 * This instance is used by tests that need a lot of records.
	 * 
	 * @return active SFDC session using the default credentials for testing.
	 */
	public static LoginManager.Session getReadOnlySession() {
		
		if(  null == readOnlyKey ) {
			throw new Error("No ReadOnly database has been defined in the configuration file");
		}
		return getSession( readOnlyKey );
	}
	
	public static LoginCredentials getTestLoginCredentials() {
		return getLoginCredentials( testKey );
	}
}
