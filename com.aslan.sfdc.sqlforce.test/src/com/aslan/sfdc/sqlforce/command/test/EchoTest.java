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

import junit.framework.TestCase;

import com.aslan.sfdc.sqlforce.SQLForceSession;
import com.aslan.sfdc.sqlforce.test.SQLForceFactory;

/**
 * Unit Tests for the ECHO command.
 * @author greg
 *
 */
public class EchoTest extends TestCase {

	public void testExecute() throws Exception {
		SQLForceSession session = SQLForceFactory.getForceSession();
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		
		session.runCommands( "ECHO Hello", bOut );
		String echo = new String( bOut.toByteArray());
		
		assertNotNull(echo);
		assertEquals( "Hello", echo.trim() );
		
		bOut.reset();
		session.getEnvironment().setenv( "EchoTest", "23");
		session.runCommands( "ECHO Hello $EchoTest", bOut );
		echo = new String( bOut.toByteArray());
		
		assertEquals( "Hello 23", echo.trim());
		
	}

}
