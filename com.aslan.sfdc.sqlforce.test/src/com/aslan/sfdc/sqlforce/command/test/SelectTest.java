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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.aslan.sfdc.sqlforce.SQLForceSession;
import com.aslan.sfdc.sqlforce.test.SQLForceFactory;

import junit.framework.TestCase;

public class SelectTest extends TestCase {

	private static String LAST_NAME = "UnitTestInsertLastName";
	private static String FIRST_NAMES[] = {"Greg", "Mary", "Rebecca", "Caleb"};
	private static String TITLE = "UnitTestInsertTitle"; // Unique key used to delete test data
	private static String WHERE_SQL = " WHERE Title='" + TITLE + "'";
	private static Set<String> firstNameSet = null;


	/**
	 * Create the unit test data.
	 */
	protected void setUp() throws Exception {
		
		if( null == firstNameSet ) {
			firstNameSet = new HashSet<String>();
			for( String name : FIRST_NAMES ) { firstNameSet.add(name); }
		}
		SQLForceSession session = SQLForceFactory.getForceSession();
		session.runCommands( "DELETE FROM CONTACT " + WHERE_SQL + ";");
		for( String name : FIRST_NAMES ) {
			session.runCommands( "INSERT INTO Contact(FirstName,LastName,Title)"
					+ " VALUES("
							+ "'" + name + "'"
							+ ",'" + LAST_NAME + "'"
							+ ",'" + TITLE + "'"
					+ ");"
					);
		}
	}

	/**
	 * Delete the unit test data.
	 */
	protected void tearDown() throws Exception {
		SQLForceSession session = SQLForceFactory.getForceSession();
		session.runCommands( "DELETE FROM CONTACT " + WHERE_SQL + ";");
	}

	private String[] runSelect(SQLForceSession session, String sql ) throws Exception {
		ByteArrayOutputStream bOutStream = new ByteArrayOutputStream();
		
		session.runCommands( sql, bOutStream );
		String outData = new String( bOutStream.toByteArray());
	
		String nameList[] = outData.trim().split("[\\n]{1,1}");
		
		for( int n = 0; n < nameList.length; n++ ) {
			nameList[n] = nameList[n].trim();
		}
		return nameList;
	}
	
	private String runSelectX(SQLForceSession session, String sql ) throws Exception {
		ByteArrayOutputStream bOutStream = new ByteArrayOutputStream();
		
		session.runCommands( sql, bOutStream );
		String outData = new String( bOutStream.toByteArray());
	
		return outData;
	}
	/**
	 * Simple Select with no parameter substitution.
	 * 
	 * @throws Exception
	 */
	public void testSimpleSelect() throws Exception {
		SQLForceSession session = SQLForceFactory.getForceSession();
		
		session.runCommands("SELECT FirstName FROM Contact WHERE Title='" + TITLE + "'");
		assertEquals(Integer.toString(FIRST_NAMES.length), session.getenv("ROW_COUNT"));
		
		session.runCommands("SELECT COUNT() FROM Contact WHERE Title='" + TITLE + "'");
		assertEquals("1", session.getenv("ROW_COUNT"));
		
		String nameList[] = runSelect( session, "SELECT FirstName FROM Contact WHERE Title='" + TITLE + "'");
		
		
		assertTrue( null != nameList );
		
		assertEquals( FIRST_NAMES.length, nameList.length );
		
		for( String name : nameList) {
			assertTrue( firstNameSet.contains(name));
		}
		
		//
		// Test a multiple line query
		//
		nameList = runSelect( session, "SELECT DISTINCT\n\nFirstName\n\n\nFROM Contact\n\n\nWHERE Title='"+ TITLE + "'");assertTrue( null != nameList );
		assertEquals( FIRST_NAMES.length, nameList.length );
	}
	
	/**
	 * Test SELECTX query (output XML rather than rows).
	 * @throws Exception
	 */
	public void testSimpleSelectX() throws Exception {
		SQLForceSession session = SQLForceFactory.getForceSession();
		
		String xml = runSelectX( session, "SELECTX FirstName FROM Contact WHERE Title='" + TITLE + "'");
		assertTrue( null != xml );
		
		xml = runSelectX( session, "SELECTX FirstName FROM Contact WHERE Title='WillNotFindThisTitleInMillionYears'");
		assertTrue( null != xml );
		assertTrue( xml.contains("<Table"));
	}
	
