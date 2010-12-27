/**
 * 
 */
package com.aslan.sfdc.extract.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.aslan.sfdc.extract.ansi.AnsiDatabaseBuilder;
import com.aslan.sfdc.extract.ansi.AnsiDatabaseBuilder.IValueMaker;
import com.aslan.sfdc.extract.ansi.AnsiDatabaseBuilder.QuotedValueMaker;
import com.aslan.sfdc.extract.ansi.AnsiDatabaseBuilder.SkipValueMaker;
import com.sforce.soap.partner.DescribeSObjectResult;
import com.sforce.soap.partner.Field;
import com.sforce.soap.partner.FieldType;

/**
 * Extract a Salesforce Database into an Oracle Database.
 * 
 * @author snort
 *
 */
public class OracleDatabaseBuilder extends AnsiDatabaseBuilder {
	private Connection connection;
	
	public OracleDatabaseBuilder(  Connection connection ) throws Exception {
		this.connection = connection;
	}
	
	/**
	 * Oracle limits table/column names to 30 characters. 
	 */
	@Override
	public String getExportedName(String columnOrTableName) {
		
		if( columnOrTableName.length() > 30 ) {
			return columnOrTableName.substring(0, 30);
		}
		/*
		if( "CASE".equalsIgnoreCase(columnOrTableName)) {
			return "Case2";
		}
		*/
		if( "USER".equalsIgnoreCase(columnOrTableName)) {
			return "USER2";
		}
		
		if( "NUMBER".equalsIgnoreCase(columnOrTableName)) {
			return "NUMBER2";
		}
		
		if( "GROUP".equalsIgnoreCase(columnOrTableName)) {
			return "GROUP2";
		}
		/*
		if( "COMMIT".equalsIgnoreCase(columnOrTableName)) {
			return "Commit2";
		}
		*/
		return super.getExportedName(columnOrTableName);
	}
	

	@Override
	protected String getNativeDateTime(String sfdcDateTime) {

		String rawValue = sfdcDateTime.replaceFirst("T", " ").replaceAll("Z", "");
		
		return "TO_TIMESTAMP ('" +  rawValue + "', 'YYYY-MM-DDHH24:MI:SS.FF')";

	}

	@Override
	protected String getNativeDate(String sfdcDate) {
	
		return "TO_DATE('" +  sfdcDate + "', 'YYYY-MM-DD')";
	}

