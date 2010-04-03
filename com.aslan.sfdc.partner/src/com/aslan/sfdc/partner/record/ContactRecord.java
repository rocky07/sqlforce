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
 * Access to a SalesForce Contact Object using the SFDC Partner interface.
 * 
 * @author SObjectRecordCodeGenerator
 *
 */
public class  ContactRecord  extends AbstractSObjectRecord {
	
	
	public static final String SOBJECT_NAME = "Contact";
	
	//
	// Constants for all fields in a Contact.
	// Use these constants when forming a SFDC query in Java to
	// avoid typing mistakes.
	//
	public static final String F_ACCOUNT_ID = "AccountId";
	public static final String F_ASSISTANT_NAME = "AssistantName";
	public static final String F_ASSISTANT_PHONE = "AssistantPhone";
	public static final String F_BIRTHDATE = "Birthdate";
	public static final String F_CONNECTION_RECEIVED_ID = "ConnectionReceivedId";
	public static final String F_CONNECTION_SENT_ID = "ConnectionSentId";
	public static final String F_CREATED_BY_ID = "CreatedById";
	public static final String F_CREATED_DATE = "CreatedDate";
	public static final String F_CURRENCY_ISO_CODE = "CurrencyIsoCode";
	public static final String F_DEPARTMENT = "Department";
	public static final String F_DESCRIPTION = "Description";
	public static final String F_EMAIL = "Email";
	public static final String F_EMAIL_BOUNCED_DATE = "EmailBouncedDate";
	public static final String F_EMAIL_BOUNCED_REASON = "EmailBouncedReason";
	public static final String F_FAX = "Fax";
	public static final String F_FIRST_NAME = "FirstName";
	public static final String F_HOME_PHONE = "HomePhone";
	public static final String F_IS_DELETED = "IsDeleted";
	public static final String F_LAST_ACTIVITY_DATE = "LastActivityDate";
	public static final String F_LAST_C_U_REQUEST_DATE = "LastCURequestDate";
	public static final String F_LAST_C_U_UPDATE_DATE = "LastCUUpdateDate";
	public static final String F_LAST_MODIFIED_BY_ID = "LastModifiedById";
	public static final String F_LAST_MODIFIED_DATE = "LastModifiedDate";
	public static final String F_LAST_NAME = "LastName";
	public static final String F_LEAD_SOURCE = "LeadSource";
	public static final String F_MAILING_CITY = "MailingCity";
	public static final String F_MAILING_COUNTRY = "MailingCountry";
	public static final String F_MAILING_POSTAL_CODE = "MailingPostalCode";
	public static final String F_MAILING_STATE = "MailingState";
	public static final String F_MAILING_STREET = "MailingStreet";
	public static final String F_MASTER_RECORD_ID = "MasterRecordId";
	public static final String F_MOBILE_PHONE = "MobilePhone";
	public static final String F_NAME = "Name";
	public static final String F_OTHER_CITY = "OtherCity";
	public static final String F_OTHER_COUNTRY = "OtherCountry";
	public static final String F_OTHER_PHONE = "OtherPhone";
	public static final String F_OTHER_POSTAL_CODE = "OtherPostalCode";
	public static final String F_OTHER_STATE = "OtherState";
	public static final String F_OTHER_STREET = "OtherStreet";
	public static final String F_OWNER_ID = "OwnerId";
	public static final String F_PHONE = "Phone";
	public static final String F_REPORTS_TO_ID = "ReportsToId";
	public static final String F_SALUTATION = "Salutation";
	public static final String F_SYSTEM_MODSTAMP = "SystemModstamp";
	public static final String F_TITLE = "Title";

	
	public ContactRecord() {
		this( (SObject) null);
	}
	
