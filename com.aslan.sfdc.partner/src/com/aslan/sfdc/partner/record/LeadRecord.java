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
 * Access to a SalesForce Lead Object using the SFDC Partner interface.
 * 
 * @author SObjectRecordCodeGenerator
 *
 */
public class  LeadRecord  extends AbstractSObjectRecord {
	
	
	public static final String SOBJECT_NAME = "Lead";
	
	//
	// Constants for all fields in a Lead.
	// Use these constants when forming a SFDC query in Java to
	// avoid typing mistakes.
	//
	public static final String F_ANNUAL_REVENUE = "AnnualRevenue";
	public static final String F_CITY = "City";
	public static final String F_COMPANY = "Company";
	public static final String F_CONNECTION_RECEIVED_ID = "ConnectionReceivedId";
	public static final String F_CONNECTION_SENT_ID = "ConnectionSentId";
	public static final String F_CONVERTED_ACCOUNT_ID = "ConvertedAccountId";
	public static final String F_CONVERTED_CONTACT_ID = "ConvertedContactId";
	public static final String F_CONVERTED_DATE = "ConvertedDate";
	public static final String F_CONVERTED_OPPORTUNITY_ID = "ConvertedOpportunityId";
	public static final String F_COUNTRY = "Country";
	public static final String F_CREATED_BY_ID = "CreatedById";
	public static final String F_CREATED_DATE = "CreatedDate";
	public static final String F_CURRENCY_ISO_CODE = "CurrencyIsoCode";
	public static final String F_DESCRIPTION = "Description";
	public static final String F_EMAIL = "Email";
	public static final String F_EMAIL_BOUNCED_DATE = "EmailBouncedDate";
	public static final String F_EMAIL_BOUNCED_REASON = "EmailBouncedReason";
	public static final String F_FIRST_NAME = "FirstName";
	public static final String F_INDUSTRY = "Industry";
	public static final String F_IS_CONVERTED = "IsConverted";
	public static final String F_IS_DELETED = "IsDeleted";
	public static final String F_IS_UNREAD_BY_OWNER = "IsUnreadByOwner";
	public static final String F_LAST_ACTIVITY_DATE = "LastActivityDate";
	public static final String F_LAST_MODIFIED_BY_ID = "LastModifiedById";
	public static final String F_LAST_MODIFIED_DATE = "LastModifiedDate";
	public static final String F_LAST_NAME = "LastName";
	public static final String F_LEAD_SOURCE = "LeadSource";
	public static final String F_MASTER_RECORD_ID = "MasterRecordId";
	public static final String F_NAME = "Name";
	public static final String F_NUMBER_OF_EMPLOYEES = "NumberOfEmployees";
	public static final String F_OWNER_ID = "OwnerId";
	public static final String F_PHONE = "Phone";
	public static final String F_POSTAL_CODE = "PostalCode";
	public static final String F_RATING = "Rating";
	public static final String F_SALUTATION = "Salutation";
	public static final String F_STATE = "State";
	public static final String F_STATUS = "Status";
	public static final String F_STREET = "Street";
	public static final String F_SYSTEM_MODSTAMP = "SystemModstamp";
	public static final String F_TITLE = "Title";
	public static final String F_WEBSITE = "Website";

	
	public LeadRecord() {
		this( (SObject) null);
	}
	
