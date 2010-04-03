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
 * Access to a SalesForce Campaign Object using the SFDC Partner interface.
 * 
 * @author SObjectRecordCodeGenerator
 *
 */
public class  CampaignRecord  extends AbstractSObjectRecord {
	
	
	public static final String SOBJECT_NAME = "Campaign";
	
	//
	// Constants for all fields in a Campaign.
	// Use these constants when forming a SFDC query in Java to
	// avoid typing mistakes.
	//
	public static final String F_ACTUAL_COST = "ActualCost";
	public static final String F_AMOUNT_ALL_OPPORTUNITIES = "AmountAllOpportunities";
	public static final String F_AMOUNT_WON_OPPORTUNITIES = "AmountWonOpportunities";
	public static final String F_BUDGETED_COST = "BudgetedCost";
	public static final String F_CREATED_BY_ID = "CreatedById";
	public static final String F_CREATED_DATE = "CreatedDate";
	public static final String F_CURRENCY_ISO_CODE = "CurrencyIsoCode";
	public static final String F_DESCRIPTION = "Description";
	public static final String F_END_DATE = "EndDate";
	public static final String F_EXPECTED_RESPONSE = "ExpectedResponse";
	public static final String F_EXPECTED_REVENUE = "ExpectedRevenue";
	public static final String F_IS_ACTIVE = "IsActive";
	public static final String F_IS_DELETED = "IsDeleted";
	public static final String F_LAST_ACTIVITY_DATE = "LastActivityDate";
	public static final String F_LAST_MODIFIED_BY_ID = "LastModifiedById";
	public static final String F_LAST_MODIFIED_DATE = "LastModifiedDate";
	public static final String F_NAME = "Name";
	public static final String F_NUMBER_OF_CONTACTS = "NumberOfContacts";
	public static final String F_NUMBER_OF_CONVERTED_LEADS = "NumberOfConvertedLeads";
	public static final String F_NUMBER_OF_LEADS = "NumberOfLeads";
	public static final String F_NUMBER_OF_OPPORTUNITIES = "NumberOfOpportunities";
	public static final String F_NUMBER_OF_RESPONSES = "NumberOfResponses";
	public static final String F_NUMBER_OF_WON_OPPORTUNITIES = "NumberOfWonOpportunities";
	public static final String F_NUMBER_SENT = "NumberSent";
	public static final String F_OWNER_ID = "OwnerId";
	public static final String F_START_DATE = "StartDate";
	public static final String F_STATUS = "Status";
	public static final String F_SYSTEM_MODSTAMP = "SystemModstamp";
	public static final String F_TYPE = "Type";

	
	public CampaignRecord() {
		this( (SObject) null);
	}
	
	public CampaignRecord(SObject sObject ) {
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
	// Lots and lots of type safe wrappers for Campaign parameters.
	//
	

	public Double getActualCost() { return getDoubleField(F_ACTUAL_COST); }
	public void setActualCost(Double value) { setDoubleField(F_ACTUAL_COST, value); }

	public Double getAmountAllOpportunities() { return getDoubleField(F_AMOUNT_ALL_OPPORTUNITIES); }

	public Double getAmountWonOpportunities() { return getDoubleField(F_AMOUNT_WON_OPPORTUNITIES); }

	public Double getBudgetedCost() { return getDoubleField(F_BUDGETED_COST); }
	public void setBudgetedCost(Double value) { setDoubleField(F_BUDGETED_COST, value); }

	public String getCreatedById() { return getStringField(F_CREATED_BY_ID); }

	public Calendar getCreatedDate() { return getDateTimeField(F_CREATED_DATE); }

	public String getCurrencyIsoCode() { return getStringField(F_CURRENCY_ISO_CODE); }
	public void setCurrencyIsoCode(String value) { setStringField(F_CURRENCY_ISO_CODE, value); }

	public String getDescription() { return getStringField(F_DESCRIPTION); }
	public void setDescription(String value) { setStringField(F_DESCRIPTION, value); }

	public Calendar getEndDate() { return getDateField(F_END_DATE); }
	public void setEndDate(Calendar value) { setDateField(F_END_DATE, value); }

	public Double getExpectedResponse() { return getDoubleField(F_EXPECTED_RESPONSE); }
	public void setExpectedResponse(Double value) { setDoubleField(F_EXPECTED_RESPONSE, value); }

	public Double getExpectedRevenue() { return getDoubleField(F_EXPECTED_REVENUE); }
	public void setExpectedRevenue(Double value) { setDoubleField(F_EXPECTED_REVENUE, value); }

	public Boolean getIsActive() { return getBooleanField(F_IS_ACTIVE); }
	public void setIsActive(Boolean value) { setBooleanField(F_IS_ACTIVE, value); }

	public Boolean getIsDeleted() { return getBooleanField(F_IS_DELETED); }

	public Calendar getLastActivityDate() { return getDateField(F_LAST_ACTIVITY_DATE); }

	public String getLastModifiedById() { return getStringField(F_LAST_MODIFIED_BY_ID); }

	public Calendar getLastModifiedDate() { return getDateTimeField(F_LAST_MODIFIED_DATE); }

	public String getName() { return getStringField(F_NAME); }
	public void setName(String value) { setStringField(F_NAME, value); }

	public Integer getNumberOfContacts() { return getIntegerField(F_NUMBER_OF_CONTACTS); }

	public Integer getNumberOfConvertedLeads() { return getIntegerField(F_NUMBER_OF_CONVERTED_LEADS); }

	public Integer getNumberOfLeads() { return getIntegerField(F_NUMBER_OF_LEADS); }

	public Integer getNumberOfOpportunities() { return getIntegerField(F_NUMBER_OF_OPPORTUNITIES); }

	public Integer getNumberOfResponses() { return getIntegerField(F_NUMBER_OF_RESPONSES); }

	public Integer getNumberOfWonOpportunities() { return getIntegerField(F_NUMBER_OF_WON_OPPORTUNITIES); }

	public Double getNumberSent() { return getDoubleField(F_NUMBER_SENT); }
	public void setNumberSent(Double value) { setDoubleField(F_NUMBER_SENT, value); }

	public String getOwnerId() { return getStringField(F_OWNER_ID); }
	public void setOwnerId(String value) { setStringField(F_OWNER_ID, value); }

	public Calendar getStartDate() { return getDateField(F_START_DATE); }
	public void setStartDate(Calendar value) { setDateField(F_START_DATE, value); }

	public String getStatus() { return getStringField(F_STATUS); }
	public void setStatus(String value) { setStringField(F_STATUS, value); }

	public Calendar getSystemModstamp() { return getDateTimeField(F_SYSTEM_MODSTAMP); }

	public String getType() { return getStringField(F_TYPE); }
	public void setType(String value) { setStringField(F_TYPE, value); }

	
}

