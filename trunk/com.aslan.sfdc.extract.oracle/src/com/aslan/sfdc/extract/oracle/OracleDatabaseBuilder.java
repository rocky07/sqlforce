/**
 * 
 */
package com.aslan.sfdc.extract.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
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
		System.err.println(connection);
	}
	
	/**
	 * Oracle limits table/column names to 30 characters. 
	 */
	@Override
	protected String getExportedName(String columnOrTableName) {
		
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
System.err.println(sql);
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
	
	@Override
	public int insertData(DescribeSObjectResult sfdcTable, Field lastModifiedDateField,
			Field[] fields, List<String[]> dataRows) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		StringBuffer valueMarkers = new StringBuffer();
		sql.append("INSERT INTO " + getExportedName(sfdcTable.getName()) + "(");
		IValueMaker valueMakers[] = getValueMakers(fields);
		int nSkipped = 0;
		List<Integer> clobFields = new ArrayList<Integer>();
		
		int nFieldsFound = 0;
		for( int n = 0; n < fields.length; n++ ) {
			Field field = fields[n];
			
			sql.append( (nFieldsFound>0?",":"") + getExportedName(field.getName()));
			valueMarkers.append((nFieldsFound>0?",":"") + "?");
			nFieldsFound++;
		}
		sql.append( ") VALUES(" + valueMarkers.toString() + ")");
		String preparedSql = sql.toString();
		
		String sqlPrefix = sql.toString();
		
		Connection conn = getConnection();
		PreparedStatement pstmt = null;
		try {
			for( int n = 0; n < dataRows.size(); n ++ ) {
				String[] row = dataRows.get(n);
				sql.setLength(0);
				sql.append( sqlPrefix );
				java.util.Date lastModDate = getLastModifiedDate( sfdcTable, lastModifiedDateField, row[0] );
				if( null != lastModDate ) {
					if( isUpdateNecessary( sfdcTable, fields, row, lastModDate )) { 
						updateData( sfdcTable, fields, clobFields, row );
					} else {
						nSkipped++;
					}
					continue;
				}


				sql.append("(");
				pstmt = conn.prepareStatement(preparedSql );
				for( int k = 0; k < row.length; k++ ) {
					Field field = fields[k];

					String value = row[k];
					if( null==value || 0==value.trim().length()) {
						continue;
					}
					value = valueMakers[k].makeValue(value);
					if( value.startsWith("'") && value.endsWith("'")) {
						pstmt.setString( k+1, value.substring(1, value.length() - 1));
						continue;
					}
					
					if( isIntegerField(field) ) {
						pstmt.setInt(k+1, Integer.parseInt(value));
						continue;
					}
					
					if( isDoubleField(field)) {
						pstmt.setDouble(k+1, Double.parseDouble(value));
						continue;
					}
					
					//TODO: Special Case other field types
	
				}
				sql.append(")");
				//
				// TODO: pstmt.executeUpdate() AND add batching support.
				executeSQL(sql.toString());
			}
		} finally {
			if( null != pstmt ) {
				pstmt.close();
			}
		}
		return nSkipped;
	}
	
	private void updateData(DescribeSObjectResult sfdcTable, Field[] fields, List<Integer> clobFields,
			String[] row) throws Exception {

		IValueMaker valueMakers[] = getValueMakers(fields);
		QuotedValueMaker quoteMaker = new QuotedValueMaker();


		StringBuffer sql = new StringBuffer();
		sql.append( "UPDATE " + getExportedName(sfdcTable.getName()) + " SET " );
		String id = row[0];
		boolean firstValue = true;
		for( int n = 1; n < row.length; n++ ) {
			if( valueMakers[n] instanceof SkipValueMaker ) { continue; }
			if( isCLOBField( fields[n])) { continue; }
			String value = row[n];
			if( null==value || 0==value.trim().length()) {
				value = "null";
			} else {
				value = valueMakers[n].makeValue(value);
			}
			sql.append((firstValue?"":",") + getExportedName(fields[n].getName()) + "=" + value);
			firstValue = false;
		}
		sql.append(" WHERE id=" + quoteMaker.makeValue(id));

		sql.append(";");
		executeSQL( sql.toString());
		
		for( int k : clobFields ) {
			System.err.println("Update CLOB " + fields[k].getName());
		}
		

	}
}
