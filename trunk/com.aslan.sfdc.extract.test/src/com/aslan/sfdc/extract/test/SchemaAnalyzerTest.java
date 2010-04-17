/*******************************************************************************
 * Copyright (c) 2010 Gregory Smith (gsmithfarmer@gmail.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/

package com.aslan.sfdc.extract.test;

import com.aslan.sfdc.extract.SchemaAnalyzer;
import com.aslan.sfdc.extract.SchemaAnalyzer.Table;
import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.partner.test.SfdcTestEnvironment;

import junit.framework.TestCase;

/**
 * @author greg
 *
 */
public class SchemaAnalyzerTest extends TestCase {

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Test method for {@link com.aslan.sfdc.extract.SchemaAnalyzer#getTable(java.lang.String)}.
	 */
	public void testGetTable() throws Exception {
		LoginManager.Session session = SfdcTestEnvironment.getTestSession();
		SchemaAnalyzer analyzer = new SchemaAnalyzer(session);
		
		Table table = analyzer.getTable("Contact");
		
		assertNotNull(table);
		System.err.println(table.getName());
	}

	/**
	 * Test method for {@link com.aslan.sfdc.extract.SchemaAnalyzer#getTables()}.
	 */
	public void testGetTables() {
		LoginManager.Session session = SfdcTestEnvironment.getTestSession();
	}

}
