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
 * Access to a SalesForce Note Object using the SFDC Partner interface.
 * 
 * @author SObjectRecordCodeGenerator
 *
 */
public class  NoteRecord  extends AbstractSObjectRecord {
	
	
	public static final String SOBJECT_NAME = "Note";
	
	//
	// Constants for all fields in a Note.
	// Use these constants when forming a SFDC query in Java to
	// avoid typing mistakes.
	//
	public static final String F_BODY = "Body";
	public static final String F_CREATED_BY_ID = "CreatedById";
	public static final String F_CREATED_DATE = "CreatedDate";
	public static final String F_IS_DELETED = "IsDeleted";
	public static final String F_IS_PRIVATE = "IsPrivate";
	public static final String F_LAST_MODIFIED_BY_ID = "LastModifiedById";
	public static final String F_LAST_MODIFIED_DATE = "LastModifiedDate";
	public static final String F_OWNER_ID = "OwnerId";
	public static final String F_PARENT_ID = "ParentId";
	public static final String F_SYSTEM_MODSTAMP = "SystemModstamp";
	public static final String F_TITLE = "Title";

	
	public NoteRecord() {
		this( (SObject) null);
	}
	
	public NoteRecord(SObject sObject ) {
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
	// Lots and lots of type safe wrappers for Note parameters.
	//
	

	public String getBody() { return getStringField(F_BODY); }
	public void setBody(String value) { setStringField(F_BODY, value); }

	public String getCreatedById() { return getStringField(F_CREATED_BY_ID); }

	public Calendar getCreatedDate() { return getDateTimeField(F_CREATED_DATE); }

	public Boolean getIsDeleted() { return getBooleanField(F_IS_DELETED); }

	public Boolean getIsPrivate() { return getBooleanField(F_IS_PRIVATE); }
	public void setIsPrivate(Boolean value) { setBooleanField(F_IS_PRIVATE, value); }

	public String getLastModifiedById() { return getStringField(F_LAST_MODIFIED_BY_ID); }

	public Calendar getLastModifiedDate() { return getDateTimeField(F_LAST_MODIFIED_DATE); }

	public String getOwnerId() { return getStringField(F_OWNER_ID); }
	public void setOwnerId(String value) { setStringField(F_OWNER_ID, value); }

	public String getParentId() { return getStringField(F_PARENT_ID); }
	public void setParentId(String value) { setStringField(F_PARENT_ID, value); }

	public Calendar getSystemModstamp() { return getDateTimeField(F_SYSTEM_MODSTAMP); }

	public String getTitle() { return getStringField(F_TITLE); }
	public void setTitle(String value) { setStringField(F_TITLE, value); }

	
}
