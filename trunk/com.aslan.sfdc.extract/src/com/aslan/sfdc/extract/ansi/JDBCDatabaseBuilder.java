/*******************************************************************************
 * Copyright (c) 2010 Gregory Smith (gsmithfarmer@gmail.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/

package com.aslan.sfdc.extract.ansi;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.aslan.sfdc.extract.IDatabaseBuilder;
import com.sforce.soap.partner.DescribeSObjectResult;
import com.sforce.soap.partner.Field;
import com.sforce.soap.partner.FieldType;

/**
 * Extract Salesforce database to any JDBC database.
 * 
 * This class is abstract because a few decisions about table creation must be made by a concrete class.
 * 
 * @author greg
 *
 */
public abstract class JDBCDatabaseBuilder implements IDatabaseBuilder {

	/**
	 * SAX Parser handler for picking up local configuration data.
	 * 
	 */
	private class ConfigHandler extends DefaultHandler
	{

		ConfigHandler( ) {}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			
			if( "MapName".equals(qName)) {
				String sfdc = attributes.getValue("sfdcName");
				String name = attributes.getValue("exportName");
				
				exportNameMap.put(sfdc, name);
			}
		}
		
		
	}

	protected abstract String getStringType( int width );
	protected abstract String getIntType( int width );
	protected abstract String getDecimalType( int width, int scale );
	
	
	public abstract Connection getConnection();
	protected abstract int getMaxVarcharLength();
	
	protected Map<String,String> exportNameMap = new HashMap<String,String>();

	private static SimpleDateFormat sfdcDateTimeParser = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS'Z'");
	private static SimpleDateFormat sfdcDateParser = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat sfdcTimeParser = new SimpleDateFormat("hh:mm:ss.SSS'Z'");
	
	public JDBCDatabaseBuilder() {

		sfdcDateTimeParser.setTimeZone( TimeZone.getTimeZone("GMT+0"));
		sfdcDateParser.setTimeZone( TimeZone.getTimeZone("GMT+0"));
		sfdcTimeParser.setTimeZone( TimeZone.getTimeZone("GMT+0"));
		
		//
		// Load configuration data
		//
		
		InputStream inStream = null;
		try {
			inStream = JDBCDatabaseBuilder.class.getResourceAsStream("JDBCDatabaseBuilderConfig.xml");
			SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
			ConfigHandler handler = new ConfigHandler();
			parser.parse(inStream, handler );
			
		} catch(Exception e) {
			e.printStackTrace(); // Never should happen. xml is packaged in the jar.
		} finally {
			if( null != inStream ) {
				try {
					inStream.close();
				} catch (IOException e) {
					;
				}
			}
		}
	}
	
	
	protected String getBooleanType() {
		return "BOOLEAN";
	}

	protected String getDateTimeType() {
		return "TIMESTAMP";
	}

	protected String getTimeType() {
		return "TIME";
	}
	
	protected String getDateType() {
		return "DATE";
	}

	protected String getCLOBType() {
		return "CLOB";
	}
	
	
	protected boolean isVarcharField( Field field ) {
		String fieldTypeName = field.getType().getValue();
		
		return FIELDTYPE_STRING.equals(fieldTypeName)
				|| FIELDTYPE_PHONE.equals(fieldTypeName)
				|| FIELDTYPE_EMAIL.equals(fieldTypeName)
				|| FIELDTYPE_TEXTAREA.equals(fieldTypeName)
				|| FIELDTYPE_URL.equals(fieldTypeName)
				|| FIELDTYPE_COMBOBOX.equals(fieldTypeName)
				|| FIELDTYPE_MULTIPICKLIST.equals(fieldTypeName)
				|| FIELDTYPE_PICKLIST.equals(fieldTypeName)
				;
	}
	
	protected boolean isIdField( Field field ) {
		String fieldTypeName = field.getType().getValue();
		
		return FIELDTYPE_STRING.equals(fieldTypeName)
				|| FIELDTYPE_PHONE.equals(fieldTypeName)
				|| FIELDTYPE_EMAIL.equals(fieldTypeName)
				|| FIELDTYPE_TEXTAREA.equals(fieldTypeName)
				|| FIELDTYPE_URL.equals(fieldTypeName)
				|| FIELDTYPE_COMBOBOX.equals(fieldTypeName)
				|| FIELDTYPE_MULTIPICKLIST.equals(fieldTypeName)
				|| FIELDTYPE_PICKLIST.equals(fieldTypeName)
				;
	}
	
	protected boolean isQuotedField( Field field ) {
		String fieldTypeName = field.getType().getValue();
		
		return isVarcharField(field)
			|| FIELDTYPE_ID.equals(fieldTypeName)
			|| FIELDTYPE_REFERENCE.equals(fieldTypeName)
			|| FIELDTYPE_BASE64.equals(fieldTypeName)
			;
	}
	
	protected boolean isIntegerField( Field field ) {
		
		String fieldTypeName = field.getType().getValue();
		
		if(FIELDTYPE_INT.equals( fieldTypeName)) { return true; }
		
		if( isDoubleField(field)
				&& 00==field.getScale()
			) { return true; }
		return false;
	}
	protected boolean isDoubleField( Field field ) {
		
		String fieldTypeName = field.getType().getValue();
		
		return
				FIELDTYPE_DOUBLE.equals(fieldTypeName)
				|| FIELDTYPE_PERCENT.equals(fieldTypeName)
				|| FIELDTYPE_CURRENCY.equals(fieldTypeName)
			;
	}
	
	protected String getExportedName( String columnOrTableName ) {
		
		if( exportNameMap.containsKey(columnOrTableName )) {
			return exportNameMap.get(columnOrTableName);
		}
		return columnOrTableName;
	}
	
	/* (non-Javadoc)
	 * @see com.aslan.sfdc.extract.IDatabaseBuilder#createTable(com.sforce.soap.partner.DescribeSObjectResult)
	 */
	@Override
	public void createTable(DescribeSObjectResult sfdcTable) throws Exception {
		
		List<String> columnDefs = new ArrayList<String>();
		
		for( Field field: sfdcTable.getFields()) {
			FieldType fieldType = field.getType();

			String fieldName = field.getName();
			
			String fieldTypeName = fieldType.getValue();
			int fieldLength = field.getByteLength();
			String nullOption = field.isNillable()?" NULL ":" NOT NULL ";
			nullOption = " NULL "; // See the Salesforce table GROUP -- the parameter is advisory only.
			String uniqueOption = field.isUnique()?" UNIQUE ":"";
			uniqueOption = ""; // Salesforce allows multiple null values on unique columns (for historical data);
			
			if( FIELDTYPE_ID.equals(fieldTypeName)) {
				
				columnDefs.add( getExportedName(fieldName) + " CHAR(" + fieldLength + ") PRIMARY KEY");
				
			} else if( isVarcharField( field )) {
					
				columnDefs.add( getExportedName(fieldName) + " " + getStringType(fieldLength) + " " + nullOption + uniqueOption );
				
			} else if( FIELDTYPE_INT.equals( fieldTypeName)) {

				columnDefs.add( getExportedName(fieldName) + " " + getIntType( field.getDigits()) + " " + nullOption + uniqueOption);
				
			} else if( FIELDTYPE_DOUBLE.equals(fieldTypeName)
					|| FIELDTYPE_PERCENT.equals(fieldTypeName)
					|| FIELDTYPE_CURRENCY.equals(fieldTypeName)){
				
				columnDefs.add( getExportedName(fieldName) 
						+ " " + getDecimalType(field.getPrecision(), field.getScale()) + " "
						+ nullOption + uniqueOption);
				
			} else if( FIELDTYPE_BOOLEAN.equals(fieldTypeName)) {
				
				columnDefs.add( getExportedName(fieldName) + " " + getBooleanType() + " " + nullOption + uniqueOption);
				
			}  else if( FIELDTYPE_REFERENCE.equals(fieldTypeName)) {
				
				columnDefs.add( getExportedName(fieldName) + " CHAR(" + fieldLength + ")" + nullOption + uniqueOption);
				
			}  else if( FIELDTYPE_DATETIME.equals(fieldTypeName)) {
				
				columnDefs.add( getExportedName(fieldName) +  " " + getDateTimeType() + " " + nullOption + uniqueOption);
				
			} else if( FIELDTYPE_DATE.equals(fieldTypeName)) {
				
				columnDefs.add( getExportedName(fieldName) + " " + getDateType() + " " + nullOption + uniqueOption);
				
			} else if( FIELDTYPE_TIME.equals(fieldTypeName)) {
				
				columnDefs.add( getExportedName(fieldName) + " " + getTimeType() + " " + nullOption + uniqueOption);
				
			} else if( FIELDTYPE_ANYTYPE.equals(fieldTypeName)) {
				
				columnDefs.add( getExportedName(fieldName) + " " + getCLOBType() + " ");
				
			} else if( FIELDTYPE_BASE64.equals(fieldTypeName)) {
				
				columnDefs.add( getExportedName(fieldName) + " " + getCLOBType() + " ");
				
			} else {
				throw new Exception("Unrecognized field type: " + fieldTypeName );
			}
			
		}

		StringBuffer sql = new StringBuffer();
		
		sql.append( "CREATE TABLE " + getExportedName(sfdcTable.getName()) + "(");
		for( int n = 0; n < columnDefs.size(); n++ ) {
			sql.append( ((n>0)?",":"") + columnDefs.get(n) );
		}
		sql.append(");");
		executeSQL( sql.toString());

	}
	
	
	protected int findSqlType( Field field ) throws Exception {
		FieldType fieldType = field.getType();
		String fieldTypeName = fieldType.getValue();
		int fieldLength = field.getByteLength();

		
		if( FIELDTYPE_ID.equals(fieldTypeName)) {
			return java.sql.Types.CHAR;
			
		} 
		if( isVarcharField( field )) {
			if( fieldLength > getMaxVarcharLength() ) { return java.sql.Types.CLOB; }
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
			return java.sql.Types.BOOLEAN;
		}
		
		if( FIELDTYPE_REFERENCE.equals(fieldTypeName)) {
			
			return java.sql.Types.CHAR;
			
		}

		if( FIELDTYPE_DATETIME.equals(fieldTypeName)) {
			
			return java.sql.Types.TIMESTAMP;
			
		} 
		
		if( FIELDTYPE_DATE.equals(fieldTypeName)) {
			
			return java.sql.Types.DATE;
			
		} 
		
		if( FIELDTYPE_TIME.equals(fieldTypeName)) {
			
			return java.sql.Types.TIME;
			
		}
		if( FIELDTYPE_ANYTYPE.equals(fieldTypeName)) {
			
			return java.sql.Types.CLOB;
			
		}
		if( FIELDTYPE_BASE64.equals(fieldTypeName)) {
			
			return java.sql.Types.CLOB;
			
		}
		throw new Exception("Unrecognized field type: " + fieldTypeName );
		
	}

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
	
	private Date sfdcDateToDate( String sfdcDate ) throws Exception {
		Timestamp tt = sfdcDateTimeToTimestamp(sfdcDate);
		
		return new Date(tt.getTime());
		
	}
	
	private Time sfdcDateToTime( String sfdcDate ) throws Exception {
		Timestamp tt = sfdcDateTimeToTimestamp(sfdcDate);
		
		return new Time(tt.getTime());
		
	}
	
	/**
	 * Salesforce uses timestamp ranges that are out of range for some databases (let the implementation decide what to do.
	 * 
	 * @param pstmt	set a parameter for this statement.
	 * @param index parameter index.
	 * @param timestamp the timestamp from salesforce.
	 * @throws Exception if anything goes wrong.
	 */
	protected void setTimestampField( PreparedStatement pstmt, int index, Timestamp timestamp ) throws Exception  {
		pstmt.setTimestamp( index, timestamp );
	}
	
	private void setPreparedStatementValue( PreparedStatement pstmt, Field field, String value, int index ) throws Exception{
		
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
				//
				// Use parseDouble because a SFDC double with zero digits afterwards may come back
				// from SFDC as 1.0.
				//
				pstmt.setInt(index, new Double(Double.parseDouble(value)).intValue());
			}
		} break;
		
		case java.sql.Types.DECIMAL: {
			pstmt.setDouble( index,Double.parseDouble(value));
		} break;
		
		case java.sql.Types.TIMESTAMP: {
			setTimestampField( pstmt, index, sfdcDateTimeToTimestamp(value) );

		} break;
		
		case java.sql.Types.DATE: {
			pstmt.setDate(index, sfdcDateToDate(value));
		} break;
		
		case java.sql.Types.TIME: {
			pstmt.setTime(index, sfdcDateToTime(value));
		} break;
		
		case java.sql.Types.BOOLEAN: {
			pstmt.setBoolean(index, "true".equalsIgnoreCase(value)?true:false);
		} break;
		
		default: {
			throw new Exception("Internal logic error. Unrecognized java.sql.Types of " + sqlType + " for field type " + field.getType().getValue());
		}
		}
		
	}
	

	public void executeSQL(String sql) throws Exception {
		Statement stmt = null;
		
		if( sql.endsWith(";")) {
			sql = sql.substring( 0, sql.length() - 1 );
		}

		try {
			stmt = getConnection().createStatement();
			stmt.executeUpdate(sql);
		} finally {
			if( null != stmt ) {
				stmt.close();
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
				if( isUpdateNecessary( lastModifiedDateField, fields, row, lastModDate )) { 
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
				for( int n = 0; n < insertRows.size(); n ++ ) {
					String[] row = dataRows.get(insertRows.get(n));
					for( int k = 0; k < row.length; k++ ) {
						Field field = fields[k];
						//if( field.getName().equalsIgnoreCase("BodyCrc")) { row[k] = "1"; }
						
						setPreparedStatementValue( pstmt, field, row[k], k+1);
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
				for( int n = 0; n < updateRows.size(); n ++ ) {
					String[] row = dataRows.get(updateRows.get(n));
					//
					// Set the ID on the WHERE Clause.
					//
					setPreparedStatementValue( pstmt, fields[0], row[0], row.length );
					
					//
					// Set all other parameters.
					//
					for( int k = 1; k < row.length; k++ ) {
						Field field = fields[k];
						setPreparedStatementValue( pstmt, field, row[k], k);
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
	
	
	/**
	 * Determine if the data in a row needs to be updated.
	 * 
	 * @param sfdcTable
	 * @param fields
	 * @param row
	 * @return
	 * @throws Exception
	 */
	protected boolean isUpdateNecessary(Field lastModifiedDateField, Field[] fields, String[] row, java.util.Date lastModDate ) throws Exception {
		//
		// Find the time of last modification in Salesforce.
		//
		if( null == lastModifiedDateField) { return true; }
		
		java.util.Date sfdcDate = null;
		for( int n=0; n < fields.length; n++ ) {
			Field field = fields[n];
			if( field.getName().equalsIgnoreCase(lastModifiedDateField.getName())) {
				String ansiDate = row[n];
				sfdcDate = sfdcDateToDate( ansiDate );

				break;
			}
		}
		
		if( null == sfdcDate )  { return false; }
		
		return sfdcDate.getTime() > lastModDate.getTime();
	}
	
	/**
	 * Determine the last date on which a record in the destination database was modified.
	 * 
	 * @param sfdcTable the salesforce table
	 * @param lastModifiedDateField the field that holds the most recent modification date.
	 * @param id unique salesforce record id.
	 * @return modification date or null if the data cannot be determined.
	 * @throws Exception if anything goes wrong.
	 */
	protected java.util.Date getLastModifiedDate(DescribeSObjectResult sfdcTable, Field lastModifiedDateField, String id ) throws Exception {
		
		if( null == lastModifiedDateField) { return null; }
		
		Statement stmt = null;
		ResultSet result = null;
		java.util.Date lastModDate = null;
		Connection connection = getConnection();
		if( null == connection) { return null; }
		
		try {
			stmt = connection.createStatement();
			result = stmt.executeQuery("SELECT " + getExportedName(lastModifiedDateField.getName()) + " FROM " + getExportedName(sfdcTable.getName()) + " WHERE id='" + id + "'");
			if( result.next()) {
				long gmtModTime = result.getTimestamp(1).getTime();
				
				lastModDate = new java.util.Date( gmtModTime );
			}
		} catch( Exception e ) {
			e.printStackTrace();
		} finally {
			if( null != result) { result.close(); }
			if( null != stmt ) { stmt.close(); }
		}
		
		return lastModDate;
	}
	
	@Override
	public boolean isTableNew(DescribeSObjectResult sfdcTable) throws Exception {
		Statement stmt = null;
		ResultSet result = null;
		boolean foundIt = false;
		Connection connection = getConnection();
		if( null == connection) { return false; }
		
		try {
			stmt = connection.createStatement();
			result = stmt.executeQuery("SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHERE UPPER(table_name)='" + getExportedName(sfdcTable.getName()).toUpperCase() + "'");
			foundIt = result.next();
		} catch( Exception e ) {
			e.printStackTrace();
		} finally {
			if( null != result) { result.close(); }
			if( null != stmt ) { stmt.close(); }
		}
		
		return !foundIt;
	}

}
