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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.aslan.sfdc.partner.DefaultSObjectUpdateCallback;
import com.aslan.sfdc.partner.ISObjectUpdateCallback;
import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.partner.SObjectCreateHelper;
import com.aslan.sfdc.partner.SObjectDeleteHelper;
import com.aslan.sfdc.partner.SObjectQueryHelper;
import com.aslan.sfdc.partner.SObjectUpdateHelper;
import com.aslan.sfdc.partner.record.ContactRecord;
import com.aslan.sfdc.partner.record.ISObjectRecord;
import com.sforce.soap.partner.SaveResult;

import junit.framework.TestCase;

public class SObjectUpdateHelperTest extends TestCase {

	private LoginManager.Session testSession = SfdcTestEnvironment.getTestSession();
	private static List<String> contactIdList = new ArrayList<String>();
	private static Set<String> contactIdSet = new HashSet<String>();
	private final String[] FIRST_NAMES = {"Gregory", "Mary", "Rebecca", "Caleb", "Ruth", "Hannah", "Josiah",
								"Nathan", "Sarah", "Noah"};
	private final String LAST_NAME = "ThisLastNameShouldBeUnique";
	
	public SObjectUpdateHelperTest() throws Exception {
		new SObjectDeleteHelper().deleteWhere( testSession, 
				ContactRecord.SOBJECT_NAME, 
				ContactRecord.F_LAST_NAME + "='" + LAST_NAME + "'"
		);
	}
	protected void setUp() throws Exception {
		super.setUp();
		
		contactIdList.clear();
		contactIdSet.clear();
		
		SObjectCreateHelper createHelper = new SObjectCreateHelper();
		
		List<ISObjectRecord> contacts = new ArrayList<ISObjectRecord>();
		for( String firstName : FIRST_NAMES ) {
			ContactRecord cc = new ContactRecord();
			cc.setFirstName( firstName );
			cc.setLastName(LAST_NAME);
			contacts.add( cc );
		}
		
		List<SaveResult> saveResults = createHelper.create( testSession, contacts );
		for( SaveResult cc : saveResults ) {
			if( cc.isSuccess()) {
				contactIdList.add( cc.getId());
				contactIdSet.add( cc.getId());
			}
		}
	}

	protected void tearDown() throws Exception {
		try {
			new SObjectDeleteHelper().delete( testSession, contactIdList );
		} catch( Exception e) {}; // OK -- some unit tests delete records
		contactIdList.clear();
		contactIdSet.clear();

	}
	
