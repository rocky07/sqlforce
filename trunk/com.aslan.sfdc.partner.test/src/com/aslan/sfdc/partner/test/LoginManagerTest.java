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

import java.util.List;

import com.aslan.sfdc.partner.LoginCredentials;
import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.partner.record.ProfileRecord;
import com.aslan.sfdc.partner.record.UserRecord;
import com.sforce.soap.partner.Field;


import junit.framework.TestCase;

/**
 * @author snort
 *
 */
public class LoginManagerTest extends TestCase {
	private LoginManager.Session testSession = SfdcTestEnvironment.getTestSession();
	/**
	 * Test method for {@link com.certara.sfdc.LoginManager#login(java.lang.String, java.lang.String, java.lang.String, int)}.
	 */
	public void testLogin() throws Exception {
		
		SfdcTestEnvironment env = new SfdcTestEnvironment();
		
		LoginManager mgr = new LoginManager();
		
		LoginManager.Session session = mgr.login( env.getUsername(), env.getPassword(), env.getActivationKey(), LoginManager.DEFAULT_TIMEOUT);
		
		assertNotNull( session );
		
		assertNotNull(session.getBinding().getUserInfo().getUserEmail());
		assertNotNull( session.getBinding().getUserInfo().getOrganizationName());
		
		//session.logout(); // Cannot do this because it messes up the connection cache in the SfdcTestEnvironment
		
		//
		// Make a login fail.
		//
		try {
			session = mgr.login( env.getUsername(), "ZZ" + env.getPassword(), env.getActivationKey(), LoginManager.DEFAULT_TIMEOUT);
			fail("Login did not fail when password is bad");
		} catch( Exception e ) {}
		
		//
		// Make a sandbox login fail
		//
		try {
			LoginCredentials credentials = new LoginCredentials( LoginCredentials.ConnectionType.Sandbox,
					 "jjjjjj@jjj.jjj.sandboxName", "ZZ" + env.getPassword(), env.getActivationKey());
			session = mgr.login( credentials,  LoginManager.DEFAULT_TIMEOUT );
			fail("Sandbox Login did not fail when username is bad");
		} catch( Exception e ) {}

	}
	
	public void testGetOwners() throws Exception {
		List<String> owners = testSession.getOwners("Contact");
		
		assertNotNull(owners);
		assertTrue( owners.size() > 0 );
	}
	
	public void testGetField() throws Exception {
		
		assertNotNull( testSession.getField("Contact", "LastName"));
		
		try {
			testSession.getField("Contact", "notReallyFieldName");
			fail("Found a field that is not defined in salesforce");
		} catch(Exception e ){}
	}
	
	public void testIsFieldListAvailable() throws Exception {
		
		assertTrue( testSession.isFieldListAvailable("Contact", new String[] { "LastName", "FirstName"}));
		assertFalse( testSession.isFieldListAvailable("Contact", new String[] { "LastName", "FirstNameZZZZZ"}));
	}
	
	public void testVariousEnvironmentFetches() throws Exception {
		
		UserRecord userRecord = testSession.getUserRecord();
		assertNotNull(userRecord);
		
		ProfileRecord profileRecord = testSession.getProfileRecord();
		assertNotNull(profileRecord);
		
		testSession.isAdministrator(); // ensure it can be called.
		
		testSession.isAuditFieldWriteEnabled(); // ensure it can be called.
		
		testSession.isMultiCurrency(); // ensure it can be called.
		
		testSession.isSandbox();
		
		List<String> userIds = testSession.getAllUserIds();
		assertNotNull(userIds);
		assertTrue( userIds.size() > 0 );
		
		String userId =  testSession.getUserIdByUsername(testSession.getUserRecord().getUsername() );
		assertNotNull(userId);
		assertEquals( userId, testSession.getUserRecord().getId());
		
		try {
			userId = testSession.getUserIdByUsername("NotReallyAUser@zzz.aaa");
			fail("Found an id for a user nto in the database");
		} catch( Exception e ) {}
		
		assertNotNull( testSession.getBinding());
		assertNotNull( testSession.getLoginResult());
		
		if( testSession.isSandbox()) {
			assertNotNull( testSession.getSandboxName());
		} else {
			assertNull( testSession.getSandboxName());
		}
		
		assertNotNull( testSession.getDescribeSObjectResult("Contact"));
		
		//
		// Look for timestamp fields
		//
		Field f = testSession.getLastModifiedField("Contact");
		assertEquals( "SystemModstamp", f.getName());
	
		
		f = testSession.getLastModifiedField("AccountShare");
		assertEquals( "LastModifiedDate", f.getName());

	}

}
