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
 * Access to a SalesForce OpportunityTeamMember Object using the SFDC Partner interface.
 * 
 * @author SObjectRecordCodeGenerator
 *
 */
public class  OpportunityTeamMemberRecord  extends AbstractSObjectRecord {
	
	
	public static final String SOBJECT_NAME = "OpportunityTeamMember";
	
	//
	// Constants for all fields in a OpportunityTeamMember.
	// Use these constants when forming a SFDC query in Java to
	// avoid typing mistakes.
	//
	public static final String F_CREATED_BY_ID = "CreatedById";
	public static final String F_CREATED_DATE = "CreatedDate";
	public static final String F_IS_DELETED = "IsDeleted";
	public static final String F_LAST_MODIFIED_BY_ID = "LastModifiedById";
	public static final String F_LAST_MODIFIED_DATE = "LastModifiedDate";
	public static final String F_OPPORTUNITY_ACCESS_LEVEL = "OpportunityAccessLevel";
	public static final String F_OPPORTUNITY_ID = "OpportunityId";
	public static final String F_SYSTEM_MODSTAMP = "SystemModstamp";
	public static final String F_TEAM_MEMBER_ROLE = "TeamMemberRole";
	public static final String F_USER_ID = "UserId";

	
	public OpportunityTeamMemberRecord() {
		this( (SObject) null);
	}
	
	public OpportunityTeamMemberRecord(SObject sObject ) {
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
	// Lots and lots of type safe wrappers for OpportunityTeamMember parameters.
	//
	

	public String getCreatedById() { return getStringField(F_CREATED_BY_ID); }

	public Calendar getCreatedDate() { return getDateTimeField(F_CREATED_DATE); }

	public Boolean getIsDeleted() { return getBooleanField(F_IS_DELETED); }

	public String getLastModifiedById() { return getStringField(F_LAST_MODIFIED_BY_ID); }

	public Calendar getLastModifiedDate() { return getDateTimeField(F_LAST_MODIFIED_DATE); }

	public String getOpportunityAccessLevel() { return getStringField(F_OPPORTUNITY_ACCESS_LEVEL); }

	public String getOpportunityId() { return getStringField(F_OPPORTUNITY_ID); }
	public void setOpportunityId(String value) { setStringField(F_OPPORTUNITY_ID, value); }

	public Calendar getSystemModstamp() { return getDateTimeField(F_SYSTEM_MODSTAMP); }

	public String getTeamMemberRole() { return getStringField(F_TEAM_MEMBER_ROLE); }
	public void setTeamMemberRole(String value) { setStringField(F_TEAM_MEMBER_ROLE, value); }

	public String getUserId() { return getStringField(F_USER_ID); }
	public void setUserId(String value) { setStringField(F_USER_ID, value); }

	
}
