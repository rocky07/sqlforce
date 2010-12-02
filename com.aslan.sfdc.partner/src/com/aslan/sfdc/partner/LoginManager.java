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
 * 
 */
package com.aslan.sfdc.partner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aslan.sfdc.partner.record.AccountRecord;
import com.aslan.sfdc.partner.record.AttachmentRecord;
import com.aslan.sfdc.partner.record.NoteRecord;
import com.aslan.sfdc.partner.record.ProfileRecord;
import com.aslan.sfdc.partner.record.TaskRecord;
import com.aslan.sfdc.partner.record.UserRecord;
import com.sforce.soap.partner.DescribeSObjectResult;
import com.sforce.soap.partner.Field;
import com.sforce.soap.partner.GetUserInfoResult;
import com.sforce.soap.partner.LoginResult;
import com.sforce.soap.partner.SessionHeader;
import com.sforce.soap.partner.SforceServiceLocator;
import com.sforce.soap.partner.Soap;
import com.sforce.soap.partner.SoapBindingStub;
import com.sforce.soap.partner.sobject.SObject;

/**
 * Manage connections to a Sales Force database when communicating using the Partner WSDL.
 * 
 * @author snort
 *
 */
public class LoginManager {

	public static int DEFAULT_TIMEOUT = 60000;
	
	/**
	 * Connection to a SFDC database that keeps a handle to the database and can answer a few common meta-data questions.
	 * 
	 * @author snort
	 *
	 */
	public class Session {
		private Soap binding;
		private LoginResult loginResult;
		private String sandboxName;
		private UserRecord userRecord;
		private ProfileRecord profileRecord;
		private boolean isAdministrator = false;
		private boolean isMultiCurrency = false;
		private boolean isAuditFieldWriteEnabled = false;

		
		//
		// The exhaustive set for some objects cannot be determine without scanning all data records.
		// Keep track of these and use an alternate method of return who possible owners are.
		//
		private Set<String> noOwnerSelectSet = new HashSet<String>();
		private String noOwnerSelect[] = {
				NoteRecord.SOBJECT_NAME,
				TaskRecord.SOBJECT_NAME,
				AttachmentRecord.SOBJECT_NAME,
		};
		
		//
		// Remember the SFDC descriptor for all sobjects that have been requested.
		// This structure saves a lot of round-trips to SFDC by assuming that the schema will not change
		// within the session.
		//
		private Map<String,DescribeSObjectResult> describeHash = new HashMap<String, DescribeSObjectResult>();
		
		//
		// Map all fields via "sobject.fieldname" (all lower case) to the corresponding field descriptor.
		// The purpose of this object is to support fast lookup of fields.
		//
		private Map<String, Field> fieldMap = new HashMap<String,Field>();
		
		private Session(LoginResult loginResult, SoapBindingStub binding, String sandboxName ) throws Exception {
			this.loginResult = loginResult;
			this.binding = binding;
			this.sandboxName = sandboxName;
			
			GetUserInfoResult uiResult = binding.getUserInfo();
			SObjectQueryHelper query = new SObjectQueryHelper();
			userRecord = new UserRecord( query.findSObject( this, UserRecord.SOBJECT_NAME, uiResult.getUserId()));
			profileRecord = new ProfileRecord( query.findSObject( this, ProfileRecord.SOBJECT_NAME, userRecord.getProfileId()));
			
			isAdministrator = "System Administrator".equalsIgnoreCase(profileRecord.getName());
			isMultiCurrency = isFieldAvailable(AccountRecord.SOBJECT_NAME, AccountRecord.F_CURRENCY_ISO_CODE);
			
			Field field = this.getField(AccountRecord.SOBJECT_NAME, AccountRecord.F_CREATED_BY_ID);
			isAuditFieldWriteEnabled = field.isCreateable();
			
			for(String name : noOwnerSelect) {
				noOwnerSelectSet.add(name);
			}
		}
		
		
		/**
		 * Put the meta-data for an SObject into a local cache.
		 * 
		 * @param sObjectName load this sobject.
		 * @throws Exception if the sobjecdt cannot be found (sfdc throws an exception).
		 */
		private void cacheSObject( String sObjectName) throws Exception {
			String lowerName = sObjectName.toLowerCase();
			
			if( describeHash.containsKey(lowerName ))  { return; }
			
			DescribeSObjectResult describeSObjectResult = binding.describeSObject(sObjectName);
			describeHash.put( lowerName, describeSObjectResult);
				
			for( Field field : describeSObjectResult.getFields()) {
				fieldMap.put( lowerName + "." + field.getName().toLowerCase(), field);
			}
		}
		
