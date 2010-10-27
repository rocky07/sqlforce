/*******************************************************************************
 * Copyright (c) 2010 Gregory Smith (gsmithfarmer@gmail.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/

package com.aslan.sfdc.extract.h2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

import com.aslan.sfdc.extract.ansi.AnsiDatabaseBuilder;
import com.sforce.soap.partner.DescribeSObjectResult;

/**
 * Extract a Salesforce Database into a H2 Database.
 * 
 * @author greg
 *
 */
public class H2DatabaseBuilder extends AnsiDatabaseBuilder {

	private Connection connection;
	
	public H2DatabaseBuilder(  Connection connection ) throws Exception {
		this.connection = connection;
	}
	
	@Override
	protected String getDecimalType(int width, int scale) {
		if( 0 == scale ) {
			return "INTEGER";
		} else {
			return "DECIMAL(" + width + "," + scale + ")";
		}
			
	}


	@Override
	protected String getIntType(int width) {
		return "INTEGER";
	}


	@Override
	protected String getStringType(int width) {
		if( width > 32000 ) {
			return "CLOB";
		}
		return "VARCHAR(" + width + ")";
	}
	
	
	/* (non-Javadoc)
	 * @see com.aslan.sfdc.extract.ansi.AnsiDatabaseBuilder#executeSQL(java.lang.String)
	 */
	@Override
	public void executeSQL(String sql) throws Exception {
		PreparedStatement stmt = null;
		
		try {
			stmt = connection.prepareStatement(sql);
			stmt.execute();
		} finally {
			if( null != stmt ) {
				stmt.close();
			}
		}

	}

	@Override
	public Connection getConnection() {
		return connection;
	}

}
