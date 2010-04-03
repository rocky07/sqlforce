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
 * Access to a SalesForce Solution Object using the SFDC Partner interface.
 * 
 * @author SObjectRecordCodeGenerator
 *
 */
public class  SolutionRecord  extends AbstractSObjectRecord {
	
	
	public static final String SOBJECT_NAME = "Solution";
	
	//
	// Constants for all fields in a Solution.
	// Use these constants when forming a SFDC query in Java to
	// avoid typing mistakes.
	//
	public static final String F_CREATED_BY_ID = "CreatedById";
	public static final String F_CREATED_DATE = "CreatedDate";
	public static final String F_CURRENCY_ISO_CODE = "CurrencyIsoCode";
	public static final String F_IS_DELETED = "IsDeleted";
	public static final String F_IS_HTML = "IsHtml";
	public static final String F_IS_PUBLISHED = "IsPublished";
	public static final String F_IS_PUBLISHED_IN_PUBLIC_KB = "IsPublishedInPublicKb";
	public static final String F_IS_REVIEWED = "IsReviewed";
	public static final String F_LAST_MODIFIED_BY_ID = "LastModifiedById";
	public static final String F_LAST_MODIFIED_DATE = "LastModifiedDate";
	public static final String F_OWNER_ID = "OwnerId";
	public static final String F_SOLUTION_NAME = "SolutionName";
	public static final String F_SOLUTION_NOTE = "SolutionNote";
	public static final String F_SOLUTION_NUMBER = "SolutionNumber";
	public static final String F_STATUS = "Status";
	public static final String F_SYSTEM_MODSTAMP = "SystemModstamp";
	public static final String F_TIMES_USED = "TimesUsed";

	
	public SolutionRecord() {
		this( (SObject) null);
	}
	
	public SolutionRecord(SObject sObject ) {
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
	// Lots and lots of type safe wrappers for Solution parameters.
	//
	

	public String getCreatedById() { return getStringField(F_CREATED_BY_ID); }

	public Calendar getCreatedDate() { return getDateTimeField(F_CREATED_DATE); }

	public String getCurrencyIsoCode() { return getStringField(F_CURRENCY_ISO_CODE); }
	public void setCurrencyIsoCode(String value) { setStringField(F_CURRENCY_ISO_CODE, value); }

	public Boolean getIsDeleted() { return getBooleanField(F_IS_DELETED); }

	public Boolean getIsHtml() { return getBooleanField(F_IS_HTML); }

	public Boolean getIsPublished() { return getBooleanField(F_IS_PUBLISHED); }
	public void setIsPublished(Boolean value) { setBooleanField(F_IS_PUBLISHED, value); }

	public Boolean getIsPublishedInPublicKb() { return getBooleanField(F_IS_PUBLISHED_IN_PUBLIC_KB); }
	public void setIsPublishedInPublicKb(Boolean value) { setBooleanField(F_IS_PUBLISHED_IN_PUBLIC_KB, value); }

	public Boolean getIsReviewed() { return getBooleanField(F_IS_REVIEWED); }

	public String getLastModifiedById() { return getStringField(F_LAST_MODIFIED_BY_ID); }

	public Calendar getLastModifiedDate() { return getDateTimeField(F_LAST_MODIFIED_DATE); }

	public String getOwnerId() { return getStringField(F_OWNER_ID); }
	public void setOwnerId(String value) { setStringField(F_OWNER_ID, value); }

	public String getSolutionName() { return getStringField(F_SOLUTION_NAME); }
	public void setSolutionName(String value) { setStringField(F_SOLUTION_NAME, value); }

	public String getSolutionNote() { return getStringField(F_SOLUTION_NOTE); }
	public void setSolutionNote(String value) { setStringField(F_SOLUTION_NOTE, value); }

	public String getSolutionNumber() { return getStringField(F_SOLUTION_NUMBER); }

	public String getStatus() { return getStringField(F_STATUS); }
	public void setStatus(String value) { setStringField(F_STATUS, value); }

	public Calendar getSystemModstamp() { return getDateTimeField(F_SYSTEM_MODSTAMP); }

	public Integer getTimesUsed() { return getIntegerField(F_TIMES_USED); }

	
}
