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
 * Access to a SalesForce OpportunityHistory Object using the SFDC Partner interface.
 * 
 * @author SObjectRecordCodeGenerator
 *
 */
public class  OpportunityHistoryRecord  extends AbstractSObjectRecord {
	
	
	public static final String SOBJECT_NAME = "OpportunityHistory";
	
	//
	// Constants for all fields in a OpportunityHistory.
	// Use these constants when forming a SFDC query in Java to
	// avoid typing mistakes.
	//
	public static final String F_AMOUNT = "Amount";
	public static final String F_CLOSE_DATE = "CloseDate";
	public static final String F_CREATED_BY_ID = "CreatedById";
	public static final String F_CREATED_DATE = "CreatedDate";
	public static final String F_CURRENCY_ISO_CODE = "CurrencyIsoCode";
	public static final String F_EXPECTED_REVENUE = "ExpectedRevenue";
	public static final String F_FORECAST_CATEGORY = "ForecastCategory";
	public static final String F_IS_DELETED = "IsDeleted";
	public static final String F_OPPORTUNITY_ID = "OpportunityId";
	public static final String F_PROBABILITY = "Probability";
	public static final String F_STAGE_NAME = "StageName";
	public static final String F_SYSTEM_MODSTAMP = "SystemModstamp";

	
	public OpportunityHistoryRecord() {
		this( (SObject) null);
	}
	
	public OpportunityHistoryRecord(SObject sObject ) {
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
	// Lots and lots of type safe wrappers for OpportunityHistory parameters.
	//
	

	public Double getAmount() { return getDoubleField(F_AMOUNT); }

	public Calendar getCloseDate() { return getDateField(F_CLOSE_DATE); }

	public String getCreatedById() { return getStringField(F_CREATED_BY_ID); }

	public Calendar getCreatedDate() { return getDateTimeField(F_CREATED_DATE); }

	public String getCurrencyIsoCode() { return getStringField(F_CURRENCY_ISO_CODE); }

	public Double getExpectedRevenue() { return getDoubleField(F_EXPECTED_REVENUE); }

	public String getForecastCategory() { return getStringField(F_FORECAST_CATEGORY); }

	public Boolean getIsDeleted() { return getBooleanField(F_IS_DELETED); }

	public String getOpportunityId() { return getStringField(F_OPPORTUNITY_ID); }

	public Double getProbability() { return getDoubleField(F_PROBABILITY); }

	public String getStageName() { return getStringField(F_STAGE_NAME); }

	public Calendar getSystemModstamp() { return getDateTimeField(F_SYSTEM_MODSTAMP); }

	
}
