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
 * Access to a SalesForce CaseSolution Object using the SFDC Partner interface.
 * 
 * @author SObjectRecordCodeGenerator
 *
 */
public class  CaseSolutionRecord  extends AbstractSObjectRecord {
	
	
	public static final String SOBJECT_NAME = "CaseSolution";
	
	//
	// Constants for all fields in a CaseSolution.
	// Use these constants when forming a SFDC query in Java to
	// avoid typing mistakes.
	//
	public static final String F_CASE_ID = "CaseId";
	public static final String F_CREATED_BY_ID = "CreatedById";
	public static final String F_CREATED_DATE = "CreatedDate";
	public static final String F_IS_DELETED = "IsDeleted";
	public static final String F_SOLUTION_ID = "SolutionId";
	public static final String F_SYSTEM_MODSTAMP = "SystemModstamp";

	
	public CaseSolutionRecord() {
		this( (SObject) null);
	}
	
	public CaseSolutionRecord(SObject sObject ) {
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
	// Lots and lots of type safe wrappers for CaseSolution parameters.
	//
	

	public String getCaseId() { return getStringField(F_CASE_ID); }
	public void setCaseId(String value) { setStringField(F_CASE_ID, value); }

	public String getCreatedById() { return getStringField(F_CREATED_BY_ID); }

	public Calendar getCreatedDate() { return getDateTimeField(F_CREATED_DATE); }

	public Boolean getIsDeleted() { return getBooleanField(F_IS_DELETED); }

	public String getSolutionId() { return getStringField(F_SOLUTION_ID); }
	public void setSolutionId(String value) { setStringField(F_SOLUTION_ID, value); }

	public Calendar getSystemModstamp() { return getDateTimeField(F_SYSTEM_MODSTAMP); }

	
}