		/**
		 * Log out of the current session.
		 * 
		 * @throws Exception if the login fails.
		 */
		public void logout() throws Exception {
			getBinding().logout();
		}
		/**
		 * Return the user record for the person using this session.
		 * 
		 * @return current user.
		 */
		public UserRecord getUserRecord() {
			return userRecord;
		}
		
		/**
		 * Return the profile associated with the current user.
		 * 
		 * @return profile record.
		 */
		public ProfileRecord getProfileRecord() {
			return profileRecord;
		}
		
		/**
		 * Determine if the current user is a system administrator.
		 * 
		 * @return true if administrator, else false.
		 */
		public boolean isAdministrator() {
			return isAdministrator;
		}
		
		/**
		 * Determine is this session can write to SalesForce audit fields on a create operaiton.
		 * 
		 * @return true if audit fields are writable, else false.
		 */
		public boolean isAuditFieldWriteEnabled() {
			return isAuditFieldWriteEnabled;
		}
		/**
		 * Determine if the session has enabled the multi-currency option.
		 * 
		 * @return true if enabled, else false.
		 */
		public boolean isMultiCurrency() {
			return isMultiCurrency;
		}
		
		/**
		 * Return the ids of all users defined for the database.
		 * 
		 * @return all user ids.
		 * @throws Exception if SalesForce croaks.
		 */
		public List<String> getAllUserIds() throws Exception {
			List<SObject> 	sObjList = new SObjectQueryHelper().findSObjectList(
					this, UserRecord.SOBJECT_NAME, new String[] {"ID"},
					(String)null);
			
			List<String> idList = new ArrayList<String>( sObjList.size());
			for( SObject sObj : sObjList ) {
				idList.add( sObj.getId());
			}

			return idList;
		}
		
		/**
		 * Return the SDFDC id of a particular user given their unique user id.
		 * 
		 * @param username find this user.
		 * @return id of the user 
		 * @throws Exception if SalesForce croaks or the user name is not found.
		 */
		public String getUserIdByUsername(String username ) throws Exception {
			List<SObject> 	sObjList = new SObjectQueryHelper().findSObjectList(
					this, UserRecord.SOBJECT_NAME, new String[] {"ID"},
					"username='" + username + "'");
			
			if( 0==sObjList.size()) { throw new Exception("Username " + username + " was not found in SalesForce");}
			return sObjList.get(0).getId();
		}
		/**
		 * Return the list of owners that own objects of a particular type.
		 * 
		 * @param sObjectName look at this object.
		 * @return list of user ids (zero length if no records).
		 * @throws Exception if SFDC fails.
		 */
		public List<String> getOwners( String sObjectName ) throws Exception {

			String sqlWhere = null;

			if( !noOwnerSelectSet.contains(sObjectName)) { // The best way -- no uninteresting owners will be returned.
				sqlWhere = "id in (Select ownerId from " + sObjectName + ")";
			}
			
			List<SObject> 	sObjList = new SObjectQueryHelper().findSObjectList(
					this, UserRecord.SOBJECT_NAME, new String[] {"ID"},
					sqlWhere);


			List<String> idList = new ArrayList<String>( sObjList.size());
			for( SObject sObj : sObjList ) {
				idList.add( sObj.getId());
			}

			return idList;
		}
		/**
		 * Get the handle to the SFDC instance.
		 * 
		 * @return binding to the SFDC instance.
		 */
		public Soap getBinding() { return binding; }
		
