/*******************************************************************************
 * Copyright (c) 2010 Gregory Smith (gsmithfarmer@gmail.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/

package com.aslan.sdfc.extract.h2.test;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

import junit.framework.TestCase;

import com.aslan.sfdc.extract.ExtractionManager;
import com.aslan.sfdc.extract.ExtractionRuleset;
import com.aslan.sfdc.extract.IDatabaseBuilder;
import com.aslan.sfdc.extract.IExtractionMonitor;
import com.aslan.sfdc.extract.SwingExtractionMonitor;
import com.aslan.sfdc.extract.ExtractionRuleset.TableRule;
import com.aslan.sfdc.extract.h2.H2DatabaseBuilder;
import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.partner.test.SfdcTestEnvironment;

/**
 * Extract a complete database (takes a long time).
 * 
 * @author greg
 * 
 */
public class FullDatabaseExtractTest extends TestCase {

	IExtractionMonitor monitor = new SwingExtractionMonitor();
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		Class.forName("org.h2.Driver");
		super.setUp();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testExtractAllTables() throws Exception {
		Connection connection = null;	
		File baseDatabaseName = new File("C:/tmp/MyTestH2Database");
		System.err.println("Extracting to: " + baseDatabaseName);
		try {
			connection = DriverManager.getConnection("jdbc:h2:"
					+ baseDatabaseName.getAbsolutePath(), "sa", "fred");

			//LoginManager.Session session = SfdcTestEnvironment.getTestSession();
			LoginManager.Session session = SfdcTestEnvironment.getSession("Sandbox");
			IDatabaseBuilder builder = new H2DatabaseBuilder( connection );
			
			ExtractionManager mgr = new ExtractionManager(session, builder);
			
			ExtractionRuleset rules = new ExtractionRuleset();
			
			//rules.includeTable(new TableRule("User", true));
			rules.excludeTable(new TableRule("Attachment"));
			rules.includeTable(new TableRule(".*", true));
			//rules.includeTable( new TableRule("Contact"));
			
			mgr.extractSchema( rules, monitor);
			mgr.extractData(rules, monitor);
			
		} finally {
			if (null != connection) {
				connection.close();
			}
			
			System.err.println("Database was: " + baseDatabaseName);
		}

	}
	

}
