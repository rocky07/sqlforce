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
 * Access to a SalesForce Profile Object using the SFDC Partner interface.
 * 
 * @author SObjectRecordCodeGenerator
 *
 */
public class  ProfileRecord  extends AbstractSObjectRecord {
	
	
	public static final String SOBJECT_NAME = "Profile";
	
	//
	// Constants for all fields in a Profile.
	// Use these constants when forming a SFDC query in Java to
	// avoid typing mistakes.
	//
	public static final String F_CREATED_BY_ID = "CreatedById";
	public static final String F_CREATED_DATE = "CreatedDate";
	public static final String F_DESCRIPTION = "Description";
	public static final String F_LAST_MODIFIED_BY_ID = "LastModifiedById";
	public static final String F_LAST_MODIFIED_DATE = "LastModifiedDate";
	public static final String F_NAME = "Name";
	public static final String F_PERMISSIONS_API_ENABLED = "PermissionsApiEnabled";
	public static final String F_PERMISSIONS_API_USER_ONLY = "PermissionsApiUserOnly";
	public static final String F_PERMISSIONS_AUTHOR_APEX = "PermissionsAuthorApex";
	public static final String F_PERMISSIONS_CONVERT_LEADS = "PermissionsConvertLeads";
	public static final String F_PERMISSIONS_CREATE_MULTIFORCE = "PermissionsCreateMultiforce";
	public static final String F_PERMISSIONS_CUSTOMIZE_APPLICATION = "PermissionsCustomizeApplication";
	public static final String F_PERMISSIONS_DISABLE_NOTIFICATIONS = "PermissionsDisableNotifications";
	public static final String F_PERMISSIONS_EDIT_CASE_COMMENTS = "PermissionsEditCaseComments";
	public static final String F_PERMISSIONS_EDIT_EVENT = "PermissionsEditEvent";
	public static final String F_PERMISSIONS_EDIT_OPP_LINE_ITEM_UNIT_PRICE = "PermissionsEditOppLineItemUnitPrice";
	public static final String F_PERMISSIONS_EDIT_OWN_QUOTA = "PermissionsEditOwnQuota";
	public static final String F_PERMISSIONS_EDIT_PUBLIC_DOCUMENTS = "PermissionsEditPublicDocuments";
	public static final String F_PERMISSIONS_EDIT_READONLY_FIELDS = "PermissionsEditReadonlyFields";
	public static final String F_PERMISSIONS_EDIT_REPORTS = "PermissionsEditReports";
	public static final String F_PERMISSIONS_EDIT_TASK = "PermissionsEditTask";
	public static final String F_PERMISSIONS_IMPORT_LEADS = "PermissionsImportLeads";
	public static final String F_PERMISSIONS_INSTALL_MULTIFORCE = "PermissionsInstallMultiforce";
	public static final String F_PERMISSIONS_MANAGE_ANALYTIC_SNAPSHOTS = "PermissionsManageAnalyticSnapshots";
	public static final String F_PERMISSIONS_MANAGE_BUSINESS_HOUR_HOLIDAYS = "PermissionsManageBusinessHourHolidays";
	public static final String F_PERMISSIONS_MANAGE_CALL_CENTERS = "PermissionsManageCallCenters";
	public static final String F_PERMISSIONS_MANAGE_CASES = "PermissionsManageCases";
	public static final String F_PERMISSIONS_MANAGE_CATEGORIES = "PermissionsManageCategories";
	public static final String F_PERMISSIONS_MANAGE_CSS_USERS = "PermissionsManageCssUsers";
	public static final String F_PERMISSIONS_MANAGE_CUSTOM_REPORT_TYPES = "PermissionsManageCustomReportTypes";
	public static final String F_PERMISSIONS_MANAGE_DASHBOARDS = "PermissionsManageDashboards";
	public static final String F_PERMISSIONS_MANAGE_LEADS = "PermissionsManageLeads";
	public static final String F_PERMISSIONS_MANAGE_MOBILE = "PermissionsManageMobile";
	public static final String F_PERMISSIONS_MANAGE_PARTNER_NET_CONN = "PermissionsManagePartnerNetConn";
	public static final String F_PERMISSIONS_MANAGE_SELF_SERVICE = "PermissionsManageSelfService";
	public static final String F_PERMISSIONS_MANAGE_SOLUTIONS = "PermissionsManageSolutions";
	public static final String F_PERMISSIONS_MANAGE_USERS = "PermissionsManageUsers";
	public static final String F_PERMISSIONS_MASS_INLINE_EDIT = "PermissionsMassInlineEdit";
	public static final String F_PERMISSIONS_MODIFY_ALL_DATA = "PermissionsModifyAllData";
	public static final String F_PERMISSIONS_OVERRIDE_FORECASTS = "PermissionsOverrideForecasts";
	public static final String F_PERMISSIONS_PASSWORD_NEVER_EXPIRES = "PermissionsPasswordNeverExpires";
	public static final String F_PERMISSIONS_PUBLISH_MULTIFORCE = "PermissionsPublishMultiforce";
	public static final String F_PERMISSIONS_RUN_REPORTS = "PermissionsRunReports";
	public static final String F_PERMISSIONS_SCHEDULE_JOB = "PermissionsScheduleJob";
	public static final String F_PERMISSIONS_SCHEDULE_REPORTS = "PermissionsScheduleReports";
	public static final String F_PERMISSIONS_SEND_SIT_REQUESTS = "PermissionsSendSitRequests";
	public static final String F_PERMISSIONS_SOLUTION_IMPORT = "PermissionsSolutionImport";
	public static final String F_PERMISSIONS_TRANSFER_ANY_CASE = "PermissionsTransferAnyCase";
	public static final String F_PERMISSIONS_TRANSFER_ANY_ENTITY = "PermissionsTransferAnyEntity";
	public static final String F_PERMISSIONS_TRANSFER_ANY_LEAD = "PermissionsTransferAnyLead";
	public static final String F_PERMISSIONS_USE_TEAM_REASSIGN_WIZARDS = "PermissionsUseTeamReassignWizards";
	public static final String F_PERMISSIONS_VIEW_ALL_DATA = "PermissionsViewAllData";
	public static final String F_PERMISSIONS_VIEW_ALL_FORECASTS = "PermissionsViewAllForecasts";
	public static final String F_PERMISSIONS_VIEW_SETUP = "PermissionsViewSetup";
	public static final String F_SYSTEM_MODSTAMP = "SystemModstamp";
	public static final String F_USER_LICENSE_ID = "UserLicenseId";
	public static final String F_USER_TYPE = "UserType";

	
	public ProfileRecord() {
		this( (SObject) null);
	}
	
