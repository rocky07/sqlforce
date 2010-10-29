/*******************************************************************************
 * Copyright (c) 2010 Gregory Smith (gsmithfarmer@gmail.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/

package com.aslan.sfdc.copyforce.sqlserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.aslan.parser.commandline.CommandLineParser;
import com.aslan.parser.commandline.CommandLineParser.SwitchDef;
import com.aslan.sfdc.copyforce.CopyForce;
import com.aslan.sfdc.extract.IDatabaseBuilder;
import com.aslan.sfdc.extract.sqlserver.SqlServerDatabaseBuilder;

/**
 * Copy a Salesforce database to a SQL Server, creating the schema on the destination database as required.
 * 
 * CopyForce copies the schema and data from any Salesforce database to a SQL Server database. Both complete and incremental copying are supported.
 * <p>
 * This class only contains that CopyForce details that are specific to the SQL Server implementation. Most documentation and details are contained in the base class
 * {@link com.aslan.sfdc.copyforce.CopyForce}. However, the SQL Server implementation adds the following command line switches.
 * 
 <table border="2">
 <tr align="left" valign="top">
 <th>Command Line Switch</th><th>Description</th>
 </tr>
 <tr align="left" valign="top"><td>-sqlserver string</td>
 <td>Connection string for accessing SQL Server.
 Examples:
 <ul>
 <li>-sqlserver  "//localhost;databaseName=sqlforcetest;username=me;password=caleb&noah;"</li>
 </ul>
 Internally, the connection string will be passed to the SQL Server <i>DriverManager.getConnection()</i> method (prefixed by <i>jdbc:sqlserver:</i>).
 </td>
 </tr>
 
 </table>
 *  <p>
 *  Example 1: Copy all Salesforce tables to an empty SQL Server database.
 *  <blockquote>
 *  java -jar CopyForceSqlServer.jar -salesforce Production,myname@gmail.com,myPassword,SecurityToken 
 *  -sqlserver "//localhost;databaseName=sqlforcetest;username=me;password=caleb&noah;" -schema -gui
 *  </blockquote>
 *  
 *  <p>
 *  Example 2: Update existing Account and Contact tables in SQL Server database..
 *  <blockquote>
 *  java -jar CopyForceSqlServer.jar -include "Account,Contact" -salesforce Production,myname@gmail.com,myPassword,SecurityToken  -gui 
 *  -sqlserver "//localhost;databaseName=sqlforcetest;username=me;password=caleb&noah;"
 *  </blockquote>
 * @author gsmithfarmer@gmail.com
 *
 */
public class CopyForceSQLServer extends CopyForce {
	private static final String SW_SQLSERVER = "sqlserver";

	
	private static final SwitchDef[]  cmdSwitches = {
		new SwitchDef( "string", SW_SQLSERVER, "connection string for a SQLServer database")

	};

	
	private Connection connection = null;
	
	public CopyForceSQLServer() {
		this.addCmdSwitches(cmdSwitches);
	}
	
	@Override
	protected IDatabaseBuilder getDatabaseBuilder(CommandLineParser parser) throws Exception {
		
		if( !parser.isSet(SW_SQLSERVER )) {
			throw new Exception("You must specified a SQL Server database using the -" + SW_SQLSERVER + " switch");
		}
		
		String connectString = parser.getString(SW_SQLSERVER);
		
		if( !connectString.startsWith("jdbc:sqlserver:")) {
			connectString = "jdbc:sqlserver:" + connectString;
		}
		
		connection = DriverManager.getConnection(connectString);
		return  new SqlServerDatabaseBuilder( connection );
	}
	/**
	 * Copy Salesforce to SQL Server.
	 * 
	 * @param args parameters controlling the copy.
	 */
	public static void main(String[] args) {
		CopyForceSQLServer copier = new CopyForceSQLServer();
		
		try {
			copier.execute( args );
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if( null != copier.connection ) {
				try {
					copier.connection.close();
				} catch (SQLException e1) {
					;
				}
			}
		}
		System.exit(0);

	}

}
