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

import junit.framework.TestCase;

import com.aslan.sfdc.sqlforce.SQLForceSession;
import com.aslan.sfdc.sqlforce.test.SQLForceFactory;

/**
 * Unit Tests for the SET command.
 * @author greg
 *
 */
public class SetTest extends TestCase {

	public void testExecute() throws Exception {
		SQLForceSession session = SQLForceFactory.getForceSession();
		
		session.runCommands( "SET BLUEBIRD=13");
		
		assertEquals( "13", session.getenv("BLUEBIRD"));
		
		session.runCommands( "SET REDBIRD=Hello $BLUEBIRD");
		assertEquals("Hello 13", session.getenv( "REDBIRD"));
		
		
		session.runCommands( "SET REDBIRD=");
		assertNull(session.getenv( "REDBIRD"));
		
		session.runCommands( "SET REDBIRD= ");
		assertEquals(" ", session.getenv( "REDBIRD"));
	}

}
