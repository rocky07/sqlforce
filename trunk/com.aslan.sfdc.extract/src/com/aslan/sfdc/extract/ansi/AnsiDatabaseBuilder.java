/*******************************************************************************
 * Copyright (c) 2010 Gregory Smith (gsmithfarmer@gmail.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/

package com.aslan.sfdc.extract.ansi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	public abstract String getStringType( int width );
	public abstract String getIntType( int width );
	public abstract String getDecimalType( int width, int scale );
	
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
			return "true".equalsIgnoreCase(value)?"TRUE":"FALSE";
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
				
				columnDefs.add( fieldName + " BOOLEAN " + nullOption + uniqueOption);
				
			}  else if( FIELDTYPE_REFERENCE.equals(fieldTypeName)) {
				
				columnDefs.add( fieldName + " CHAR(" + fieldLength + ")" + nullOption + uniqueOption);
				
			}  else if( FIELDTYPE_DATETIME.equals(fieldTypeName)) {
				
				columnDefs.add( fieldName + " TIMESTAMP " + nullOption + uniqueOption);
				
			} else if( FIELDTYPE_DATE.equals(fieldTypeName)) {
				
				columnDefs.add( fieldName + " DATE " + nullOption + uniqueOption);
				
			} else if( FIELDTYPE_TIME.equals(fieldTypeName)) {
				
				columnDefs.add( fieldName + " TIME " + nullOption + uniqueOption);
				
			} else if( FIELDTYPE_ANYTYPE.equals(fieldTypeName)) {
				
				columnDefs.add( fieldName + " CLOB ");
				
			} else if( FIELDTYPE_BASE64.equals(fieldTypeName)) {
				
				columnDefs.add( fieldName + " CLOB ");
				
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
	@Override
	public void insertData(DescribeSObjectResult sfdcTable, Field[] fields,
			List<String[]> dataRows) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		sql.append("INSERT INTO " + getLocalName(sfdcTable) + "(");
		IValueMaker valueMakers[] = new IValueMaker[fields.length];
		
		for( int n = 0; n < fields.length; n++ ) {
			Field field = fields[n];
			String fieldTypeName = field.getType().getValue();
			
			sql.append( (n>0?",":"") + field.getName());
			
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
		sql.append( ") VALUES");
		
		for( int n = 0; n < dataRows.size(); n ++ ) {
			String[] row = dataRows.get(n);
			
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
		}
		sql.append(";");
		executeSQL( sql.toString());
	}
	
	
	

}
