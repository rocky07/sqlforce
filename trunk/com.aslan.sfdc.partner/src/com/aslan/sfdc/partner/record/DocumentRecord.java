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
 * Access to a SalesForce Document Object using the SFDC Partner interface.
 * 
 * @author SObjectRecordCodeGenerator
 *
 */
public class  DocumentRecord  extends AbstractSObjectRecord {
	
	
	public static final String SOBJECT_NAME = "Document";
	
	//
	// Constants for all fields in a Document.
	// Use these constants when forming a SFDC query in Java to
	// avoid typing mistakes.
	//
	public static final String F_AUTHOR_ID = "AuthorId";
	public static final String F_BODY = "Body";
	public static final String F_BODY_LENGTH = "BodyLength";
	public static final String F_CONTENT_TYPE = "ContentType";
	public static final String F_CREATED_BY_ID = "CreatedById";
	public static final String F_CREATED_DATE = "CreatedDate";
	public static final String F_DESCRIPTION = "Description";
	public static final String F_DEVELOPER_NAME = "DeveloperName";
	public static final String F_FOLDER_ID = "FolderId";
	public static final String F_IS_BODY_SEARCHABLE = "IsBodySearchable";
	public static final String F_IS_DELETED = "IsDeleted";
	public static final String F_IS_INTERNAL_USE_ONLY = "IsInternalUseOnly";
	public static final String F_IS_PUBLIC = "IsPublic";
	public static final String F_KEYWORDS = "Keywords";
	public static final String F_LAST_MODIFIED_BY_ID = "LastModifiedById";
	public static final String F_LAST_MODIFIED_DATE = "LastModifiedDate";
	public static final String F_NAME = "Name";
	public static final String F_NAMESPACE_PREFIX = "NamespacePrefix";
	public static final String F_SYSTEM_MODSTAMP = "SystemModstamp";
	public static final String F_TYPE = "Type";
	public static final String F_URL = "Url";

	
	public DocumentRecord() {
		this( (SObject) null);
	}
	
	public DocumentRecord(SObject sObject ) {
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
	// Lots and lots of type safe wrappers for Document parameters.
	//
	

	public String getAuthorId() { return getStringField(F_AUTHOR_ID); }
	public void setAuthorId(String value) { setStringField(F_AUTHOR_ID, value); }

	public String getBody() { return getStringField(F_BODY); }
	public void setBody(String value) { setStringField(F_BODY, value); }

	public Integer getBodyLength() { return getIntegerField(F_BODY_LENGTH); }

	public String getContentType() { return getStringField(F_CONTENT_TYPE); }
	public void setContentType(String value) { setStringField(F_CONTENT_TYPE, value); }

	public String getCreatedById() { return getStringField(F_CREATED_BY_ID); }

	public Calendar getCreatedDate() { return getDateTimeField(F_CREATED_DATE); }

	public String getDescription() { return getStringField(F_DESCRIPTION); }
	public void setDescription(String value) { setStringField(F_DESCRIPTION, value); }

	public String getDeveloperName() { return getStringField(F_DEVELOPER_NAME); }
	public void setDeveloperName(String value) { setStringField(F_DEVELOPER_NAME, value); }

	public String getFolderId() { return getStringField(F_FOLDER_ID); }
	public void setFolderId(String value) { setStringField(F_FOLDER_ID, value); }

	public Boolean getIsBodySearchable() { return getBooleanField(F_IS_BODY_SEARCHABLE); }

	public Boolean getIsDeleted() { return getBooleanField(F_IS_DELETED); }

	public Boolean getIsInternalUseOnly() { return getBooleanField(F_IS_INTERNAL_USE_ONLY); }
	public void setIsInternalUseOnly(Boolean value) { setBooleanField(F_IS_INTERNAL_USE_ONLY, value); }

	public Boolean getIsPublic() { return getBooleanField(F_IS_PUBLIC); }
	public void setIsPublic(Boolean value) { setBooleanField(F_IS_PUBLIC, value); }

	public String getKeywords() { return getStringField(F_KEYWORDS); }
	public void setKeywords(String value) { setStringField(F_KEYWORDS, value); }

	public String getLastModifiedById() { return getStringField(F_LAST_MODIFIED_BY_ID); }

	public Calendar getLastModifiedDate() { return getDateTimeField(F_LAST_MODIFIED_DATE); }

	public String getName() { return getStringField(F_NAME); }
	public void setName(String value) { setStringField(F_NAME, value); }

	public String getNamespacePrefix() { return getStringField(F_NAMESPACE_PREFIX); }
	public void setNamespacePrefix(String value) { setStringField(F_NAMESPACE_PREFIX, value); }

	public Calendar getSystemModstamp() { return getDateTimeField(F_SYSTEM_MODSTAMP); }

	public String getType() { return getStringField(F_TYPE); }
	public void setType(String value) { setStringField(F_TYPE, value); }

	public String getUrl() { return getStringField(F_URL); }
	public void setUrl(String value) { setStringField(F_URL, value); }

	
}
