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
 * Access to a SalesForce Opportunity Object using the SFDC Partner interface.
 * 
 * @author SObjectRecordCodeGenerator
 *
 */
public class  OpportunityRecord  extends AbstractSObjectRecord {
	
	
	public static final String SOBJECT_NAME = "Opportunity";
	
	//
	// Constants for all fields in a Opportunity.
	// Use these constants when forming a SFDC query in Java to
	// avoid typing mistakes.
	//
	public static final String F_ACCOUNT_ID = "AccountId";
	public static final String F_AMOUNT = "Amount";
	public static final String F_CAMPAIGN_ID = "CampaignId";
	public static final String F_CLOSE_DATE = "CloseDate";
	public static final String F_CREATED_BY_ID = "CreatedById";
	public static final String F_CREATED_DATE = "CreatedDate";
	public static final String F_DESCRIPTION = "Description";
	public static final String F_EXPECTED_REVENUE = "ExpectedRevenue";
	public static final String F_FISCAL = "Fiscal";
	public static final String F_FISCAL_QUARTER = "FiscalQuarter";
	public static final String F_FISCAL_YEAR = "FiscalYear";
	public static final String F_FORECAST_CATEGORY = "ForecastCategory";
	public static final String F_HAS_OPPORTUNITY_LINE_ITEM = "HasOpportunityLineItem";
	public static final String F_IS_CLOSED = "IsClosed";
	public static final String F_IS_DELETED = "IsDeleted";
	public static final String F_IS_WON = "IsWon";
	public static final String F_LAST_ACTIVITY_DATE = "LastActivityDate";
	public static final String F_LAST_MODIFIED_BY_ID = "LastModifiedById";
	public static final String F_LAST_MODIFIED_DATE = "LastModifiedDate";
	public static final String F_LEAD_SOURCE = "LeadSource";
	public static final String F_NAME = "Name";
	public static final String F_NEXT_STEP = "NextStep";
	public static final String F_OWNER_ID = "OwnerId";
	public static final String F_PRICEBOOK2_ID = "Pricebook2Id";
	public static final String F_PROBABILITY = "Probability";
	public static final String F_STAGE_NAME = "StageName";
	public static final String F_SYSTEM_MODSTAMP = "SystemModstamp";
	public static final String F_TOTAL_OPPORTUNITY_QUANTITY = "TotalOpportunityQuantity";
	public static final String F_TYPE = "Type";

	
	public OpportunityRecord() {
		this( (SObject) null);
	}
	
	public OpportunityRecord(SObject sObject ) {
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
	// Lots and lots of type safe wrappers for Opportunity parameters.
	//
	

	public String getAccountId() { return getStringField(F_ACCOUNT_ID); }
	public void setAccountId(String value) { setStringField(F_ACCOUNT_ID, value); }

	public Double getAmount() { return getDoubleField(F_AMOUNT); }
	public void setAmount(Double value) { setDoubleField(F_AMOUNT, value); }

	public String getCampaignId() { return getStringField(F_CAMPAIGN_ID); }
	public void setCampaignId(String value) { setStringField(F_CAMPAIGN_ID, value); }

	public Calendar getCloseDate() { return getDateField(F_CLOSE_DATE); }
	public void setCloseDate(Calendar value) { setDateField(F_CLOSE_DATE, value); }

	public String getCreatedById() { return getStringField(F_CREATED_BY_ID); }
	public void setCreatedById(String value) { setStringField(F_CREATED_BY_ID, value); }

	public Calendar getCreatedDate() { return getDateTimeField(F_CREATED_DATE); }
	public void setCreatedDate(Calendar value) { setDateTimeField(F_CREATED_DATE, value); }

	public String getDescription() { return getStringField(F_DESCRIPTION); }
	public void setDescription(String value) { setStringField(F_DESCRIPTION, value); }

	public Double getExpectedRevenue() { return getDoubleField(F_EXPECTED_REVENUE); }

	public String getFiscal() { return getStringField(F_FISCAL); }

	public Integer getFiscalQuarter() { return getIntegerField(F_FISCAL_QUARTER); }

	public Integer getFiscalYear() { return getIntegerField(F_FISCAL_YEAR); }

	public String getForecastCategory() { return getStringField(F_FORECAST_CATEGORY); }
	public void setForecastCategory(String value) { setStringField(F_FORECAST_CATEGORY, value); }

	public Boolean getHasOpportunityLineItem() { return getBooleanField(F_HAS_OPPORTUNITY_LINE_ITEM); }

	public Boolean getIsClosed() { return getBooleanField(F_IS_CLOSED); }

	public Boolean getIsDeleted() { return getBooleanField(F_IS_DELETED); }

	public Boolean getIsWon() { return getBooleanField(F_IS_WON); }

	public Calendar getLastActivityDate() { return getDateField(F_LAST_ACTIVITY_DATE); }

	public String getLastModifiedById() { return getStringField(F_LAST_MODIFIED_BY_ID); }
	public void setLastModifiedById(String value) { setStringField(F_LAST_MODIFIED_BY_ID, value); }

	public Calendar getLastModifiedDate() { return getDateTimeField(F_LAST_MODIFIED_DATE); }
	public void setLastModifiedDate(Calendar value) { setDateTimeField(F_LAST_MODIFIED_DATE, value); }

	public String getLeadSource() { return getStringField(F_LEAD_SOURCE); }
	public void setLeadSource(String value) { setStringField(F_LEAD_SOURCE, value); }

	public String getName() { return getStringField(F_NAME); }
	public void setName(String value) { setStringField(F_NAME, value); }

	public String getNextStep() { return getStringField(F_NEXT_STEP); }
	public void setNextStep(String value) { setStringField(F_NEXT_STEP, value); }

	public String getOwnerId() { return getStringField(F_OWNER_ID); }
	public void setOwnerId(String value) { setStringField(F_OWNER_ID, value); }

	public String getPricebook2Id() { return getStringField(F_PRICEBOOK2_ID); }
	public void setPricebook2Id(String value) { setStringField(F_PRICEBOOK2_ID, value); }

	public Double getProbability() { return getDoubleField(F_PROBABILITY); }
	public void setProbability(Double value) { setDoubleField(F_PROBABILITY, value); }

	public String getStageName() { return getStringField(F_STAGE_NAME); }
	public void setStageName(String value) { setStringField(F_STAGE_NAME, value); }

	public Calendar getSystemModstamp() { return getDateTimeField(F_SYSTEM_MODSTAMP); }

	public Double getTotalOpportunityQuantity() { return getDoubleField(F_TOTAL_OPPORTUNITY_QUANTITY); }
	public void setTotalOpportunityQuantity(Double value) { setDoubleField(F_TOTAL_OPPORTUNITY_QUANTITY, value); }

	public String getType() { return getStringField(F_TYPE); }
	public void setType(String value) { setStringField(F_TYPE, value); }

	
}
