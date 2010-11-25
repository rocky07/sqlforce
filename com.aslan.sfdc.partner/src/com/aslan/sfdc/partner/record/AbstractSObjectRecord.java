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
/**
 * 
 */
package com.aslan.sfdc.partner.record;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.axis.message.MessageElement;

import com.aslan.sfdc.partner.record.ISObjectRecord;

import com.sforce.soap.partner.sobject.SObject;

/**
 * Base record class that all other Record classes should extend.
 * 
 * This class has several functions:
 * <ul>
 * <li>Manage access to data in a base SObject.
 * <li>Allow new values to replace the default values in an SObject.
 * <li>Wrap type safe access methods in front of the string field data in SObject.
 * </ul>
 * 
 * @author snort
 *
 */
abstract public class AbstractSObjectRecord implements ISObjectRecord {

	class LocalField {
		private String name;
		private String value;
		
		private LocalField( String name, String value ) {
			this.name = name;
			this.value = value;
		}
		
		String getValue() { return value; }
		String getName() { return name; }
	}
	private SObject sObject = null;
	private Map<String,LocalField> overriddenFields = new HashMap<String,LocalField>();
	private static SimpleDateFormat sfdcDateTimeParser = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	private static SimpleDateFormat sfdcDateParser = new SimpleDateFormat("yyyy-MM-dd");
	
	protected AbstractSObjectRecord(SObject sObject ) {

		this.sObject = sObject;
	}
	
	/**
	 * Grab the currrent value for any field given its name.
	 * 
	 * If a caller has ever set the value of a field using {@link #setAnyField(String, String)} then this will be returned
	 * as the current value. Otherwise, the value in the SObject passed to the constructor {@link #AbstractSObjectRecord(SObject)}
	 * will be searched. If the field cannot be found, then null is returned.
	 * 
	 * @param fieldName look for the value in this field.
	 * 
	 * @return current value or null if the field cannot be found.
	 */
	protected String getAnyField( String fieldName ) {
		String value = null;
		String lowerFieldName = fieldName.toLowerCase();
		
		if( overriddenFields.containsKey(lowerFieldName)) {
			value = overriddenFields.get(lowerFieldName).getValue();
		} else if( null != sObject ) {
			for( MessageElement mm : sObject.get_any()) {
				if( fieldName.equalsIgnoreCase(mm.getName())) {
					value = mm.getValue();
				}
			}
		}
		
		return value;
	}
	
	/**
	 * Set the current value of any field.
	 * 
	 * Note that the new value is only cached in this class -- it is not written to sfdc.
	 * 
	 * @param fieldName set this field.
	 * @param value new value for the field (may be null).
	 */
	protected void setAnyField( String fieldName, String value ) {
		String lowerFieldName = fieldName.toLowerCase();
		
		overriddenFields.put(lowerFieldName, new LocalField( fieldName, value ));
		
	}
	
	/* (non-Javadoc)
	 * @see com.aslan.sfdc.partner.record.ISObjectRecord#getSObjectName()
	 */
	public abstract String getSObjectName();
	
	
	/**
	 * Generate a newline delimited string of all name value pairs that have been explicitly fetched or set.
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		SortedMap<String,String> map = new TreeMap<String,String>();
		
		for( String key : overriddenFields.keySet()) {
			LocalField field = overriddenFields.get(key);
			map.put( field.getName(), field.getValue());
		}
		
		if( null != sObject ) {
			for( MessageElement mm : sObject.get_any()) {
				if( !map.containsKey(mm.getName())) {
					map.put( mm.getName(), mm.getValue());
				}
			}
		}
		
		for( String key : map.keySet()) {
			buffer.append( (buffer.length()>0?"\n":"") + key + " = " + map.get(key));
		}
		
		return buffer.toString();
	}

	/* (non-Javadoc)
	 * @see com.aslan.sfdc.partner.record.ISObjectRecord#getStringField(java.lang.String)
	 */
	public String getStringField( String fieldName ) {
		return getAnyField(fieldName);
	}
	
	/* (non-Javadoc)
	 * @see com.aslan.sfdc.partner.record.ISObjectRecord#setStringField(java.lang.String, java.lang.String)
	 */
	public void setStringField( String fieldName, String value ) {
		setAnyField( fieldName, value );
	}
	
	/* (non-Javadoc)
	 * @see com.aslan.sfdc.partner.record.ISObjectRecord#getIntegerField(java.lang.String)
	 */
	public Integer getIntegerField( String fieldName ) {
		String sValue = getAnyField( fieldName );

		if( null == sValue ) { return null; }

		return new Double(sValue).intValue();

	}
	
	/*
	 * (non-Javadoc)
	 * @see com.aslan.sfdc.partner.record.ISObjectRecord#setIntegerField(java.lang.String, java.lang.Integer)
	 */
	public void setIntegerField(String fieldName, Integer value) {
		setAnyField( fieldName, null==value?null:value.toString() );
		
	}

	/* (non-Javadoc)
	 * @see com.aslan.sfdc.partner.record.ISObjectRecord#setDoubleField(java.lang.String, java.lang.Integer)
	 */
	public void setDoubleField( String fieldName, Integer value ) {
		setAnyField( fieldName, null==value?null:value.toString() );
	}
	
