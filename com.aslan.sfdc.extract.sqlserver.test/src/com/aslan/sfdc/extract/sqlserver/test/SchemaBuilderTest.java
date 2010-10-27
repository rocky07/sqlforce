/*******************************************************************************
 * Copyright (c) 2010 Gregory Smith (gsmithfarmer@gmail.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/

package com.aslan.sfdc.extract.sqlserver.test;

import java.sql.Connection;

import junit.framework.TestCase;

import com.aslan.sfdc.extract.ExtractionManager;
import com.aslan.sfdc.extract.ExtractionRuleset;
import com.aslan.sfdc.extract.IDatabaseBuilder;
import com.aslan.sfdc.extract.IExtractionMonitor;
import com.aslan.sfdc.extract.SwingExtractionMonitor;
import com.aslan.sfdc.extract.ExtractionRuleset.TableRule;
import com.aslan.sfdc.extract.sqlserver.SqlServerDatabaseBuilder;
import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.partner.test.JDBCTestEnvironment;
import com.aslan.sfdc.partner.test.SfdcTestEnvironment;

/**
 * Verify that an H2 Schema can be build from Salesforce.
 * 
 * @author greg
 * 
 */
public class SchemaBuilderTest extends TestCase {

	IExtractionMonitor monitor = new SwingExtractionMonitor();

	public void testExtractFullSchema() throws Exception {
		Connection connection = null;

		try {

			connection = JDBCTestEnvironment.getInstance().getConnection("copyforce.sqlserver");

			LoginManager.Session session = SfdcTestEnvironment.getTestSession();
			IDatabaseBuilder builder = new SqlServerDatabaseBuilder( connection );
			
			ExtractionManager mgr = new ExtractionManager(session, builder);
			
			ExtractionRuleset rules = new ExtractionRuleset();
			
			rules.includeTable(new TableRule(".*", false));
		
			monitor =  new SwingExtractionMonitor();
			mgr.extractSchema( rules, monitor);
	
			
		} finally {
			if (null != connection) {
				connection.close();
			}
	
		}

	}
	

}
