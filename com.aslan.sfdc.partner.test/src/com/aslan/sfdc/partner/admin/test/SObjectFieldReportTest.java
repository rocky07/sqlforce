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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import junit.framework.TestCase;

import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.partner.admin.SObjectFieldReport;
import com.aslan.sfdc.partner.test.SfdcTestEnvironment;

public class SObjectFieldReportTest extends TestCase {

	private LoginManager.Session session = SfdcTestEnvironment.getTestSession();
	
	public void testGetFieldReport() throws Exception {
		ByteArrayOutputStream bOut = new ByteArrayOutputStream();
		PrintStream pOut = new PrintStream(bOut);
		SObjectFieldReport mgr = new SObjectFieldReport( session );
		
		mgr.generateReport( "Product2", true, pOut);
		
		pOut.flush();
		assertTrue( bOut.toByteArray().length > 0 );
	}
}
