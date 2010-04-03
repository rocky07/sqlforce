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
 * Access to a SalesForce Attachment Object using the SFDC Partner interface.
 * 
 * @author SObjectRecordCodeGenerator
 *
 */
public class  AttachmentRecord  extends AbstractSObjectRecord {
	
	
	public static final String SOBJECT_NAME = "Attachment";
	
	//
	// Constants for all fields in a Attachment.
	// Use these constants when forming a SFDC query in Java to
	// avoid typing mistakes.
	//
	public static final String F_BODY = "Body";
	public static final String F_BODY_LENGTH = "BodyLength";
	public static final String F_CONTENT_TYPE = "ContentType";
	public static final String F_CREATED_BY_ID = "CreatedById";
	public static final String F_CREATED_DATE = "CreatedDate";
	public static final String F_IS_DELETED = "IsDeleted";
	public static final String F_IS_PRIVATE = "IsPrivate";
	public static final String F_LAST_MODIFIED_BY_ID = "LastModifiedById";
	public static final String F_LAST_MODIFIED_DATE = "LastModifiedDate";
	public static final String F_NAME = "Name";
	public static final String F_OWNER_ID = "OwnerId";
	public static final String F_PARENT_ID = "ParentId";
	public static final String F_SYSTEM_MODSTAMP = "SystemModstamp";

	
	public AttachmentRecord() {
		this( (SObject) null);
	}
	
	public AttachmentRecord(SObject sObject ) {
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
	// Lots and lots of type safe wrappers for Attachment parameters.
	//
	

	public String getBody() { return getStringField(F_BODY); }
	public void setBody(String value) { setStringField(F_BODY, value); }

	public Integer getBodyLength() { return getIntegerField(F_BODY_LENGTH); }

	public String getContentType() { return getStringField(F_CONTENT_TYPE); }
	public void setContentType(String value) { setStringField(F_CONTENT_TYPE, value); }

	public String getCreatedById() { return getStringField(F_CREATED_BY_ID); }

	public Calendar getCreatedDate() { return getDateTimeField(F_CREATED_DATE); }

	public Boolean getIsDeleted() { return getBooleanField(F_IS_DELETED); }

	public Boolean getIsPrivate() { return getBooleanField(F_IS_PRIVATE); }
	public void setIsPrivate(Boolean value) { setBooleanField(F_IS_PRIVATE, value); }

	public String getLastModifiedById() { return getStringField(F_LAST_MODIFIED_BY_ID); }

	public Calendar getLastModifiedDate() { return getDateTimeField(F_LAST_MODIFIED_DATE); }

	public String getName() { return getStringField(F_NAME); }
	public void setName(String value) { setStringField(F_NAME, value); }

	public String getOwnerId() { return getStringField(F_OWNER_ID); }
	public void setOwnerId(String value) { setStringField(F_OWNER_ID, value); }

	public String getParentId() { return getStringField(F_PARENT_ID); }
	public void setParentId(String value) { setStringField(F_PARENT_ID, value); }

	public Calendar getSystemModstamp() { return getDateTimeField(F_SYSTEM_MODSTAMP); }

	
}