	/* (non-Javadoc)
	 * @see com.aslan.sfdc.partner.record.ISObjectRecord#getDoubleField(java.lang.String)
	 */
	public Double getDoubleField( String fieldName ) {
		String sValue = getAnyField( fieldName );

		if( null == sValue ) { return null; }

		return new Double(sValue);

	}
	
	/* (non-Javadoc)
	 * @see com.aslan.sfdc.partner.record.ISObjectRecord#setDoubleField(java.lang.String, java.lang.Double)
	 */
	public void setDoubleField( String fieldName, Double value ) {
		setAnyField( fieldName, null==value?null:value.toString() );
	}
	
	/* (non-Javadoc)
	 * @see com.aslan.sfdc.partner.record.ISObjectRecord#getDateField(java.lang.String)
	 */
	public Calendar getDateField( String fieldName ) {
		String tmpStr = getAnyField( fieldName );
		
		if( null == tmpStr ) { return null; }
		
		try {
			Date dd = sfdcDateParser.parse(tmpStr);
			Calendar cc = Calendar.getInstance();
			cc.setTime(dd);
			return cc;
		} catch (ParseException e) {
			throw new Error( e.getMessage());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.aslan.sfdc.partner.record.ISObjectRecord#setDateField(java.lang.String, java.util.Calendar)
	 */
	public void setDateField( String fieldName, Calendar value ) {

		setAnyField( fieldName, null==value?null:sfdcDateParser.format(value.getTime()));
	}
	/*
	 * (non-Javadoc)
	 * @see com.aslan.sfdc.partner.record.ISObjectRecord#getDateTimeField(java.lang.String)
	 */
	public Calendar getDateTimeField(String fieldName) {
		String tmpStr = getAnyField( fieldName );
		
		if( null == tmpStr ) { return null; }
		
		try {
			Date dd = sfdcDateTimeParser.parse(tmpStr);
			Calendar cc = Calendar.getInstance();
			cc.setTime(dd);
		
			return cc;
		} catch (ParseException e) {
			throw new Error( e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.aslan.sfdc.partner.record.ISObjectRecord#setDateTimeField(java.lang.String, java.util.Calendar)
	 */
	public void setDateTimeField(String fieldName, Calendar value) {
		// 2008-05-29T15:52:16.000Z -- must be GMT time.
		
		String date = null==value?null:sfdcDateTimeParser.format(value.getTime());

		setAnyField( fieldName, date);		
	}

	/*
	 * (non-Javadoc)
	 * @see com.aslan.sfdc.partner.record.ISObjectRecord#getMultiPickListField(java.lang.String)
	 */
	public String[] getMultiPickListField( String fieldName ) {
		String tmpStr = getAnyField( fieldName );
		
		if( null == tmpStr ) { return null; }
		
		return tmpStr.split(";");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.aslan.sfdc.partner.record.ISObjectRecord#setMultiPickListField(java.lang.String, java.lang.String[])
	 */
	public void setMultiPickListField(String fieldName, String[] valueList) {
		if( null == valueList || 0==valueList.length) {
			setAnyField( fieldName, null);
		} else {
			StringBuffer b = new StringBuffer();
			for( String v : valueList ) {
				b.append((b.length()==0?"":";") + v );
			}
			setAnyField( fieldName, b.toString());
		}
		
	}

	
	/*
	 * (non-Javadoc)
	 * @see com.aslan.sfdc.partner.record.ISObjectRecord#getBooleanField(java.lang.String)
	 */
	public Boolean getBooleanField(String fieldName) {
		String tmpStr = getAnyField( fieldName );
		
		if( null == tmpStr ) { return null; }
		return "true".equals(tmpStr);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aslan.sfdc.partner.record.ISObjectRecord#setBooleanField(java.lang.String, java.lang.Boolean)
	 */
	public void setBooleanField(String fieldName, Boolean value) {
		
		setAnyField( fieldName, (null==value)?null:(value?"true":"false"));
	}

	/* (non-Javadoc)
	 * @see com.aslan.sfdc.partner.record.ISObjectRecord#getId()
	 */
	public String getId() {
	
		if( null==sObject || overriddenFields.containsKey(F_ID.toLowerCase())) {
			return getAnyField( F_ID );
		}
		return sObject.getId();
	}
	
	@Override
	public void setId(String value) {
		setAnyField( F_ID, value);
		
	}

	/* (non-Javadoc)
	 * @see com.aslan.sfdc.partner.record.ISObjectRecord#getType()
	 */
	public String getType() {
		return null==sObject?null:sObject.getType();
	}

	/*
	 * (non-Javadoc)
	 * @see com.aslan.sfdc.partner.record.ISObjectRecord#getAllFields()
	 */
	public Map<String, String> getAllFields() {
		Map<String,String> map = new TreeMap<String,String>();
		
		for( String key : overriddenFields.keySet()) {
			LocalField field = overriddenFields.get(key);
			map.put( field.getName(), field.getValue());
		}
		
		if( null != sObject ) {
			for( MessageElement mm : sObject.get_any()) {
				if( !map.containsKey(mm.getName())) {
					map.put( mm.getName(), mm.getValue());
				}
			}
		}
		
		return map;
	}
	
	
}
