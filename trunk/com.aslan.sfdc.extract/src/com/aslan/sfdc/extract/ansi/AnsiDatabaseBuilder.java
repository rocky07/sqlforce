/*******************************************************************************
 * Copyright (c) 2010 Gregory Smith (gsmithfarmer@gmail.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/

package com.aslan.sfdc.extract.ansi;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import com.aslan.sfdc.extract.IDatabaseBuilder;
import com.sforce.soap.partner.DescribeSObjectResult;
import com.sforce.soap.partner.Field;
import com.sforce.soap.partner.FieldType;

/**
 * Extract Salesforce database by building ANSI SQL statements that can be used to construct the
 * equivalent database.
 * 
 * @author greg
 *
 */
public abstract class AnsiDatabaseBuilder implements IDatabaseBuilder {

	protected abstract String getStringType( int width );
	protected abstract String getIntType( int width );
	protected abstract String getDecimalType( int width, int scale );
	
	
	public abstract Connection getConnection();
	
	private DateFormat dateParser;
	
	public AnsiDatabaseBuilder() {
		dateParser = new SimpleDateFormat( "yyyy-mm-dd'T'HH:mm:ss.SSS'Z'");
		dateParser.setTimeZone( TimeZone.getTimeZone("GMT+0"));
	}
	
	protected boolean isMultiValueInsertSupported() {
		return true;
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
	
	protected String getTruthValue( boolean isTrue ) {
		return isTrue?"TRUE":"FALSE";
	}
	/**
	 * Run a bit of SQL created by the class.
	 * 
	 * @param sql run this SQL
	 * @throws Exception if anything fails.
	 */
	public abstract void executeSQL( String sql ) throws Exception;
	
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
	private interface IValueMaker {
		String makeValue( String value );
	}
	
	private class QuotedValueMaker implements IValueMaker {

		@Override
		public String makeValue(String value) {
			
			return "'" + value.replace("'", "''") + "'";
		}
	}
	
	private class IntegerValueMaker implements IValueMaker {

		@Override
		public String makeValue(String value) {
			Double dd = Double.parseDouble(value);
			return (new Integer(dd.intValue())).toString();
		}
	}
	
	private class DoubleValueMaker implements IValueMaker {

		@Override
		public String makeValue(String value) {
			Double dd = Double.parseDouble(value);
			return dd.toString();
		}
	}
	
	private class BooleanValueMaker implements IValueMaker {

		@Override
		public String makeValue(String value) {
			return getTruthValue("true".equalsIgnoreCase(value) );
		}
	}
	
	private class TimeValueMaker implements IValueMaker {

		@Override
		public String makeValue(String value) {
			return "'" + value + "'";
		}
	}
	
	private class DateValueMaker implements IValueMaker {

		@Override
		public String makeValue(String value) {
			return "'" + value + "'";
		}
	}
	
	private class DateTimeValueMaker implements IValueMaker {

		@Override
		public String makeValue(String value) {
			return "'" + value + "'";
		}
	}
	private class SkipValueMaker implements IValueMaker {

		@Override
		public String makeValue(String value) {
			return "null";
		}
	}
	protected String getLocalName(DescribeSObjectResult sfdcTable ) {
		if( "GROUP".equalsIgnoreCase(sfdcTable.getName())) {
			return "GROUP2";
		}
		
		return sfdcTable.getName();
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
			
			if( FIELDTYPE_ID.equals(fieldTypeName)) {
				
				columnDefs.add( fieldName + " CHAR(" + fieldLength + ") PRIMARY KEY");
				
			} else if( isVarcharField( field )) {
					
				columnDefs.add( fieldName + " " + getStringType(fieldLength) + " " + nullOption + uniqueOption );
				
			} else if( FIELDTYPE_INT.equals( fieldTypeName)) {
				
				columnDefs.add( fieldName + " " + getIntType( field.getDigits()) + " " + nullOption + uniqueOption);
				
			} else if( FIELDTYPE_DOUBLE.equals(fieldTypeName)
					|| FIELDTYPE_PERCENT.equals(fieldTypeName)
					|| FIELDTYPE_CURRENCY.equals(fieldTypeName)){
				
				columnDefs.add( fieldName 
						+ " " + getDecimalType(field.getPrecision(), field.getScale()) + " "
						+ nullOption + uniqueOption);
				
			} else if( FIELDTYPE_BOOLEAN.equals(fieldTypeName)) {
				
				columnDefs.add( fieldName + " " + getBooleanType() + " " + nullOption + uniqueOption);
				
			}  else if( FIELDTYPE_REFERENCE.equals(fieldTypeName)) {
				
				columnDefs.add( fieldName + " CHAR(" + fieldLength + ")" + nullOption + uniqueOption);
				
			}  else if( FIELDTYPE_DATETIME.equals(fieldTypeName)) {
				
				columnDefs.add( fieldName +  " " + getDateTimeType() + " " + nullOption + uniqueOption);
				
			} else if( FIELDTYPE_DATE.equals(fieldTypeName)) {
				
				columnDefs.add( fieldName + " " + getDateType() + " " + nullOption + uniqueOption);
				
			} else if( FIELDTYPE_TIME.equals(fieldTypeName)) {
				
				columnDefs.add( fieldName + " " + getTimeType() + " " + nullOption + uniqueOption);
				
			} else if( FIELDTYPE_ANYTYPE.equals(fieldTypeName)) {
				
				columnDefs.add( fieldName + " " + getCLOBType() + " ");
				
			} else if( FIELDTYPE_BASE64.equals(fieldTypeName)) {
				
				columnDefs.add( fieldName + " " + getCLOBType() + " ");
				
			} else {
				throw new Exception("Unrecognized field type: " + fieldTypeName );
			}
			
		}

		StringBuffer sql = new StringBuffer();
		
		sql.append( "CREATE TABLE " + getLocalName(sfdcTable) + "(");
		for( int n = 0; n < columnDefs.size(); n++ ) {
			sql.append( ((n>0)?",":"") + columnDefs.get(n) );
		}
		sql.append(");");
		executeSQL( sql.toString());
	}
	
