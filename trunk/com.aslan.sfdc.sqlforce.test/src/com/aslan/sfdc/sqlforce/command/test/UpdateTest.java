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

public class UpdateTest extends TestCase {

	private static String LAST_NAME = "UnitTestUpdateLastName";
	private static String FIRST_NAME = "UnitTestUpdateFirstName";
	private static String WHERE_SQL = " WHERE FirstName='" + FIRST_NAME + "' AND LastName='" + LAST_NAME + "'";
	
	private static String[] testIds;
	/**
	 * Create the unit test data.
	 */
	protected void setUp() throws Exception {
		
		SQLForceSession session = SQLForceFactory.getForceSession();
		String findWHERE = " LastName LIKE '" + LAST_NAME + "%'"
							+ " AND FirstName LIKE '" + FIRST_NAME + "%'";
		
		session.runCommands( "DELETE FROM Contact WHERE " + findWHERE);
		
		StringBuffer valueList = new StringBuffer();
		for( int n = 0; n < 255; n++ ) {
			if( n > 0 ) { valueList.append(","); }
			String suffix = (0==n?"":Integer.toString(n));
			valueList.append( "('" + LAST_NAME + suffix + "', '" + FIRST_NAME + suffix + "')");
		}
		String insertSQL = "INSERT INTO Contact(LastName,FirstName) VALUES" + valueList.toString();
		
		session.runCommands( insertSQL );
		
		testIds = session.getEnvironment().getInsertIds().toArray( new String[0] );
		
//		ByteArrayOutputStream bOutStream = new ByteArrayOutputStream();
//		
//		String sql = "SELECT ID from Contact WHERE " + findWHERE 
//					+ " ORDER BY LastName ASC"
//					;
//		session.runCommands( sql, bOutStream );
//		String outData = new String( bOutStream.toByteArray());
//	
//		testIds = outData.trim().split("[\\n]{1,1}");
//		
//		for( int n = 0; n < testIds.length; n++ ) {
//			testIds[n] = testIds[n].trim();
//		}

	}

	/**
	 * Delete the unit test data.
	 */
	protected void tearDown() throws Exception {
		SQLForceSession session = SQLForceFactory.getForceSession();
		
		String idList = null;
		for( String id : testIds ) {
			idList = (null==idList?"":(idList + ","));
			idList += "'" + id + "'";
		}
		
		session.runCommands( "DELETE FROM CONTACT WHERE ID IN (" + idList + ");");
	}

	private String getTestField(SQLForceSession session) throws Exception {
		return getField( session, testIds[0], "Title");
		
	}
	

	private String getField( SQLForceSession session, String id, String fieldName) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		
		String sql = "SELECT " + fieldName + " FROM Contact WHERE Id='" + id + "'";
		session.runCommands( sql, outStream );
		