		/**
		 * Set header data that contains SalesForce credential data.
		 * 
		 * Web service stubs can use this call to re-use the connection information from this session.
		 * 
		 * @param service
		 * @param stub
		 */
		public void initBindingHeader(org.apache.axis.client.Service service, org.apache.axis.client.Stub stub ) {
			 SessionHeader sh = new SessionHeader();
			 sh.setSessionId(loginResult.getSessionId());
			 
			 stub.setHeader(
					 service.getServiceName().getNamespaceURI(), "SessionHeader", sh);
		}
		/**
		 * Get the credentials used to connect to SFDC.
		 * 
		 * @return login credentials.
		 */
		public LoginResult getLoginResult() { return loginResult; }
		
		/**
		 * Determine if the session is running in a sandbox.
		 * 
		 * @return true if running in a sandbox, else false.
		 */
		public boolean isSandbox() {
			return !(null==sandboxName);
		}
		
		/**
		 * Return the name of the sandbox (or null if not running in a sandbox).
		 * 
		 * @return the sandbox for the session or null if not a sandbox.
		 */
		public String getSandboxName() {
			return sandboxName;
		}
		/**
		 * Get the sfdc metadata for an SObject (from a local cache that only fetches from SFDC once per session).
		 * 
		 * @param sObjectName look up this object
		 * @return meta data
		 * @throws Exception if the SObject does not exist in SFDC.
		 */
		public DescribeSObjectResult getDescribeSObjectResult( String sObjectName ) throws Exception {
			cacheSObject(sObjectName);
			
			return describeHash.get(sObjectName.toLowerCase());
		}
		
		/**
		 * Return the sfdc Field descriptor within an SObject.
		 * 
		 * @param sObjectName look in this SObject
		 * @param fieldName grab this field descriptor
		 * @return a field descriptor
		 * @throws Exception if the field is not defined (or not accessible).
		 */
		public Field getField( String sObjectName, String fieldName )  throws Exception {
			
			cacheSObject(sObjectName);
			
			Field field = fieldMap.get(sObjectName.toLowerCase() + "." + fieldName.toLowerCase());
			if( null == field ) {
				throw new Exception("No field named " + fieldName + " in " + sObjectName);
			}
			return field;
			
		}
		
