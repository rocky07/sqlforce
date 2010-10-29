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
 * Verify that an H2 Schema can be build from Salesforce.
 * 
 * @author greg
 * 
 */
public class SchemaBuilderTest extends TestCase {

	IExtractionMonitor monitor = new SwingExtractionMonitor();

	private void rmdir( File dir ) {
		if( dir.isDirectory()) {
			for( File file : dir.listFiles()) {
				rmdir( file );
			}
		}
		dir.delete();
	}
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
		File baseDatabaseName = File.createTempFile("TestH2Database", "");
		baseDatabaseName.deleteOnExit();

		try {
			connection = DriverManager.getConnection("jdbc:h2:"
					+ baseDatabaseName.getAbsolutePath(), "sa", "fred");

			LoginManager.Session session = SfdcTestEnvironment.getTestSession();
			IDatabaseBuilder builder = new H2DatabaseBuilder( connection );
			
			ExtractionManager mgr = new ExtractionManager(session, builder);
			
			ExtractionRuleset rules = new ExtractionRuleset();
			
			rules.includeTable(new TableRule(".*", true));
		
			monitor =  new SwingExtractionMonitor();
			mgr.extractSchema( rules, monitor);
	
			
		} finally {
			if (null != connection) {
				connection.close();
			}
			
			File dbFile = new File( baseDatabaseName.getAbsolutePath() + ".h2.db");
			dbFile.delete();
			File lobDir = new File(baseDatabaseName.getAbsolutePath() + ".lobs.db" );
			rmdir(lobDir);
			
			System.err.println("Database was: " + baseDatabaseName);
		}

	}
	

}
