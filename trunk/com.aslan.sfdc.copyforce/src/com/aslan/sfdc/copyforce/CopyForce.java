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
import com.aslan.parser.commandline.CommandLineParser.SwitchDef;
import com.aslan.sfdc.extract.DefaultExtractionMonitor;
import com.aslan.sfdc.extract.ExtractionManager;
import com.aslan.sfdc.extract.ExtractionRuleset;
import com.aslan.sfdc.extract.IDatabaseBuilder;
import com.aslan.sfdc.extract.IExtractionMonitor;
import com.aslan.sfdc.extract.OutputStreamExtractionMonitor;
import com.aslan.sfdc.extract.SwingExtractionMonitor;
import com.aslan.sfdc.extract.ExtractionRuleset.TableRule;
import com.aslan.sfdc.partner.LoginCredentials;
import com.aslan.sfdc.partner.LoginCredentialsRegistry;
import com.aslan.sfdc.partner.LoginManager;

/**
 * Copy a Salesforce database to another database, creating the schema on the destination database as required.
  
  CopyForce copies the schema and data from any Salesforce database to another database. Both complete and incremental copying are supported.
  This particular class contains the parameters and control mechanisms that are common to all instances of CopyForce. There is always a concreted
  class that extends this class for each particular designation database (example: CopyForceH2, CopyForceSqlServer).
  <p>
  The following command line switches are supported by all concrete implementations:
 
  <table border="2">
  <tr align="left" valign="top">
  <th>Command Line Switch</th><th>Description</th>
  </tr>
  <tr align="left" valign="top"><td>-salesforce string</td>
  <td>Salesforce database from which data will be copied. This value can be a:
  <ul>
  <li>Name of a Salesforce profile defined in $HOME/sqlforce.ini</li>
  <li>Comma separated string containing [Production||Sandbox],Username,Password,SecurityToken</li>
  </ul>
  Examples:
  <ul>
  <li>-salesforce Sandbox</li>
  <li>-salesforce Production,gsmithfarmer@gmail.com,holyMoly,99898aasf89asf</li>
  </ul>
  </td>
  </tr>
  
   <tr align="left" valign="top"><td>-schema</td>
   <td>Create the Salesforce schema in the destination databases. Note that if a schema object already exists in the destination then CopyForce
   will skip it automatically.
   </td>
   </tr>
   
   <tr align="left" valign="top"><td>-include string</td>
   <td>Comma separated list of tables (or regular expressions) to copy from Salesforce. By default, all tables all included. Examples:
   <ul>
   <li>-include "Account,Contact"</li>
   <li>-include "Prod.*,Price.*"</li>
   </ul>
   </td>
   </tr>
   
   
   <tr align="left" valign="top"><td>-exclude string</td>
   <td>Comma separated list of tables (or regular expressions) to EXCLUDE from the copy from Salesforce. By default, no tables are excluded. Examples:
   <ul>
   <li>-exclude "Attachment"</li>
   <li>-exclude "Attachment,Software.*"</li>
   </ul>
   </td>
   </tr>
   
   <tr align="left" valign="top"><td>-config file</td>
   <td>XML file that specifies what to copy/exclude from Salesforce. This file should be used when the simple <i>-include</i> and <i>-exclude</i> are insufficient 
   to represent the parameters for the copy. See later in this documentation for the format of a configuration file.
   </td>
   </tr>
   
   <tr align="left" valign="top"><td>-gui</td>
   <td>Display a UI that indicates the progress of the copy. By default, progress is written to stdout.
   </td>
   </tr>
   
   <tr align="left" valign="top"><td>-silent</td>
   <td>Do not display progress messages.
   </td>
   </tr>
   
   <tr align="left" valign="top"><td>-timeout ms</td>
   <td>Set the timeout value for calls to Salesforce. The default is 1,000,000 and rarely needs to changed.
   </td>
   </tr>
   
   <tr align="left" valign="top"><td>-buffer mBytes</td>
   <td>Set the number of megabytes CopyForce will buffer from Salesforce before writing them to the destination database.
   </td>
   </tr>
  </table>
  <p>
  The list of objects copied from Salesforce can optionally be controlled by a configuration file (see the <i>-config</i> command line switch). A configuration file
  must be in the following form:
  <blockquote>
  &lt;CopyForce&gt;<br></br>
  	<blockquote>
  		&lt;include table="nameMatchingPattern" where="optional Where Clause" &gt;
  <br></br>
  		&lt;exclude table="nameMatchingPattern" &gt;
 
   </blockquote>
  &lt;/CopyForce&gt;
  </blockquote>
  where:
  <ul>
  <li>The &lt;include&gt; element indicates tables that will be include in the copy. The <i>name</i> attribute is a regular expression that will be used to find matching
  table names (a plain table name (like Account) is, of course, a regular expression. The optional <i>where</i> attribute is should be a string that can be appended to a
  <i>WHERE</i> clause for SOQL against the table.</li>
  <li>The &lt;exclude&gt; element indicates tables that should be excluded from the copy.
  </ul>
  Example: Copy all tables except Attachments:
  <blockquote>
  &lt;CopyForce&gt;
  <blockquote>
  	&lt;include table=".*"  &gt;
  <br></br>
  		&lt;exclude table="Attachment" &gt;
  </blockquote>
  &lt;/CopyForce&gt;
  </blockquote>
 * @author gsmithfarmer@gmail.com
 *
 */
