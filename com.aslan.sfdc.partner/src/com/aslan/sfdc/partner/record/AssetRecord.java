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
 * Access to a SalesForce Asset Object using the SFDC Partner interface.
 * 
 * @author SObjectRecordCodeGenerator
 *
 */
public class  AssetRecord  extends AbstractSObjectRecord {
	
	
	public static final String SOBJECT_NAME = "Asset";
	
	//
	// Constants for all fields in a Asset.
	// Use these constants when forming a SFDC query in Java to
	// avoid typing mistakes.
	//
	public static final String F_ACCOUNT_ID = "AccountId";
	public static final String F_CONTACT_ID = "ContactId";
	public static final String F_CREATED_BY_ID = "CreatedById";
	public static final String F_CREATED_DATE = "CreatedDate";
	public static final String F_CURRENCY_ISO_CODE = "CurrencyIsoCode";
	public static final String F_DESCRIPTION = "Description";
	public static final String F_INSTALL_DATE = "InstallDate";
	public static final String F_IS_COMPETITOR_PRODUCT = "IsCompetitorProduct";
	public static final String F_IS_DELETED = "IsDeleted";
	public static final String F_LAST_MODIFIED_BY_ID = "LastModifiedById";
	public static final String F_LAST_MODIFIED_DATE = "LastModifiedDate";
	public static final String F_NAME = "Name";
	public static final String F_PRICE = "Price";
	public static final String F_PRODUCT2_ID = "Product2Id";
	public static final String F_PURCHASE_DATE = "PurchaseDate";
	public static final String F_QUANTITY = "Quantity";
	public static final String F_SERIAL_NUMBER = "SerialNumber";
	public static final String F_STATUS = "Status";
	public static final String F_SYSTEM_MODSTAMP = "SystemModstamp";
	public static final String F_USAGE_END_DATE = "UsageEndDate";

	
	public AssetRecord() {
		this( (SObject) null);
	}
	
	public AssetRecord(SObject sObject ) {
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
	// Lots and lots of type safe wrappers for Asset parameters.
	//
	

	public String getAccountId() { return getStringField(F_ACCOUNT_ID); }
	public void setAccountId(String value) { setStringField(F_ACCOUNT_ID, value); }

	public String getContactId() { return getStringField(F_CONTACT_ID); }
	public void setContactId(String value) { setStringField(F_CONTACT_ID, value); }

	public String getCreatedById() { return getStringField(F_CREATED_BY_ID); }

	public Calendar getCreatedDate() { return getDateTimeField(F_CREATED_DATE); }

	public String getCurrencyIsoCode() { return getStringField(F_CURRENCY_ISO_CODE); }
	public void setCurrencyIsoCode(String value) { setStringField(F_CURRENCY_ISO_CODE, value); }

	public String getDescription() { return getStringField(F_DESCRIPTION); }
	public void setDescription(String value) { setStringField(F_DESCRIPTION, value); }

	public Calendar getInstallDate() { return getDateField(F_INSTALL_DATE); }
	public void setInstallDate(Calendar value) { setDateField(F_INSTALL_DATE, value); }

	public Boolean getIsCompetitorProduct() { return getBooleanField(F_IS_COMPETITOR_PRODUCT); }
	public void setIsCompetitorProduct(Boolean value) { setBooleanField(F_IS_COMPETITOR_PRODUCT, value); }

	public Boolean getIsDeleted() { return getBooleanField(F_IS_DELETED); }

	public String getLastModifiedById() { return getStringField(F_LAST_MODIFIED_BY_ID); }

	public Calendar getLastModifiedDate() { return getDateTimeField(F_LAST_MODIFIED_DATE); }

	public String getName() { return getStringField(F_NAME); }
	public void setName(String value) { setStringField(F_NAME, value); }

	public Double getPrice() { return getDoubleField(F_PRICE); }
	public void setPrice(Double value) { setDoubleField(F_PRICE, value); }

	public String getProduct2Id() { return getStringField(F_PRODUCT2_ID); }
	public void setProduct2Id(String value) { setStringField(F_PRODUCT2_ID, value); }

	public Calendar getPurchaseDate() { return getDateField(F_PURCHASE_DATE); }
	public void setPurchaseDate(Calendar value) { setDateField(F_PURCHASE_DATE, value); }

	public Double getQuantity() { return getDoubleField(F_QUANTITY); }
	public void setQuantity(Double value) { setDoubleField(F_QUANTITY, value); }

	public String getSerialNumber() { return getStringField(F_SERIAL_NUMBER); }
	public void setSerialNumber(String value) { setStringField(F_SERIAL_NUMBER, value); }

	public String getStatus() { return getStringField(F_STATUS); }
	public void setStatus(String value) { setStringField(F_STATUS, value); }

	public Calendar getSystemModstamp() { return getDateTimeField(F_SYSTEM_MODSTAMP); }

	public Calendar getUsageEndDate() { return getDateField(F_USAGE_END_DATE); }
	public void setUsageEndDate(Calendar value) { setDateField(F_USAGE_END_DATE, value); }

	
}

