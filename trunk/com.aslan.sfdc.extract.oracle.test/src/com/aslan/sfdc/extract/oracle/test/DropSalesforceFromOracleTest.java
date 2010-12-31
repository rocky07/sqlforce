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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

import com.aslan.sfdc.extract.IExtractionMonitor;
import com.aslan.sfdc.extract.oracle.OracleDatabaseBuilder;
import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.partner.test.JDBCTestEnvironment;
import com.aslan.sfdc.partner.test.JDBCTestEnvironment.Credentials;
import com.aslan.sfdc.partner.test.SfdcTestEnvironment;
import com.sforce.soap.partner.DescribeGlobalResult;
import com.sforce.soap.partner.DescribeGlobalSObjectResult;

/**
 * Drop all salesforce tables from an existing oracle instance.
 * 
 * @author greg
 * 
 */
public class DropSalesforceFromOracleTest extends TestCase {

	IExtractionMonitor monitor;

	List<String> allTableNames = new ArrayList<String>();
	
	private List<String> getAllTableNames(LoginManager.Session session) throws Exception {
		if( 0 == allTableNames.size()) {
			DescribeGlobalResult describeGlobalResult = session.getBinding().describeGlobal();
			
			DescribeGlobalSObjectResult resultList[] = describeGlobalResult.getSobjects();
			for( DescribeGlobalSObjectResult result : resultList ) {
				allTableNames.add( result.getName());

			}
		}
		
		return allTableNames;
	}
	public void testDropSalesforceTablesFromOracle() throws Exception {
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

			LoginManager.Session session = SfdcTestEnvironment.getReadOnlySession();
			OracleDatabaseBuilder builder = new OracleDatabaseBuilder( connection );
			
			for( String sfdcTable : getAllTableNames(session)) {
				try {
					String name = builder.getExportedName(sfdcTable);
				
					System.err.println("Dropping: " + sfdcTable );
					builder.executeSQL("DROP TABLE " + name );
				} catch(Exception e ) {
					System.err.println("Failed to drop: " + sfdcTable );
				}
			}
	
	
			
		} finally {
			if (null != connection) {
				connection.close();
			}
	
		}

	}
	

}