public abstract class CopyForce {

	private class CopyThread extends Thread {
		private CommandLineParser parser;
		private Exception exception;
		private IExtractionMonitor monitor;
		private ExtractionRuleset rules = new ExtractionRuleset();

		CopyThread( CommandLineParser parser, IExtractionMonitor monitor ) {
			this.parser = parser;
			this.monitor = monitor;
		}
		
		private void addCommandLineIncludeRules( String arg ) {
			String newRules[] = arg.split("\\s*,\\s*");
			
			for( String rule : newRules ) {
				rules.includeTable(new TableRule(rule, false));
			}
		}
		
		private void addCommandLineExcludeRules( String arg ) {
			String newRules[] = arg.split("\\s*,\\s*");
			
			for( String rule : newRules ) {
				rules.excludeTable(new TableRule(rule, false));
			}
		}
		private void copy() throws Exception {
			
			traceMode = parser.isSet( SW_TRACE);
			salesforceTimeout = parser.getInt(SW_TIMEOUT);
			salesforceRowBufferMB = parser.getInt(SW_BUFFER );
			
			//
			// Login into Salesforce.
			//
		
			if( !parser.isSet(SW_SALESFORCE)) {
				throw new Exception("Required switch " + SW_SALESFORCE + " was not specified");
			}
			String connectString =  parser.getString(SW_SALESFORCE);
			trace("Connect to Salesforce - " + connectString );
			monitor.reportMessage("Connecting to Salesforce");
			LoginManager.Session session = connectToSalesforce( connectString );
			
			//
			// Determine what should be transferred to the output database.
			//
			
			if( parser.isSet(SW_CONFIG)) {
				trace("Load extraction rules from " + parser.getString(SW_CONFIG));
				SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
				ConfigSaxHandler handler = new ConfigSaxHandler(rules);
				
				saxParser.parse( parser.getFile(SW_CONFIG), handler);
				
			} 
			
			if( parser.isSet( SW_INCLUDE )) {
				addCommandLineIncludeRules( parser.getString(SW_INCLUDE));
			}
			
			if( 0 == rules.getIncludedTableRules().size()) {
				rules.includeTable(new TableRule(".*", false));
			}
	
			if( parser.isSet( SW_EXCLUDE)) {
				addCommandLineExcludeRules(parser.getString(SW_EXCLUDE));
			}
			//
			// Connect to the destination database.
			//
			monitor.reportMessage("Connecting to destination database");
			IDatabaseBuilder builder = getDatabaseBuilder(parser);
			
			//
			// Start the extraction
			//
			ExtractionManager mgr = new ExtractionManager(session, builder);
			mgr.setMaxBytesToBuffer( salesforceRowBufferMB*(1024*1024));
			if( parser.isSet( SW_SCHEMA) ) {
				trace("Start creation of Schema in target database");
				mgr.extractSchema( rules, monitor);
			}
			
			
			trace("Start copy of data from Salesforce to target database");
			mgr.extractData( rules, monitor);
			trace("Finished");
			
			monitor.reportMessage("Copy is Complete");
		}
		
		public void run() {
			exception = null;
			try {
				copy();
			} catch( Exception e ) {
				exception = e;
			}
		}
	}
	
	
	private static final String SW_SALESFORCE = "salesforce";
	private static final String SW_LOG = "log";
	private static final String SW_VERSION = "version";
	private static final String SW_SCHEMA = "schema";
	private static final String SW_SILENT = "silent";
	private static final String SW_CONFIG = "config";
	private static final String SW_TRACE = "trace";
	private static final String SW_TIMEOUT = "timeout";
	private static final String SW_BUFFER = "buffer";
	private static final String SW_GUI = "gui";
	private static final String SW_INCLUDE = "include";
	private static final String SW_EXCLUDE = "exclude";