	/**
	 * Test for the most general purpose update methods (others use this method).
	 * 
	 * @throws Exception
	 */
	public void testUpdateSessionISObjectRecordArrayISObjectUpdateCallback() throws Exception {
		SObjectUpdateHelper helper = new SObjectUpdateHelper();
		final Set<ISObjectRecord> updateSet = new HashSet<ISObjectRecord>();
		final Set<ISObjectRecord> errorSet = new HashSet<ISObjectRecord>();
		
		ISObjectUpdateCallback callback = new DefaultSObjectUpdateCallback() {

			public void error(int rowNumber, ISObjectRecord sObjRecord,
					SaveResult saveResult, String message) {
				errorSet.add(sObjRecord);
				
				assertNotNull( saveResult );
				assertNotNull( message );
			}

			@Override
			public void update(int rowNumber, ISObjectRecord sObjRecord,
					SaveResult saveResult) {
				updateSet.add(sObjRecord);
			}
			
		};
		List<ISObjectRecord> updateList = new ArrayList<ISObjectRecord>();
		
		for( String id : contactIdList ) {
			ContactRecord cc = new ContactRecord();
			cc.setId( id );
			cc.setTitle("BrownCow");
			updateList.add(cc);
		}
		
		int nUpdated = helper.update(testSession, updateList.toArray(new ISObjectRecord[0]), callback);
		
		assertEquals( contactIdList.size(), nUpdated);
		assertEquals( contactIdList.size(), updateSet.size());
		assertEquals( 0, errorSet.size());
		
		ContactRecord ccUpdate = new ContactRecord( new SObjectQueryHelper().findSObject(testSession, ContactRecord.SOBJECT_NAME, contactIdList.get(0)));
		assertEquals( "BrownCow", ccUpdate.getTitle());
		//
		// Update Nothing
		//
		updateSet.clear();
		errorSet.clear();
		
		nUpdated = helper.update(testSession, new ISObjectRecord[0], callback);
		
		assertEquals( 0, nUpdated );
		assertEquals( 0, updateSet.size());
		assertEquals( 0, errorSet.size());
		
		//
		// Make a few updates fail.
		//
		updateList.clear();
		for( String id : contactIdList ) {
			ContactRecord cc = new ContactRecord();
			cc.setId( id );
			cc.setAccountId("InvalidAccountId");
			updateList.add(cc);
		}
		
		updateSet.clear();
		errorSet.clear();
		
		nUpdated = helper.update(testSession, updateList.toArray(new ISObjectRecord[0]), callback);
		
		assertEquals( 0, nUpdated);
		assertEquals( contactIdList.size(), errorSet.size());
		assertEquals( 0, updateSet.size());
	}

	
	public void testUpdateSessionListOfISObjectRecordISObjectUpdateCallback() throws Exception {
		SObjectUpdateHelper helper = new SObjectUpdateHelper();
		final Set<ISObjectRecord> updateSet = new HashSet<ISObjectRecord>();
		final Set<ISObjectRecord> errorSet = new HashSet<ISObjectRecord>();
		
		ISObjectUpdateCallback callback = new DefaultSObjectUpdateCallback() {

			public void error(int rowNumber, ISObjectRecord sObjRecord,
					SaveResult saveResult, String message) {
				errorSet.add(sObjRecord);
				
				assertNotNull( saveResult );
				assertNotNull( message );
			}

			@Override
			public void update(int rowNumber, ISObjectRecord sObjRecord,
					SaveResult saveResult) {
				updateSet.add(sObjRecord);
			}
			
		};
		List<ISObjectRecord> updateList = new ArrayList<ISObjectRecord>();
		
		for( String id : contactIdList ) {
			ContactRecord cc = new ContactRecord();
			cc.setId( id );
			cc.setTitle("BrownCow");
			updateList.add(cc);
		}
		
		int nUpdated = helper.update(testSession, updateList, callback);
		
		assertEquals( contactIdList.size(), nUpdated);
		assertEquals( contactIdList.size(), updateSet.size());
		assertEquals( 0, errorSet.size());
		
	}

	
	public void testUpdateSessionListOfISObjectRecord() throws Exception {
		SObjectUpdateHelper helper = new SObjectUpdateHelper();
		
		List<ISObjectRecord> updateList = new ArrayList<ISObjectRecord>();
		
		for( String id : contactIdList ) {
			ContactRecord cc = new ContactRecord();
			cc.setId( id );
			cc.setTitle("BrownCow");
			updateList.add(cc);
		}
		
		List<SaveResult> saveResults = helper.update(testSession, updateList);
		
		assertEquals( contactIdList.size(), saveResults.size());
		
		for( SaveResult result : saveResults ) {
			assertTrue( result.isSuccess());
		}
	}

	public void testUpdateSessionISObjectRecordArray() throws Exception {
		SObjectUpdateHelper helper = new SObjectUpdateHelper();
	
		List<ISObjectRecord> updateList = new ArrayList<ISObjectRecord>();
		
		for( String id : contactIdList ) {
			ContactRecord cc = new ContactRecord();
			cc.setId( id );
			cc.setTitle("BrownCow");
			updateList.add(cc);
		}
		
		List<SaveResult> saveResults = helper.update(testSession, updateList.toArray(new ISObjectRecord[0]));
		
		assertEquals( contactIdList.size(), saveResults.size());
		
		for( SaveResult result : saveResults ) {
			assertTrue( result.isSuccess());
		}
		
	}

	public void testUpdateSessionISObjectRecord() throws Exception {
		SObjectUpdateHelper helper = new SObjectUpdateHelper();
		
		ContactRecord cc = new ContactRecord();
		cc.setId( contactIdList.get(0));
		cc.setAccountId(null);
		cc.setTitle("CEO");
		SaveResult result = helper.update( testSession, cc);
		
		assertTrue( result.isSuccess());
		
		ContactRecord ccUpdate = new ContactRecord( new SObjectQueryHelper().findSObject(testSession, ContactRecord.SOBJECT_NAME, contactIdList.get(0)));
		assertEquals( "CEO", ccUpdate.getTitle());
		assertEquals( LAST_NAME, ccUpdate.getLastName());
		
		cc.setAccountId("BadAccountId");
		result = helper.update( testSession, cc);
		assertFalse( result.isSuccess());
		
		
	}

}
