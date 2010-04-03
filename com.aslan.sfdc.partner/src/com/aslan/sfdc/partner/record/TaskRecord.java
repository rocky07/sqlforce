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
 * This code was originally generated using ${link com.aslan.sfdc.partner.record.SObjectRecordCodeGenerator}.
 * Be careful if you decide to modify it by hand.
 */
package com.aslan.sfdc.partner.record;

import java.util.Calendar;

import com.sforce.soap.partner.sobject.SObject;

/**
 * Access to a SalesForce Task Object using the SFDC Partner interface.
 * 
 * @author SObjectRecordCodeGenerator
 *
 */
public class  TaskRecord  extends AbstractSObjectRecord {
	
	
	public static final String SOBJECT_NAME = "Task";
	
	//
	// Constants for all fields in a Task.
	// Use these constants when forming a SFDC query in Java to
	// avoid typing mistakes.
	//
	public static final String F_ACCOUNT_ID = "AccountId";
	public static final String F_ACTIVITY_DATE = "ActivityDate";
	public static final String F_CALL_DISPOSITION = "CallDisposition";
	public static final String F_CALL_DURATION_IN_SECONDS = "CallDurationInSeconds";
	public static final String F_CALL_OBJECT = "CallObject";
	public static final String F_CALL_TYPE = "CallType";
	public static final String F_CONNECTION_RECEIVED_ID = "ConnectionReceivedId";
	public static final String F_CONNECTION_SENT_ID = "ConnectionSentId";
	public static final String F_CREATED_BY_ID = "CreatedById";
	public static final String F_CREATED_DATE = "CreatedDate";
	public static final String F_CURRENCY_ISO_CODE = "CurrencyIsoCode";
	public static final String F_DESCRIPTION = "Description";
	public static final String F_IS_ARCHIVED = "IsArchived";
	public static final String F_IS_CLOSED = "IsClosed";
	public static final String F_IS_DELETED = "IsDeleted";
	public static final String F_IS_REMINDER_SET = "IsReminderSet";
	public static final String F_LAST_MODIFIED_BY_ID = "LastModifiedById";
	public static final String F_LAST_MODIFIED_DATE = "LastModifiedDate";
	public static final String F_OWNER_ID = "OwnerId";
	public static final String F_PRIORITY = "Priority";
	public static final String F_REMINDER_DATE_TIME = "ReminderDateTime";
	public static final String F_STATUS = "Status";
	public static final String F_SUBJECT = "Subject";
	public static final String F_SYSTEM_MODSTAMP = "SystemModstamp";
	public static final String F_WHAT_ID = "WhatId";
	public static final String F_WHO_ID = "WhoId";

	
	public TaskRecord() {
		this( (SObject) null);
	}
	
	public TaskRecord(SObject sObject ) {
		super(sObject);
	}

	/*
	 * (non-Javadoc)
	 * @see com.aslan.sfdc.partner.record.AbstractRecord#getSObjectName()
	 */
	public String getSObjectName() {
		return SOBJECT_NAME;
	}

	//
	// Lots and lots of type safe wrappers for Task parameters.
	//
	

	public String getAccountId() { return getStringField(F_ACCOUNT_ID); }

	public Calendar getActivityDate() { return getDateField(F_ACTIVITY_DATE); }
	public void setActivityDate(Calendar value) { setDateField(F_ACTIVITY_DATE, value); }

	public String getCallDisposition() { return getStringField(F_CALL_DISPOSITION); }
	public void setCallDisposition(String value) { setStringField(F_CALL_DISPOSITION, value); }

	public Integer getCallDurationInSeconds() { return getIntegerField(F_CALL_DURATION_IN_SECONDS); }
	public void setCallDurationInSeconds(Integer value) { setIntegerField(F_CALL_DURATION_IN_SECONDS, value); }

	public String getCallObject() { return getStringField(F_CALL_OBJECT); }
	public void setCallObject(String value) { setStringField(F_CALL_OBJECT, value); }

	public String getCallType() { return getStringField(F_CALL_TYPE); }
	public void setCallType(String value) { setStringField(F_CALL_TYPE, value); }

	public String getConnectionReceivedId() { return getStringField(F_CONNECTION_RECEIVED_ID); }

	public String getConnectionSentId() { return getStringField(F_CONNECTION_SENT_ID); }

	public String getCreatedById() { return getStringField(F_CREATED_BY_ID); }
	public void setCreatedById(String value) { setStringField(F_CREATED_BY_ID, value); }

	public Calendar getCreatedDate() { return getDateTimeField(F_CREATED_DATE); }
	public void setCreatedDate(Calendar value) { setDateTimeField(F_CREATED_DATE, value); }

	public String getCurrencyIsoCode() { return getStringField(F_CURRENCY_ISO_CODE); }
	public void setCurrencyIsoCode(String value) { setStringField(F_CURRENCY_ISO_CODE, value); }

	public String getDescription() { return getStringField(F_DESCRIPTION); }
	public void setDescription(String value) { setStringField(F_DESCRIPTION, value); }

	public Boolean getIsArchived() { return getBooleanField(F_IS_ARCHIVED); }

	public Boolean getIsClosed() { return getBooleanField(F_IS_CLOSED); }

	public Boolean getIsDeleted() { return getBooleanField(F_IS_DELETED); }

	public Boolean getIsReminderSet() { return getBooleanField(F_IS_REMINDER_SET); }
	public void setIsReminderSet(Boolean value) { setBooleanField(F_IS_REMINDER_SET, value); }

	public String getLastModifiedById() { return getStringField(F_LAST_MODIFIED_BY_ID); }
	public void setLastModifiedById(String value) { setStringField(F_LAST_MODIFIED_BY_ID, value); }

	public Calendar getLastModifiedDate() { return getDateTimeField(F_LAST_MODIFIED_DATE); }
	public void setLastModifiedDate(Calendar value) { setDateTimeField(F_LAST_MODIFIED_DATE, value); }

	public String getOwnerId() { return getStringField(F_OWNER_ID); }
	public void setOwnerId(String value) { setStringField(F_OWNER_ID, value); }

	public String getPriority() { return getStringField(F_PRIORITY); }
	public void setPriority(String value) { setStringField(F_PRIORITY, value); }

	public Calendar getReminderDateTime() { return getDateTimeField(F_REMINDER_DATE_TIME); }
	public void setReminderDateTime(Calendar value) { setDateTimeField(F_REMINDER_DATE_TIME, value); }

	public String getStatus() { return getStringField(F_STATUS); }
	public void setStatus(String value) { setStringField(F_STATUS, value); }

	public String getSubject() { return getStringField(F_SUBJECT); }
	public void setSubject(String value) { setStringField(F_SUBJECT, value); }

	public Calendar getSystemModstamp() { return getDateTimeField(F_SYSTEM_MODSTAMP); }

	public String getWhatId() { return getStringField(F_WHAT_ID); }
	public void setWhatId(String value) { setStringField(F_WHAT_ID, value); }

	public String getWhoId() { return getStringField(F_WHO_ID); }
	public void setWhoId(String value) { setStringField(F_WHO_ID, value); }

	
}
