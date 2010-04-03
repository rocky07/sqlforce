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
package com.aslan.sfdc.partner.test;

import java.util.Calendar;
import java.util.List;

import junit.framework.TestCase;

import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.partner.SObjectCreateHelper;
import com.aslan.sfdc.partner.record.AccountRecord;
import com.aslan.sfdc.partner.record.ContactRecord;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.SaveResult;

public class SObjectCreateHelperTest extends TestCase {
	

	private LoginManager.Session testSession = SfdcTestEnvironment.getTestSession();
	
	public void testCreateAccount() throws Exception {
	
		String accountName = "Greg Test " + Calendar.getInstance().getTime().toString();
		AccountRecord newAccount = new AccountRecord();
		
		newAccount.setName( accountName );
		newAccount.setAnnualRevenue(13000.14);
		newAccount.setIndustry("Energy");
		
		
		SObjectCreateHelper helper = new SObjectCreateHelper();
		
		
		List<SaveResult> results = helper.create(testSession, new AccountRecord[] {newAccount});
		
		assertNotNull(results);
		assertEquals( 1, results.size());
		
		for( SaveResult rr : results ) {
			
			if( !rr.isSuccess()) {
				
				for( com.sforce.soap.partner.Error ss : rr.getErrors()) {
					fail(ss.getMessage() );
				}
			}
		}
	
		String accountId = results.get(0).getId();
		
		ContactRecord contact = new ContactRecord();
		
		contact.setAccountId(accountId);
		contact.setFirstName("Mary");
		contact.setLastName( "Hoiles");
		Calendar birthdate = Calendar.getInstance();
		birthdate.set(1960,2,23);
		contact.setBirthdate( birthdate);
		
		results = helper.create(testSession, new ContactRecord[] {contact});
		
		assertNotNull(results);
		assertEquals( 1, results.size());
		
		for( SaveResult rr : results ) {
			
			if( !rr.isSuccess()) {
				
				for( com.sforce.soap.partner.Error ss : rr.getErrors()) {
					fail(ss.getMessage() );
				}
			}
		}
		
		QueryResult qr = testSession.getBinding().query("SELECT Id, Name, Birthdate FROM Contact where id='" +
				results.get(0).getId() + "' LIMIT 1");
		contact = new ContactRecord(qr.getRecords(0));
		
		contact.getBirthdate();

	}

	
	
	
}
