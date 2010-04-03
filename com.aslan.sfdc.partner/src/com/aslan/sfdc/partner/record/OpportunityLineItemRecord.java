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
 * Access to a SalesForce OpportunityLineItem Object using the SFDC Partner interface.
 * 
 * @author SObjectRecordCodeGenerator
 *
 */
public class  OpportunityLineItemRecord  extends AbstractSObjectRecord {
	
	
	public static final String SOBJECT_NAME = "OpportunityLineItem";
	
	//
	// Constants for all fields in a OpportunityLineItem.
	// Use these constants when forming a SFDC query in Java to
	// avoid typing mistakes.
	//
	public static final String F_CONNECTION_RECEIVED_ID = "ConnectionReceivedId";
	public static final String F_CONNECTION_SENT_ID = "ConnectionSentId";
	public static final String F_CREATED_BY_ID = "CreatedById";
	public static final String F_CREATED_DATE = "CreatedDate";
	public static final String F_CURRENCY_ISO_CODE = "CurrencyIsoCode";
	public static final String F_DESCRIPTION = "Description";
	public static final String F_HAS_QUANTITY_SCHEDULE = "HasQuantitySchedule";
	public static final String F_HAS_REVENUE_SCHEDULE = "HasRevenueSchedule";
	public static final String F_HAS_SCHEDULE = "HasSchedule";
	public static final String F_IS_DELETED = "IsDeleted";
	public static final String F_LAST_MODIFIED_BY_ID = "LastModifiedById";
	public static final String F_LAST_MODIFIED_DATE = "LastModifiedDate";
	public static final String F_LIST_PRICE = "ListPrice";
	public static final String F_OPPORTUNITY_ID = "OpportunityId";
	public static final String F_PRICEBOOK_ENTRY_ID = "PricebookEntryId";
	public static final String F_QUANTITY = "Quantity";
	public static final String F_SERVICE_DATE = "ServiceDate";
	public static final String F_SORT_ORDER = "SortOrder";
	public static final String F_SYSTEM_MODSTAMP = "SystemModstamp";
	public static final String F_TOTAL_PRICE = "TotalPrice";
	public static final String F_UNIT_PRICE = "UnitPrice";

	
	public OpportunityLineItemRecord() {
		this( (SObject) null);
	}
	
	public OpportunityLineItemRecord(SObject sObject ) {
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
	// Lots and lots of type safe wrappers for OpportunityLineItem parameters.
	//
	

	public String getConnectionReceivedId() { return getStringField(F_CONNECTION_RECEIVED_ID); }

	public String getConnectionSentId() { return getStringField(F_CONNECTION_SENT_ID); }

	public String getCreatedById() { return getStringField(F_CREATED_BY_ID); }

	public Calendar getCreatedDate() { return getDateTimeField(F_CREATED_DATE); }

	public String getCurrencyIsoCode() { return getStringField(F_CURRENCY_ISO_CODE); }

	public String getDescription() { return getStringField(F_DESCRIPTION); }
	public void setDescription(String value) { setStringField(F_DESCRIPTION, value); }

	public Boolean getHasQuantitySchedule() { return getBooleanField(F_HAS_QUANTITY_SCHEDULE); }

	public Boolean getHasRevenueSchedule() { return getBooleanField(F_HAS_REVENUE_SCHEDULE); }

	public Boolean getHasSchedule() { return getBooleanField(F_HAS_SCHEDULE); }

	public Boolean getIsDeleted() { return getBooleanField(F_IS_DELETED); }

	public String getLastModifiedById() { return getStringField(F_LAST_MODIFIED_BY_ID); }

	public Calendar getLastModifiedDate() { return getDateTimeField(F_LAST_MODIFIED_DATE); }

	public Double getListPrice() { return getDoubleField(F_LIST_PRICE); }

	public String getOpportunityId() { return getStringField(F_OPPORTUNITY_ID); }
	public void setOpportunityId(String value) { setStringField(F_OPPORTUNITY_ID, value); }

	public String getPricebookEntryId() { return getStringField(F_PRICEBOOK_ENTRY_ID); }
	public void setPricebookEntryId(String value) { setStringField(F_PRICEBOOK_ENTRY_ID, value); }

	public Double getQuantity() { return getDoubleField(F_QUANTITY); }
	public void setQuantity(Double value) { setDoubleField(F_QUANTITY, value); }

	public Calendar getServiceDate() { return getDateField(F_SERVICE_DATE); }
	public void setServiceDate(Calendar value) { setDateField(F_SERVICE_DATE, value); }

	public Integer getSortOrder() { return getIntegerField(F_SORT_ORDER); }

	public Calendar getSystemModstamp() { return getDateTimeField(F_SYSTEM_MODSTAMP); }

	public Double getTotalPrice() { return getDoubleField(F_TOTAL_PRICE); }
	public void setTotalPrice(Double value) { setDoubleField(F_TOTAL_PRICE, value); }

	public Double getUnitPrice() { return getDoubleField(F_UNIT_PRICE); }
	public void setUnitPrice(Double value) { setDoubleField(F_UNIT_PRICE, value); }

	
}
