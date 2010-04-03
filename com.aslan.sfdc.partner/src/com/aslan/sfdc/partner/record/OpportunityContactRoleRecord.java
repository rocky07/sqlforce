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
 * Access to a SalesForce OpportunityContactRole Object using the SFDC Partner interface.
 * 
 * @author SObjectRecordCodeGenerator
 *
 */
public class  OpportunityContactRoleRecord  extends AbstractSObjectRecord {
	
	
	public static final String SOBJECT_NAME = "OpportunityContactRole";
	
	//
	// Constants for all fields in a OpportunityContactRole.
	// Use these constants when forming a SFDC query in Java to
	// avoid typing mistakes.
	//
	public static final String F_CONTACT_ID = "ContactId";
	public static final String F_CREATED_BY_ID = "CreatedById";
	public static final String F_CREATED_DATE = "CreatedDate";
	public static final String F_IS_DELETED = "IsDeleted";
	public static final String F_IS_PRIMARY = "IsPrimary";
	public static final String F_LAST_MODIFIED_BY_ID = "LastModifiedById";
	public static final String F_LAST_MODIFIED_DATE = "LastModifiedDate";
	public static final String F_OPPORTUNITY_ID = "OpportunityId";
	public static final String F_ROLE = "Role";
	public static final String F_SYSTEM_MODSTAMP = "SystemModstamp";

	
	public OpportunityContactRoleRecord() {
		this( (SObject) null);
	}
	
	public OpportunityContactRoleRecord(SObject sObject ) {
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
	// Lots and lots of type safe wrappers for OpportunityContactRole parameters.
	//
	

	public String getContactId() { return getStringField(F_CONTACT_ID); }
	public void setContactId(String value) { setStringField(F_CONTACT_ID, value); }

	public String getCreatedById() { return getStringField(F_CREATED_BY_ID); }

	public Calendar getCreatedDate() { return getDateTimeField(F_CREATED_DATE); }

	public Boolean getIsDeleted() { return getBooleanField(F_IS_DELETED); }

	public Boolean getIsPrimary() { return getBooleanField(F_IS_PRIMARY); }
	public void setIsPrimary(Boolean value) { setBooleanField(F_IS_PRIMARY, value); }

	public String getLastModifiedById() { return getStringField(F_LAST_MODIFIED_BY_ID); }

	public Calendar getLastModifiedDate() { return getDateTimeField(F_LAST_MODIFIED_DATE); }

	public String getOpportunityId() { return getStringField(F_OPPORTUNITY_ID); }
	public void setOpportunityId(String value) { setStringField(F_OPPORTUNITY_ID, value); }

	public String getRole() { return getStringField(F_ROLE); }
	public void setRole(String value) { setStringField(F_ROLE, value); }

	public Calendar getSystemModstamp() { return getDateTimeField(F_SYSTEM_MODSTAMP); }

	
}
