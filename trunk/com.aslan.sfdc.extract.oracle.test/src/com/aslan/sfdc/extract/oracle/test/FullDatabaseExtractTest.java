/*******************************************************************************
 * Copyright (c) 2010 Gregory Smith (gsmithfarmer@gmail.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/

package com.aslan.sfdc.extract.oracle.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import junit.framework.TestCase;

import com.aslan.sfdc.extract.ExtractionManager;
import com.aslan.sfdc.extract.ExtractionRuleset;
import com.aslan.sfdc.extract.IDatabaseBuilder;
import com.aslan.sfdc.extract.IExtractionMonitor;
import com.aslan.sfdc.extract.OutputStreamExtractionMonitor;
import com.aslan.sfdc.extract.SwingExtractionMonitor;
import com.aslan.sfdc.extract.ExtractionRuleset.TableRule;
import com.aslan.sfdc.extract.oracle.OracleDatabaseBuilder;
import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.partner.test.JDBCTestEnvironment;
import com.aslan.sfdc.partner.test.JDBCTestEnvironment.Credentials;
import com.aslan.sfdc.partner.test.SfdcTestEnvironment;

/**
 * Verify that an all SFDC tables can be extracted.
 * 
 * @author greg
 * 
 */
public class FullDatabaseExtractTest extends TestCase {

	IExtractionMonitor monitor;

	public void testExtractFullSchema() throws Exception {
		Connection connection = null;

		try {
			/**
			 * For a connection where oracle will handle large strings (CLOBs) automatically.
			 */
			Credentials credentials  = JDBCTestEnvironment.getInstance().getCredentials("copyforce.oracle");
			if( null == credentials.connection) {
				if(null != credentials.driver) {
					Class.forName(credentials.driver);
				}
				Properties props = new Properties();
				props.put( "user", credentials.username );
				props.put( "password", credentials.password );
				props.put( "SetBigStringTryClob", "true" );
				Connection con = DriverManager.getConnection( credentials.url, props );
				credentials.connection = con;
			}
			
			connection = JDBCTestEnvironment.getInstance().getConnection("copyforce.oracle");

			LoginManager.Session session = SfdcTestEnvironment.getTestSession();
			IDatabaseBuilder builder = new OracleDatabaseBuilder( connection );
			
			ExtractionManager mgr = new ExtractionManager(session, builder);
			
			ExtractionRuleset rules = new ExtractionRuleset();
			
			rules.includeTable(new TableRule("Attachment", false));

		
			//monitor =  new SwingExtractionMonitor();
			monitor = new OutputStreamExtractionMonitor( System.err );
			mgr.extractSchema( rules, monitor);
			mgr.extractData( rules, monitor);
	
			
		} finally {
			if (null != connection) {
				connection.close();
			}
	
		}

	}
	

}
