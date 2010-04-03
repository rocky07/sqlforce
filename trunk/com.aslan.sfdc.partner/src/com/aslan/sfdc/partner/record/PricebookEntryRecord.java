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
 * Access to a SalesForce PricebookEntry Object using the SFDC Partner interface.
 * 
 * @author SObjectRecordCodeGenerator
 *
 */
public class  PricebookEntryRecord  extends AbstractSObjectRecord {
	
	
	public static final String SOBJECT_NAME = "PricebookEntry";
	
	//
	// Constants for all fields in a PricebookEntry.
	// Use these constants when forming a SFDC query in Java to
	// avoid typing mistakes.
	//
	public static final String F_CREATED_BY_ID = "CreatedById";
	public static final String F_CREATED_DATE = "CreatedDate";
	public static final String F_CURRENCY_ISO_CODE = "CurrencyIsoCode";
	public static final String F_IS_ACTIVE = "IsActive";
	public static final String F_IS_DELETED = "IsDeleted";
	public static final String F_LAST_MODIFIED_BY_ID = "LastModifiedById";
	public static final String F_LAST_MODIFIED_DATE = "LastModifiedDate";
	public static final String F_NAME = "Name";
	public static final String F_PRICEBOOK2_ID = "Pricebook2Id";
	public static final String F_PRODUCT2_ID = "Product2Id";
	public static final String F_PRODUCT_CODE = "ProductCode";
	public static final String F_SYSTEM_MODSTAMP = "SystemModstamp";
	public static final String F_UNIT_PRICE = "UnitPrice";
	public static final String F_USE_STANDARD_PRICE = "UseStandardPrice";

	
	public PricebookEntryRecord() {
		this( (SObject) null);
	}
	
	public PricebookEntryRecord(SObject sObject ) {
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
	// Lots and lots of type safe wrappers for PricebookEntry parameters.
	//
	

	public String getCreatedById() { return getStringField(F_CREATED_BY_ID); }

	public Calendar getCreatedDate() { return getDateTimeField(F_CREATED_DATE); }

	public String getCurrencyIsoCode() { return getStringField(F_CURRENCY_ISO_CODE); }
	public void setCurrencyIsoCode(String value) { setStringField(F_CURRENCY_ISO_CODE, value); }

	public Boolean getIsActive() { return getBooleanField(F_IS_ACTIVE); }
	public void setIsActive(Boolean value) { setBooleanField(F_IS_ACTIVE, value); }

	public Boolean getIsDeleted() { return getBooleanField(F_IS_DELETED); }

	public String getLastModifiedById() { return getStringField(F_LAST_MODIFIED_BY_ID); }

	public Calendar getLastModifiedDate() { return getDateTimeField(F_LAST_MODIFIED_DATE); }

	public String getName() { return getStringField(F_NAME); }

	public String getPricebook2Id() { return getStringField(F_PRICEBOOK2_ID); }
	public void setPricebook2Id(String value) { setStringField(F_PRICEBOOK2_ID, value); }

	public String getProduct2Id() { return getStringField(F_PRODUCT2_ID); }
	public void setProduct2Id(String value) { setStringField(F_PRODUCT2_ID, value); }

	public String getProductCode() { return getStringField(F_PRODUCT_CODE); }

	public Calendar getSystemModstamp() { return getDateTimeField(F_SYSTEM_MODSTAMP); }

	public Double getUnitPrice() { return getDoubleField(F_UNIT_PRICE); }
	public void setUnitPrice(Double value) { setDoubleField(F_UNIT_PRICE, value); }

	public Boolean getUseStandardPrice() { return getBooleanField(F_USE_STANDARD_PRICE); }
	public void setUseStandardPrice(Boolean value) { setBooleanField(F_USE_STANDARD_PRICE, value); }

	
}
