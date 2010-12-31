/**
 * 
 */
package com.aslan.sfdc.extract.oracle;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.aslan.sfdc.extract.ansi.JDBCDatabaseBuilder;
import com.sforce.soap.partner.DescribeSObjectResult;
import com.sforce.soap.partner.Field;
import com.sforce.soap.partner.FieldType;

/**
 * Extract a Salesforce Database into an Oracle Database.
 * 
 * @author snort
 *
 */
public class OracleDatabaseBuilder extends JDBCDatabaseBuilder {
	private Connection connection;
	
	public OracleDatabaseBuilder(  Connection connection ) throws Exception {
		this.connection = connection;
	}
	
	@Override
	protected int getMaxVarcharLength() {
		return 4000;
	}

	/**
	 * Oracle limits table/column names to 30 characters. 
	 */
	@Override
	public String getExportedName(String columnOrTableName) {
		
		if( columnOrTableName.length() > 30 ) {
			return columnOrTableName.substring(0, 30);
		}

		if( "USER".equalsIgnoreCase(columnOrTableName)) {
			return "USER2";
		}
		
		if( "NUMBER".equalsIgnoreCase(columnOrTableName)) {
			return "NUMBER2";
		}
		
		if( "GROUP".equalsIgnoreCase(columnOrTableName)) {
			return "GROUP2";
		}

		return super.getExportedName(columnOrTableName);
	}
	

	
	@Override
	public String getDecimalType(int width, int scale) {
		if( 0 == scale ) {
			return "NUMBER(" + width + ", 0)";
		} else {
			return "NUMBER(" + width + "," + scale + ")";
		}
			
	}


	@Override
	public String getIntType(int width) {
		return "DECIMAL(38,0)";
	}


	@Override
	public String getStringType(int width) {
		if( width > 4000 ) {
			return "CLOB";
		}
		return "VARCHAR2(" + width + ")";
	}
	
	@Override
	public String getBooleanType() {
		return "DECIMAL(1,0)";
	}

	@Override
	public String getCLOBType() {
		return "CLOB";
	}

	@Override
	public String getDateTimeType() {
		return "TIMESTAMP(3)";
	}

	@Override
	public String getDateType() {
		return "TIMESTAMP(3)";
	}

	@Override
	public String getTimeType() {
		return "TIMESTAMP(3)";
	}

	
	@Override
	public boolean isTableNew(DescribeSObjectResult sfdcTable) throws Exception {
		Statement stmt = null;
		ResultSet result = null;
		boolean foundIt = false;
		Connection connection = getConnection();
		if( null == connection) { return false; }
		
		String tableName = getExportedName(sfdcTable.getName()).toUpperCase();
		try {
			stmt = connection.createStatement();

			result = stmt.executeQuery("SELECT COUNT(*) FROM USER_TABLES WHERE UPPER(table_name)='" + tableName + "'");
			result.next();
			foundIt = (result.getInt(1)>0);
		} catch( Exception e ) {
			e.printStackTrace();
		} finally {
			if( null != result) { result.close(); }
			if( null != stmt ) { stmt.close(); }
		}
		
		return !foundIt;
	}

	@Override
	public Connection getConnection() {
		return connection;
	}
	
	protected int findSqlType( Field field ) throws Exception {
		FieldType fieldType = field.getType();
		String fieldTypeName = fieldType.getValue();

		
		if( FIELDTYPE_BOOLEAN.equals(fieldTypeName)) {
			return java.sql.Types.INTEGER;
		}
		
		if( FIELDTYPE_DATETIME.equals(fieldTypeName)) {
			
			return java.sql.Types.TIMESTAMP;
			
		} 
		
		if( FIELDTYPE_DATE.equals(fieldTypeName)) {
			
			return java.sql.Types.TIMESTAMP;
			
		} 
		
		if( FIELDTYPE_TIME.equals(fieldTypeName)) {
			
			return java.sql.Types.TIMESTAMP;
			
		}

		return super.findSqlType(field);

	}
	
}
