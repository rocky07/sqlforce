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
 * Access to a SalesForce UserRole Object using the SFDC Partner interface.
 * 
 * @author SObjectRecordCodeGenerator
 *
 */
public class  UserRoleRecord  extends AbstractSObjectRecord {
	
	
	public static final String SOBJECT_NAME = "UserRole";
	
	//
	// Constants for all fields in a UserRole.
	// Use these constants when forming a SFDC query in Java to
	// avoid typing mistakes.
	//
	public static final String F_CASE_ACCESS_FOR_ACCOUNT_OWNER = "CaseAccessForAccountOwner";
	public static final String F_CONTACT_ACCESS_FOR_ACCOUNT_OWNER = "ContactAccessForAccountOwner";
	public static final String F_FORECAST_USER_ID = "ForecastUserId";
	public static final String F_LAST_MODIFIED_BY_ID = "LastModifiedById";
	public static final String F_LAST_MODIFIED_DATE = "LastModifiedDate";
	public static final String F_MAY_FORECAST_MANAGER_SHARE = "MayForecastManagerShare";
	public static final String F_NAME = "Name";
	public static final String F_OPPORTUNITY_ACCESS_FOR_ACCOUNT_OWNER = "OpportunityAccessForAccountOwner";
	public static final String F_PARENT_ROLE_ID = "ParentRoleId";
	public static final String F_PORTAL_ACCOUNT_ID = "PortalAccountId";
	public static final String F_PORTAL_ACCOUNT_OWNER_ID = "PortalAccountOwnerId";
	public static final String F_PORTAL_TYPE = "PortalType";
	public static final String F_ROLLUP_DESCRIPTION = "RollupDescription";
	public static final String F_SYSTEM_MODSTAMP = "SystemModstamp";

	
	public UserRoleRecord() {
		this( (SObject) null);
	}
	
	public UserRoleRecord(SObject sObject ) {
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
	// Lots and lots of type safe wrappers for UserRole parameters.
	//
	

	public String getCaseAccessForAccountOwner() { return getStringField(F_CASE_ACCESS_FOR_ACCOUNT_OWNER); }
	public void setCaseAccessForAccountOwner(String value) { setStringField(F_CASE_ACCESS_FOR_ACCOUNT_OWNER, value); }

	public String getContactAccessForAccountOwner() { return getStringField(F_CONTACT_ACCESS_FOR_ACCOUNT_OWNER); }

	public String getForecastUserId() { return getStringField(F_FORECAST_USER_ID); }
	public void setForecastUserId(String value) { setStringField(F_FORECAST_USER_ID, value); }

	public String getLastModifiedById() { return getStringField(F_LAST_MODIFIED_BY_ID); }

	public Calendar getLastModifiedDate() { return getDateTimeField(F_LAST_MODIFIED_DATE); }

	public Boolean getMayForecastManagerShare() { return getBooleanField(F_MAY_FORECAST_MANAGER_SHARE); }

	public String getName() { return getStringField(F_NAME); }
	public void setName(String value) { setStringField(F_NAME, value); }

	public String getOpportunityAccessForAccountOwner() { return getStringField(F_OPPORTUNITY_ACCESS_FOR_ACCOUNT_OWNER); }
	public void setOpportunityAccessForAccountOwner(String value) { setStringField(F_OPPORTUNITY_ACCESS_FOR_ACCOUNT_OWNER, value); }

	public String getParentRoleId() { return getStringField(F_PARENT_ROLE_ID); }
	public void setParentRoleId(String value) { setStringField(F_PARENT_ROLE_ID, value); }

	public String getPortalAccountId() { return getStringField(F_PORTAL_ACCOUNT_ID); }

	public String getPortalAccountOwnerId() { return getStringField(F_PORTAL_ACCOUNT_OWNER_ID); }

	public String getPortalType() { return getStringField(F_PORTAL_TYPE); }

	public String getRollupDescription() { return getStringField(F_ROLLUP_DESCRIPTION); }
	public void setRollupDescription(String value) { setStringField(F_ROLLUP_DESCRIPTION, value); }

	public Calendar getSystemModstamp() { return getDateTimeField(F_SYSTEM_MODSTAMP); }

	
}

