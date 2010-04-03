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

import com.aslan.sfdc.partner.DefaultSObjectDeleteCallback;
import com.aslan.sfdc.partner.ISObjectDeleteCallback;
import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.partner.SObjectCreateHelper;
import com.aslan.sfdc.partner.SObjectDeleteHelper;
import com.aslan.sfdc.partner.record.ContactRecord;
import com.aslan.sfdc.partner.record.ISObjectRecord;
import com.sforce.soap.partner.DeleteResult;
import com.sforce.soap.partner.SaveResult;

import junit.framework.TestCase;

/**
 * Unit tests for {@link com.aslan.sfdc.partner.SObjectDeleteHelper}.
 * 
 * Note that 100% coverage is hard to achieve for this class. Two reasons:
 * <ul>
 * <li>Make SFDC fail on a legitimate delete call (for id that exists) is hard.
 * <li>The method {@link com.aslan.sfdc.partner.SObjectDeleteHelper#deleteAll(com.aslan.sfdc.partner.LoginManager.Session, String)}
 * is not safe to unit test. It deletes all objects in a table.
 * </ul>
 * @author greg
 *
 */
public class SObjectDeleteHelperTest extends TestCase {
	
	private LoginManager.Session testSession = SfdcTestEnvironment.getTestSession();
	private static List<String> contactIdList = new ArrayList<String>();
	private static Set<String> contactIdSet = new HashSet<String>();
	private final String[] FIRST_NAMES = {"Gregory", "Mary", "Rebecca", "Caleb", "Ruth", "Hannah", "Josiah",
								"Nathan", "Sarah", "Noah"};
	private final String LAST_NAME = "ThisLastNameShouldBeUnique";
	
	public SObjectDeleteHelperTest() throws Exception {
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
		super.tearDown();

		try {
			new SObjectDeleteHelper().delete( testSession, contactIdList );
		} catch( Exception e) {}; // OK -- some unit tests delete records
		contactIdList.clear();
		contactIdSet.clear();

	}

	public void testDeleteSessionListOfStringISObjectDeleteCallback() throws Exception {
		SObjectDeleteHelper helper = new SObjectDeleteHelper();
		final Set<String> deleteSet = new HashSet<String>();
		final Set<String> errorSet = new HashSet<String>();
		
		ISObjectDeleteCallback callback = new DefaultSObjectDeleteCallback() {

			public void delete(int rowNumber, String id,
					DeleteResult deleteResult) {
				
				deleteSet.add(id);
			}

			public void error(int rowNumber, String id,
					DeleteResult deleteResult, String message) {
				errorSet.add(id);
			}
			
		};
		
		int nDeleted = helper.delete(testSession, contactIdList, callback);
		
		assertEquals( nDeleted, contactIdList.size());
		assertEquals( nDeleted, deleteSet.size());
		assertEquals( 0, errorSet.size());
		assertTrue( deleteSet.containsAll(contactIdSet));
		assertTrue( contactIdSet.containsAll(deleteSet));
		
		//
		// Delete nothing.
		//
		deleteSet.clear();
		errorSet.clear();
		
		
		nDeleted = helper.delete(testSession, contactIdList, callback);
		assertEquals( 0, nDeleted );
		assertEquals( contactIdSet.size(), errorSet.size());
		
	}

	
	public void testDeleteWhereSessionStringStringISObjectDeleteCallback() throws Exception {
		SObjectDeleteHelper helper = new SObjectDeleteHelper();
		final Set<String> deleteSet = new HashSet<String>();
		final Set<String> errorSet = new HashSet<String>();
		
		ISObjectDeleteCallback callback = new DefaultSObjectDeleteCallback() {

			public void delete(int rowNumber, String id,
					DeleteResult deleteResult) {
				
				deleteSet.add(id);
			}

			public void error(int rowNumber, String id,
					DeleteResult deleteResult, String message) {
				errorSet.add(id);
			}
			
		};
		
		int nDeleted = helper.deleteWhere(testSession, ContactRecord.SOBJECT_NAME, 
				"LastName='" + LAST_NAME + "'", callback);
		

		assertEquals( nDeleted, deleteSet.size());
		assertEquals( 0, errorSet.size());
		assertTrue( deleteSet.containsAll(contactIdSet));
		//assertTrue( contactIdSet.containsAll(deleteSet));
		
		//
		// Delete nothing.
		//
		deleteSet.clear();
		errorSet.clear();
		
		
		nDeleted = helper.deleteWhere(testSession, ContactRecord.SOBJECT_NAME, 
				"LastName='" + LAST_NAME + "'", callback);
		assertEquals( 0, nDeleted );
		assertEquals( 0, errorSet.size());
	}

	
	public void testDeleteWithStatus() throws Exception{
		SObjectDeleteHelper helper = new SObjectDeleteHelper();
	
		List<DeleteResult> resultList = helper.deleteWithStatus(testSession, contactIdList);
		
		assertEquals( resultList.size(), contactIdList.size());
		for( DeleteResult rr : resultList ) {
			assertTrue( rr.isSuccess());
			assertTrue( contactIdSet.contains(rr.getId()));
		}
		
		//
		// Delete nothing.
		//
		resultList = helper.deleteWithStatus(testSession, contactIdList);
		assertEquals( resultList.size(), contactIdList.size());
		
		for( DeleteResult rr : resultList ) {
			assertTrue( !rr.isSuccess());
		}
	}

	public void testDeleteWhereWithStatus() throws Exception{
		SObjectDeleteHelper helper = new SObjectDeleteHelper();
		String sqlWhere = "LastName='" + LAST_NAME + "'";
		List<DeleteResult> resultList = helper.deleteWhereWithStatus(testSession, 
				ContactRecord.SOBJECT_NAME, sqlWhere);
		
		assertEquals( resultList.size(), contactIdList.size());
		for( DeleteResult rr : resultList ) {
			assertTrue( rr.isSuccess());
			assertTrue( contactIdSet.contains(rr.getId()));
		}
		
		//
		// Delete nothing.
		//
		resultList = helper.deleteWhereWithStatus(testSession, 
				ContactRecord.SOBJECT_NAME, sqlWhere);
		assertEquals( 0, resultList.size());
		
	}

	public void testDeleteSessionListOfString() throws Exception {
		SObjectDeleteHelper helper = new SObjectDeleteHelper();
		
		helper.delete(testSession, contactIdList);
		
		//
		// Delete nothing.
		//
		try {
			helper.delete(testSession, contactIdList);
			fail("No exception when trying to delete non-existent object");
		} catch( Exception e ) {};
	}

	public void testDeleteSessionString() throws Exception {
		SObjectDeleteHelper helper = new SObjectDeleteHelper();
		String id = contactIdList.get(0);
		
		helper.delete(testSession, id);
		
		//
		// Delete nothing.
		//
		try {
			helper.delete(testSession, id);
			fail("No exception when trying to delete non-existent object");
		} catch( Exception e ) {};
	}

	public void testDeleteAll() {
		// Not implemented on purpose -- clears all rows in a table.
	}

	public void testDeleteWhereSessionStringString() throws Exception {
		SObjectDeleteHelper helper = new SObjectDeleteHelper();
		String sqlWhere = "LastName='" + LAST_NAME + "'";
		
		helper.deleteWhere(testSession, 
				ContactRecord.SOBJECT_NAME, sqlWhere);
		
		//
		// Delete nothing.
		//
		helper.deleteWhere(testSession, 
					ContactRecord.SOBJECT_NAME, sqlWhere);
		
		
		
	}

}