	private static final SwitchDef[]  baseCmdSwitches = {
		new SwitchDef( "string", SW_SALESFORCE, "profileName OR ConnectionType,Username,Password[,SecurityToken]")
		,new SwitchDef( "string", SW_INCLUDE, null, "comma separated list of tables (or regexp) to export from salesforce" )
		,new SwitchDef( "string", SW_EXCLUDE,  null, "comma separated list of tables (or regexp) to exclude from the export from salesforce" )
		,new SwitchDef( "string", SW_LOG, "error", "info,warning,error:Set the default message level written to stderr" )
		,new SwitchDef( "xfile", SW_CONFIG, "Description what should be transferred from Salesforce")
		,new SwitchDef( "none", SW_VERSION, "Print the version number of the program to stderr" )
		,new SwitchDef( "none", SW_SILENT, "If specified then progress is not written to stdout")
		,new SwitchDef( "none", SW_SCHEMA, "If set schema the system will create the schema before transferring data" )
		,new SwitchDef( "none", SW_TRACE, "If set then be verbose about program flow" )
		,new SwitchDef( "int", SW_TIMEOUT, "1000000", "Maximum time (milliseconds) for Salesforce" )
		,new SwitchDef( "int", SW_BUFFER, "2", "Number of megabytes to use when buffering Salesforce data" )
		,new SwitchDef( "none", SW_GUI, "If set then show progress in a GUI" )
	};
	
	
	private List<SwitchDef> cmdSwitches = new ArrayList<SwitchDef>();
	private boolean traceMode = false;
	private int salesforceTimeout = 1000000;
	private int salesforceRowBufferMB = 20;
	
	private class ConfigSaxHandler extends DefaultHandler {
		private ExtractionRuleset ruleSet;
		ConfigSaxHandler( ExtractionRuleset ruleSet ) {
			this.ruleSet = ruleSet;
		}
		
		private String getAttribute(Attributes attributes, String name ) {
			String value = attributes.getValue( name);
			
			if( null==value || 0==value.trim().length()) { return null; }
			return value;
		}
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			
			if( "include".equals( qName)) {
				String name = getAttribute( attributes, "table");
				if( null != name ) {
					
					TableRule rule = new TableRule(name);
					String predicate = getAttribute( attributes, "where");
					if( null != predicate ) {
						rule.setPredicate( predicate);
					}
					
					ruleSet.includeTable(rule);
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
	protected CopyForce() {
		for( SwitchDef s : baseCmdSwitches ) {
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
		} else if( tokens.length >=3 || tokens.length <= 4 ) {
			String connectionType = tokens[0].trim();
			String username = tokens[1].trim();
			String password = tokens[2].trim();
			String securityKey = (4==tokens.length?tokens[3].trim():"");
			
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
		
		
		return new LoginManager().login( credentials, salesforceTimeout );
	}
	/**
	 * Add one or more command line switches to the list of standard switches.
	 * 
	 * @param switchList extra command line switches.
	 */
	protected void addCmdSwitches( SwitchDef[] switchList ) {
		for( SwitchDef s : switchList ) {
			cmdSwitches.add(s);
		}
	}
	
	/**
	 * Start a copy from Salesforce.
	 * 
	 * @param args command line switches controlling the copy.
	 * 
	 * @throws Exception if anything goes wrong.
	 */
	public void execute( String args[]) throws Exception {
		CommandLineParser parser = new CommandLineParser(cmdSwitches.toArray( new SwitchDef[0]));

		args = (null==args?new String[0]:args);
		parser.parse( args );
		
		boolean isGUI = parser.isSet( SW_GUI );
		boolean isSilent = parser.isSet(SW_SILENT);
		
		IExtractionMonitor monitor;
		if( isSilent ) {
			monitor = new DefaultExtractionMonitor();
		} else if( isGUI ) {
			monitor = new SwingExtractionMonitor();
			
		} else {
			monitor = new OutputStreamExtractionMonitor();
		}
		
		
		CopyThread thread = new CopyThread( parser, monitor );
		thread.start();
		thread.join();
		
		if( null != thread.exception ) {
			throw thread.exception;
		}
	}

}
