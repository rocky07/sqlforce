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
 * Copy a Salesforce instance to a SQL Server database.
 * 
 * @author gsmithfarmer@gmail.com
 *
 */
public class CopyForceSQLServer extends CopyForce {
	public static final String SW_SQLSERVER = "sqlserver";

	
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
	 * @param args
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