	/**
	 * Simple Select COUNT().
	 * 
	 * @throws Exception
	 */
	public void testCountSelect() throws Exception {
		SQLForceSession session = SQLForceFactory.getForceSession();
		
		session.runCommands("SELECT COUNT() FROM Contact WHERE Title='" + TITLE + "'");
		assertEquals("1", session.getenv("ROW_COUNT"));
		
		//
		// Test a multiple line query
		//
		session.runCommands( "SELECT COUNT()\n\n\nFROM Contact\n\n\nWHERE Title='"+ TITLE + "'");
		assertEquals("1", session.getenv("ROW_COUNT"));
	}
	/**
	 * Test a SELECT DISTINCT query.
	 * 
	 * @throws Exception
	 */
	public void testDistinctSelect() throws Exception {
		SQLForceSession session = SQLForceFactory.getForceSession();
		
		String nameList[] = runSelect( session, "SELECT DISTINCT LastName FROM Contact WHERE Title='" + TITLE + "'");
		assertTrue( null != nameList );
		
		assertEquals( 1, nameList.length );
		assertEquals( LAST_NAME, nameList[0]);
		
		//
		// Choose a column that will have null values.
		//
		nameList = runSelect( session, "SELECT DISTINCT LastName,Phone  FROM Contact WHERE Title='" + TITLE + "'");
		assertEquals( 1, nameList.length );
	}
	
	/**
	 * Test a SELECT ... UNION SELECT query.
	 * 
	 * @throws Exception
	 */
	public void testUnionSelect() throws Exception {
		SQLForceSession session = SQLForceFactory.getForceSession();
		String baseSQL = "SELECT FirstName FROM Contact WHERE Title='" + TITLE + "'";
		String nameList[] = runSelect( session, baseSQL + " UNION " + baseSQL );
		assertTrue( null != nameList );
		
		assertEquals( 2*FIRST_NAMES.length, nameList.length );
		
		Map<String,Integer> countMap = new HashMap<String,Integer>();
		for( String name : nameList) {
			assertTrue( firstNameSet.contains(name));
			
			if( countMap.containsKey(name)) {
				countMap.put( name, 1 + countMap.get(name));
			} else {
				countMap.put( name, 1);
			}
		}
		
		assertEquals( FIRST_NAMES.length, countMap.size());
		for( String name : FIRST_NAMES ) {
			assertEquals(new Integer(2), countMap.get(name));
		}
	}
	
	/**
	 * Test a SELECT DISTINCT .. UNION SELECT query.
	 * 
	 * @throws Exception
	 */
	public void testDistinctUnionSelect() throws Exception {
		SQLForceSession session = SQLForceFactory.getForceSession();
		String whatSQL = "FirstName FROM Contact WHERE Title='" + TITLE + "'";
		String nameList[] = runSelect( session, "SELECT DISTINCT " + whatSQL + " UNION SELECT " + whatSQL );
		assertTrue( null != nameList );
		
		assertEquals( FIRST_NAMES.length, nameList.length );
		
		Map<String,Integer> countMap = new HashMap<String,Integer>();
		for( String name : nameList) {
			assertTrue( firstNameSet.contains(name));
			
			if( countMap.containsKey(name)) {
				countMap.put( name, 1 + countMap.get(name));
			} else {
				countMap.put( name, 1);
			}
		}
		
		assertEquals( FIRST_NAMES.length, countMap.size());
		for( String name : FIRST_NAMES ) {
			assertEquals(new Integer(1), countMap.get(name));
		}
	}
	
	/**
	 * Test redirection of the select output to a file.
	 * 
	 * @throws Exception
	 */
	public void testRedirectOutput() throws Exception {
		SQLForceSession session = SQLForceFactory.getForceSession();
		
		File tmpFile = File.createTempFile("SelectTest", ".txt");
		tmpFile.deleteOnExit();
		
		String sql = "SELECT FirstName FROM Contact WHERE Title='" + TITLE + "'"
		+ " OUTPUT '" + tmpFile.getAbsolutePath() + "'";
		
		session.runCommands(sql);
		assertEquals(Integer.toString(FIRST_NAMES.length), session.getenv("ROW_COUNT"));
		
		assertTrue( tmpFile.exists() );
		
		BufferedReader reader = new BufferedReader( new FileReader( tmpFile));
		int nFound = 0;
		try {
			String inline;
			while( null != (inline = reader.readLine())) {
				assertTrue( firstNameSet.contains(inline));
				nFound++;
			}
		} finally {
			reader.close();
		}
	
		assertEquals( FIRST_NAMES.length, nFound );
		tmpFile.delete();
	}
	/**
	 * Test various forms of syntax errors that should be caught.
	 * 
	 * @throws Exception
	 */
	public void testSelectSyntaxErrors() throws Exception {
		SQLForceSession session = SQLForceFactory.getForceSession();
		
		try { 
			session.runCommands( "SELECT BAD_SYNTAX");
			fail("Did not invalid SOQL syntax");
		} catch( Exception e){}
		
		
	}
}
