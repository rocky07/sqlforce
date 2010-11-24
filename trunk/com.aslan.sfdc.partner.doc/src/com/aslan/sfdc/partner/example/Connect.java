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
package com.aslan.sfdc.partner.example;

import com.aslan.sfdc.partner.LoginCredentials;
import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.partner.LoginCredentials.ConnectionType;

/**
 * Establish a connection to Salesforce.
 * 
 * Demonstrate how to connect to Salesforce when you  manage the repository that stores
 * username, password, and security tokens.
 * 
 * @author gsmithfarmer@gmail.com
 *
 */
public class Connect {
	
	/**
	 * Connect to Salesforce using credentials that are hardcoded into the application.
	 * 
	 * @return an open Salesforce connection
	 * @throws Exception if the login fails.
	 */
	public LoginManager.Session getConnection() throws Exception {
		
		//
		// My repository of salesforce credentials is hardcoded here!
		// (No..they will not work until you change them to your own).
		//
		ConnectionType connectionType = LoginCredentials.ConnectionType.Production; // Production or Sandbox
		String	username = "gsmithfarmer@gmail.com";
		String	password = "yourpassword";
		String securityToken = "GetThisFromSalesforce";
		
		//
		// Setup the credentials and attempt the connection. If the connetion fails then
		// an exception will be thrown.
		//
		LoginCredentials credentials = new LoginCredentials(connectionType, username, password, securityToken);

		LoginManager.Session session = (new LoginManager()).login( credentials );
		
		return session;
	}
	public static void main( String[] args )  {
		Connect driver = new Connect();
		
		try {
			LoginManager.Session session = driver.getConnection();
			
			System.err.println( "You are connected. Are you an administrator? " + session.isAdministrator());
			
			session.logout();
			
		} catch (Exception e) {
			// Drop here if we failed to login into Salesforce.
			e.printStackTrace();
		}
	}

}
