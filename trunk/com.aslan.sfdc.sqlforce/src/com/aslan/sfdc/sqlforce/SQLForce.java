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

import java.io.ByteArrayInputStream;
import java.io.PrintStream;

import com.aslan.parser.commandline.CommandLineParser;

/**
 * Provide a command line interface to SalesForce SQL capabilities including
 * SQL like UPDATE and DELETE statements.
 * 
 * 
 * @author snort
 *
 */
public class SQLForce {

	public static final String SW_CONNECT = "connect";
	public static final String SW_LOG = "log";
	public static final String SW_INIT = "init";
	public static final String SW_VERSION = "version";
	public static final String SW_SILENT = "silent";

	private static final String[] cmdSwitches = {
		"connect:string:::profileName OR ConnectionType,Username,Password[,SecurityToken]",
		"log:string:error:info,warning,error:Set the default message level written to stderr",
		"init:xfile:::SQL Force commands to run before processing additional user input",
		"version:none:::Print the version number of the program to stderr",
		"silent:none:::Do not issue a prompt. Useful when capturing stdout to a file"
	};


	public SQLForce() {

	}

	public void execute( String[] args ) {

		SQLForceCommandLineReader clr = new SQLForceCommandLineReader();
		
		PrintStream outPrintStream = new PrintStream( System.out );
		PrintStream errPrintStream = new PrintStream( System.err );
		

		SQLForceEnvironment env = new SQLForceEnvironment(outPrintStream, errPrintStream );

		CommandLineParser parser = new CommandLineParser(cmdSwitches);

		args = (null==args?new String[0]:args);
		parser.parse( args );

		//
		// Set up the lexical analyzer to read from the remaining file names on
		// the input line OR stdin if no filenames were specified.
		//
		// If there are extra arguments, assume that they are file names and push
		// them onto the Lexical reader stack.
		//
		LexicalAnalyzer lex;
		String remainingArgs[] = parser.getParams();
		
		if( 0 == remainingArgs.length ) {
			lex = new LexicalAnalyzer(System.in, outPrintStream );
			clr.setPrompt( "SQLForce> ");
		} else {
			lex = new LexicalAnalyzer( new ByteArrayInputStream( new byte[0]), outPrintStream );
			clr.setPrompt(null);
			
			for( int n = remainingArgs.length - 1; n >= 0; n-- ) {
				lex.include( "OPEN " + remainingArgs[n] );
			}
		}
		
		/**
		 * If requested, print version information to stderr.
		 * 
		 */
		if( parser.isSet(SW_VERSION)) {
			errPrintStream.println(
					Version.PROGRAM + " " + Version.VERSION
					+ "\n" + Version.COPYRIGHT
					+ "\n" + "Contact: " + Version.CONTACT 
					+ "\n"
					);
		}
		//
		// Set the default logging level.
		//
		String logLevel = parser.getString(SW_LOG);
		env.setenv( SQLForceEnvironment.LOG_LEVEL, logLevel);

		//
		// If the user supplied an initialization script then run it.
		//
		if( parser.isSet( SW_INIT )) {
			lex.include( "OPEN " + parser.getString(SW_INIT));
		}
		//
		// If connection parameters were given on the command line, then make sure
		// that a connect is the first command executed.
		//
		if( parser.isSet( SW_CONNECT )) {
			String[] connectParams = parser.getString(SW_CONNECT ).split(",");

			if( 1 == connectParams.length ) {
				lex.include( "CONNECT " 
						+ "PROFILE " + connectParams[0] );
				
				env.log("Connecting to Salesforce using profile " + connectParams[0] );
			} else if( connectParams.length >=3 &&  connectParams.length <=4) {
				lex.include( "CONNECT " 
						+ connectParams[0]
						                + " " + connectParams[1]
						                + " " + connectParams[2]
						                + (4==connectParams.length?(" " + connectParams[3]):"")
				);
				env.log("Connecting to Salesforce using " + connectParams[1] );
			} else {
				throw new IllegalArgumentException("The " + SW_CONNECT
						+ " must be in the form: '-connect PROFILE profileName' "
						+ " or '-connect [Production|Sandbox],Username,Password[,SecurityToken]'"
						
				);


			}

			
			

			
		}

		//
		// If the caller does not to see a prompt, then make it so.
		//
		if( parser.isSet( SW_SILENT)) {
			clr.setPrompt( (String)null);
		}

		clr.execute(lex, env);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		SQLForce force = new SQLForce();

		force.execute( args );


	}

}