	public LeadRecord(SObject sObject ) {
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
	// Lots and lots of type safe wrappers for Lead parameters.
	//
	

	public Double getAnnualRevenue() { return getDoubleField(F_ANNUAL_REVENUE); }
	public void setAnnualRevenue(Double value) { setDoubleField(F_ANNUAL_REVENUE, value); }

	public String getCity() { return getStringField(F_CITY); }
	public void setCity(String value) { setStringField(F_CITY, value); }

	public String getCompany() { return getStringField(F_COMPANY); }
	public void setCompany(String value) { setStringField(F_COMPANY, value); }

	public String getConnectionReceivedId() { return getStringField(F_CONNECTION_RECEIVED_ID); }

	public String getConnectionSentId() { return getStringField(F_CONNECTION_SENT_ID); }

	public String getConvertedAccountId() { return getStringField(F_CONVERTED_ACCOUNT_ID); }
	public void setConvertedAccountId(String value) { setStringField(F_CONVERTED_ACCOUNT_ID, value); }

	public String getConvertedContactId() { return getStringField(F_CONVERTED_CONTACT_ID); }
	public void setConvertedContactId(String value) { setStringField(F_CONVERTED_CONTACT_ID, value); }

	public Calendar getConvertedDate() { return getDateField(F_CONVERTED_DATE); }
	public void setConvertedDate(Calendar value) { setDateField(F_CONVERTED_DATE, value); }

	public String getConvertedOpportunityId() { return getStringField(F_CONVERTED_OPPORTUNITY_ID); }
	public void setConvertedOpportunityId(String value) { setStringField(F_CONVERTED_OPPORTUNITY_ID, value); }

	public String getCountry() { return getStringField(F_COUNTRY); }
	public void setCountry(String value) { setStringField(F_COUNTRY, value); }

	public String getCreatedById() { return getStringField(F_CREATED_BY_ID); }
	public void setCreatedById(String value) { setStringField(F_CREATED_BY_ID, value); }

	public Calendar getCreatedDate() { return getDateTimeField(F_CREATED_DATE); }
	public void setCreatedDate(Calendar value) { setDateTimeField(F_CREATED_DATE, value); }

	public String getCurrencyIsoCode() { return getStringField(F_CURRENCY_ISO_CODE); }
	public void setCurrencyIsoCode(String value) { setStringField(F_CURRENCY_ISO_CODE, value); }

	public String getDescription() { return getStringField(F_DESCRIPTION); }
	public void setDescription(String value) { setStringField(F_DESCRIPTION, value); }

	public String getEmail() { return getStringField(F_EMAIL); }
	public void setEmail(String value) { setStringField(F_EMAIL, value); }

	public Calendar getEmailBouncedDate() { return getDateTimeField(F_EMAIL_BOUNCED_DATE); }

	public String getEmailBouncedReason() { return getStringField(F_EMAIL_BOUNCED_REASON); }

	public String getFirstName() { return getStringField(F_FIRST_NAME); }
	public void setFirstName(String value) { setStringField(F_FIRST_NAME, value); }

	public String getIndustry() { return getStringField(F_INDUSTRY); }
	public void setIndustry(String value) { setStringField(F_INDUSTRY, value); }

	public Boolean getIsConverted() { return getBooleanField(F_IS_CONVERTED); }
	public void setIsConverted(Boolean value) { setBooleanField(F_IS_CONVERTED, value); }

	public Boolean getIsDeleted() { return getBooleanField(F_IS_DELETED); }

	public Boolean getIsUnreadByOwner() { return getBooleanField(F_IS_UNREAD_BY_OWNER); }
	public void setIsUnreadByOwner(Boolean value) { setBooleanField(F_IS_UNREAD_BY_OWNER, value); }

	public Calendar getLastActivityDate() { return getDateField(F_LAST_ACTIVITY_DATE); }

	public String getLastModifiedById() { return getStringField(F_LAST_MODIFIED_BY_ID); }
	public void setLastModifiedById(String value) { setStringField(F_LAST_MODIFIED_BY_ID, value); }

	public Calendar getLastModifiedDate() { return getDateTimeField(F_LAST_MODIFIED_DATE); }
	public void setLastModifiedDate(Calendar value) { setDateTimeField(F_LAST_MODIFIED_DATE, value); }

	public String getLastName() { return getStringField(F_LAST_NAME); }
	public void setLastName(String value) { setStringField(F_LAST_NAME, value); }

	public String getLeadSource() { return getStringField(F_LEAD_SOURCE); }
	public void setLeadSource(String value) { setStringField(F_LEAD_SOURCE, value); }

	public String getMasterRecordId() { return getStringField(F_MASTER_RECORD_ID); }

	public String getName() { return getStringField(F_NAME); }

	public Integer getNumberOfEmployees() { return getIntegerField(F_NUMBER_OF_EMPLOYEES); }
	public void setNumberOfEmployees(Integer value) { setIntegerField(F_NUMBER_OF_EMPLOYEES, value); }

	public String getOwnerId() { return getStringField(F_OWNER_ID); }
	public void setOwnerId(String value) { setStringField(F_OWNER_ID, value); }

	public String getPhone() { return getStringField(F_PHONE); }
	public void setPhone(String value) { setStringField(F_PHONE, value); }

	public String getPostalCode() { return getStringField(F_POSTAL_CODE); }
	public void setPostalCode(String value) { setStringField(F_POSTAL_CODE, value); }

	public String getRating() { return getStringField(F_RATING); }
	public void setRating(String value) { setStringField(F_RATING, value); }

	public String getSalutation() { return getStringField(F_SALUTATION); }
	public void setSalutation(String value) { setStringField(F_SALUTATION, value); }

	public String getState() { return getStringField(F_STATE); }
	public void setState(String value) { setStringField(F_STATE, value); }

	public String getStatus() { return getStringField(F_STATUS); }
	public void setStatus(String value) { setStringField(F_STATUS, value); }

	public String getStreet() { return getStringField(F_STREET); }
	public void setStreet(String value) { setStringField(F_STREET, value); }

	public Calendar getSystemModstamp() { return getDateTimeField(F_SYSTEM_MODSTAMP); }

	public String getTitle() { return getStringField(F_TITLE); }
	public void setTitle(String value) { setStringField(F_TITLE, value); }

	public String getWebsite() { return getStringField(F_WEBSITE); }
	public void setWebsite(String value) { setStringField(F_WEBSITE, value); }

	
}

