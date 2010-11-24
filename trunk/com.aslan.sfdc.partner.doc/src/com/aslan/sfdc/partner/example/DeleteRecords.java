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
 * Demonstrate how to delete records into Salesforce.
 * 
 * Since SOQL does not support an <i>DELETE</i> statement, the process for deleting records is always:
 * <ul>
 * <li>Find the Salesforce ID of the records to delete.</li>
 * <li>Call a Salesforce Delete statement.</li>
 * </ul>
 * <p>
 * In practice I rarely use Java to delete records. My preference is to use Jython module from the SQLForce project.
 * SQLForce supports a ANSI DELETE statement directly (as well as UPDATE and UPDATE).
 * <p>
 * See {@link com.aslan.sfdc.partner.SObjectDeleteHelper} for many variations on deleting Salesforce records.
 * Another good source of sample code are the unit tests in com.aslan.sfdc.partner.test.
 * @author gsmithfarmer@gmail.com
 *
 */
public class DeleteRecords {

	
	/**
	 * Delete an account records from Salesfore.
	 * 
	 * @param session an active Salesforce session.
	 * @throws Exception if anything goes wrong.
	 */
	public void deleteRecord( LoginManager.Session session ) throws Exception {
		
		//
		// Build a new Account Record can safely delete.
		//
		AccountRecord account = new AccountRecord();
		
		account.setName("My Sample Account");
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
			DeleteRecords driver = new DeleteRecords();
			
			driver.deleteRecord(session);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
}
