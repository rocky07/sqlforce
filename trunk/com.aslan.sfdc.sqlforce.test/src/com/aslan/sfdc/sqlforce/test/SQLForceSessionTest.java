package com.aslan.sfdc.sqlforce.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.aslan.sfdc.partner.LoginCredentials;
import com.aslan.sfdc.partner.test.SfdcTestEnvironment;
import com.aslan.sfdc.sqlforce.SQLForceEnvironment;
import com.aslan.sfdc.sqlforce.SQLForceSession;

import junit.framework.TestCase;

public class SQLForceSessionTest extends TestCase {

	private static SQLForceSession session;
	private static String LAST_NAME = "UnitTestInsertLastName";
	private static String FIRST_NAME = "UnitTestInsertLastName";
	private static String WHERE_SQL = " WHERE FirstName='" + FIRST_NAME + "' AND LastName='" + LAST_NAME + "'";
	
	protected void setUp() throws Exception {
		
		if( null == session ) {
			session = new SQLForceSession();
			
			LoginCredentials cred = SfdcTestEnvironment.getTestLoginCredentials();
			
			String cmd = "CONNECT " + cred.getConnectionType().name()
				+ " " + cred.getUsername()
				+ " " + cred.getPassword()
				+ " " + cred.getSecurityToken();
			
			session.runCommands( cmd );
		}
		super.setUp();
	}

	protected void tearDown() throws Exception {
		session.runCommands( "DELETE FROM CONTACT " + WHERE_SQL + ";");
	}

	public void testGetEnvironment() throws Exception {
		SQLForceEnvironment env = session.getEnvironment();
		
		assertNotNull(env);
		
	}

	public void testRunCommandsInputStreamOutputStreamOutputStream() throws Exception {
		ByteArrayInputStream bQuery;
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		ByteArrayOutputStream bErr = new ByteArrayOutputStream();
		
		//
		// Run a query that works.
		//
		bQuery = new ByteArrayInputStream( "SELECT COUNT() FROM CONTACT".getBytes());
		session.runCommands( bQuery, bOut, bErr );
		assertTrue( bOut.toByteArray().length > 0 );
		assertTrue( bErr.toByteArray().length == 0 );
		
		//
		// Run a query that fails.
		//
		
		bQuery = new ByteArrayInputStream( "SELECT NotAColumnName FROM CONTACT".getBytes());
		bOut.reset();
		bErr.reset();
		try {
			session.runCommands( bQuery, bOut, bErr );
		} catch( Exception e ) {}
		
		assertTrue( bErr.toByteArray().length > 0 ); 
		assertTrue( bOut.toByteArray().length == 0 ); 
	}

	

	public void testRunCommandsStringOutputStream() throws Exception {
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		
		session.runCommands( "SELECT COUNT() FROM Contact", bOut );
		
		String result = new String(bOut.toByteArray());
		assertTrue( result.length() > 0 );
		
	}

	/**
	 * Run various flavors of commands.
	 * 
	 * @throws Exception
	 */
	public void testRunCommandsString() throws Exception {
		
		session.runCommands( "INSERT INTO Contact(FirstName,LastName) VALUES('" + FIRST_NAME + "','" + LAST_NAME + "')");
		
		String status = session.getenv("ROW_COUNT");
		assertEquals("1", status );
		
		session.runCommands( "DELETE FROM CONTACT " + WHERE_SQL );
		status = session.getenv("ROW_COUNT");
		assertEquals("1", status );
		
		session.runCommands( "SELECT COUNT() FROM Contact" );
		status = session.getenv("ROW_COUNT");
		assertNotNull(status);
		
		try {
			session.runCommands( "SELECT NotAColumnName FROM Contact" );
			fail("Invalid query did not cause an exception");
		} catch( Exception e ) {
			
		}
	}

	public void testGetenv() throws Exception {
		session.runCommands("SELECT COUNT() FROM Contact");
		String status = session.getenv("STATUS");
		
		assertNull(status);
	}

}
