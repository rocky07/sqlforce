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
package com.aslan.sfdc.sqlforce;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aslan.sfdc.partner.LoginManager;

/**
 * Stuff about the current environment that a command may required to execute.
 * 
 * @author snort
 *
 */
public final class SQLForceEnvironment {

	//
	// Standard Environment variables
	//
	
	//
	// Level of log information to print on stderr.
	//
	public static final String LOG_LEVEL = "LOG";
	
	//
	// Number of rows successfully "processed" in the most recent "SQL" statement.
	// SELECT -- number of rows selected
	// UPDATE -- number of rows updated
	// DELETE -- number of rows deleted
	// INSERT -- number or rows inserted
	//
	public static final String ROW_COUNT = "ROW_COUNT";
	
	//
	// Number of rows that failed to be processed in the most recent "SQL" statement.
	//
	// SELECT -- always zero
	// UPDATE -- number of rows where an update failed
	// DELETE -- number of rows where an delete failed
	// INSERT -- number or rows where an insert failed
	//
	public static final String ROW_ERROR_COUNT = "ROW_ERROR_COUNT";
	
	//
	// If the most recent command succeeded then this environment variable will
	// be null. If the the most recent command failed, then the variable will
	// contain the last error logged by the command.
	//
	public static final String STATUS = "STATUS";
	
	//
	// Comma separated list of the Salesforce IDs of the most recent list
	// of rows created using the INSERT command. Example:
	//
	//   00339393849ddh,003399898988ddi
	//
	// If a row failed to be inserted, it will be represented by adjacent commas
	// in the list. Example: 2nd Row failed to insert
	//
	//   00339393849ddh,,003399898988ddi
	//
	//
	public static final String INSERT_IDS = "INSERT_IDS";
	
	public enum LogLevel {
		INFO,
		WARNING,
		ERROR
	}
	
	private LogLevel logLevel = LogLevel.ERROR;
	
	private PrintStream outStream;
	private PrintStream errStream;
	
	private Map<String,String> env = new HashMap<String,String>();
	private LoginManager.Session session; 
	
	private List<String> lastInsertedIds = new ArrayList<String>();
	
	private Pattern envVariablePattern = Pattern.compile( 
			"\\$(\\w+)|\\$\\{([^}]+)}", Pattern.MULTILINE);
	
	
	public SQLForceEnvironment() {
		this( new PrintStream(System.out), new PrintStream(System.err));
	}
	
	public SQLForceEnvironment( PrintStream outStream, PrintStream errStream ) {
		this.outStream = outStream;
		this.errStream = errStream;
		
		setenv( LOG_LEVEL, logLevel.name());
		//
		// Put system environment variables properties into the local map.
		//
		Map<String,String> env = System.getenv();
		for( String key : env.keySet() ) {
			setenv( key, env.get(key));
		}
		
		Properties props = System.getProperties();
		for( Object key : props.keySet()) {
			setenv( key.toString(), props.getProperty( key.toString()).toString());
		}
		
		setenv( ROW_COUNT, "0");
		setenv( ROW_ERROR_COUNT, "0");
	}
	
	public void println(String value) {
		outStream.println(value);
	}
	
	public void print(String value) {
		outStream.print(value);
	}
	
	private boolean shouldLog( LogLevel level ) {
		switch( level ) {
		case ERROR: { return true; }
		case WARNING: { return logLevel==LogLevel.WARNING || logLevel==LogLevel.INFO;}
		case INFO: { return logLevel==LogLevel.INFO; }
		}
		return true; // Logic error;
	}
	
	public void log( LogLevel level, String message  ) {
		if( shouldLog(level)) {
			errStream.println(level.name() + ": " + message);
		}
	}
	
	public void log( String message ) {
		logInfo( message );
	}
	
	public void logInfo( String message ) {
		log( LogLevel.INFO, message );
	}
	public void logError( String message ) {
		log( LogLevel.ERROR, message );
		setenv( STATUS, message );
	}
	
	public void logWarning( String message ) {
		log( LogLevel.WARNING, message );
	}
	
	public String getenv( String name ) {
		return env.get( name);
	}
	
	public void setenv( String name, String value ) {
		env.put( name, value );
		
		if( name.equalsIgnoreCase(LOG_LEVEL)) {
			if( null == value ) { value="ERROR"; }
			if( "ERROR".equalsIgnoreCase(value)) {
				logLevel = LogLevel.ERROR;
			} else if( "WARNING".equalsIgnoreCase(value)) {
				logLevel = LogLevel.WARNING;
			} else if( "INFO".equalsIgnoreCase(value)) {
				logLevel = LogLevel.INFO;
			} else {
				logError("Unrecognized log level '" + value + "' ignored");
			}
			
		}
	}
	
