/*******************************************************************************
 * Copyright (c) 2010 Gregory Smith (gsmithfarmer@gmail.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/

package com.aslan.sfdc.copyforce;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.aslan.parser.commandline.CommandLineParser;
import com.aslan.sfdc.extract.DefaultExtractionMonitor;
import com.aslan.sfdc.extract.ExtractionManager;
import com.aslan.sfdc.extract.ExtractionRuleset;
import com.aslan.sfdc.extract.IDatabaseBuilder;
import com.aslan.sfdc.extract.IExtractionMonitor;
import com.aslan.sfdc.extract.ExtractionRuleset.TableRule;
import com.aslan.sfdc.partner.LoginCredentials;
import com.aslan.sfdc.partner.LoginCredentialsRegistry;
import com.aslan.sfdc.partner.LoginManager;

/**
 * Copy a Salesforce database to another database.
 * 
 * @author greg
 *
 */
public abstract class CopyForce {

	public static int DEFAULT_TIMEOUT = 100000;
	public static final String SW_CONNECT = "connect";
	public static final String SW_LOG = "log";
	public static final String SW_INIT = "init";
	public static final String SW_VERSION = "version";
	public static final String SW_SCHEMA = "schema";
	public static final String SW_SILENT = "silent";
	public static final String SW_CONFIG = "config";
	public static final String SW_TRACE = "trace";

	private static final String[] baseCmdSwitches = {
		"connect:string:::profileName OR ConnectionType,Username,Password,SecurityToken",
		"log:string:error:info,warning,error:Set the default message level written to stderr",
		"config:xfile:::Description what should be transferred from Salesforce",
		"version:none:::Print the version number of the program to stderr",
		"schema:none:::If set schema the system will create the schema before transferring data",
		"silent:none:::If specified then progress is not written to stdout",
		"trace:none:::If set the be verbose about program flow",
	};
	
	private List<String> cmdSwitches = new ArrayList<String>();
	private boolean traceMode = false;
	
	private class ConfigSaxHandler extends DefaultHandler {
		private ExtractionRuleset ruleSet;
		ConfigSaxHandler( ExtractionRuleset ruleSet ) {
			this.ruleSet = ruleSet;
		}
		
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			
			if( "include".equals( qName)) {
				String name = attributes.getValue( "table");
				if( null != name && name.trim().length()>0) {
					ruleSet.includeTable(new TableRule(name));
					trace("Include table " + name );
				}
			}
			
			if( "exclude".equals( qName)) {
				String name = attributes.getValue( "table");
				if( null != name && name.trim().length()>0) {
					ruleSet.excludeTable(new TableRule(name));
					trace("Exclude table " + name );
				}
			}
		}
	}
	public CopyForce() {
		for( String s : baseCmdSwitches ) {
			cmdSwitches.add(s);
		}
		
	}
	
	protected void trace( String message ) {
		if( traceMode ) {
			System.err.println(">>> " + message );
		}
		
	}
	protected abstract IDatabaseBuilder getDatabaseBuilder(CommandLineParser parser) throws Exception;
	
	private LoginManager.Session connectToSalesforce( String connectionString ) throws Exception {
		String tokens[] = connectionString.split(",");
		LoginCredentials credentials = null;
		
		if( 1 == tokens.length ) {
			String profile = tokens[0].trim();
			credentials = LoginCredentialsRegistry.getInstance().getCredentials(profile);
			if( null == credentials ) {
				throw new Exception("A Profile with the name '" + profile + "' was not found in the credentials registry");
			}
		} else if( 4 == tokens.length ) {
			String connectionType = tokens[0].trim();
			String username = tokens[1].trim();
			String password = tokens[2].trim();
			String securityKey = tokens[3].trim();
			
			LoginCredentials.ConnectionType cType = null;
			if( "PRODUCTION".equalsIgnoreCase(connectionType)) {
				cType = LoginCredentials.ConnectionType.Production;
			} else if( "SANDBOX".equalsIgnoreCase(connectionType)) {
				cType = LoginCredentials.ConnectionType.Sandbox;
			} 
			
			credentials = new LoginCredentials( cType, username, password, securityKey);
		} else {
			throw new Exception("Unrecognize format for the Salesforce connect string: " + connectionString );
		}
		
		
		return new LoginManager().login( credentials, DEFAULT_TIMEOUT );
	}
	/**
	 * Add one or more command line switches to the list of standard switches.
	 * 
	 * @param switchList extra command line switches.
	 */
	public void addCmdSwitches( String[] switchList ) {
		for( String s : switchList ) {
			cmdSwitches.add(s);
		}
	}
	
	public void execute( String args[]) throws Exception {
		CommandLineParser parser = new CommandLineParser(cmdSwitches.toArray( new String[0]));

		args = (null==args?new String[0]:args);
		parser.parse( args );
		
		traceMode = parser.isSet( SW_TRACE);
		
		//
		// Login into Salesforce.
		//
	
		if( !parser.isSet(SW_CONNECT)) {
			throw new Exception("Required switch " + SW_CONNECT + " was not specified");
		}
		String connectString =  parser.getString(SW_CONNECT);
		trace("Connect to Salesforce - " + connectString );
		LoginManager.Session session = connectToSalesforce( connectString );
		
		//
		// Determine what should be transferred to the output database.
		//
		
		ExtractionRuleset rules = new ExtractionRuleset();
		if( parser.isSet(SW_CONFIG)) {
			trace("Load extraction rules from " + parser.getString(SW_CONFIG));
			SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
			ConfigSaxHandler handler = new ConfigSaxHandler(rules);
			
			saxParser.parse( parser.getFile(SW_CONFIG), handler);
			
		} else { // Copy everything if rules are not specified.
			trace("Using default extraction rules");
			rules.includeTable(new TableRule(".*"));
		}
		//
		// Connect to the destination database.
		//
		IDatabaseBuilder builder = getDatabaseBuilder(parser);
		boolean isSilent = parser.isSet(SW_SILENT);
		
		IExtractionMonitor monitor = isSilent?
			new DefaultExtractionMonitor()
			: new DefaultExtractionMonitor() {

				@Override
				public void reportMessage(String msg) {
					System.err.println(msg);
				}
				
			};
		
		//
		// Start the extraction
		//
		ExtractionManager mgr = new ExtractionManager(session, builder);
		if( parser.isSet( SW_SCHEMA) ) {
			trace("Start creation of Schema in target database");
			mgr.extractSchema( rules, monitor);
		}
		
		
		trace("Start copy of data from Salesforce to target database");
		mgr.extractData( rules, monitor);
		trace("Finished");
		
	}

}
