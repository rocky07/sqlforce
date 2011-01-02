/**
 * 
 */
package com.aslan.sfdc.extract.sqlserver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.TimeZone;

import com.aslan.sfdc.extract.ansi.JDBCDatabaseBuilder;
import com.sforce.soap.partner.Field;

/**
 * Extract a Salesforce Database into a Microsoft SQL/Server Database.
 * 
 * @author snort
 *
 */
public class SqlServerDatabaseBuilder extends JDBCDatabaseBuilder {
	
	private final int MAX_VARCHAR = 8000;
	private final int MAX_INTEGER_WIDTH = 9;
	
	private Connection connection;
	
	public SqlServerDatabaseBuilder(  Connection connection ) throws Exception {
		this.connection = connection;
	}
	
	@Override
	protected int getMaxVarcharLength() {

		return MAX_VARCHAR;
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
	public String getDecimalType(int width, int scale) {
		
		if( 0 == scale ) {
			return getIntType( width );
		} else {
			return "DECIMAL(" + width + "," + scale + ")";
		}
			
	}


	@Override
	public String getIntType(int width) {
		if( width > MAX_INTEGER_WIDTH) {
			return "DECIMAL(" + width + ",0)";
		}
		return "INT";
	}


	@Override
	public String getStringType(int width) {
		if( width > getMaxVarcharLength() ) {
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
	public Connection getConnection() {
		return connection;
	}
	
	@Override
	protected int findSqlType( Field field ) throws Exception {

		String fieldTypeName = field.getType().getValue();
		
		if( FIELDTYPE_DATE.equals(fieldTypeName)) {
			return java.sql.Types.TIMESTAMP;
		} 
		
		if( FIELDTYPE_TIME.equals(fieldTypeName)) {
			return java.sql.Types.TIMESTAMP;
		}
		
		return super.findSqlType(field);

	}


	@Override
	protected void setTimestampField(PreparedStatement pstmt, int index,
			Timestamp timestamp) throws Exception {

		Calendar beginDate = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

		beginDate.set(1800, 0, 0, 0, 0, 0);
		
		Timestamp beginTimestamp = new Timestamp( beginDate.getTime().getTime());
		
		if( beginTimestamp.after(timestamp)) {
			timestamp = beginTimestamp;
		}
	
		super.setTimestampField(pstmt, index, timestamp);
	}


	
}