	/**
	 * Replace all occurrences of environment variable references in a string with the
	 * corresponding environment variable values.
	 * 
	 * Environment variables must be referenced using the syntax:
	 * <pre>
	 * ${varname}
	 * </pre>
	 * @param value replace environment variables in this value.
	 * 
	 * @return value with environment variables replaced.
	 */
	public String replaceEnv( String value ) {
		
		if( null == value || 0==value.trim().length()) { return value; }
		
		StringBuffer outValue = new StringBuffer();
		Matcher matcher = envVariablePattern.matcher(value);
		int anchor = 0;
		while( matcher.find(anchor)) {
			MatchResult result = matcher.toMatchResult();
			
			outValue.append( value.substring( anchor, result.start()));
			anchor = result.end();
			
			String varname = matcher.group(1);
			varname = (null==varname)?matcher.group(2):varname;
			String var = getenv(varname);
			
			var = null==var?"":var;
			
			outValue.append(var);
		}
		
		outValue.append( value.substring(anchor));
		return outValue.toString();
	}
	/**
	 * If we are not connected to SalesForce throw an exception.
	 * 
	 * @throws Exception if not connected to SFDC.
	 */
	public void checkSession() throws Exception {
		if( null == session ) {
			throw new Exception("Not connected to SalesForce");
		}
	}

	/**
	 * Return the currently attached SalesForce session.
	 * 
	 * @return sfdc session (or null if not connected).
	 */
	public LoginManager.Session getSession() { 
		return session; 
	}
	
	/**
	 * Set the currently attached SalesForce session (null to clear).
	 * 
	 * @param session an active SFDC session (or null to clear).
	 */
	public void setSession( LoginManager.Session session ) {
		this.session = session;
	}
	
	/**
	 * Set environment variables that track the number of success and failure
	 * rows in the most recent SQL operation.
	 * 
	 * @param nRows number of rows that succeeded.
	 * @param nErrorRows number of rows that failed.
	 */
	public void setSQLRowResultCounts( int nRows, int nErrorRows ) {
		setenv( ROW_COUNT, Integer.toString(nRows));
		setenv( ROW_ERROR_COUNT, Integer.toString( nErrorRows ));
	}
	
	/**
	 * Determines if the value of a string should be interpreted as true.
	 * 
	 * @param value check this value
	 * @return true if value means true, else false.
	 */
	public boolean isValueTrue( String value ) {
		if( null == value ) { return false; }
		return ("Y".equalsIgnoreCase(value)
				|| "YES".equalsIgnoreCase(value)
				|| "TRUE".equalsIgnoreCase(value)
				|| "1".equalsIgnoreCase(value)
		);

	}
	
	/**
	 * Return the stream used for stdout.
	 * 
	 * @return the stdout stream.
	 */
	public PrintStream getStdoutStream() { return outStream; }
	
	/**
	 * Return the stream used for stderr.
	 * 
	 * @return the stderr stream.
	 */
	public PrintStream getStderrStream() { return errStream; }
	
	/**
	 * Change the default output stream.
	 * 
	 * @param outStream new output stream.
	 */
	public void setStdoutStream( PrintStream outStream ) {
		this.outStream = outStream;
	}
	
	/**
	 * Change the default error stream.
	 * 
	 * @param outStream new error stream.
	 */
	public void setStderrStream( PrintStream errStream ) {
		this.errStream = errStream;
	}
	
	/**
	 * Set the last set of SalesForce Ids that were inserted into SalesForce (by the INSERT command).
	 * 
	 * @param idList list of ids that were inserted.
	 */
	public void setInsertIds( List<String> idList ) {
		lastInsertedIds.clear();
		if( null != idList ) { lastInsertedIds.addAll(idList); }
		
		StringBuffer b = new StringBuffer();
		for( String id : idList ) {
			
			if( 0 != b.length()) { b.append(","); }
			if( null != id ) { b.append(id); }
		}
		
		setenv( INSERT_IDS, b.toString());
	}
	
	/**
	 * Get the last set of SalesForce Ids that were inserted into SalesForce (by the INSERT command).
	 * 
	 * @return list of ids that were inserted (never null but may be an empty list).
	 */
	public List<String> getInsertIds() {
		return lastInsertedIds;
	}
}
