/*******************************************************************************
 * Copyright (c) 2010 Gregory Smith (gsmithfarmer@gmail.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/

package com.aslan.sfdc.extract.ansi;

import java.io.OutputStream;
import java.sql.Connection;
import java.util.Calendar;

import com.sforce.soap.partner.DescribeSObjectResult;

/**
 * Write all SQL statements that could be used to recreate a Salesforce Database to a stream.
 * @author greg
 *
 */
public class SQLEmitterDatabaseBuilder extends AnsiDatabaseBuilder {

	private OutputStream emitStream;
	
	public SQLEmitterDatabaseBuilder( OutputStream outStream ) {
		emitStream = outStream;
		
	}
	
	
	@Override
	public String getDecimalType(int width, int scale) {
		return "DECIMAL(" + width + "," + scale + ")";
	}


	@Override
	public String getIntType(int width) {
		return "DECIMAL(" + width + ",0)";
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
		emitStream.write( sql.getBytes() );
		emitStream.write( '\n' );

	}


	@Override
	public boolean isTableNew(DescribeSObjectResult sfdcTable) throws Exception {
		return false;
	}


	@Override
	public Connection getConnection() {
		return null;
	}

}
