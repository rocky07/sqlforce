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

import com.aslan.sfdc.sqlforce.SQLForceSession;
import com.aslan.sfdc.sqlforce.test.SQLForceFactory;

/**
 * Unit Tests for the DELETE command.
 * @author greg
 *
 */
public class DeleteTest extends TestCase {

	private static String LAST_NAME = "UnitTestLastNameToDelete";
	private static String FIRST_NAME = "UnitTestFirstNameToDelete";
	private static String WHERE_SQL = "WHERE FirstName='" + FIRST_NAME + "' AND LastName='" + LAST_NAME + "'";
	private static String SELECT_SQL = "SELECT FirstName FROM Contact " + WHERE_SQL + ";";

	public void testExecute() throws Exception {
		SQLForceSession session = SQLForceFactory.getForceSession();
		
		//
		// Delete any leftover unit test data
		//
		session.runCommands( "DELETE FROM CONTACT " + WHERE_SQL + ";");
		
		session.runCommands( SELECT_SQL );
		assertEquals("0", session.getenv( "ROW_COUNT"));
		
		session.runCommands( "INSERT INTO Contact(LastName,FirstName)"
				+ " VALUES('" + LAST_NAME + "','" + FIRST_NAME + "');");
		
		assertEquals("1", session.getenv( "ROW_COUNT"));
		
		session.runCommands( "DELETE FROM CONTACT " + WHERE_SQL + ";");
		assertEquals("1", session.getenv( "ROW_COUNT"));
		
		session.runCommands( SELECT_SQL );
		assertEquals("0", session.getenv( "ROW_COUNT"));
		
	}
	
	public void testInvalidStatement() throws Exception {
		
		SQLForceSession session = SQLForceFactory.getForceSession();
		
		try {
			session.runCommands( "DELETE NOT_A_FROM;");
			fail("Did not catch missing FROM");
		} catch( Exception e ){}
		
		try {
			session.runCommands( "DELETE FROM Contact;");
			fail("Did not catch Universal Delete");
		} catch( Exception e ){}
		
		try {
			session.runCommands( "DELETE FROM Contact WHERE;");
			fail("Did not catch Missing WHERE Clause");
		} catch( Exception e ){}
		
		try {
			session.runCommands( "DELETE FROM 456;");
			fail("Did not catch missing table name");
		} catch( Exception e ){}
		
		try {
			session.runCommands( "DELETE FROM Contact ABC=23;");
			fail("Did not catch missing WHERE");
		} catch( Exception e ){}
	}

}
