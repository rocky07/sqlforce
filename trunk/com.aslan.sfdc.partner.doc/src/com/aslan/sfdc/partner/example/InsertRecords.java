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
package com.aslan.sfdc.partner.example;

import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.partner.SObjectCreateHelper;
import com.aslan.sfdc.partner.SObjectDeleteHelper;
import com.aslan.sfdc.partner.record.AccountRecord;
import com.sforce.soap.partner.SaveResult;

/**
 * Demonstrate how to insert new records into Salesforce.
 * 
 * Since SOQL does not support an <i>INSERT</i> statement, the process for updating records is always:
 * <ul>
 * <li>Create an array of SObject records minus the id field</li>
 * <li>Call a Salesforce Insert (or upsert) statement.</li>
 * </ul>
 * <p>
 * In practice I rarely use Java to update records. My preference is to use Jython module from the SQLForce project.
 * SQLForce supports a ANSI INSERT statement directly (as well as UPDATE and DELETE).
 * <p>
 * See {@link com.aslan.sfdc.partner.SObjectCreateHelper} for many variations on inserting Salesforce records.
 * Another good source of sample code are the unit tests in com.aslan.sfdc.partner.test.
 * @author gsmithfarmer@gmail.com
 *
 */
public class InsertRecords {

	
	/**
	 * Insert a new account record into Salesfore.
	 * 
	 * @param session an active Salesforce session.
	 * @throws Exception if anything goes wrong.
	 */
	public void insertRecords( LoginManager.Session session ) throws Exception {
		
		//
		// Build a new Account Record.
		//
		AccountRecord account = new AccountRecord();
		
		account.setName("My Sample Account");
		account.setBillingCountry("USA");
		account.setStringField("ShippingCountry", "USA"); // Another way to set a field. Good technique for custom fields.
		
		//
		// Create the new account.
		//
		SaveResult result = new SObjectCreateHelper().create(session, account);
		
		//
		// Verify that the insertion worked.
		//
		if( result.isSuccess()) {
			System.err.println("New Account is: " + result.getId());
		} else {
			System.err.println("Oops: failed to create an account");
		}
		
		//
		// Delete the account we just created.
		//
		if( result.isSuccess()) {
			new SObjectDeleteHelper().delete( session, result.getId());
		}
		
	}
	/**
	 * Call all of the example methods in this class.
	 * 
	 * @param args ignored.
	 */
	public static void main( String[] args ) {
		
		try {
			LoginManager.Session session = ExampleSessionFactory.getSession();
			InsertRecords driver = new InsertRecords();
			
			driver.insertRecords(session);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
}
