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
 * Access to a SalesForce Account Object using the SFDC Partner interface.
 * 
 * @author SObjectRecordCodeGenerator
 *
 */
public class  AccountRecord  extends AbstractSObjectRecord {
	
	
	public static final String SOBJECT_NAME = "Account";
	
	//
	// Constants for all fields in a Account.
	// Use these constants when forming a SFDC query in Java to
	// avoid typing mistakes.
	//
	public static final String F_ANNUAL_REVENUE = "AnnualRevenue";
	public static final String F_BILLING_CITY = "BillingCity";
	public static final String F_BILLING_COUNTRY = "BillingCountry";
	public static final String F_BILLING_POSTAL_CODE = "BillingPostalCode";
	public static final String F_BILLING_STATE = "BillingState";
	public static final String F_BILLING_STREET = "BillingStreet";
	public static final String F_CONNECTION_RECEIVED_ID = "ConnectionReceivedId";
	public static final String F_CONNECTION_SENT_ID = "ConnectionSentId";
	public static final String F_CREATED_BY_ID = "CreatedById";
	public static final String F_CREATED_DATE = "CreatedDate";
	public static final String F_CURRENCY_ISO_CODE = "CurrencyIsoCode";
	public static final String F_DESCRIPTION = "Description";
	public static final String F_FAX = "Fax";
	public static final String F_INDUSTRY = "Industry";
	public static final String F_IS_DELETED = "IsDeleted";
	public static final String F_LAST_ACTIVITY_DATE = "LastActivityDate";
	public static final String F_LAST_MODIFIED_BY_ID = "LastModifiedById";
	public static final String F_LAST_MODIFIED_DATE = "LastModifiedDate";
	public static final String F_MASTER_RECORD_ID = "MasterRecordId";
	public static final String F_NAME = "Name";
	public static final String F_NUMBER_OF_EMPLOYEES = "NumberOfEmployees";
	public static final String F_OWNER_ID = "OwnerId";
	public static final String F_PARENT_ID = "ParentId";
	public static final String F_PHONE = "Phone";
	public static final String F_SHIPPING_CITY = "ShippingCity";
	public static final String F_SHIPPING_COUNTRY = "ShippingCountry";
	public static final String F_SHIPPING_POSTAL_CODE = "ShippingPostalCode";
	public static final String F_SHIPPING_STATE = "ShippingState";
	public static final String F_SHIPPING_STREET = "ShippingStreet";
	public static final String F_SITE = "Site";
	public static final String F_SYSTEM_MODSTAMP = "SystemModstamp";
	public static final String F_TICKER_SYMBOL = "TickerSymbol";
	public static final String F_TYPE = "Type";
	public static final String F_WEBSITE = "Website";

	
	public AccountRecord() {
		this( (SObject) null);
	}
	
	public AccountRecord(SObject sObject ) {
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
	// Lots and lots of type safe wrappers for Account parameters.
	//
	

	public Double getAnnualRevenue() { return getDoubleField(F_ANNUAL_REVENUE); }
	public void setAnnualRevenue(Double value) { setDoubleField(F_ANNUAL_REVENUE, value); }

	public String getBillingCity() { return getStringField(F_BILLING_CITY); }
	public void setBillingCity(String value) { setStringField(F_BILLING_CITY, value); }

	public String getBillingCountry() { return getStringField(F_BILLING_COUNTRY); }
	public void setBillingCountry(String value) { setStringField(F_BILLING_COUNTRY, value); }

	public String getBillingPostalCode() { return getStringField(F_BILLING_POSTAL_CODE); }
	public void setBillingPostalCode(String value) { setStringField(F_BILLING_POSTAL_CODE, value); }

	public String getBillingState() { return getStringField(F_BILLING_STATE); }
	public void setBillingState(String value) { setStringField(F_BILLING_STATE, value); }

	public String getBillingStreet() { return getStringField(F_BILLING_STREET); }
	public void setBillingStreet(String value) { setStringField(F_BILLING_STREET, value); }

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

	public String getFax() { return getStringField(F_FAX); }
	public void setFax(String value) { setStringField(F_FAX, value); }

	public String getIndustry() { return getStringField(F_INDUSTRY); }
	public void setIndustry(String value) { setStringField(F_INDUSTRY, value); }

	public Boolean getIsDeleted() { return getBooleanField(F_IS_DELETED); }

	public Calendar getLastActivityDate() { return getDateField(F_LAST_ACTIVITY_DATE); }

	public String getLastModifiedById() { return getStringField(F_LAST_MODIFIED_BY_ID); }
	public void setLastModifiedById(String value) { setStringField(F_LAST_MODIFIED_BY_ID, value); }

	public Calendar getLastModifiedDate() { return getDateTimeField(F_LAST_MODIFIED_DATE); }
	public void setLastModifiedDate(Calendar value) { setDateTimeField(F_LAST_MODIFIED_DATE, value); }

	public String getMasterRecordId() { return getStringField(F_MASTER_RECORD_ID); }

	public String getName() { return getStringField(F_NAME); }
	public void setName(String value) { setStringField(F_NAME, value); }

	public Integer getNumberOfEmployees() { return getIntegerField(F_NUMBER_OF_EMPLOYEES); }
	public void setNumberOfEmployees(Integer value) { setIntegerField(F_NUMBER_OF_EMPLOYEES, value); }

	public String getOwnerId() { return getStringField(F_OWNER_ID); }
	public void setOwnerId(String value) { setStringField(F_OWNER_ID, value); }

	public String getParentId() { return getStringField(F_PARENT_ID); }
	public void setParentId(String value) { setStringField(F_PARENT_ID, value); }

	public String getPhone() { return getStringField(F_PHONE); }
	public void setPhone(String value) { setStringField(F_PHONE, value); }

	public String getShippingCity() { return getStringField(F_SHIPPING_CITY); }
	public void setShippingCity(String value) { setStringField(F_SHIPPING_CITY, value); }

	public String getShippingCountry() { return getStringField(F_SHIPPING_COUNTRY); }
	public void setShippingCountry(String value) { setStringField(F_SHIPPING_COUNTRY, value); }

	public String getShippingPostalCode() { return getStringField(F_SHIPPING_POSTAL_CODE); }
	public void setShippingPostalCode(String value) { setStringField(F_SHIPPING_POSTAL_CODE, value); }

	public String getShippingState() { return getStringField(F_SHIPPING_STATE); }
	public void setShippingState(String value) { setStringField(F_SHIPPING_STATE, value); }

	public String getShippingStreet() { return getStringField(F_SHIPPING_STREET); }
	public void setShippingStreet(String value) { setStringField(F_SHIPPING_STREET, value); }

	public String getSite() { return getStringField(F_SITE); }
	public void setSite(String value) { setStringField(F_SITE, value); }

	public Calendar getSystemModstamp() { return getDateTimeField(F_SYSTEM_MODSTAMP); }

	public String getTickerSymbol() { return getStringField(F_TICKER_SYMBOL); }
	public void setTickerSymbol(String value) { setStringField(F_TICKER_SYMBOL, value); }

	public String getType() { return getStringField(F_TYPE); }
	public void setType(String value) { setStringField(F_TYPE, value); }

	public String getWebsite() { return getStringField(F_WEBSITE); }
	public void setWebsite(String value) { setStringField(F_WEBSITE, value); }

	
}

