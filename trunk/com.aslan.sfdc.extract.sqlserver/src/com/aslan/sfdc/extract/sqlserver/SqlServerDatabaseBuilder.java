/**
 * 
 */
package com.aslan.sfdc.extract.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.aslan.sfdc.extract.ansi.AnsiDatabaseBuilder;

/**
 * Extract a Salesforce Database into a Microsoft SQL/Server Database.
 * 
 * @author snort
 *
 */
public class SqlServerDatabaseBuilder extends AnsiDatabaseBuilder {
	private Connection connection;
	
	public SqlServerDatabaseBuilder(  Connection connection ) throws Exception {
		this.connection = connection;
	}
	
	@Override
	protected String getExportedName(String columnOrTableName) {

		if( "CASE".equalsIgnoreCase(columnOrTableName)) {
			return "Case2";
		}
		if( "USER".equalsIgnoreCase(columnOrTableName)) {
			return "User2";
		}
		
		if( "COMMIT".equalsIgnoreCase(columnOrTableName)) {
			return "Commit2";
		}
		return super.getExportedName(columnOrTableName);
	}
	

	@Override
	protected boolean isMultiValueInsertSupported() {
		return false;
	}
	
	@Override
	public String getDecimalType(int width, int scale) {
		if( 0 == scale ) {
			return "INT";
		} else {
			return "DECIMAL(" + width + "," + scale + ")";
		}
			
	}


	@Override
	public String getIntType(int width) {
		return "INT";
	}


	@Override
	public String getStringType(int width) {
		if( width > 8000 ) {
			return "TEXT";
		}
		return "VARCHAR(" + width + ")";
	}
	
	@Override
	public String getBooleanType() {
		return "BIT";
	}

	@Override
	public String getCLOBType() {
		return "TEXT";
	}

	@Override
	public String getDateTimeType() {
		return "DATETIME";
	}

	@Override
	public String getDateType() {
		return "DATETIME";
	}

	@Override
	public String getTimeType() {
		return "DATETIME";
	}

	@Override
	protected String getTruthValue(boolean isTrue) {
		return isTrue?"1":"0";
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
