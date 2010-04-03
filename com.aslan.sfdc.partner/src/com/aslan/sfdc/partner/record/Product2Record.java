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
 * Access to a SalesForce Product2 Object using the SFDC Partner interface.
 * 
 * @author SObjectRecordCodeGenerator
 *
 */
public class  Product2Record  extends AbstractSObjectRecord {
	
	
	public static final String SOBJECT_NAME = "Product2";
	
	//
	// Constants for all fields in a Product2.
	// Use these constants when forming a SFDC query in Java to
	// avoid typing mistakes.
	//
	public static final String F_CONNECTION_RECEIVED_ID = "ConnectionReceivedId";
	public static final String F_CONNECTION_SENT_ID = "ConnectionSentId";
	public static final String F_CREATED_BY_ID = "CreatedById";
	public static final String F_CREATED_DATE = "CreatedDate";
	public static final String F_CURRENCY_ISO_CODE = "CurrencyIsoCode";
	public static final String F_DESCRIPTION = "Description";
	public static final String F_FAMILY = "Family";
	public static final String F_IS_ACTIVE = "IsActive";
	public static final String F_IS_DELETED = "IsDeleted";
	public static final String F_LAST_MODIFIED_BY_ID = "LastModifiedById";
	public static final String F_LAST_MODIFIED_DATE = "LastModifiedDate";
	public static final String F_NAME = "Name";
	public static final String F_NUMBER_OF_REVENUE_INSTALLMENTS = "NumberOfRevenueInstallments";
	public static final String F_PRODUCT_CODE = "ProductCode";
	public static final String F_REVENUE_INSTALLMENT_PERIOD = "RevenueInstallmentPeriod";
	public static final String F_REVENUE_SCHEDULE_TYPE = "RevenueScheduleType";
	public static final String F_SYSTEM_MODSTAMP = "SystemModstamp";

	
	public Product2Record() {
		this( (SObject) null);
	}
	
	public Product2Record(SObject sObject ) {
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
	// Lots and lots of type safe wrappers for Product2 parameters.
	//
	

	public String getConnectionReceivedId() { return getStringField(F_CONNECTION_RECEIVED_ID); }

	public String getConnectionSentId() { return getStringField(F_CONNECTION_SENT_ID); }

	public String getCreatedById() { return getStringField(F_CREATED_BY_ID); }

	public Calendar getCreatedDate() { return getDateTimeField(F_CREATED_DATE); }

	public String getCurrencyIsoCode() { return getStringField(F_CURRENCY_ISO_CODE); }
	public void setCurrencyIsoCode(String value) { setStringField(F_CURRENCY_ISO_CODE, value); }

	public String getDescription() { return getStringField(F_DESCRIPTION); }
	public void setDescription(String value) { setStringField(F_DESCRIPTION, value); }

	public String getFamily() { return getStringField(F_FAMILY); }
	public void setFamily(String value) { setStringField(F_FAMILY, value); }

	public Boolean getIsActive() { return getBooleanField(F_IS_ACTIVE); }
	public void setIsActive(Boolean value) { setBooleanField(F_IS_ACTIVE, value); }

	public Boolean getIsDeleted() { return getBooleanField(F_IS_DELETED); }

	public String getLastModifiedById() { return getStringField(F_LAST_MODIFIED_BY_ID); }

	public Calendar getLastModifiedDate() { return getDateTimeField(F_LAST_MODIFIED_DATE); }

	public String getName() { return getStringField(F_NAME); }
	public void setName(String value) { setStringField(F_NAME, value); }

	public Integer getNumberOfRevenueInstallments() { return getIntegerField(F_NUMBER_OF_REVENUE_INSTALLMENTS); }
	public void setNumberOfRevenueInstallments(Integer value) { setIntegerField(F_NUMBER_OF_REVENUE_INSTALLMENTS, value); }

	public String getProductCode() { return getStringField(F_PRODUCT_CODE); }
	public void setProductCode(String value) { setStringField(F_PRODUCT_CODE, value); }

	public String getRevenueInstallmentPeriod() { return getStringField(F_REVENUE_INSTALLMENT_PERIOD); }
	public void setRevenueInstallmentPeriod(String value) { setStringField(F_REVENUE_INSTALLMENT_PERIOD, value); }

	public String getRevenueScheduleType() { return getStringField(F_REVENUE_SCHEDULE_TYPE); }
	public void setRevenueScheduleType(String value) { setStringField(F_REVENUE_SCHEDULE_TYPE, value); }

	public Calendar getSystemModstamp() { return getDateTimeField(F_SYSTEM_MODSTAMP); }

	
}
