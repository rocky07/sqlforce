/*******************************************************************************
 * Copyright (c) 2010 Gregory Smith (gsmithfarmer@gmail.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/

package com.aslan.sfdc.extract.test;

import java.util.List;

import com.aslan.sfdc.extract.SchemaAnalyzer;
import com.aslan.sfdc.extract.SchemaAnalyzer.Table;
import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.partner.test.SfdcTestEnvironment;

import junit.framework.TestCase;

public class SchemaAnalyzerTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetTable() throws Exception {
		LoginManager.Session session = SfdcTestEnvironment.getTestSession();
		SchemaAnalyzer analyzer = new SchemaAnalyzer( session );
		
		Table table = analyzer.getTable("User");
		assertNotNull(table);
		
		try {
			table = analyzer.getTable("ThisIsNotATableName");
			fail("Found a non-existent table");
		} catch( Exception e ) {}
		
	}

	public void testGetTables() throws Exception {
		LoginManager.Session session = SfdcTestEnvironment.getTestSession();
		SchemaAnalyzer analyzer = new SchemaAnalyzer( session );

		List<Table> tables = analyzer.getTables();
		
		for( Table table : tables ) {
			System.err.print( table.getName() + " : " );
			for( Table ref : table.getTableReferences()) {
				System.err.print( ref.getName() + ", " );
			}
			System.err.println("");
			
		}
		
	}

}