	@Override
	protected boolean isMultiValueInsertSupported() {
		return false;
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
	protected String getTruthValue(boolean isTrue) {
		return isTrue?"1":"0";
	}

	private boolean isCLOBField( Field field ) {
		if( 1 < 2) { return false; }
		if( !isQuotedField(field)) { return false; }
		
		int fieldLength = field.getByteLength();
		if( fieldLength > 4000 ) { return true; }
		
		FieldType fieldType = field.getType();
		String fieldTypeName = fieldType.getValue();
		if( FIELDTYPE_ANYTYPE.equals(fieldTypeName)) { return true; }
		if( FIELDTYPE_BASE64.equals(fieldTypeName)) { return true; }
		return false;
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
	/* (non-Javadoc)
	 * @see com.aslan.sfdc.extract.ansi.AnsiDatabaseBuilder#executeSQL(java.lang.String)
	 */
	@Override
	public void executeSQL(String sql) throws Exception {
		Statement stmt = null;
		
		if( sql.endsWith(";")) {
			sql = sql.substring( 0, sql.length() - 1 );
		}

		try {
			stmt = connection.createStatement();
			stmt.executeUpdate(sql);
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
	
	private int findSqlType( Field field ) throws Exception {
		FieldType fieldType = field.getType();
		String fieldTypeName = fieldType.getValue();
		int fieldLength = field.getByteLength();

		
		if( FIELDTYPE_ID.equals(fieldTypeName)) {
			return java.sql.Types.CHAR;
			
		} 
		if( isVarcharField( field )) {
			if( fieldLength > 4000 ) { return java.sql.Types.CLOB; }
			return java.sql.Types.VARCHAR;
		}

		if( FIELDTYPE_INT.equals( fieldTypeName)) {
			return java.sql.Types.INTEGER;
		
		} 
		
		if( FIELDTYPE_DOUBLE.equals(fieldTypeName)
				|| FIELDTYPE_PERCENT.equals(fieldTypeName)
				|| FIELDTYPE_CURRENCY.equals(fieldTypeName)){
			
			return java.sql.Types.DECIMAL;
		
		} 
		
		if( FIELDTYPE_BOOLEAN.equals(fieldTypeName)) {
			return java.sql.Types.INTEGER;
		}
		
		if( FIELDTYPE_REFERENCE.equals(fieldTypeName)) {
			
			return java.sql.Types.CHAR;
			
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
		if( FIELDTYPE_ANYTYPE.equals(fieldTypeName)) {
			
			return java.sql.Types.CLOB;
			
		}
		if( FIELDTYPE_BASE64.equals(fieldTypeName)) {
			
			return java.sql.Types.CLOB;
			
		}
		throw new Exception("Unrecognized field type: " + fieldTypeName );

		
		
	}
	
	private static SimpleDateFormat sfdcDateTimeParser = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
	private static SimpleDateFormat sfdcDateParser = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sfdcTimeParser = new SimpleDateFormat("hh:mm:ss.SSS'Z'");
	private Timestamp sfdcDateTimeToTimestamp(String sfdcDateTime) throws Exception {
		
		if( sfdcDateTime.endsWith("Z")) {
			if( sfdcDateTime.contains("T")) {
				return new Timestamp(sfdcDateTimeParser.parse(sfdcDateTime).getTime());
			} else {
				return new Timestamp(sfdcTimeParser.parse(sfdcDateTime).getTime());
			}
		}
		return new Timestamp(sfdcDateParser.parse(sfdcDateTime).getTime());
	
	}
	private void setStatementValue( PreparedStatement pstmt, Field field, String value, int index ) throws Exception{
		
		int sqlType = findSqlType(field);
		if( null==value || 0==value.trim().length()) {
			pstmt.setNull( index, sqlType, (String)null);
			return;
		}
		
		FieldType fieldType = field.getType();
		String fieldTypeName = fieldType.getValue();
		
		switch( sqlType ) {
		case java.sql.Types.CLOB: 
		case java.sql.Types.CHAR:
		case java.sql.Types.VARCHAR:
		{
			pstmt.setString( index, value);
		} break;
		
		case java.sql.Types.INTEGER: {
			
			if( FIELDTYPE_BOOLEAN.equals(fieldTypeName)) {
				pstmt.setInt(index, "true".equalsIgnoreCase(value)?1:0);
			} else {
				pstmt.setInt(index, Integer.parseInt(value));
			}
		} break;
		
		case java.sql.Types.DECIMAL: {
			pstmt.setDouble( index,Double.parseDouble(value));
		} break;
		
		case java.sql.Types.TIMESTAMP: {
			pstmt.setTimestamp( index, sfdcDateTimeToTimestamp(value));
		} break;
		
		default: {
			throw new Exception("Internal logic error. Unrecognized java.sql.Types of " + sqlType + " for field type " + field.getType().getValue());
		}
		}
		
	}
	@Override
	public int insertData(DescribeSObjectResult sfdcTable, Field lastModifiedDateField,
			Field[] fields, List<String[]> dataRows) throws Exception {
		
	
		int nSkipped = 0;
		List<Integer> insertRows = new ArrayList<Integer>();
		List<Integer> updateRows = new ArrayList<Integer>();
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		
		/**
		 * Figure out which rows are updates and which are inserts.
		 */
		for( int n = 0; n < dataRows.size(); n++ ) {
			String[] row = dataRows.get(n);
			java.util.Date lastModDate = getLastModifiedDate( sfdcTable, lastModifiedDateField, row[0] );
			if( null != lastModDate ) {
				if( isUpdateNecessary( sfdcTable, fields, row, lastModDate )) { 
					updateRows.add(n);
				} else {
					nSkipped++;
				}
				continue;
			} else {
				insertRows.add(n);
			}
		}
		
		//
		// Do inserts first.
		//
		if( 0 != insertRows.size() ) {
			StringBuffer colNames = new StringBuffer();
			StringBuffer paramMarkers = new StringBuffer();
			
			for( int n = 0; n < fields.length; n++ ) {
				
				colNames.append( (n>0?",":"") + getExportedName(fields[n].getName()));
				paramMarkers.append(n>0?",?":"?");
				
			}
			pstmt = conn.prepareStatement("INSERT INTO " + getExportedName(sfdcTable.getName()) + "(" + colNames.toString() + ") VALUES (" + paramMarkers.toString() + ")" );
			
			try {
				for( int n = 0; n < dataRows.size(); n ++ ) {
					String[] row = dataRows.get(n);
					for( int k = 0; k < row.length; k++ ) {
						Field field = fields[k];
						setStatementValue( pstmt, field, row[k], k+1);
					}
					pstmt.addBatch();
				}
				pstmt.executeBatch();
			} finally {
				pstmt.close();
			}
		}
		
		//
		// Do Updates next.
		//
		if( 0 != updateRows.size()) {
			StringBuffer colNames = new StringBuffer();

			for( int n = 1; n < fields.length; n++ ) {
				colNames.append( (n>1?",":"") + getExportedName(fields[n].getName()) + "=?");
			}
			
			pstmt = conn.prepareStatement("UPDATE " + getExportedName(sfdcTable.getName()) 
					+ " SET " + colNames.toString()
					+ " WHERE id=?" );

			try {
				for( int n = 0; n < dataRows.size(); n ++ ) {
					String[] row = dataRows.get(n);
					//
					// Set the ID on the WHERE Clause.
					//
					setStatementValue( pstmt, fields[0], row[0], row.length );
					
					//
					// Set all other parameters.
					//
					for( int k = 1; k < row.length; k++ ) {
						Field field = fields[k];
						setStatementValue( pstmt, field, row[k], k);
					}
					
					pstmt.addBatch();
				}
				pstmt.executeBatch();
			} finally {
				pstmt.close();
			}
		
		}

		
		return nSkipped;
	}
	

}
