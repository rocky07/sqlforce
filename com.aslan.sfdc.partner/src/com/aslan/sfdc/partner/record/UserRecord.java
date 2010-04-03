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
 * Access to a SalesForce User Object using the SFDC Partner interface.
 * 
 * @author SObjectRecordCodeGenerator
 *
 */
public class  UserRecord  extends AbstractSObjectRecord {
	
	
	public static final String SOBJECT_NAME = "User";
	
	//
	// Constants for all fields in a User.
	// Use these constants when forming a SFDC query in Java to
	// avoid typing mistakes.
	//
	public static final String F_ALIAS = "Alias";
	public static final String F_CALL_CENTER_ID = "CallCenterId";
	public static final String F_CITY = "City";
	public static final String F_COMMUNITY_NICKNAME = "CommunityNickname";
	public static final String F_COMPANY_NAME = "CompanyName";
	public static final String F_CONTACT_ID = "ContactId";
	public static final String F_COUNTRY = "Country";
	public static final String F_CREATED_BY_ID = "CreatedById";
	public static final String F_CREATED_DATE = "CreatedDate";
	public static final String F_CURRENCY_ISO_CODE = "CurrencyIsoCode";
	public static final String F_DEFAULT_CURRENCY_ISO_CODE = "DefaultCurrencyIsoCode";
	public static final String F_DELEGATED_APPROVER_ID = "DelegatedApproverId";
	public static final String F_DEPARTMENT = "Department";
	public static final String F_DIVISION = "Division";
	public static final String F_EMAIL = "Email";
	public static final String F_EMAIL_ENCODING_KEY = "EmailEncodingKey";
	public static final String F_EMPLOYEE_NUMBER = "EmployeeNumber";
	public static final String F_EXTENSION = "Extension";
	public static final String F_FAX = "Fax";
	public static final String F_FIRST_NAME = "FirstName";
	public static final String F_FORECAST_ENABLED = "ForecastEnabled";
	public static final String F_IS_ACTIVE = "IsActive";
	public static final String F_LANGUAGE_LOCALE_KEY = "LanguageLocaleKey";
	public static final String F_LAST_LOGIN_DATE = "LastLoginDate";
	public static final String F_LAST_MODIFIED_BY_ID = "LastModifiedById";
	public static final String F_LAST_MODIFIED_DATE = "LastModifiedDate";
	public static final String F_LAST_NAME = "LastName";
	public static final String F_LOCALE_SID_KEY = "LocaleSidKey";
	public static final String F_MANAGER_ID = "ManagerId";
	public static final String F_MOBILE_PHONE = "MobilePhone";
	public static final String F_NAME = "Name";
	public static final String F_OFFLINE_PDA_TRIAL_EXPIRATION_DATE = "OfflinePdaTrialExpirationDate";
	public static final String F_OFFLINE_TRIAL_EXPIRATION_DATE = "OfflineTrialExpirationDate";
	public static final String F_PHONE = "Phone";
	public static final String F_POSTAL_CODE = "PostalCode";
	public static final String F_PROFILE_ID = "ProfileId";
	public static final String F_RECEIVES_ADMIN_INFO_EMAILS = "ReceivesAdminInfoEmails";
	public static final String F_RECEIVES_INFO_EMAILS = "ReceivesInfoEmails";
	public static final String F_STATE = "State";
	public static final String F_STREET = "Street";
	public static final String F_SYSTEM_MODSTAMP = "SystemModstamp";
	public static final String F_TIME_ZONE_SID_KEY = "TimeZoneSidKey";
	public static final String F_TITLE = "Title";
	public static final String F_USERNAME = "Username";
	public static final String F_USER_PERMISSIONS_AVANTGO_USER = "UserPermissionsAvantgoUser";
	public static final String F_USER_PERMISSIONS_CALL_CENTER_AUTO_LOGIN = "UserPermissionsCallCenterAutoLogin";
	public static final String F_USER_PERMISSIONS_MARKETING_USER = "UserPermissionsMarketingUser";
	public static final String F_USER_PERMISSIONS_MOBILE_USER = "UserPermissionsMobileUser";
	public static final String F_USER_PERMISSIONS_OFFLINE_USER = "UserPermissionsOfflineUser";
	public static final String F_USER_PREFERENCES_ACTIVITY_REMINDERS_POPUP = "UserPreferencesActivityRemindersPopup";
	public static final String F_USER_PREFERENCES_APEX_PAGES_DEVELOPER_MODE = "UserPreferencesApexPagesDeveloperMode";
	public static final String F_USER_PREFERENCES_EVENT_REMINDERS_CHECKBOX_DEFAULT = "UserPreferencesEventRemindersCheckboxDefault";
	public static final String F_USER_PREFERENCES_REMINDER_SOUND_OFF = "UserPreferencesReminderSoundOff";
	public static final String F_USER_PREFERENCES_TASK_REMINDERS_CHECKBOX_DEFAULT = "UserPreferencesTaskRemindersCheckboxDefault";
	public static final String F_USER_ROLE_ID = "UserRoleId";
	public static final String F_USER_TYPE = "UserType";

	
	public UserRecord() {
		this( (SObject) null);
	}
	