	public ContactRecord(SObject sObject ) {
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
	// Lots and lots of type safe wrappers for Contact parameters.
	//
	

	public String getAccountId() { return getStringField(F_ACCOUNT_ID); }
	public void setAccountId(String value) { setStringField(F_ACCOUNT_ID, value); }

	public String getAssistantName() { return getStringField(F_ASSISTANT_NAME); }
	public void setAssistantName(String value) { setStringField(F_ASSISTANT_NAME, value); }

	public String getAssistantPhone() { return getStringField(F_ASSISTANT_PHONE); }
	public void setAssistantPhone(String value) { setStringField(F_ASSISTANT_PHONE, value); }

	public Calendar getBirthdate() { return getDateField(F_BIRTHDATE); }
	public void setBirthdate(Calendar value) { setDateField(F_BIRTHDATE, value); }

	public String getConnectionReceivedId() { return getStringField(F_CONNECTION_RECEIVED_ID); }

	public String getConnectionSentId() { return getStringField(F_CONNECTION_SENT_ID); }

	public String getCreatedById() { return getStringField(F_CREATED_BY_ID); }
	public void setCreatedById(String value) { setStringField(F_CREATED_BY_ID, value); }

	public Calendar getCreatedDate() { return getDateTimeField(F_CREATED_DATE); }
	public void setCreatedDate(Calendar value) { setDateTimeField(F_CREATED_DATE, value); }

	public String getCurrencyIsoCode() { return getStringField(F_CURRENCY_ISO_CODE); }
	public void setCurrencyIsoCode(String value) { setStringField(F_CURRENCY_ISO_CODE, value); }

	public String getDepartment() { return getStringField(F_DEPARTMENT); }
	public void setDepartment(String value) { setStringField(F_DEPARTMENT, value); }

	public String getDescription() { return getStringField(F_DESCRIPTION); }
	public void setDescription(String value) { setStringField(F_DESCRIPTION, value); }

	public String getEmail() { return getStringField(F_EMAIL); }
	public void setEmail(String value) { setStringField(F_EMAIL, value); }

	public Calendar getEmailBouncedDate() { return getDateTimeField(F_EMAIL_BOUNCED_DATE); }
	public void setEmailBouncedDate(Calendar value) { setDateTimeField(F_EMAIL_BOUNCED_DATE, value); }

	public String getEmailBouncedReason() { return getStringField(F_EMAIL_BOUNCED_REASON); }
	public void setEmailBouncedReason(String value) { setStringField(F_EMAIL_BOUNCED_REASON, value); }

	public String getFax() { return getStringField(F_FAX); }
	public void setFax(String value) { setStringField(F_FAX, value); }

	public String getFirstName() { return getStringField(F_FIRST_NAME); }
	public void setFirstName(String value) { setStringField(F_FIRST_NAME, value); }

	public String getHomePhone() { return getStringField(F_HOME_PHONE); }
	public void setHomePhone(String value) { setStringField(F_HOME_PHONE, value); }

	public Boolean getIsDeleted() { return getBooleanField(F_IS_DELETED); }

	public Calendar getLastActivityDate() { return getDateField(F_LAST_ACTIVITY_DATE); }

	public Calendar getLastCURequestDate() { return getDateTimeField(F_LAST_C_U_REQUEST_DATE); }

	public Calendar getLastCUUpdateDate() { return getDateTimeField(F_LAST_C_U_UPDATE_DATE); }

	public String getLastModifiedById() { return getStringField(F_LAST_MODIFIED_BY_ID); }
	public void setLastModifiedById(String value) { setStringField(F_LAST_MODIFIED_BY_ID, value); }

	public Calendar getLastModifiedDate() { return getDateTimeField(F_LAST_MODIFIED_DATE); }
	public void setLastModifiedDate(Calendar value) { setDateTimeField(F_LAST_MODIFIED_DATE, value); }

	public String getLastName() { return getStringField(F_LAST_NAME); }
	public void setLastName(String value) { setStringField(F_LAST_NAME, value); }

	public String getLeadSource() { return getStringField(F_LEAD_SOURCE); }
	public void setLeadSource(String value) { setStringField(F_LEAD_SOURCE, value); }

	public String getMailingCity() { return getStringField(F_MAILING_CITY); }
	public void setMailingCity(String value) { setStringField(F_MAILING_CITY, value); }

	public String getMailingCountry() { return getStringField(F_MAILING_COUNTRY); }
	public void setMailingCountry(String value) { setStringField(F_MAILING_COUNTRY, value); }

	public String getMailingPostalCode() { return getStringField(F_MAILING_POSTAL_CODE); }
	public void setMailingPostalCode(String value) { setStringField(F_MAILING_POSTAL_CODE, value); }

	public String getMailingState() { return getStringField(F_MAILING_STATE); }
	public void setMailingState(String value) { setStringField(F_MAILING_STATE, value); }

	public String getMailingStreet() { return getStringField(F_MAILING_STREET); }
	public void setMailingStreet(String value) { setStringField(F_MAILING_STREET, value); }

	public String getMasterRecordId() { return getStringField(F_MASTER_RECORD_ID); }

	public String getMobilePhone() { return getStringField(F_MOBILE_PHONE); }
	public void setMobilePhone(String value) { setStringField(F_MOBILE_PHONE, value); }

	public String getName() { return getStringField(F_NAME); }

	public String getOtherCity() { return getStringField(F_OTHER_CITY); }
	public void setOtherCity(String value) { setStringField(F_OTHER_CITY, value); }

	public String getOtherCountry() { return getStringField(F_OTHER_COUNTRY); }
	public void setOtherCountry(String value) { setStringField(F_OTHER_COUNTRY, value); }

	public String getOtherPhone() { return getStringField(F_OTHER_PHONE); }
	public void setOtherPhone(String value) { setStringField(F_OTHER_PHONE, value); }

	public String getOtherPostalCode() { return getStringField(F_OTHER_POSTAL_CODE); }
	public void setOtherPostalCode(String value) { setStringField(F_OTHER_POSTAL_CODE, value); }

	public String getOtherState() { return getStringField(F_OTHER_STATE); }
	public void setOtherState(String value) { setStringField(F_OTHER_STATE, value); }

	public String getOtherStreet() { return getStringField(F_OTHER_STREET); }
	public void setOtherStreet(String value) { setStringField(F_OTHER_STREET, value); }

	public String getOwnerId() { return getStringField(F_OWNER_ID); }
	public void setOwnerId(String value) { setStringField(F_OWNER_ID, value); }

	public String getPhone() { return getStringField(F_PHONE); }
	public void setPhone(String value) { setStringField(F_PHONE, value); }

	public String getReportsToId() { return getStringField(F_REPORTS_TO_ID); }
	public void setReportsToId(String value) { setStringField(F_REPORTS_TO_ID, value); }

	public String getSalutation() { return getStringField(F_SALUTATION); }
	public void setSalutation(String value) { setStringField(F_SALUTATION, value); }

	public Calendar getSystemModstamp() { return getDateTimeField(F_SYSTEM_MODSTAMP); }

	public String getTitle() { return getStringField(F_TITLE); }
	public void setTitle(String value) { setStringField(F_TITLE, value); }

	
}