		/**
		 * Determine if SDFDC know about a specific field.
		 * 
		 * A field can be unknown for two reasons
		 * <ul>
		 * <li>The field is not defined.
		 * <li>The field is defined by not accessible from this session.
		 * </ul>
		 * @param sObjectName look in this SObject.
		 * @param fieldName look for this field.
		 * @return true if accessible, else false.
		 */
		public boolean isFieldAvailable( String sObjectName, String fieldName ) {
			
			try {
				getField( sObjectName, fieldName ); //Exception thrown if not found.
				
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		
		/**
		 * Return the field that can be used the most recent modification time for a table.
		 * 
		 * @param sObjectName look in this SObject.
		 * 
		 * @return a field or null if not such field can be found.
		 * @throws Exception if a suitable field is not found.
		 */
		public Field getLastModifiedField(String sObjectName) throws Exception {
			
			
			cacheSObject(sObjectName);
		
			for( String name : new String[] {"SystemModstamp", "LastModifiedDate", "CreatedDate"}) {
				
				Field field = fieldMap.get(sObjectName.toLowerCase() + "." + name.toLowerCase());
				if( null != field) { return field; }
			}
			
			throw new Exception(sObjectName + " does not have a field suitable for LastModifiedDate");
		}
		
		/**
		 * Determine if all fields in a list of fields is available to the current session.
		 * 
		 * @param sObjectName look in this SObject. 
		 * @param fieldNameList look at all of these fields.
		 * @return true if all fields are accessble, else false.
		 */
		public boolean isFieldListAvailable( String sObjectName, String[] fieldNameList ) {
			for( String name : fieldNameList ) {
				try {
					getField( sObjectName, name );
				} catch (Exception e) {
					return false;
				}
			}
			return true;
		}
	}
	
	/**
	 * Login into SFDC.
	 * 
	 * Each time this method is called, a new session with SFDC will be attempted. Most callers should save the result of
	 * a successful call to this method and passed it between objects (to share the connection).
	 * 
	 * @param credentials username, password,etc that will be used to connect to sfdc.
	 * @param timeoutInMs how long to wait for a connection. If <=0 defaults o 6 seconds.
	 * @return a validated connection to SFDC.
	 * @throws Exception if connection fails.
	 */
	public Session login( LoginCredentials credentials, int timeoutInMs ) throws Exception {
		SoapBindingStub binding;
		SforceServiceLocator locator = new SforceServiceLocator();
		String sandboxName = null;

		switch( credentials.getConnectionType() ) {
		case Production: {
			
		} break;
		
		case Sandbox: {
			locator.setSoapEndpointAddress("https://test.salesforce.com/services/Soap/u/20.0");
			int n = credentials.getUsername().lastIndexOf(".");
			if( n < 0 ) {
				throw new Exception("Sandbox login attempt but no sandbox specified on the user name.");
			}
			sandboxName = credentials.getUsername().substring( n + 1 );
		} break;
		
		default: {
			throw new Exception("Salesforce of type " + credentials.getConnectionType() + " is not supported");
		}
		}
		
		binding = (SoapBindingStub) locator.getSoap();
		binding.setTimeout( timeoutInMs<=0?DEFAULT_TIMEOUT:timeoutInMs);

		String securityKey = credentials.getSecurityToken();
		
		LoginResult loginResult = binding.login( 
					credentials.getUsername(), 
					credentials.getPassword() + (null==securityKey?"":securityKey));

		binding._setProperty(SoapBindingStub.ENDPOINT_ADDRESS_PROPERTY,
				loginResult.getServerUrl());


		// Create a new session header object and set the session id to that
		// returned by the login
		SessionHeader sh = new SessionHeader();
		sh.setSessionId(loginResult.getSessionId());
		binding.setHeader(new SforceServiceLocator().getServiceName()
				.getNamespaceURI(), "SessionHeader", sh);

	
		return new Session(loginResult,  binding, sandboxName );
	}
	
	/**
	 * Login into a Salesforce database.
	 * 
	 * @param credentials user credentials.
	 * @return a validated connection to SFDC.
	 * @throws Exception if connection fails.
	 */
	public Session login( LoginCredentials credentials ) throws Exception {
		return login( credentials, DEFAULT_TIMEOUT );
	}
	
	/**
	 * Login into SFDC first try a regular instance, then a sandbox if the regular instance fails.
	 * 
	 * Each time this method is called, a new session with SFDC will be attempted. Most callers should save the result of
	 * a successful call to this method and passed it between objects (to share the connection).
	 * 
	 * @param username this user
	 * @param password this password
	 * @param securityToken key assigned by SFDC for API access
	 * @param timeoutInMs how long to wait for a connection. If <=0 defaults o 6 seconds.
	 * @return a validated connection to SFDC.
	 * @throws Exception if connection fails.
	 */
	public Session login( String username, String password, String securityToken, int timeoutInMs ) throws Exception {

		try {
			LoginCredentials credentials = new LoginCredentials( LoginCredentials.ConnectionType.Production, username, password, securityToken);
			return login( credentials, timeoutInMs );
		} catch( Exception e ) {
			try {
				LoginCredentials credentials = new LoginCredentials( LoginCredentials.ConnectionType.Sandbox, username, password, securityToken);
				return login( credentials, timeoutInMs );
			} catch( Exception e2 ) {
				throw e;
			}
		}
		
	}
	

}