	public UserRecord(SObject sObject ) {
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
	// Lots and lots of type safe wrappers for User parameters.
	//
	

	public String getAlias() { return getStringField(F_ALIAS); }
	public void setAlias(String value) { setStringField(F_ALIAS, value); }

	public String getCallCenterId() { return getStringField(F_CALL_CENTER_ID); }
	public void setCallCenterId(String value) { setStringField(F_CALL_CENTER_ID, value); }

	public String getCity() { return getStringField(F_CITY); }
	public void setCity(String value) { setStringField(F_CITY, value); }

	public String getCommunityNickname() { return getStringField(F_COMMUNITY_NICKNAME); }
	public void setCommunityNickname(String value) { setStringField(F_COMMUNITY_NICKNAME, value); }

	public String getCompanyName() { return getStringField(F_COMPANY_NAME); }
	public void setCompanyName(String value) { setStringField(F_COMPANY_NAME, value); }

	public String getContactId() { return getStringField(F_CONTACT_ID); }
	public void setContactId(String value) { setStringField(F_CONTACT_ID, value); }

	public String getCountry() { return getStringField(F_COUNTRY); }
	public void setCountry(String value) { setStringField(F_COUNTRY, value); }

	public String getCreatedById() { return getStringField(F_CREATED_BY_ID); }

	public Calendar getCreatedDate() { return getDateTimeField(F_CREATED_DATE); }

	public String getCurrencyIsoCode() { return getStringField(F_CURRENCY_ISO_CODE); }
	public void setCurrencyIsoCode(String value) { setStringField(F_CURRENCY_ISO_CODE, value); }

	public String getDefaultCurrencyIsoCode() { return getStringField(F_DEFAULT_CURRENCY_ISO_CODE); }
	public void setDefaultCurrencyIsoCode(String value) { setStringField(F_DEFAULT_CURRENCY_ISO_CODE, value); }

	public String getDelegatedApproverId() { return getStringField(F_DELEGATED_APPROVER_ID); }
	public void setDelegatedApproverId(String value) { setStringField(F_DELEGATED_APPROVER_ID, value); }

	public String getDepartment() { return getStringField(F_DEPARTMENT); }
	public void setDepartment(String value) { setStringField(F_DEPARTMENT, value); }

	public String getDivision() { return getStringField(F_DIVISION); }
	public void setDivision(String value) { setStringField(F_DIVISION, value); }

	public String getEmail() { return getStringField(F_EMAIL); }
	public void setEmail(String value) { setStringField(F_EMAIL, value); }

	public String getEmailEncodingKey() { return getStringField(F_EMAIL_ENCODING_KEY); }
	public void setEmailEncodingKey(String value) { setStringField(F_EMAIL_ENCODING_KEY, value); }

	public String getEmployeeNumber() { return getStringField(F_EMPLOYEE_NUMBER); }
	public void setEmployeeNumber(String value) { setStringField(F_EMPLOYEE_NUMBER, value); }

	public String getExtension() { return getStringField(F_EXTENSION); }
	public void setExtension(String value) { setStringField(F_EXTENSION, value); }

	public String getFax() { return getStringField(F_FAX); }
	public void setFax(String value) { setStringField(F_FAX, value); }

	public String getFirstName() { return getStringField(F_FIRST_NAME); }
	public void setFirstName(String value) { setStringField(F_FIRST_NAME, value); }

	public Boolean getForecastEnabled() { return getBooleanField(F_FORECAST_ENABLED); }
	public void setForecastEnabled(Boolean value) { setBooleanField(F_FORECAST_ENABLED, value); }

	public Boolean getIsActive() { return getBooleanField(F_IS_ACTIVE); }
	public void setIsActive(Boolean value) { setBooleanField(F_IS_ACTIVE, value); }

	public String getLanguageLocaleKey() { return getStringField(F_LANGUAGE_LOCALE_KEY); }
	public void setLanguageLocaleKey(String value) { setStringField(F_LANGUAGE_LOCALE_KEY, value); }

	public Calendar getLastLoginDate() { return getDateTimeField(F_LAST_LOGIN_DATE); }

	public String getLastModifiedById() { return getStringField(F_LAST_MODIFIED_BY_ID); }

	public Calendar getLastModifiedDate() { return getDateTimeField(F_LAST_MODIFIED_DATE); }

	public String getLastName() { return getStringField(F_LAST_NAME); }
	public void setLastName(String value) { setStringField(F_LAST_NAME, value); }

	public String getLocaleSidKey() { return getStringField(F_LOCALE_SID_KEY); }
	public void setLocaleSidKey(String value) { setStringField(F_LOCALE_SID_KEY, value); }

	public String getManagerId() { return getStringField(F_MANAGER_ID); }
	public void setManagerId(String value) { setStringField(F_MANAGER_ID, value); }

	public String getMobilePhone() { return getStringField(F_MOBILE_PHONE); }
	public void setMobilePhone(String value) { setStringField(F_MOBILE_PHONE, value); }

	public String getName() { return getStringField(F_NAME); }

	public Calendar getOfflinePdaTrialExpirationDate() { return getDateTimeField(F_OFFLINE_PDA_TRIAL_EXPIRATION_DATE); }

	public Calendar getOfflineTrialExpirationDate() { return getDateTimeField(F_OFFLINE_TRIAL_EXPIRATION_DATE); }

	public String getPhone() { return getStringField(F_PHONE); }
	public void setPhone(String value) { setStringField(F_PHONE, value); }

	public String getPostalCode() { return getStringField(F_POSTAL_CODE); }
	public void setPostalCode(String value) { setStringField(F_POSTAL_CODE, value); }

	public String getProfileId() { return getStringField(F_PROFILE_ID); }
	public void setProfileId(String value) { setStringField(F_PROFILE_ID, value); }

	public Boolean getReceivesAdminInfoEmails() { return getBooleanField(F_RECEIVES_ADMIN_INFO_EMAILS); }
	public void setReceivesAdminInfoEmails(Boolean value) { setBooleanField(F_RECEIVES_ADMIN_INFO_EMAILS, value); }

	public Boolean getReceivesInfoEmails() { return getBooleanField(F_RECEIVES_INFO_EMAILS); }
	public void setReceivesInfoEmails(Boolean value) { setBooleanField(F_RECEIVES_INFO_EMAILS, value); }

	public String getState() { return getStringField(F_STATE); }
	public void setState(String value) { setStringField(F_STATE, value); }

	public String getStreet() { return getStringField(F_STREET); }
	public void setStreet(String value) { setStringField(F_STREET, value); }

	public Calendar getSystemModstamp() { return getDateTimeField(F_SYSTEM_MODSTAMP); }

	public String getTimeZoneSidKey() { return getStringField(F_TIME_ZONE_SID_KEY); }
	public void setTimeZoneSidKey(String value) { setStringField(F_TIME_ZONE_SID_KEY, value); }

	public String getTitle() { return getStringField(F_TITLE); }
	public void setTitle(String value) { setStringField(F_TITLE, value); }

	public String getUsername() { return getStringField(F_USERNAME); }
	public void setUsername(String value) { setStringField(F_USERNAME, value); }

	public Boolean getUserPermissionsAvantgoUser() { return getBooleanField(F_USER_PERMISSIONS_AVANTGO_USER); }
	public void setUserPermissionsAvantgoUser(Boolean value) { setBooleanField(F_USER_PERMISSIONS_AVANTGO_USER, value); }

	public Boolean getUserPermissionsCallCenterAutoLogin() { return getBooleanField(F_USER_PERMISSIONS_CALL_CENTER_AUTO_LOGIN); }
	public void setUserPermissionsCallCenterAutoLogin(Boolean value) { setBooleanField(F_USER_PERMISSIONS_CALL_CENTER_AUTO_LOGIN, value); }

	public Boolean getUserPermissionsMarketingUser() { return getBooleanField(F_USER_PERMISSIONS_MARKETING_USER); }
	public void setUserPermissionsMarketingUser(Boolean value) { setBooleanField(F_USER_PERMISSIONS_MARKETING_USER, value); }

	public Boolean getUserPermissionsMobileUser() { return getBooleanField(F_USER_PERMISSIONS_MOBILE_USER); }
	public void setUserPermissionsMobileUser(Boolean value) { setBooleanField(F_USER_PERMISSIONS_MOBILE_USER, value); }

	public Boolean getUserPermissionsOfflineUser() { return getBooleanField(F_USER_PERMISSIONS_OFFLINE_USER); }
	public void setUserPermissionsOfflineUser(Boolean value) { setBooleanField(F_USER_PERMISSIONS_OFFLINE_USER, value); }

	public Boolean getUserPreferencesActivityRemindersPopup() { return getBooleanField(F_USER_PREFERENCES_ACTIVITY_REMINDERS_POPUP); }

	public Boolean getUserPreferencesApexPagesDeveloperMode() { return getBooleanField(F_USER_PREFERENCES_APEX_PAGES_DEVELOPER_MODE); }

	public Boolean getUserPreferencesEventRemindersCheckboxDefault() { return getBooleanField(F_USER_PREFERENCES_EVENT_REMINDERS_CHECKBOX_DEFAULT); }

	public Boolean getUserPreferencesReminderSoundOff() { return getBooleanField(F_USER_PREFERENCES_REMINDER_SOUND_OFF); }

	public Boolean getUserPreferencesTaskRemindersCheckboxDefault() { return getBooleanField(F_USER_PREFERENCES_TASK_REMINDERS_CHECKBOX_DEFAULT); }

	public String getUserRoleId() { return getStringField(F_USER_ROLE_ID); }
	public void setUserRoleId(String value) { setStringField(F_USER_ROLE_ID, value); }

	public String getUserType() { return getStringField(F_USER_TYPE); }

	
}

