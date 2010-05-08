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

import com.aslan.sfdc.extract.ansi.AnsiDatabaseBuilder;

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
	public String getDecimalType(int width, int scale) {
		if( 0 == scale ) {
			return "INTEGER";
		} else {
			return "DECIMAL(" + width + "," + scale + ")";
		}
			
	}


	@Override
	public String getIntType(int width) {
		return "INTEGER";
	}


	@Override
	public String getStringType(int width) {
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
//			System.err.println(sql.substring( 0, 40 ));
			stmt = connection.prepareStatement(sql);
			stmt.execute();
		} finally {
			if( null != stmt ) {
				stmt.close();
			}
		}

	}

}