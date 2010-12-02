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
package com.aslan.sfdc.partner;

/**
 * Credentials required to login into a SalesForce Database.
 * @author greg
 *
 */
public class LoginCredentials {

	public enum ConnectionType {
		Sandbox,
		Production
	}
	
	private ConnectionType connectionType;
	private String username;
	private String password;
	private String securityToken;

	/**
	 * Register credentials that can be used to connect to Salesforce.
	 * 
	 * @param connectionType Production or Sandbox
	 * @param username your username
	 * @param password your password
	 * @param securityToken the token provided by salesforce or null (or blank) if you organization does not need one.
	 */
	public LoginCredentials( ConnectionType connectionType, String username, String password, String securityToken ) {
		this.connectionType = connectionType;
		this.username = username;
		this.password = password;
		this.securityToken = securityToken;
	}

	/**
	 * Register credentials that can be used to connect to Salesforce when a security token is not required
	 * or the token is already appended to the password.
	 * 
	 * @param connectionType Production or Sandbox
	 * @param username your username
	 * @param password your password
	 */
	public LoginCredentials( ConnectionType connectionType, String username, String password ) {
		this( connectionType, username, password, "");

	}
	
	public ConnectionType getConnectionType() { return connectionType; }
	public String getUsername() { return username; }
	public String getPassword() { return password; }
	public String getSecurityToken() { return securityToken; }

}
