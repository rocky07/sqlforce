package com.aslan.sfdc.partner.test;

import java.io.File;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;

import com.aslan.sfdc.partner.LoginCredentials;
import com.aslan.sfdc.partner.LoginCredentialsRegistry;


import junit.framework.TestCase;

public class LoginCredentialsRegistryTest extends TestCase {

	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testAddCredentials() {
		LoginCredentialsRegistry registry = LoginCredentialsRegistry.getInstance();
		String credName = "LoginCredentialsRegistryTest";
		
		LoginCredentials cred = new LoginCredentials( LoginCredentials.ConnectionType.Production, "Fred", "Flintstone", "xyzzy");
		
		registry.addCredentials( credName, cred);
		assertEquals( cred, registry.getCredentials(credName));
		
		cred = new LoginCredentials( LoginCredentials.ConnectionType.Production, "Barney", "Rubble", "xyzzy");
		registry.addCredentials( credName.toUpperCase(), cred);
		assertEquals( cred, registry.getCredentials(credName));
	}

	public void testGetCredentials() {
		LoginCredentialsRegistry registry = LoginCredentialsRegistry.getInstance();
		String credName = "LoginCredentialRegistryTestTestGetCredentials";
		
		LoginCredentials cred = new LoginCredentials( LoginCredentials.ConnectionType.Production, "Fred", "Flintstone", "xyzzy");
		
		assertNull( registry.getCredentials( credName ));
		registry.addCredentials( credName, cred);
		assertEquals( cred, registry.getCredentials(credName));
		
	}

	public void testGetCredentialNames() {
		LoginCredentialsRegistry registry = LoginCredentialsRegistry.getInstance();
		String credName = "LoginCredentialRegistryTestGetCredentialNames";
		
		LoginCredentials cred = new LoginCredentials( LoginCredentials.ConnectionType.Production, "Fred", "Flintstone", "xyzzy");
		
		for( String name : registry.getCredentialNames()) {
			
			assertFalse( credName.equals(name) );
		}
		
		registry.addCredentials( credName, cred);
		Set<String> nameSet = new HashSet<String>();
		for( String name : registry.getCredentialNames()) {
			nameSet.add(name);
		}
		
		assertTrue( nameSet.contains(credName));
	}

	public void testLoadCredentials() throws Exception {
		File tmpFile = File.createTempFile("LoginCredentialsRegistryTest", ".xml");
		tmpFile.deleteOnExit();
		
		PrintStream pOut = new PrintStream( tmpFile );
		String credName = "LoginCredentialRegistryTestLoad";
		pOut.println("<sqlforce>");
			pOut.println("<connection "
					+ " name=\"" + credName + "\""
					+ " type=\"Production\""
					+ " username=\"fred\""
					+ " password=\"flintstone\""
					+ " token=\"xyzzy\""
					+ "/>"
					);
			pOut.println("<connection "
					+ " name=\"" + credName + "2\""
					+ " type=\"Sandbox\""
					+ " username=\"fred\""
					+ " password=\"flintstone\""
					+ " token=\"xyzzy\""
					+ "/>"
					);
		pOut.println("</sqlforce>");
		pOut.close();
		
		LoginCredentialsRegistry registry = LoginCredentialsRegistry.getInstance();
		registry.loadCredentials( tmpFile );
		
		LoginCredentials cred = registry.getCredentials(credName );
		assertNotNull(cred);
		assertEquals( cred.getUsername(), "fred");
		assertEquals( cred.getPassword(), "flintstone");
		assertEquals( cred.getSecurityToken(), "xyzzy");
		assertEquals( cred.getConnectionType(), LoginCredentials.ConnectionType.Production );
	}

}
