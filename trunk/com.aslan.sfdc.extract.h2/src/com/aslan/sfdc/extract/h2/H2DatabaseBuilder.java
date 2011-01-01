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

import com.aslan.sfdc.extract.ansi.JDBCDatabaseBuilder;
import com.sforce.soap.partner.Field;

/**
 * Extract a Salesforce Database into a H2 Database.
 * 
 * @author greg
 *
 */
public class H2DatabaseBuilder extends JDBCDatabaseBuilder {

	private Connection connection;
	private final int MAX_VARCHAR = 4000;
	private final int MAX_INTEGER_WIDTH = 9;
	
	public H2DatabaseBuilder(  Connection connection ) throws Exception {
		this.connection = connection;
	}
	
	@Override
	protected int getMaxVarcharLength() {
		return MAX_VARCHAR;
	}

	@Override
	protected String getDecimalType(int width, int scale) {
		if( 0 == scale ) {
			return getIntType( width );
		} else {
			return "DECIMAL(" + width + "," + scale + ")";
		}
			
	}


	@Override
	protected String getIntType(int width) {
		if( width > MAX_INTEGER_WIDTH) {
			return "DECIMAL(" + width + ",0)";
		}
		return "INTEGER";
	}


	@Override
	protected String getStringType(int width) {
		if( width > MAX_VARCHAR ) {
			return "CLOB";
		}
		return "VARCHAR(" + width + ")";
	}
	

	@Override
	public Connection getConnection() {
		return connection;
	}

	protected int findSqlType( Field field ) throws Exception {

		if( isIntegerField(field) && field.getDigits() > MAX_INTEGER_WIDTH) {
			return java.sql.Types.DECIMAL;
		}
		
		return super.findSqlType(field);

	}
}
