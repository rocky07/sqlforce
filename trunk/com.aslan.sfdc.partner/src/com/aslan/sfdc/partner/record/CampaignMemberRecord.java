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
 * Access to a SalesForce CampaignMember Object using the SFDC Partner interface.
 * 
 * @author SObjectRecordCodeGenerator
 *
 */
public class  CampaignMemberRecord  extends AbstractSObjectRecord {
	
	
	public static final String SOBJECT_NAME = "CampaignMember";
	
	//
	// Constants for all fields in a CampaignMember.
	// Use these constants when forming a SFDC query in Java to
	// avoid typing mistakes.
	//
	public static final String F_CAMPAIGN_ID = "CampaignId";
	public static final String F_CONTACT_ID = "ContactId";
	public static final String F_CREATED_BY_ID = "CreatedById";
	public static final String F_CREATED_DATE = "CreatedDate";
	public static final String F_FIRST_RESPONDED_DATE = "FirstRespondedDate";
	public static final String F_HAS_RESPONDED = "HasResponded";
	public static final String F_IS_DELETED = "IsDeleted";
	public static final String F_LAST_MODIFIED_BY_ID = "LastModifiedById";
	public static final String F_LAST_MODIFIED_DATE = "LastModifiedDate";
	public static final String F_LEAD_ID = "LeadId";
	public static final String F_STATUS = "Status";
	public static final String F_SYSTEM_MODSTAMP = "SystemModstamp";

	
	public CampaignMemberRecord() {
		this( (SObject) null);
	}
	
	public CampaignMemberRecord(SObject sObject ) {
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
	// Lots and lots of type safe wrappers for CampaignMember parameters.
	//
	

	public String getCampaignId() { return getStringField(F_CAMPAIGN_ID); }
	public void setCampaignId(String value) { setStringField(F_CAMPAIGN_ID, value); }

	public String getContactId() { return getStringField(F_CONTACT_ID); }
	public void setContactId(String value) { setStringField(F_CONTACT_ID, value); }

	public String getCreatedById() { return getStringField(F_CREATED_BY_ID); }
	public void setCreatedById(String value) { setStringField(F_CREATED_BY_ID, value); }

	public Calendar getCreatedDate() { return getDateTimeField(F_CREATED_DATE); }
	public void setCreatedDate(Calendar value) { setDateTimeField(F_CREATED_DATE, value); }

	public Calendar getFirstRespondedDate() { return getDateField(F_FIRST_RESPONDED_DATE); }

	public Boolean getHasResponded() { return getBooleanField(F_HAS_RESPONDED); }

	public Boolean getIsDeleted() { return getBooleanField(F_IS_DELETED); }

	public String getLastModifiedById() { return getStringField(F_LAST_MODIFIED_BY_ID); }
	public void setLastModifiedById(String value) { setStringField(F_LAST_MODIFIED_BY_ID, value); }

	public Calendar getLastModifiedDate() { return getDateTimeField(F_LAST_MODIFIED_DATE); }
	public void setLastModifiedDate(Calendar value) { setDateTimeField(F_LAST_MODIFIED_DATE, value); }

	public String getLeadId() { return getStringField(F_LEAD_ID); }
	public void setLeadId(String value) { setStringField(F_LEAD_ID, value); }

	public String getStatus() { return getStringField(F_STATUS); }
	public void setStatus(String value) { setStringField(F_STATUS, value); }

	public Calendar getSystemModstamp() { return getDateTimeField(F_SYSTEM_MODSTAMP); }

	
}
