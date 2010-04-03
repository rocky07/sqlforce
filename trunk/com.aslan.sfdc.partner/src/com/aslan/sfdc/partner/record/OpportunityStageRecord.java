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
 * Access to a SalesForce OpportunityStage Object using the SFDC Partner interface.
 * 
 * @author SObjectRecordCodeGenerator
 *
 */
public class  OpportunityStageRecord  extends AbstractSObjectRecord {
	
	
	public static final String SOBJECT_NAME = "OpportunityStage";
	
	//
	// Constants for all fields in a OpportunityStage.
	// Use these constants when forming a SFDC query in Java to
	// avoid typing mistakes.
	//
	public static final String F_CREATED_BY_ID = "CreatedById";
	public static final String F_CREATED_DATE = "CreatedDate";
	public static final String F_DEFAULT_PROBABILITY = "DefaultProbability";
	public static final String F_DESCRIPTION = "Description";
	public static final String F_FORECAST_CATEGORY = "ForecastCategory";
	public static final String F_FORECAST_CATEGORY_NAME = "ForecastCategoryName";
	public static final String F_IS_ACTIVE = "IsActive";
	public static final String F_IS_CLOSED = "IsClosed";
	public static final String F_IS_WON = "IsWon";
	public static final String F_LAST_MODIFIED_BY_ID = "LastModifiedById";
	public static final String F_LAST_MODIFIED_DATE = "LastModifiedDate";
	public static final String F_MASTER_LABEL = "MasterLabel";
	public static final String F_SORT_ORDER = "SortOrder";
	public static final String F_SYSTEM_MODSTAMP = "SystemModstamp";

	
	public OpportunityStageRecord() {
		this( (SObject) null);
	}
	
	public OpportunityStageRecord(SObject sObject ) {
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
	// Lots and lots of type safe wrappers for OpportunityStage parameters.
	//
	

	public String getCreatedById() { return getStringField(F_CREATED_BY_ID); }

	public Calendar getCreatedDate() { return getDateTimeField(F_CREATED_DATE); }

	public Double getDefaultProbability() { return getDoubleField(F_DEFAULT_PROBABILITY); }

	public String getDescription() { return getStringField(F_DESCRIPTION); }

	public String getForecastCategory() { return getStringField(F_FORECAST_CATEGORY); }

	public String getForecastCategoryName() { return getStringField(F_FORECAST_CATEGORY_NAME); }

	public Boolean getIsActive() { return getBooleanField(F_IS_ACTIVE); }

	public Boolean getIsClosed() { return getBooleanField(F_IS_CLOSED); }

	public Boolean getIsWon() { return getBooleanField(F_IS_WON); }

	public String getLastModifiedById() { return getStringField(F_LAST_MODIFIED_BY_ID); }

	public Calendar getLastModifiedDate() { return getDateTimeField(F_LAST_MODIFIED_DATE); }

	public String getMasterLabel() { return getStringField(F_MASTER_LABEL); }

	public Integer getSortOrder() { return getIntegerField(F_SORT_ORDER); }

	public Calendar getSystemModstamp() { return getDateTimeField(F_SYSTEM_MODSTAMP); }

	
}
