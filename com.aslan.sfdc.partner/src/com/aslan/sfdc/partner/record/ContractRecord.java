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
 * Access to a SalesForce Contract Object using the SFDC Partner interface.
 * 
 * @author SObjectRecordCodeGenerator
 *
 */
public class  ContractRecord  extends AbstractSObjectRecord {
	
	
	public static final String SOBJECT_NAME = "Contract";
	
	//
	// Constants for all fields in a Contract.
	// Use these constants when forming a SFDC query in Java to
	// avoid typing mistakes.
	//
	public static final String F_ACCOUNT_ID = "AccountId";
	public static final String F_ACTIVATED_BY_ID = "ActivatedById";
	public static final String F_ACTIVATED_DATE = "ActivatedDate";
	public static final String F_BILLING_CITY = "BillingCity";
	public static final String F_BILLING_COUNTRY = "BillingCountry";
	public static final String F_BILLING_POSTAL_CODE = "BillingPostalCode";
	public static final String F_BILLING_STATE = "BillingState";
	public static final String F_BILLING_STREET = "BillingStreet";
	public static final String F_COMPANY_SIGNED_DATE = "CompanySignedDate";
	public static final String F_COMPANY_SIGNED_ID = "CompanySignedId";
	public static final String F_CONTRACT_NUMBER = "ContractNumber";
	public static final String F_CONTRACT_TERM = "ContractTerm";
	public static final String F_CREATED_BY_ID = "CreatedById";
	public static final String F_CREATED_DATE = "CreatedDate";
	public static final String F_CUSTOMER_SIGNED_DATE = "CustomerSignedDate";
	public static final String F_CUSTOMER_SIGNED_ID = "CustomerSignedId";
	public static final String F_CUSTOMER_SIGNED_TITLE = "CustomerSignedTitle";
	public static final String F_DESCRIPTION = "Description";
	public static final String F_END_DATE = "EndDate";
	public static final String F_IS_DELETED = "IsDeleted";
	public static final String F_LAST_ACTIVITY_DATE = "LastActivityDate";
	public static final String F_LAST_APPROVED_DATE = "LastApprovedDate";
	public static final String F_LAST_MODIFIED_BY_ID = "LastModifiedById";
	public static final String F_LAST_MODIFIED_DATE = "LastModifiedDate";
	public static final String F_OWNER_EXPIRATION_NOTICE = "OwnerExpirationNotice";
	public static final String F_OWNER_ID = "OwnerId";
	public static final String F_SPECIAL_TERMS = "SpecialTerms";
	public static final String F_START_DATE = "StartDate";
	public static final String F_STATUS = "Status";
	public static final String F_STATUS_CODE = "StatusCode";
	public static final String F_SYSTEM_MODSTAMP = "SystemModstamp";

	
	public ContractRecord() {
		this( (SObject) null);
	}
	
	public ContractRecord(SObject sObject ) {
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
	// Lots and lots of type safe wrappers for Contract parameters.
	//
	

	public String getAccountId() { return getStringField(F_ACCOUNT_ID); }
	public void setAccountId(String value) { setStringField(F_ACCOUNT_ID, value); }

	public String getActivatedById() { return getStringField(F_ACTIVATED_BY_ID); }

	public Calendar getActivatedDate() { return getDateTimeField(F_ACTIVATED_DATE); }

	public String getBillingCity() { return getStringField(F_BILLING_CITY); }
	public void setBillingCity(String value) { setStringField(F_BILLING_CITY, value); }

	public String getBillingCountry() { return getStringField(F_BILLING_COUNTRY); }
	public void setBillingCountry(String value) { setStringField(F_BILLING_COUNTRY, value); }

	public String getBillingPostalCode() { return getStringField(F_BILLING_POSTAL_CODE); }
	public void setBillingPostalCode(String value) { setStringField(F_BILLING_POSTAL_CODE, value); }

	public String getBillingState() { return getStringField(F_BILLING_STATE); }
	public void setBillingState(String value) { setStringField(F_BILLING_STATE, value); }

	public String getBillingStreet() { return getStringField(F_BILLING_STREET); }
	public void setBillingStreet(String value) { setStringField(F_BILLING_STREET, value); }

	public Calendar getCompanySignedDate() { return getDateField(F_COMPANY_SIGNED_DATE); }
	public void setCompanySignedDate(Calendar value) { setDateField(F_COMPANY_SIGNED_DATE, value); }

	public String getCompanySignedId() { return getStringField(F_COMPANY_SIGNED_ID); }
	public void setCompanySignedId(String value) { setStringField(F_COMPANY_SIGNED_ID, value); }

	public String getContractNumber() { return getStringField(F_CONTRACT_NUMBER); }

	public Integer getContractTerm() { return getIntegerField(F_CONTRACT_TERM); }
	public void setContractTerm(Integer value) { setIntegerField(F_CONTRACT_TERM, value); }

	public String getCreatedById() { return getStringField(F_CREATED_BY_ID); }

	public Calendar getCreatedDate() { return getDateTimeField(F_CREATED_DATE); }

	public Calendar getCustomerSignedDate() { return getDateField(F_CUSTOMER_SIGNED_DATE); }
	public void setCustomerSignedDate(Calendar value) { setDateField(F_CUSTOMER_SIGNED_DATE, value); }

	public String getCustomerSignedId() { return getStringField(F_CUSTOMER_SIGNED_ID); }
	public void setCustomerSignedId(String value) { setStringField(F_CUSTOMER_SIGNED_ID, value); }

	public String getCustomerSignedTitle() { return getStringField(F_CUSTOMER_SIGNED_TITLE); }
	public void setCustomerSignedTitle(String value) { setStringField(F_CUSTOMER_SIGNED_TITLE, value); }

	public String getDescription() { return getStringField(F_DESCRIPTION); }
	public void setDescription(String value) { setStringField(F_DESCRIPTION, value); }

	public Calendar getEndDate() { return getDateField(F_END_DATE); }

	public Boolean getIsDeleted() { return getBooleanField(F_IS_DELETED); }

	public Calendar getLastActivityDate() { return getDateField(F_LAST_ACTIVITY_DATE); }

	public Calendar getLastApprovedDate() { return getDateTimeField(F_LAST_APPROVED_DATE); }

	public String getLastModifiedById() { return getStringField(F_LAST_MODIFIED_BY_ID); }

	public Calendar getLastModifiedDate() { return getDateTimeField(F_LAST_MODIFIED_DATE); }

	public String getOwnerExpirationNotice() { return getStringField(F_OWNER_EXPIRATION_NOTICE); }
	public void setOwnerExpirationNotice(String value) { setStringField(F_OWNER_EXPIRATION_NOTICE, value); }

	public String getOwnerId() { return getStringField(F_OWNER_ID); }
	public void setOwnerId(String value) { setStringField(F_OWNER_ID, value); }

	public String getSpecialTerms() { return getStringField(F_SPECIAL_TERMS); }
	public void setSpecialTerms(String value) { setStringField(F_SPECIAL_TERMS, value); }

	public Calendar getStartDate() { return getDateField(F_START_DATE); }
	public void setStartDate(Calendar value) { setDateField(F_START_DATE, value); }

	public String getStatus() { return getStringField(F_STATUS); }
	public void setStatus(String value) { setStringField(F_STATUS, value); }

	public String getStatusCode() { return getStringField(F_STATUS_CODE); }

	public Calendar getSystemModstamp() { return getDateTimeField(F_SYSTEM_MODSTAMP); }

	
}
