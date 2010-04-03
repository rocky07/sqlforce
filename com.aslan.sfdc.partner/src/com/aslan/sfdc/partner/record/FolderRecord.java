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
 * Access to a SalesForce Folder Object using the SFDC Partner interface.
 * 
 * @author SObjectRecordCodeGenerator
 *
 */
public class  FolderRecord  extends AbstractSObjectRecord {
	
	
	public static final String SOBJECT_NAME = "Folder";
	
	//
	// Constants for all fields in a Folder.
	// Use these constants when forming a SFDC query in Java to
	// avoid typing mistakes.
	//
	public static final String F_ACCESS_TYPE = "AccessType";
	public static final String F_CREATED_BY_ID = "CreatedById";
	public static final String F_CREATED_DATE = "CreatedDate";
	public static final String F_DEVELOPER_NAME = "DeveloperName";
	public static final String F_IS_READONLY = "IsReadonly";
	public static final String F_LAST_MODIFIED_BY_ID = "LastModifiedById";
	public static final String F_LAST_MODIFIED_DATE = "LastModifiedDate";
	public static final String F_NAME = "Name";
	public static final String F_NAMESPACE_PREFIX = "NamespacePrefix";
	public static final String F_SYSTEM_MODSTAMP = "SystemModstamp";
	public static final String F_TYPE = "Type";

	
	public FolderRecord() {
		this( (SObject) null);
	}
	
	public FolderRecord(SObject sObject ) {
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
	// Lots and lots of type safe wrappers for Folder parameters.
	//
	

	public String getAccessType() { return getStringField(F_ACCESS_TYPE); }
	public void setAccessType(String value) { setStringField(F_ACCESS_TYPE, value); }

	public String getCreatedById() { return getStringField(F_CREATED_BY_ID); }

	public Calendar getCreatedDate() { return getDateTimeField(F_CREATED_DATE); }

	public String getDeveloperName() { return getStringField(F_DEVELOPER_NAME); }
	public void setDeveloperName(String value) { setStringField(F_DEVELOPER_NAME, value); }

	public Boolean getIsReadonly() { return getBooleanField(F_IS_READONLY); }
	public void setIsReadonly(Boolean value) { setBooleanField(F_IS_READONLY, value); }

	public String getLastModifiedById() { return getStringField(F_LAST_MODIFIED_BY_ID); }

	public Calendar getLastModifiedDate() { return getDateTimeField(F_LAST_MODIFIED_DATE); }

	public String getName() { return getStringField(F_NAME); }
	public void setName(String value) { setStringField(F_NAME, value); }

	public String getNamespacePrefix() { return getStringField(F_NAMESPACE_PREFIX); }
	public void setNamespacePrefix(String value) { setStringField(F_NAMESPACE_PREFIX, value); }

	public Calendar getSystemModstamp() { return getDateTimeField(F_SYSTEM_MODSTAMP); }

	public String getType() { return getStringField(F_TYPE); }
	public void setType(String value) { setStringField(F_TYPE, value); }

	
}
