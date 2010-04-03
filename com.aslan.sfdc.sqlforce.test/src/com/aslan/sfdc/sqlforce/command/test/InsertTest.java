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
package com.aslan.sfdc.sqlforce.command.test;

import java.io.ByteArrayOutputStream;

import com.aslan.sfdc.sqlforce.SQLForceSession;
import com.aslan.sfdc.sqlforce.test.SQLForceFactory;

import junit.framework.TestCase;

public class InsertTest extends TestCase {

	private static String LAST_NAME = "UnitTestInsertLastName";
	private static String FIRST_NAME = "UnitTestInsertLastName";
	private static String WHERE_SQL = " WHERE FirstName='" + FIRST_NAME + "' AND LastName='" + LAST_NAME + "'";
	private static String SELECT_SQL = "SELECT COUNT() FROM Contact " + WHERE_SQL + ";";
	private static String INSERT_PREFIX = "INSERT INTO Contact(FirstName,LastName,Title) ";
	private static String ONE_VALUE = "('" + FIRST_NAME + "','" + LAST_NAME + "', null" + ")";
	private static String ONE_VALUE2 = "('" + FIRST_NAME + "','" + LAST_NAME + "','')";
	/**
	 * Create the unit test data.
	 */
	protected void setUp() throws Exception {
		
		SQLForceSession session = SQLForceFactory.getForceSession();
		session.runCommands( "DELETE FROM CONTACT " + WHERE_SQL + ";");
	
	}

	/**
	 * Delete the unit test data.
	 */
	protected void tearDown() throws Exception {
		SQLForceSession session = SQLForceFactory.getForceSession();
		session.runCommands( "DELETE FROM CONTACT " + WHERE_SQL + ";");
	}

	private String getTestField(SQLForceSession session) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		
		session.runCommands( SELECT_SQL, outStream );
		
		String value = new String( outStream.toByteArray()).trim();
		
		return value;
		
	}
	
	
	/**
	 * Test insert use Values with no parameter substitution.
	 * 
	 * @throws Exception
	 */
	public void testSimpleValueInsert() throws Exception {
		SQLForceSession session = SQLForceFactory.getForceSession();
		
		//
		// Insert a single row.
		//
		session.runCommands(INSERT_PREFIX + " VALUES" + ONE_VALUE + ";");
		
		assertEquals( "1", getTestField(session));
		
		//
		// Insert 2 more rows.
		//
		session.runCommands(INSERT_PREFIX + " VALUES" 
				+ ONE_VALUE + "," + ONE_VALUE2 + ";");
		
		assertEquals( "3", getTestField(session));
	}
	
	/**
	 * Test insert use SELECT with no parameter substitution.
	 * 
	 * @throws Exception
	 */
	public void testSimpleSelectInsert() throws Exception {
		SQLForceSession session = SQLForceFactory.getForceSession();
		
		//
		// Insert a single row.
		//
		session.runCommands(INSERT_PREFIX + " VALUES" + ONE_VALUE + ";");
		
		assertEquals( "1", getTestField(session));
		
		//
		// Insert enough data that it cannot be inserted in single batch.
		//
		String doubleItSQL = INSERT_PREFIX + 
		" SELECT FirstName, LastName, Title FROM Contact " 
		+ WHERE_SQL + ";" ;
		
		int expected = 1;
		for( int n = 0; n < 8; n++ ) {
			expected *= 2;
			session.runCommands(doubleItSQL);
			assertEquals( Integer.toString(expected), getTestField(session));
		}
		
	}

	/**
	 * Test the cases where the number of values from SELECT do not match
	 * the number of columns in the Insert.
	 * @throws Exception
	 */
	public void testSelectInsertWrongNumberOfValues() throws Exception {
		SQLForceSession session = SQLForceFactory.getForceSession();
		
		try { 
			session.runCommands( "INSERT INTO Contact(LastName, Title) "
					+ "SELECT LastName FROM Contact LIMIT 1");
			fail("Did not catch two few SELECT arguments");
		} catch( Exception e){}
		
		try { 
			session.runCommands( "INSERT INTO Contact(LastName, Title) "
					+ "SELECT LastName,FirstName,Title FROM Contact LIMIT 1");
			fail("Did not catch two many SELECT arguments");
		} catch( Exception e){}
		
		try { 
			session.runCommands( "INSERT INTO Contact(LastName, Title) "
					+ "SELECT LastName,FirstName,Title MISSING_FROM Contact LIMIT 1");
			fail("Did not catch illformed SELECT statment");
		} catch( Exception e){}
	}
	/**
	 * Test various forms of syntax errors that should be caught.
	 * 
	 * @throws Exception
	 */
	public void testInsertSyntaxErrors() throws Exception {
		SQLForceSession session = SQLForceFactory.getForceSession();
		
		try { 
			session.runCommands( "INSERT MissingINTO Contact(LastName, Title) VALUES( 'Smith', 'CEO');");
			fail("Did not catch missing INTO keyword");
		} catch( Exception e){}
		
		try { 
			session.runCommands( "INSERT INTO Contact(LastName, Title) ;");
			fail("Did not catch missing VALUES or SELECT keyword");
		} catch( Exception e){}
		
		try { 
			session.runCommands( "INSERT INTO Contact(LastName, Title) WrongKeyword;");
			fail("Did not catch wrong keyword after column list");
		} catch( Exception e){}
		
		try { 
			session.runCommands( "INSERT INTO Contact(LastName, ) VALUES( 'Smith', 'CEO');");
			fail("Did not catch illformed column spec");
		} catch( Exception e){}
		
		try { 
			session.runCommands( "INSERT INTO Contact(LastName * FirstName ) VALUES( 'Smith', 'CEO');");
			fail("Did not catch illformed column spec");
		} catch( Exception e){}
		
		try { 
			session.runCommands( "INSERT INTO Contact(LastName, Title) VALUES( 'Smith', 'CEO', 'ExtraData');");
			fail("Did not catch too many values");
		} catch( Exception e){}
		
		try { 
			session.runCommands( "INSERT INTO Contact(LastName, Title) VALUES( ,'Smith', 'CEO');");
			fail("Did not catch value list syntax error");
		} catch( Exception e){}
		
		try { 
			session.runCommands( "INSERT INTO Contact(LastName, Title) VALUES( 'Smith', 'CEO';");
			fail("Did not catch value list missing closing paren");
		} catch( Exception e){}
		
		try { 
			session.runCommands( "INSERT INTO Contact(LastName, Title) VALUES( 'Smith', 'CEO'),;");
			fail("Did not catch value list has extra punctuation");
		} catch( Exception e){}
		
		try { 
			session.runCommands( "INSERT INTO Contact(LastName, Title) VALUES( 'Smith', 'CEO') *;");
			fail("Did not catch value list has extra punctuation");
		} catch( Exception e){}
		
		try { 
			session.runCommands( "INSERT INTO Contact(LastName, Title) VALUES( 'Smith' >  'CEO');");
			fail("Did not catch illformed values clause");
		} catch( Exception e){}
		
//		try { 
//			session.runCommands( "INSERT INTO Contact(LastName, Title) VALUES( 'Smith', 'CEO');");
//			fail("Did not catch missing INTO keyword");
//		} catch( Exception e){}
		
		
	}
}
