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

import java.util.ArrayList;
import java.util.List;

import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.partner.SObjectQueryHelper;
import com.aslan.sfdc.partner.SObjectUpdateHelper;
import com.aslan.sfdc.partner.record.ISObjectRecord;
import com.aslan.sfdc.partner.record.UserRecord;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.sobject.SObject;

/**
 * Demonstrate how to update any type of record.
 * 
 * Since SOQL does not support an <i>UPDATE</i> statement, the process for updating records is always:
 * <ul>
 * <li>Find the IDs of the records to update.</li>
 * <li>Build SObject records that contain just ID and values for the fields to update.</li>
 * <li>Call a Salesforce Update statement.</li>
 * </ul>
 * <p>
 * In practice I rarely use Java to update records. My preference is to use Jython module from the SQLForce project.
 * SQLForce supports a ANSI UPDATE statement directly (as well as INSERT and DELETE).
 * <p>
 * See {@link com.aslan.sfdc.partner.SObjectUpdateHelper} for many variations on updating Salesforce records.
 * Another good source of sample code are the unit tests in com.aslan.sfdc.partner.test.
 * @author gsmithfarmer@gmail.com
 *
 */
public class UpdateRecords {

	
	/**
	 * Change the value of a column in a table for a set of records.
	 * 
	 * @param session an active Salesforce session.
	 * @throws Exception if anything goes wrong.
	 */
	public void updateRecords( LoginManager.Session session ) throws Exception {
		//
		// Find records to update. Rather than directly grabbing the row data (via {@link com.aslan.sfdc.partner.SObjectQueryHelper#runQuery2})
		// we grab the underlying Salesforce partner SObject record instead. We are going this so we can construct a type-safe way
		// of accessing the standard fields of User.
		//
		List<SObject> userRecords = new SObjectQueryHelper().runQuery( session, "SELECT id, title, name FROM User");
		
		//
		// Build a list of objects to update.
		//
		List<ISObjectRecord> updateRecords = new ArrayList<ISObjectRecord>();
		for( SObject sobj : userRecords ) {
			UserRecord originalUser = new UserRecord( sobj );
			UserRecord updateUser = new UserRecord();
			
			updateUser.setId( originalUser.getId());
			updateUser.setTitle( "Replacement Title for " + originalUser.getName());
			
			/**
			 * The following statement would also work to set a new title. The setStringField method() is useful
			 * for setting custom fields.
			 *
			updateUser.setStringField( "title", "Replacement Title for " + originalUser.getName());
			*/
			updateRecords.add(updateUser);
			
		}
		
		//
		// Update the records giving each a new title.
		//
		List<SaveResult> results = new SObjectUpdateHelper().update(session, updateRecords );
		
		//
		// Report on the status of each update. We have to check each result because Salesforce
		// may succeed on some records while failing on others.
		//
		for( SaveResult result : results ) {
			System.err.println("Update Records: " + result.getId() + " was " + result.isSuccess());
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
			UpdateRecords driver = new UpdateRecords();
			
			driver.updateRecords(session);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
}