	private IValueMaker[] getValueMakers( Field[] fields ) {
		IValueMaker valueMakers[] = new IValueMaker[fields.length];
		for( int n = 0; n < fields.length; n++ ) {
			Field field = fields[n];
			String fieldTypeName = field.getType().getValue();
			
			
			if( isQuotedField(field)) {
				valueMakers[n] = new QuotedValueMaker();
			} else if( isIntegerField(field)) {
				valueMakers[n] = new IntegerValueMaker();
			} else if( isDoubleField(field)) {
				valueMakers[n] = new DoubleValueMaker();
			} else if(FIELDTYPE_BOOLEAN.equals(fieldTypeName) ) {
				valueMakers[n] = new BooleanValueMaker();
			} else if( FIELDTYPE_DATETIME.equals( fieldTypeName)) {
				valueMakers[n] = new DateTimeValueMaker();
			} else if( FIELDTYPE_TIME.equals( fieldTypeName)) {
				valueMakers[n] = new TimeValueMaker();
			} else if( FIELDTYPE_DATE.equals( fieldTypeName)) {
				valueMakers[n] = new DateValueMaker();
			} else {
				valueMakers[n] = new SkipValueMaker();
			}
		}
		return valueMakers;
	}
	
	@Override
	public int insertData(DescribeSObjectResult sfdcTable, Field[] fields,
			List<String[]> dataRows) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO " + getLocalName(sfdcTable) + "(");
		IValueMaker valueMakers[] = getValueMakers(fields);
		int n2Insert = 0;
		int nSkipped = 0;
		
		for( int n = 0; n < fields.length; n++ ) {
			Field field = fields[n];
			
			sql.append( (n>0?",":"") + field.getName());

		}
		sql.append( ") VALUES");
		
		String sqlPrefix = sql.toString();
		
		if( isMultiValueInsertSupported()) {
			for( int n = 0; n < dataRows.size(); n ++ ) {
				String[] row = dataRows.get(n);

				java.util.Date lastModDate = getLastModifiedDate( sfdcTable, row[0] );
				if( null != lastModDate ) {
					if( isUpdateNecessary( sfdcTable, fields, row, lastModDate )) { 
						updateData( sfdcTable, fields, row );
					} else {
						nSkipped++;
					}
					continue;
				}
				if( n > 0 ) { sql.append( "\n,"); }
				sql.append("(");
				for( int k = 0; k < row.length; k++ ) {
					String value = row[k];
					if( null==value || 0==value.trim().length()) {
						value = "null";
					} else {
						value = valueMakers[k].makeValue(value);
					}
					sql.append( (k>0?",":"") + value );
				}
				sql.append(")");
				n2Insert++;
			}
			if( n2Insert > 0 ) {
				sql.append(";");
				executeSQL( sql.toString());
			}
		} else {
			for( int n = 0; n < dataRows.size(); n ++ ) {
				String[] row = dataRows.get(n);
				sql.setLength(0);
				sql.append( sqlPrefix );
				java.util.Date lastModDate = getLastModifiedDate( sfdcTable, row[0] );
				if( null != lastModDate ) {
					if( isUpdateNecessary( sfdcTable, fields, row, lastModDate )) { 
						updateData( sfdcTable, fields, row );
					} else {
						nSkipped++;
					}
					continue;
				}
				sql.append("(");
				for( int k = 0; k < row.length; k++ ) {
					String value = row[k];
					if( null==value || 0==value.trim().length()) {
						value = "null";
					} else {
						value = valueMakers[k].makeValue(value);
					}
					sql.append( (k>0?",":"") + value );
				}
				sql.append(")");
				executeSQL(sql.toString());
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
	private boolean isUpdateNecessary(DescribeSObjectResult sfdcTable, Field[] fields, String[] row, java.util.Date lastModDate ) throws Exception {
		//
		// Find the time of last modification in Salesforce.
		//
	
		java.util.Date sfdcDate = null;
		for( int n=0; n < fields.length; n++ ) {
			Field field = fields[n];
			if( field.getName().equalsIgnoreCase("systemmodstamp")) {
				String ansiDate = row[n];
				sfdcDate = dateParser.parse( ansiDate );

				break;
			}
		}
		
		if( null == sfdcDate )  { return false; }
		
		return sfdcDate.getTime() > lastModDate.getTime();
	}
	private void updateData(DescribeSObjectResult sfdcTable, Field[] fields,
			String[] row) throws Exception {

		IValueMaker valueMakers[] = getValueMakers(fields);
		QuotedValueMaker quoteMaker = new QuotedValueMaker();


		StringBuffer sql = new StringBuffer();
		sql.append( "UPDATE " + getLocalName(sfdcTable) + " SET " );
		String id = row[0];
		boolean firstValue = true;
		for( int n = 1; n < row.length; n++ ) {
			if( valueMakers[n] instanceof SkipValueMaker ) { continue; }
			String value = row[n];
			if( null==value || 0==value.trim().length()) {
				value = "null";
			} else {
				value = valueMakers[n].makeValue(value);
			}
			sql.append((firstValue?"":",") + fields[n].getName() + "=" + value);
			firstValue = false;
		}
		sql.append(" WHERE id=" + quoteMaker.makeValue(id));

		sql.append(";");
		executeSQL( sql.toString());
		

	}
	
	private java.util.Date getLastModifiedDate(DescribeSObjectResult sfdcTable, String id ) throws Exception {
		Statement stmt = null;
		ResultSet result = null;
		java.util.Date lastModDate = null;
		Connection connection = getConnection();
		if( null == connection) { return null; }
		
		try {
			stmt = connection.createStatement();
			result = stmt.executeQuery("SELECT systemmodstamp FROM " + getLocalName(sfdcTable) + " WHERE id='" + id + "'");
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
	
	private boolean isDataNew(DescribeSObjectResult sfdcTable, String id)
			throws Exception {
		java.util.Date cal = getLastModifiedDate( sfdcTable, id );
		return cal==null;
	}
	
	private boolean isDataOlder(DescribeSObjectResult sfdcTable, String id,
			Calendar calendar) throws Exception {
		
		java.util.Date dbDate = getLastModifiedDate( sfdcTable, id );
		
		return (null==dbDate)?true:(dbDate.getTime() < calendar.getTime().getTime());
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
			result = stmt.executeQuery("SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHERE table_name='" + getLocalName(sfdcTable).toUpperCase() + "'");
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
