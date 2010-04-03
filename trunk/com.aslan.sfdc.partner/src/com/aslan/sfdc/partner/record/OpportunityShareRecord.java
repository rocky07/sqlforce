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
 * Access to a SalesForce OpportunityShare Object using the SFDC Partner interface.
 * 
 * @author SObjectRecordCodeGenerator
 *
 */
public class  OpportunityShareRecord  extends AbstractSObjectRecord {
	
	
	public static final String SOBJECT_NAME = "OpportunityShare";
	
	//
	// Constants for all fields in a OpportunityShare.
	// Use these constants when forming a SFDC query in Java to
	// avoid typing mistakes.
	//
	public static final String F_IS_DELETED = "IsDeleted";
	public static final String F_LAST_MODIFIED_BY_ID = "LastModifiedById";
	public static final String F_LAST_MODIFIED_DATE = "LastModifiedDate";
	public static final String F_OPPORTUNITY_ACCESS_LEVEL = "OpportunityAccessLevel";
	public static final String F_OPPORTUNITY_ID = "OpportunityId";
	public static final String F_ROW_CAUSE = "RowCause";
	public static final String F_USER_OR_GROUP_ID = "UserOrGroupId";

	
	public OpportunityShareRecord() {
		this( (SObject) null);
	}
	
	public OpportunityShareRecord(SObject sObject ) {
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
	// Lots and lots of type safe wrappers for OpportunityShare parameters.
	//
	

	public Boolean getIsDeleted() { return getBooleanField(F_IS_DELETED); }

	public String getLastModifiedById() { return getStringField(F_LAST_MODIFIED_BY_ID); }

	public Calendar getLastModifiedDate() { return getDateTimeField(F_LAST_MODIFIED_DATE); }

	public String getOpportunityAccessLevel() { return getStringField(F_OPPORTUNITY_ACCESS_LEVEL); }
	public void setOpportunityAccessLevel(String value) { setStringField(F_OPPORTUNITY_ACCESS_LEVEL, value); }

	public String getOpportunityId() { return getStringField(F_OPPORTUNITY_ID); }
	public void setOpportunityId(String value) { setStringField(F_OPPORTUNITY_ID, value); }

	public String getRowCause() { return getStringField(F_ROW_CAUSE); }

	public String getUserOrGroupId() { return getStringField(F_USER_OR_GROUP_ID); }
	public void setUserOrGroupId(String value) { setStringField(F_USER_OR_GROUP_ID, value); }

	
}
