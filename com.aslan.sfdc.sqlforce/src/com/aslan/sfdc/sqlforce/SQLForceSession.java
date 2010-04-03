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
/**
 * 
 */
package com.aslan.sfdc.sqlforce;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Convenience wrapper for managing a SQLForce session that most likely 
 * embedded inside of an application.
 * 
 * The class maintains a constant environment (and thus a SalesForce session).
 * 
 * @author gsmithfarmer@gmail.com
 *
 */
public class SQLForceSession {

	class OutputSinkStream extends OutputStream {

		@Override
		public void write(int arg0) throws IOException {
			
		}
		
	}
	private SQLForceEnvironment env = new SQLForceEnvironment();
	
	public SQLForceSession() {
		
	}
	
	public SQLForceEnvironment getEnvironment() {
		return env;
	}
	
	
	public  void runCommands( InputStream inStream, OutputStream outStream, OutputStream errStream ) {
		
		PrintStream errPrintStream = new PrintStream(errStream);
		
		LexicalAnalyzer lex = new LexicalAnalyzer( inStream, new PrintStream(outStream) );
		lex.setPrompt((String)null);
		
		SQLForceCommandLineReader force = new SQLForceCommandLineReader();
		
		env.setStdoutStream( new PrintStream(outStream));
		env.setStderrStream( errPrintStream );
		
		
		force.execute( lex, env);
		errPrintStream.flush();
		return;
	}
	
	public  void runCommands( InputStream inStream, OutputStream outStream ) throws Exception {
		
		ByteArrayOutputStream errByteStream = new ByteArrayOutputStream();
		PrintStream errStream = new PrintStream(errByteStream);
		
		LexicalAnalyzer lex = new LexicalAnalyzer( inStream, new PrintStream(outStream) );
		lex.setPrompt((String)null);
		
		SQLForceCommandLineReader force = new SQLForceCommandLineReader();
		
		env.setStdoutStream( new PrintStream(outStream));
		env.setStderrStream( errStream );
		
		force.execute( lex, env);
		errStream.close();
		
		if( 0==errByteStream.size()) { return; }
		
		String errorMsg = new String( errByteStream.toByteArray());
		
		throw new Exception( errorMsg);
	}
	
	public void runCommands( String cmds, OutputStream outStream ) throws Exception {
		runCommands( new ByteArrayInputStream( cmds.getBytes()), outStream);
	}
	
	public void runCommands( String cmds ) throws Exception {
		runCommands( cmds, new OutputSinkStream());
	}
	
	public String getenv( String variable ) {
		return env.getenv( variable );
	}
}