		String value = new String( outStream.toByteArray()).trim();
		if( 0 == value.length())  {value = null; }
		return value;
	}
	/**
	 * Test updates with no parameter substitution.
	 * 
	 * @throws Exception
	 */
	public void testSimpleUpdate() throws Exception {
		SQLForceSession session = SQLForceFactory.getForceSession();
		
		session.runCommands( "UPDATE CONTACT SET Title='CEO'" + WHERE_SQL + ";");
		assertEquals( "CEO", getTestField(session));
		
		session.runCommands( "UPDATE CONTACT SET Title=null" + WHERE_SQL + ";");
		assertNull( getTestField(session));
		
		session.runCommands( "UPDATE CONTACT SET Title=\"Junk Yard Dog\"" + WHERE_SQL + ";");
		assertEquals( "Junk Yard Dog", getTestField(session));
		
		session.runCommands( "UPDATE CONTACT SET Title=''" + WHERE_SQL + ";");
		assertNull( getTestField(session));
	}
	
	public void testUpdateInlineIds() throws Exception {
		SQLForceSession session = SQLForceFactory.getForceSession();
		
		session.runCommands( "UPDATE Contact(title) "
								+ testIds[0] + "=('Frederick')"
								+ "," +testIds[1] + "=('Uriah')");
		
		assertEquals( "Frederick", getField( session, testIds[0], "Title"));
		assertEquals( "Uriah", getField( session, testIds[1], "Title"));
		
		session.runCommands( "UPDATE Contact(title, MailingCountry) "
				+ testIds[0] + "=(null, 'Mexico')"
				+ "," +testIds[1] + "=('Employee', 'Canada');");
		
		assertNull( getField( session, testIds[0], "Title"));
		assertEquals( "Mexico", getField( session, testIds[0], "MailingCountry"));
		assertEquals( "Employee", getField( session, testIds[1], "Title"));
		assertEquals( "Canada", getField( session, testIds[1], "MailingCountry"));
		
		//
		// Update more that a single Salesforce can update at once.
		//
		StringBuffer values = new StringBuffer();
		for( int n = 0; n < testIds.length; n++ ) {
			if( n > 0 ) { values.append(","); }
			values.append( testIds[n] + "=('Commander')");
		}
		session.runCommands( "UPDATE Contact(title) " + values.toString());
		assertEquals( "Commander", getField( session, testIds[testIds.length - 1], "Title"));
	}
	/**
	 * Test the case when multiple columns are updated in a single UPDATE statement.
	 * 
	 * @throws Exception
	 */
	public void testSimple2ParamUpdate() throws Exception {
		SQLForceSession session = SQLForceFactory.getForceSession();
		
		session.runCommands( "UPDATE CONTACT SET Title='CEO', MailingCountry='Elbonia'" + WHERE_SQL + ";");
		assertEquals( "CEO", getField(session, testIds[0], "Title"));
		assertEquals( "Elbonia", getField(session, testIds[0], "MailingCountry"));
		
		
	}
	
	/**
	 * Test replaced the assignment part of SET with a variable reference.
	 * 
	 * @throws Exception
	 */
	public void testSetValueSubstitution() throws Exception {
		
		SQLForceSession session = SQLForceFactory.getForceSession();
		
		session.getEnvironment().setenv("MYDATA", "CIO");
		
		session.runCommands( "UPDATE CONTACT SET Title='$MYDATA'" + WHERE_SQL + ";");
		assertEquals( "CIO", getTestField(session));
		
		session.getEnvironment().setenv("MYDATA", "CTO");
		session.runCommands( "UPDATE CONTACT SET Title='${MYDATA}'" + WHERE_SQL + ";");
		assertEquals( "CTO", getTestField(session));
		
		session.runCommands( "UPDATE CONTACT SET Title='${MYDATA}abc'" + WHERE_SQL + ";");
		assertEquals( "CTOabc", getTestField(session));
		
		session.getEnvironment().setenv("MYDATA", (String)null);
		session.runCommands( "UPDATE CONTACT SET Title='${MYDATA}'" + WHERE_SQL + ";");
		assertNull( getTestField(session));
		
		session.runCommands( "UPDATE CONTACT SET Title='CFO'" + WHERE_SQL + ";");
		session.runCommands( "UPDATE CONTACT SET Title=\"${MYDATA}\"" + WHERE_SQL + ";");
		assertNull( getTestField(session));
		
	}
	
	/**
	 * Test various forms of syntax errors that should be caught.
	 * 
	 * @throws Exception
	 */
	public void testUpdateSyntaxErrors() throws Exception {
		SQLForceSession session = SQLForceFactory.getForceSession();
		
		try { 
			session.runCommands( "UPDATE CONTACT MISSING_SET Title='CEO'" + WHERE_SQL + ";");
			fail("Did not catch missing SET keyword");
		} catch( Exception e){}
		
		try { 
			session.runCommands( "UPDATE CONTACT SET Title=23 MailingCountry='Russia'" + WHERE_SQL + ";");
			fail("Did not catch missing comma between SET variables.");
		} catch( Exception e){}
		
		try { 
			session.runCommands( "UPDATE CONTACT SET Title=23 " +  "WHERE;");
			fail("Did not catch missing WHERE Clause");
		} catch( Exception e){}
		
	}
}
