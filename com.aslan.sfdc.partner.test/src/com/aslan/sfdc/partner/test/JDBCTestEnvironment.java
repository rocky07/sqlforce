/**
 * 
 */
package com.aslan.sfdc.partner.test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;



/**
 * Provide access to JDBC credentials that are stored externally.
 * 
 * @author snort
 *
 */
public class JDBCTestEnvironment {

	private static JDBCTestEnvironment INSTANCE;
	private Map<String,Credentials> credentials = new HashMap<String,Credentials>();

	
	public class Credentials {
		public String name, url, username, password, driver;
		public Connection connection = null;
		
		private Credentials() {}
	}
	/**
	 * SAX Parser handler for picking up salesforce credentials from a configuration file.
	 * 
	 * @author greg
	 *
	 */
	private class CredentialsHandler extends DefaultHandler
	{
		JDBCTestEnvironment registry;
		CredentialsHandler(JDBCTestEnvironment registry ) {
			this.registry = registry;
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			
			if( !"connection".equals( qName )) { return; }
			
			Credentials cred = new Credentials();
			cred.name = attributes.getValue( "name");
			cred.url = attributes.getValue( "url");
			cred.username = attributes.getValue( "username");
			cred.password = attributes.getValue( "password");
			cred.driver = attributes.getValue( "driver");
			
			registry.credentials.put( cred.name.toUpperCase(), cred);

		}
		
		
	}
	
	private JDBCTestEnvironment() {
		String userHome = System.getProperty("user.home");
		File profile = new File( userHome + File.separator + "jdbcRegistry.ini");
		
		if( profile.exists() && profile.isFile()) {
			try {
				loadCredentials( profile );
			} catch( Exception e ) {
				throw new Error( e.getMessage());
			}
		}
	}
	
	private void loadCredentials( File file ) throws Exception {
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		CredentialsHandler handler = new CredentialsHandler(this);
		
		parser.parse( file, handler);
	}
	
	public static JDBCTestEnvironment getInstance() {
		if( null == INSTANCE ) {
			INSTANCE = new JDBCTestEnvironment();
		}
		return INSTANCE;
	}

	public Credentials getCredentials( String name ) {
		Credentials cred = credentials.get(name.toUpperCase());
		
		if( null == cred ) {
			throw new Error("No such database credentials: " + name );
		}
		return cred;
	}
	
	public Connection getConnection( String name ) throws Exception {
		Credentials cred = getCredentials( name );
		
		if( null == cred.connection ) {
			if(null != cred.driver) {
				Class.forName(cred.driver);
			}
			
			cred.connection = DriverManager.getConnection(cred.url, cred.username, cred.password );
		}
		return cred.connection;
	}
}
