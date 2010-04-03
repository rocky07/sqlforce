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
package com.aslan.sfdc.partner.record.custom;

import com.aslan.sfdc.partner.record.AbstractSObjectRecord;

import java.util.Calendar;

import com.sforce.soap.partner.sobject.SObject;

/**
 * Access to a SalesForce DuplicateRecordCandidate__c Object using the SFDC Partner interface.
 * 
 * @author SObjectRecordCodeGenerator
 *
 */
public class  DuplicateRecordCandidate__cRecord extends AbstractSObjectRecord {
	
	
	public static final String SOBJECT_NAME = "DuplicateRecordCandidate__c";
	
	//
	// Constants for all fields in a DuplicateRecordCandidate__c.
	// Use these constants when forming a SFDC query in Java to
	// avoid typing mistakes.
	//
	public static final String F_CONNECTION_RECEIVED_ID = "ConnectionReceivedId";
	public static final String F_CONNECTION_SENT_ID = "ConnectionSentId";
	public static final String F_CREATED_BY_ID = "CreatedById";
	public static final String F_CREATED_DATE = "CreatedDate";
	public static final String F_CURRENCY_ISO_CODE = "CurrencyIsoCode";
	public static final String F_IS_DELETED = "IsDeleted";
	public static final String F_LAST_MODIFIED_BY_ID = "LastModifiedById";
	public static final String F_LAST_MODIFIED_DATE = "LastModifiedDate";
	public static final String F_NAME = "Name";
	public static final String F_OWNER_ID = "OwnerId";
	public static final String F_S_OBJECT_TYPE__C = "sObjectType__c";
	public static final String F_SRC_ID__C = "srcId__c";
	public static final String F_STATUS__C = "status__c";
	public static final String F_SYSTEM_MODSTAMP = "SystemModstamp";
	public static final String F_TARGET_ID__C = "targetId__c";

	
	public DuplicateRecordCandidate__cRecord() {
		this( (SObject) null);
	}
	
	public DuplicateRecordCandidate__cRecord(SObject sObject ) {
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
	// Lots and lots of type safe wrappers for DuplicateRecordCandidate__c parameters.
	//
	

	public String getConnectionReceivedId() { return getStringField(F_CONNECTION_RECEIVED_ID); }

	public String getConnectionSentId() { return getStringField(F_CONNECTION_SENT_ID); }

	public String getCreatedById() { return getStringField(F_CREATED_BY_ID); }
	public void setCreatedById(String value) { setStringField(F_CREATED_BY_ID, value); }

	public Calendar getCreatedDate() { return getDateTimeField(F_CREATED_DATE); }
	public void setCreatedDate(Calendar value) { setDateTimeField(F_CREATED_DATE, value); }

	public String getCurrencyIsoCode() { return getStringField(F_CURRENCY_ISO_CODE); }
	public void setCurrencyIsoCode(String value) { setStringField(F_CURRENCY_ISO_CODE, value); }

	public Boolean getIsDeleted() { return getBooleanField(F_IS_DELETED); }

	public String getLastModifiedById() { return getStringField(F_LAST_MODIFIED_BY_ID); }
	public void setLastModifiedById(String value) { setStringField(F_LAST_MODIFIED_BY_ID, value); }

	public Calendar getLastModifiedDate() { return getDateTimeField(F_LAST_MODIFIED_DATE); }
	public void setLastModifiedDate(Calendar value) { setDateTimeField(F_LAST_MODIFIED_DATE, value); }

	public String getName() { return getStringField(F_NAME); }

	public String getOwnerId() { return getStringField(F_OWNER_ID); }
	public void setOwnerId(String value) { setStringField(F_OWNER_ID, value); }

	public String getSObjectType__c() { return getStringField(F_S_OBJECT_TYPE__C); }
	public void setSObjectType__c(String value) { setStringField(F_S_OBJECT_TYPE__C, value); }

	public String getSrcId__c() { return getStringField(F_SRC_ID__C); }
	public void setSrcId__c(String value) { setStringField(F_SRC_ID__C, value); }

	public String getStatus__c() { return getStringField(F_STATUS__C); }
	public void setStatus__c(String value) { setStringField(F_STATUS__C, value); }

	public Calendar getSystemModstamp() { return getDateTimeField(F_SYSTEM_MODSTAMP); }

	public String getTargetId__c() { return getStringField(F_TARGET_ID__C); }
	public void setTargetId__c(String value) { setStringField(F_TARGET_ID__C, value); }

	
}
