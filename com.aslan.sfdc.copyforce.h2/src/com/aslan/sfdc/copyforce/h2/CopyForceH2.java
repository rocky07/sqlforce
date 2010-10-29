/*******************************************************************************
 * Copyright (c) 2010 Gregory Smith (gsmithfarmer@gmail.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/

package com.aslan.sfdc.copyforce.h2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.aslan.parser.commandline.CommandLineParser;
import com.aslan.parser.commandline.CommandLineParser.SwitchDef;
import com.aslan.sfdc.copyforce.CopyForce;
import com.aslan.sfdc.extract.IDatabaseBuilder;
import com.aslan.sfdc.extract.h2.H2DatabaseBuilder;

/**
 * Copy a Salesforce database to a H2, creating the schema on the destination database as required.
 * 
 * CopyForce copies the schema and data from any Salesforce database to a new or existing H2 database. Both complete and incremental copying are supported.
 * <p>
 * This class only contains that CopyForce details that are specific to the H2 implementation. Most documentation and details are contained in the base class
 * {@link com.aslan.sfdc.copyforce.CopyForce}. However, the H2 implementation adds the following command line switches.
 * 
 * * <table border="2">
 * <tr align="left" valign="top">
 * <th>Command Line Switch</th><th>Description</th>
 * </tr>
 * <tr align="left" valign="top"><td>-h2 string</td>
 * <td>Connection string for accessing an existing (or creating a new) H2 database.
 * Examples:
 * <ul>
 * <li>-h2 C:/tmp/practiceH2</li>
 * </ul>
 * </td>
 * </tr>
 * 
 *  <tr align="left" valign="top"><td>-h2user string</td>
 *  <td>User name for the H2 database.
 *  </td>
 *  </tr>
 *  
 *  <tr align="left" valign="top"><td>-h2password string</td>
 *  <td>Password for the H2 database.
 *  </td>
 *  </tr>
 *  </table>
 *  <p>
 *  Example 1: Create a new H2 database that contains all tables from Salesforce.
 *  <blockquote>
 *  java -jar CopyForceH2.jar -salesforce Production,myname@gmail.com,myPassword,SecurityToken -h2 C:/tmp/SalesforceCopy -h2user sa -h2password aslan -schema -gui
 *  </blockquote>
 *  
 *  <p>
 *  Example 2: Update existing Account and Contact tables in an H2 database..
 *  <blockquote>
 *  java -jar CopyForceH2.jar -include "Account,Contact" -salesforce Production,myname@gmail.com,myPassword,SecurityToken -h2 C:/tmp/SalesforceCopy -h2user sa -h2password aslan  -gui
 *  </blockquote>
 * @author gsmithfarmer@gmail.com
 *
 */
public class CopyForceH2 extends CopyForce {

	private static final String SW_H2CONNECT = "h2";
	private static final String SW_H2USER = "h2user";
	private static final String SW_H2PASSWORD = "h2password";
	
	private static final SwitchDef[]  cmdSwitches = {
		new SwitchDef( "string", SW_H2CONNECT, "connection string for an H2 database")
		,new SwitchDef( "string", SW_H2USER, "error", "user name for connecting to an H2 database. If database is created this user will be used." )
		,new SwitchDef( "string", SW_H2PASSWORD, "password for connecting to an H2 database.")
	};

	
	private Connection connection = null;
	
	public CopyForceH2() {
		this.addCmdSwitches(cmdSwitches);

	}
	/* (non-Javadoc)
	 * @see com.aslan.sfdc.copyforce.CopyForce#getDatabaseBuilder()
	 */
	@Override
	protected IDatabaseBuilder getDatabaseBuilder(CommandLineParser parser) throws Exception {
		
		if( !parser.isSet(SW_H2CONNECT )) {
			throw new Exception("You must specified a H2 database using the -" + SW_H2CONNECT + " switch");
		}
		
		String connectString = parser.getString(SW_H2CONNECT);
		String username = parser.getString(SW_H2USER);
		String password = parser.getString(SW_H2PASSWORD);
		
		if( !connectString.startsWith("jdbc:h2:")) {
			connectString = "jdbc:h2:" + connectString;
		}
		
		connection = DriverManager.getConnection(connectString, username, password );
		return  new H2DatabaseBuilder( connection );
	}

	
	/**
	 * Copy Salesforce to H2.
	 * 
	 * @param args parameters controlling the copy.
	 */
	public static void main(String[] args) {
		CopyForceH2 copier = new CopyForceH2();
		
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
