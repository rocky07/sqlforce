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
package com.aslan.sfdc.sqlforce.command.test;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import junit.framework.TestCase;

import com.aslan.sfdc.sqlforce.ISQLForceCommand;
import com.aslan.sfdc.sqlforce.SQLForceCommandLineReader;
import com.aslan.sfdc.sqlforce.SQLForceSession;
import com.aslan.sfdc.sqlforce.test.SQLForceFactory;

/**
 * Unit Tests for the HELP command.
 * @author greg
 *
 */
public class HelpTest extends TestCase {

	public void testExecute() throws Exception {
		SQLForceSession session = SQLForceFactory.getForceSession();
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		
		session.runCommands( "HELP", outStream);
		
		assertTrue( outStream.size() > 0 );
		outStream.reset();
		
		session.runCommands( "HELP HELP", outStream);
		
		assertTrue( outStream.size() > 0 );
		outStream.reset();
		
		session.runCommands( "HELP QUIT", outStream);
		
		assertTrue( outStream.size() > 0 );
		outStream.reset();
		
		session.runCommands( "HELP NOTACOMMAND", outStream);
		
		assertTrue( outStream.size() > 0 );
		outStream.reset();
	}

	/**
	 * Verify that all commands return help.
	 * 
	 * @throws Exception
	 */
	public void testGetAllDetailHelp() throws Exception {
		SQLForceCommandLineReader reader = new SQLForceCommandLineReader();
		Map<String, ISQLForceCommand> commandMap = reader.getCommands();
		
		for( String name : commandMap.keySet()) {
			ISQLForceCommand cmd = commandMap.get(name);
			
			assertNotNull( cmd.getOneLineHelp());
			assertNotNull( cmd.getHelp());
		}
	}
}
