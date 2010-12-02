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
package com.aslan.sfdc.sqlforce.command.test;

import junit.framework.TestCase;

import com.aslan.sfdc.partner.LoginCredentials;
import com.aslan.sfdc.partner.test.SfdcTestEnvironment;
import com.aslan.sfdc.sqlforce.SQLForceSession;
import com.aslan.sfdc.sqlforce.test.SQLForceFactory;

/**
 * Unit Tests for the CONNECT command.
 * @author greg
 *
 */
public class ConnectTest extends TestCase {

	/**
	 * After we screw up the connection make sure it is restored for other unit tests.
	 */
	protected void tearDown() throws Exception {
		SQLForceSession session = SQLForceFactory.getForceSession();
		session.getEnvironment().setSession(SfdcTestEnvironment.getTestSession());
	}
	public void testExecute() throws Exception {
		
		LoginCredentials cred = SfdcTestEnvironment.getTestLoginCredentials();
		SQLForceSession session = SQLForceFactory.getForceSession();
		
		//
		// Try a successful login with 4 tokens.
		//
		session.runCommands("CONNECT " 
				+ " " + cred.getConnectionType().name()
				+ " " + cred.getUsername()
				+ " " + cred.getPassword()
				+ " " + cred.getSecurityToken());
		
		//
		// Try a successful login with 3 tokens.
		//
		session.runCommands("CONNECT " 
				+ " " + cred.getConnectionType().name()
				+ " " + cred.getUsername()
				+ " " + cred.getPassword()+ cred.getSecurityToken());
		//
		// Fail because of a bad security token
		//
		try {
			session.runCommands("CONNECT " 
					+ " " + cred.getConnectionType().name()
					+ " " + cred.getUsername()
					+ " " + cred.getPassword()
					+ " " + "TerribleToken");
			
			fail("CONNECT did not fail with invalid credentials");
		} catch( Exception e ){}
		
		//
		// Fail because of missing parameters
		//
		try {
			session.runCommands("CONNECT " 
					+ " " + cred.getConnectionType().name()
					+ " " + cred.getUsername()
					+ " " + cred.getPassword()
					);
			
			fail("CONNECT did not fail with missing credentials");
		} catch( Exception e ){}
		
		//
		// Fail because of wrong type
		//
		try {
			session.runCommands("CONNECT " 
					+ " " + "UNKNOWNTYPEABC"
					+ " " + cred.getUsername()
					+ " " + cred.getPassword()
					+ " " + cred.getSecurityToken()
					);
			
			fail("CONNECT did not fail with wrong connection type");
		} catch( Exception e ){}
		
	}

}