	public ProfileRecord(SObject sObject ) {
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
	// Lots and lots of type safe wrappers for Profile parameters.
	//
	

	public String getCreatedById() { return getStringField(F_CREATED_BY_ID); }

	public Calendar getCreatedDate() { return getDateTimeField(F_CREATED_DATE); }

	public String getDescription() { return getStringField(F_DESCRIPTION); }

	public String getLastModifiedById() { return getStringField(F_LAST_MODIFIED_BY_ID); }

	public Calendar getLastModifiedDate() { return getDateTimeField(F_LAST_MODIFIED_DATE); }

	public String getName() { return getStringField(F_NAME); }

	public Boolean getPermissionsApiEnabled() { return getBooleanField(F_PERMISSIONS_API_ENABLED); }

	public Boolean getPermissionsApiUserOnly() { return getBooleanField(F_PERMISSIONS_API_USER_ONLY); }

	public Boolean getPermissionsAuthorApex() { return getBooleanField(F_PERMISSIONS_AUTHOR_APEX); }

	public Boolean getPermissionsConvertLeads() { return getBooleanField(F_PERMISSIONS_CONVERT_LEADS); }

	public Boolean getPermissionsCreateMultiforce() { return getBooleanField(F_PERMISSIONS_CREATE_MULTIFORCE); }

	public Boolean getPermissionsCustomizeApplication() { return getBooleanField(F_PERMISSIONS_CUSTOMIZE_APPLICATION); }

	public Boolean getPermissionsDisableNotifications() { return getBooleanField(F_PERMISSIONS_DISABLE_NOTIFICATIONS); }

	public Boolean getPermissionsEditCaseComments() { return getBooleanField(F_PERMISSIONS_EDIT_CASE_COMMENTS); }

	public Boolean getPermissionsEditEvent() { return getBooleanField(F_PERMISSIONS_EDIT_EVENT); }

	public Boolean getPermissionsEditOppLineItemUnitPrice() { return getBooleanField(F_PERMISSIONS_EDIT_OPP_LINE_ITEM_UNIT_PRICE); }

	public Boolean getPermissionsEditOwnQuota() { return getBooleanField(F_PERMISSIONS_EDIT_OWN_QUOTA); }

	public Boolean getPermissionsEditPublicDocuments() { return getBooleanField(F_PERMISSIONS_EDIT_PUBLIC_DOCUMENTS); }

	public Boolean getPermissionsEditReadonlyFields() { return getBooleanField(F_PERMISSIONS_EDIT_READONLY_FIELDS); }

	public Boolean getPermissionsEditReports() { return getBooleanField(F_PERMISSIONS_EDIT_REPORTS); }

	public Boolean getPermissionsEditTask() { return getBooleanField(F_PERMISSIONS_EDIT_TASK); }

	public Boolean getPermissionsImportLeads() { return getBooleanField(F_PERMISSIONS_IMPORT_LEADS); }

	public Boolean getPermissionsInstallMultiforce() { return getBooleanField(F_PERMISSIONS_INSTALL_MULTIFORCE); }

	public Boolean getPermissionsManageAnalyticSnapshots() { return getBooleanField(F_PERMISSIONS_MANAGE_ANALYTIC_SNAPSHOTS); }

	public Boolean getPermissionsManageBusinessHourHolidays() { return getBooleanField(F_PERMISSIONS_MANAGE_BUSINESS_HOUR_HOLIDAYS); }

	public Boolean getPermissionsManageCallCenters() { return getBooleanField(F_PERMISSIONS_MANAGE_CALL_CENTERS); }

	public Boolean getPermissionsManageCases() { return getBooleanField(F_PERMISSIONS_MANAGE_CASES); }

	public Boolean getPermissionsManageCategories() { return getBooleanField(F_PERMISSIONS_MANAGE_CATEGORIES); }

	public Boolean getPermissionsManageCssUsers() { return getBooleanField(F_PERMISSIONS_MANAGE_CSS_USERS); }

	public Boolean getPermissionsManageCustomReportTypes() { return getBooleanField(F_PERMISSIONS_MANAGE_CUSTOM_REPORT_TYPES); }

	public Boolean getPermissionsManageDashboards() { return getBooleanField(F_PERMISSIONS_MANAGE_DASHBOARDS); }

	public Boolean getPermissionsManageLeads() { return getBooleanField(F_PERMISSIONS_MANAGE_LEADS); }

	public Boolean getPermissionsManageMobile() { return getBooleanField(F_PERMISSIONS_MANAGE_MOBILE); }

	public Boolean getPermissionsManagePartnerNetConn() { return getBooleanField(F_PERMISSIONS_MANAGE_PARTNER_NET_CONN); }

	public Boolean getPermissionsManageSelfService() { return getBooleanField(F_PERMISSIONS_MANAGE_SELF_SERVICE); }

	public Boolean getPermissionsManageSolutions() { return getBooleanField(F_PERMISSIONS_MANAGE_SOLUTIONS); }

	public Boolean getPermissionsManageUsers() { return getBooleanField(F_PERMISSIONS_MANAGE_USERS); }

	public Boolean getPermissionsMassInlineEdit() { return getBooleanField(F_PERMISSIONS_MASS_INLINE_EDIT); }

	public Boolean getPermissionsModifyAllData() { return getBooleanField(F_PERMISSIONS_MODIFY_ALL_DATA); }

	public Boolean getPermissionsOverrideForecasts() { return getBooleanField(F_PERMISSIONS_OVERRIDE_FORECASTS); }

	public Boolean getPermissionsPasswordNeverExpires() { return getBooleanField(F_PERMISSIONS_PASSWORD_NEVER_EXPIRES); }

	public Boolean getPermissionsPublishMultiforce() { return getBooleanField(F_PERMISSIONS_PUBLISH_MULTIFORCE); }

	public Boolean getPermissionsRunReports() { return getBooleanField(F_PERMISSIONS_RUN_REPORTS); }

	public Boolean getPermissionsScheduleJob() { return getBooleanField(F_PERMISSIONS_SCHEDULE_JOB); }

	public Boolean getPermissionsScheduleReports() { return getBooleanField(F_PERMISSIONS_SCHEDULE_REPORTS); }

	public Boolean getPermissionsSendSitRequests() { return getBooleanField(F_PERMISSIONS_SEND_SIT_REQUESTS); }

	public Boolean getPermissionsSolutionImport() { return getBooleanField(F_PERMISSIONS_SOLUTION_IMPORT); }

	public Boolean getPermissionsTransferAnyCase() { return getBooleanField(F_PERMISSIONS_TRANSFER_ANY_CASE); }

	public Boolean getPermissionsTransferAnyEntity() { return getBooleanField(F_PERMISSIONS_TRANSFER_ANY_ENTITY); }

	public Boolean getPermissionsTransferAnyLead() { return getBooleanField(F_PERMISSIONS_TRANSFER_ANY_LEAD); }

	public Boolean getPermissionsUseTeamReassignWizards() { return getBooleanField(F_PERMISSIONS_USE_TEAM_REASSIGN_WIZARDS); }

	public Boolean getPermissionsViewAllData() { return getBooleanField(F_PERMISSIONS_VIEW_ALL_DATA); }

	public Boolean getPermissionsViewAllForecasts() { return getBooleanField(F_PERMISSIONS_VIEW_ALL_FORECASTS); }

	public Boolean getPermissionsViewSetup() { return getBooleanField(F_PERMISSIONS_VIEW_SETUP); }

	public Calendar getSystemModstamp() { return getDateTimeField(F_SYSTEM_MODSTAMP); }

	public String getUserLicenseId() { return getStringField(F_USER_LICENSE_ID); }

	public String getUserType() { return getStringField(F_USER_TYPE); }

	
}

