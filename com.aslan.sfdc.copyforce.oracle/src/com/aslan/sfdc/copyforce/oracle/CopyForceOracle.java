/*******************************************************************************
 * Copyright (c) 2010 Gregory Smith (gsmithfarmer@gmail.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/

package com.aslan.sfdc.copyforce.oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import com.aslan.parser.commandline.CommandLineParser;
import com.aslan.parser.commandline.CommandLineParser.SwitchDef;
import com.aslan.sfdc.copyforce.CopyForce;
import com.aslan.sfdc.extract.IDatabaseBuilder;
import com.aslan.sfdc.extract.oracle.OracleDatabaseBuilder;

/**
 * Copy a Salesforce database to a Oracle, creating the schema on the destination database as required.
 * 
 * CopyForce copies the schema and data from any Salesforce database to a Oracle database. Both complete and incremental copying are supported.
 * <p>
 * This class only contains that CopyForce details that are specific to the Oracle implementation. Most documentation and details are contained in the base class
 * {@link com.aslan.sfdc.copyforce.CopyForce}. However, the Oracle implementation adds the following command line switches.
 * 
 <table border="2">
 <tr align="left" valign="top">
 <th>Command Line Switch</th><th>Description</th>
 </tr>
 <tr align="left" valign="top"><td>-oracle string</td>
 <td>Connection string for accessing Oracle. You can leave off the "jdbc:oracle:thin:@" and the system will add it for you.
 Examples:
 <ul>
 <li>-oracle  "localhost:1521:xe"</li>

 </ul>
 Internally, the connection string will be passed to the Oracle <i>DriverManager.getConnection()</i> method (prefixed by <i>jdbc:oracle:thin</i>).
 </td>
 </tr>
 
 </table>
 *  <p>
 *  Example 1: Copy all Salesforce tables to an empty Oracle database.
 *  <blockquote>
 *  java -jar CopyForceOracle.jar -salesforce Production,myname@gmail.com,myPassword,SecurityToken 
 *  -oracle "localhost:1521:xe" -orauser fred -orapassword joker -schema -gui
 *  </blockquote>
 *  
 *  <p>
 *  Example 2: Update existing Account and Contact tables in SQL Server database..
 *  <blockquote>
 *  java -jar CopyForceSqlServer.jar -include "Account,Contact" -salesforce Production,myname@gmail.com,myPassword,SecurityToken  -gui 
 *  -oracle "localhost:1521:xe" -orauser fred -orapassword joker
 *  </blockquote>
 * @author gsmithfarmer@gmail.com
 *
 */
public class CopyForceOracle extends CopyForce {
	private static final String SW_ORACLE = "oracle";
	private static final String SW_USER = "orauser";
	private static final String SW_PASSWORD = "orapassword";
	private static final String DRIVER_PREFIX = "jdbc:oracle:thin:@";

	
	private static final SwitchDef[]  cmdSwitches = {
		new SwitchDef( "string", SW_ORACLE, "connection string for an Oracle database")
		,new SwitchDef( "string", SW_USER, "user name to use when connecting to Oracle")
		,new SwitchDef( "string", SW_PASSWORD, "password to use when connecting to Oracle")

	};

	
	private Connection connection = null;
	
	public CopyForceOracle() {
		this.addCmdSwitches(cmdSwitches);
	}
	
	@Override
	protected IDatabaseBuilder getDatabaseBuilder(CommandLineParser parser) throws Exception {
		
		if( !parser.isSet(SW_ORACLE )) {
			throw new Exception("You must specified an Oracle database using the -" + SW_ORACLE + " switch");
		}
		
		String connectString = parser.getString(SW_ORACLE);
		
		if( !connectString.startsWith(DRIVER_PREFIX)) {
			connectString = DRIVER_PREFIX + connectString;
		}
		
		Properties props = new Properties();
		if( parser.isSet( SW_USER)) { props.put( "user", parser.getString( SW_USER) );}
		if( parser.isSet( SW_PASSWORD)) { props.put( "password", parser.getString(SW_PASSWORD)); }
		
		props.put( "SetBigStringTryClob", "true" );
		
		Class.forName("oracle.jdbc.driver.OracleDriver");
		Connection connection = DriverManager.getConnection( connectString, props );

		return  new OracleDatabaseBuilder( connection );
	}
	/**
	 * Copy Salesforce to Oracle.
	 * 
	 * @param args parameters controlling the copy.
	 */
	public static void main(String[] args) {
		CopyForceOracle copier = new CopyForceOracle();
		
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
