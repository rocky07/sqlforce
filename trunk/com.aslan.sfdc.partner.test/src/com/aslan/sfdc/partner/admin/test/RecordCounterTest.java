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
package com.aslan.sfdc.partner.admin.test;

import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.partner.admin.RecordCounter;
import com.aslan.sfdc.partner.test.SfdcTestEnvironment;

import junit.framework.TestCase;

public class RecordCounterTest extends TestCase {
	
	private LoginManager.Session session = SfdcTestEnvironment.getSession();
	
	public void testFindRecordCount() throws Exception {
		RecordCounter rr = new RecordCounter(session);
		
		int n = rr.findCount( "Account");
		
		assertTrue( n > 0 );

	}

	
}
