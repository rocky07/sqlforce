/*******************************************************************************
 * Copyright (c) 2010 Gregory Smith (gsmithfarmer@gmail.com).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Gregory Smith (gsmithfarmer@gmail.com) - initial API and implementation
 ******************************************************************************/
package com.aslan.sfdc.partner.record;

import java.util.Calendar;
import java.util.Map;

public interface ISObjectRecord {

	public static final String F_ID = "Id";

	String getSObjectName();

	/**
	 * Return the current value of a String field (or null if not set).
	 * 
	 * @param fieldName grab this field.
	 * @return current value of the field.
	 */
	String getStringField(String fieldName);

	/**
	 * Set the value of any field that has a string value (null is OK);
	 * 
	 * @param fieldName set this field.
	 * @param value new value for the field.
	 */
	void setStringField(String fieldName, String value);

	/**
	 * Return the current value of a Integer field (or null if not set).
	 * 
	 * @param fieldName grab this field.
	 * @return current value of the field.
	 * @throws NumberFormatException if field is non-null and not an Integer;
	 */
	Integer getIntegerField(String fieldName);

	/**
	 * Set the value of any field that has an Integer value (null is OK);
	 * 
	 * @param fieldName set this field.
	 * @param value new value for the field.
	 */
	void setIntegerField( String fieldName, Integer value );
	
	/**
	 * Set the value of any field that has an Double value (null is OK);
	 * 
	 * @param fieldName set this field.
	 * @param value new value for the field.
	 */
	void setDoubleField(String fieldName, Integer value);

	/**
	 * Return the current value of a Double field (or null if not set).
	 * 
	 * @param fieldName grab this field.
	 * @return current value of the field.
	 *  @throws NumberFormatException if field is non-null and not a Double;
	 */
	Double getDoubleField(String fieldName);

	/**
	 * Set the value of any field that has a Double value (null is OK);
	 * 
	 * @param fieldName set this field.
	 * @param value new value for the field.
	 */
	void setDoubleField(String fieldName, Double value);

	/**
	 * Get the value of a Date field (no time is stored).
	 * 
	 * @param fieldName get this field
	 * @return a date
	 */
	Calendar getDateField(String fieldName);

	/**
	 * Set the value of a Date field (no time is stored).
	 * 
	 * @param fieldName set this field
	 * @param value new value for the field
	 */
	void setDateField(String fieldName, Calendar value);
	
	/**
	 * Get the value of a DateTime field (a date plus a time is stored).
	 * 
	 * @param fieldName get this field
	 * @return a dateTime
	 */
	Calendar getDateTimeField(String fieldName);

	/**
	 * Set the value of a DateTime field (date + time is stored).
	 * 
	 * @param fieldName set this field
	 * @param value new value for the field
	 */
	void setDateTimeField(String fieldName, Calendar value);

	/**
	 * Get the value of a Boolean field.
	 * 
	 * @param fieldName get this field
	 * @return a boolean
	 */
	Boolean getBooleanField(String fieldName);

	/**
	 * Set the value of a Boolean field.
	 * 
	 * @param fieldName set this field
	 * @param value new value for the field (can be null)
	 */
	void setBooleanField(String fieldName, Boolean value);

	/**
	 * Get the set of values in a multipicklist field.
	 * 
	 * @param fieldName get this field.
	 * @return list of selected values (or null if none set).
	 */
	String[] getMultiPickListField( String fieldName );
	
	/**
	 * Set the values for a multipick list.
	 * 
	 * @param fieldName set this field
	 * @param value new list of values (use a empty list or null to clear).
	 */
	void setMultiPickListField( String fieldName, String[] value );
	
	/**
	 * Get the unique if of the record (null if the record is not from sdfdc).
	 * 
	 * @return unique id of the record.
	 */
	String getId();

	/**
	 * Set the ID for the record (useful for updates).
	 * 
	 * @param id SFDC id.
	 */
	void setId( String id );
	/**
	 * Get the data type of the record (null if the record is not from sdfc).
	 * 
	 * @return type (e.g. table) owning the record.
	 */
	String getType();

	/**
	 * Return the value of all fields that have been set for the object.
	 * 
	 * @return hash of <fieldName, Value (may be null)> for all field values.
	 */
	Map<String,String> getAllFields();
	
}
