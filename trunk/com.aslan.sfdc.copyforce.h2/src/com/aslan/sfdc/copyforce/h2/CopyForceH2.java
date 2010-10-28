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
 * Copy a Salesforce instance to a H2 database.
 * 
 * @author gsmithfarmer@gmail.com
 *
 */
public class CopyForceH2 extends CopyForce {

	public static final String SW_H2CONNECT = "h2";
	public static final String SW_H2USER = "h2user";
	public static final String SW_H2PASSWORD = "h2password";
	
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
	 * @param args
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
