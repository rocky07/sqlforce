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
 * Access to a SalesForce Case Object using the SFDC Partner interface.
 * 
 * @author SObjectRecordCodeGenerator
 *
 */
public class  CaseRecord  extends AbstractSObjectRecord {
	
	
	public static final String SOBJECT_NAME = "Case";
	
	//
	// Constants for all fields in a Case.
	// Use these constants when forming a SFDC query in Java to
	// avoid typing mistakes.
	//
	public static final String F_ACCOUNT_ID = "AccountId";
	public static final String F_CASE_NUMBER = "CaseNumber";
	public static final String F_CLOSED_DATE = "ClosedDate";
	public static final String F_CONTACT_ID = "ContactId";
	public static final String F_CREATED_BY_ID = "CreatedById";
	public static final String F_CREATED_DATE = "CreatedDate";
	public static final String F_CURRENCY_ISO_CODE = "CurrencyIsoCode";
	public static final String F_DESCRIPTION = "Description";
	public static final String F_HAS_COMMENTS_UNREAD_BY_OWNER = "HasCommentsUnreadByOwner";
	public static final String F_HAS_SELF_SERVICE_COMMENTS = "HasSelfServiceComments";
	public static final String F_IS_CLOSED = "IsClosed";
	public static final String F_IS_DELETED = "IsDeleted";
	public static final String F_IS_ESCALATED = "IsEscalated";
	public static final String F_LAST_MODIFIED_BY_ID = "LastModifiedById";
	public static final String F_LAST_MODIFIED_DATE = "LastModifiedDate";
	public static final String F_ORIGIN = "Origin";
	public static final String F_OWNER_ID = "OwnerId";
	public static final String F_PRIORITY = "Priority";
	public static final String F_REASON = "Reason";
	public static final String F_STATUS = "Status";
	public static final String F_SUBJECT = "Subject";
	public static final String F_SUPPLIED_COMPANY = "SuppliedCompany";
	public static final String F_SUPPLIED_EMAIL = "SuppliedEmail";
	public static final String F_SUPPLIED_NAME = "SuppliedName";
	public static final String F_SUPPLIED_PHONE = "SuppliedPhone";
	public static final String F_SYSTEM_MODSTAMP = "SystemModstamp";
	public static final String F_TYPE = "Type";

	
	public CaseRecord() {
		this( (SObject) null);
	}
	
	public CaseRecord(SObject sObject ) {
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
	// Lots and lots of type safe wrappers for Case parameters.
	//
	

	public String getAccountId() { return getStringField(F_ACCOUNT_ID); }
	public void setAccountId(String value) { setStringField(F_ACCOUNT_ID, value); }

	public String getCaseNumber() { return getStringField(F_CASE_NUMBER); }

	public Calendar getClosedDate() { return getDateTimeField(F_CLOSED_DATE); }
	public void setClosedDate(Calendar value) { setDateTimeField(F_CLOSED_DATE, value); }

	public String getContactId() { return getStringField(F_CONTACT_ID); }
	public void setContactId(String value) { setStringField(F_CONTACT_ID, value); }

	public String getCreatedById() { return getStringField(F_CREATED_BY_ID); }
	public void setCreatedById(String value) { setStringField(F_CREATED_BY_ID, value); }

	public Calendar getCreatedDate() { return getDateTimeField(F_CREATED_DATE); }
	public void setCreatedDate(Calendar value) { setDateTimeField(F_CREATED_DATE, value); }

	public String getCurrencyIsoCode() { return getStringField(F_CURRENCY_ISO_CODE); }
	public void setCurrencyIsoCode(String value) { setStringField(F_CURRENCY_ISO_CODE, value); }

	public String getDescription() { return getStringField(F_DESCRIPTION); }
	public void setDescription(String value) { setStringField(F_DESCRIPTION, value); }

	public Boolean getHasCommentsUnreadByOwner() { return getBooleanField(F_HAS_COMMENTS_UNREAD_BY_OWNER); }

	public Boolean getHasSelfServiceComments() { return getBooleanField(F_HAS_SELF_SERVICE_COMMENTS); }

	public Boolean getIsClosed() { return getBooleanField(F_IS_CLOSED); }

	public Boolean getIsDeleted() { return getBooleanField(F_IS_DELETED); }

	public Boolean getIsEscalated() { return getBooleanField(F_IS_ESCALATED); }
	public void setIsEscalated(Boolean value) { setBooleanField(F_IS_ESCALATED, value); }

	public String getLastModifiedById() { return getStringField(F_LAST_MODIFIED_BY_ID); }
	public void setLastModifiedById(String value) { setStringField(F_LAST_MODIFIED_BY_ID, value); }

	public Calendar getLastModifiedDate() { return getDateTimeField(F_LAST_MODIFIED_DATE); }
	public void setLastModifiedDate(Calendar value) { setDateTimeField(F_LAST_MODIFIED_DATE, value); }

	public String getOrigin() { return getStringField(F_ORIGIN); }
	public void setOrigin(String value) { setStringField(F_ORIGIN, value); }

	public String getOwnerId() { return getStringField(F_OWNER_ID); }
	public void setOwnerId(String value) { setStringField(F_OWNER_ID, value); }

	public String getPriority() { return getStringField(F_PRIORITY); }
	public void setPriority(String value) { setStringField(F_PRIORITY, value); }

	public String getReason() { return getStringField(F_REASON); }
	public void setReason(String value) { setStringField(F_REASON, value); }

	public String getStatus() { return getStringField(F_STATUS); }
	public void setStatus(String value) { setStringField(F_STATUS, value); }

	public String getSubject() { return getStringField(F_SUBJECT); }
	public void setSubject(String value) { setStringField(F_SUBJECT, value); }

	public String getSuppliedCompany() { return getStringField(F_SUPPLIED_COMPANY); }
	public void setSuppliedCompany(String value) { setStringField(F_SUPPLIED_COMPANY, value); }

	public String getSuppliedEmail() { return getStringField(F_SUPPLIED_EMAIL); }
	public void setSuppliedEmail(String value) { setStringField(F_SUPPLIED_EMAIL, value); }

	public String getSuppliedName() { return getStringField(F_SUPPLIED_NAME); }
	public void setSuppliedName(String value) { setStringField(F_SUPPLIED_NAME, value); }

	public String getSuppliedPhone() { return getStringField(F_SUPPLIED_PHONE); }
	public void setSuppliedPhone(String value) { setStringField(F_SUPPLIED_PHONE, value); }

	public Calendar getSystemModstamp() { return getDateTimeField(F_SYSTEM_MODSTAMP); }

	public String getType() { return getStringField(F_TYPE); }
	public void setType(String value) { setStringField(F_TYPE, value); }

	
}
