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
 * Access to a SalesForce CaseComment Object using the SFDC Partner interface.
 * 
 * @author SObjectRecordCodeGenerator
 *
 */
public class  CaseCommentRecord  extends AbstractSObjectRecord {
	
	
	public static final String SOBJECT_NAME = "CaseComment";
	
	//
	// Constants for all fields in a CaseComment.
	// Use these constants when forming a SFDC query in Java to
	// avoid typing mistakes.
	//
	public static final String F_COMMENT_BODY = "CommentBody";
	public static final String F_CREATED_BY_ID = "CreatedById";
	public static final String F_CREATED_DATE = "CreatedDate";
	public static final String F_IS_DELETED = "IsDeleted";
	public static final String F_IS_PUBLISHED = "IsPublished";
	public static final String F_LAST_MODIFIED_BY_ID = "LastModifiedById";
	public static final String F_LAST_MODIFIED_DATE = "LastModifiedDate";
	public static final String F_PARENT_ID = "ParentId";
	public static final String F_SYSTEM_MODSTAMP = "SystemModstamp";

	
	public CaseCommentRecord() {
		this( (SObject) null);
	}
	
	public CaseCommentRecord(SObject sObject ) {
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
	// Lots and lots of type safe wrappers for CaseComment parameters.
	//
	

	public String getCommentBody() { return getStringField(F_COMMENT_BODY); }
	public void setCommentBody(String value) { setStringField(F_COMMENT_BODY, value); }

	public String getCreatedById() { return getStringField(F_CREATED_BY_ID); }
	public void setCreatedById(String value) { setStringField(F_CREATED_BY_ID, value); }

	public Calendar getCreatedDate() { return getDateTimeField(F_CREATED_DATE); }
	public void setCreatedDate(Calendar value) { setDateTimeField(F_CREATED_DATE, value); }

	public Boolean getIsDeleted() { return getBooleanField(F_IS_DELETED); }

	public Boolean getIsPublished() { return getBooleanField(F_IS_PUBLISHED); }
	public void setIsPublished(Boolean value) { setBooleanField(F_IS_PUBLISHED, value); }

	public String getLastModifiedById() { return getStringField(F_LAST_MODIFIED_BY_ID); }
	public void setLastModifiedById(String value) { setStringField(F_LAST_MODIFIED_BY_ID, value); }

	public Calendar getLastModifiedDate() { return getDateTimeField(F_LAST_MODIFIED_DATE); }
	public void setLastModifiedDate(Calendar value) { setDateTimeField(F_LAST_MODIFIED_DATE, value); }

	public String getParentId() { return getStringField(F_PARENT_ID); }
	public void setParentId(String value) { setStringField(F_PARENT_ID, value); }

	public Calendar getSystemModstamp() { return getDateTimeField(F_SYSTEM_MODSTAMP); }

	
}
