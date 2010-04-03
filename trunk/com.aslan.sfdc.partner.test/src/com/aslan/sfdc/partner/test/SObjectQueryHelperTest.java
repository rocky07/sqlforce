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

import com.aslan.sfdc.partner.DefaultSObjectQuery2Callback;
import com.aslan.sfdc.partner.DefaultSObjectQueryCallback;
import com.aslan.sfdc.partner.ISObjectQuery2Callback;
import com.aslan.sfdc.partner.ISObjectQueryCallback;
import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.partner.SObjectCreateHelper;
import com.aslan.sfdc.partner.SObjectDeleteHelper;
import com.aslan.sfdc.partner.SObjectQueryHelper;
import com.aslan.sfdc.partner.record.ContactRecord;
import com.aslan.sfdc.partner.record.ISObjectRecord;
import com.aslan.sfdc.partner.record.ProfileRecord;
import com.aslan.sfdc.partner.record.UserRecord;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.sobject.SObject;

import junit.framework.TestCase;

/**
 * Unit tests for {@link com.aslan.sfdc.partner.SObjectQueryHelper}.
 * 
 * Coverage was almost at 100% on 31-Dec-2009 EXCEPT for a couple lines that handle the
 * case where SalesForce returns the results of a query in multiple packets. This case is
 * not covered in the unit tests because it takes too long to run.
 * 
 * @author greg
 *
 */
public class SObjectQueryHelperTest extends TestCase {

	//
	// If tearDownTestData==true then the unit test will clean up test data after EACH test.
	// This make each test take 4-5 seconds.
	//
	// If tearDownTestData==false then unit tests share test data and only the first unit test
	// takes 4-5 seconds to run. The bad part -- test data is left in whatever database you are
	// running against.
	//
	private static boolean tearDownTestData = false;
	private LoginManager.Session testSession = SfdcTestEnvironment.getTestSession();
	private static List<String> contactIdList = new ArrayList<String>();
	private static Set<String> contactIdSet = new HashSet<String>();
	private final String[] FIRST_NAMES = {"Gregory", "Mary", "Rebecca", "Caleb", "Ruth", "Hannah", "Josiah",
								"Nathan", "Sarah", "Noah"};
	private final String LAST_NAME = "ThisLastNameShouldBeUnique";
	private final String LAST_NAME_EXTRA = "ThisLastNameShouldBeUniqueExtra";
	
	public SObjectQueryHelperTest() throws Exception {
		
		//
		// Delete any records that may be left over because of crapped out salesforce session.
		//
		if( contactIdSet.isEmpty()) {
			new SObjectDeleteHelper().deleteWhere( testSession, 
				ContactRecord.SOBJECT_NAME, 
				ContactRecord.F_LAST_NAME + "='" + LAST_NAME + "'"
				+ " OR " + ContactRecord.F_LAST_NAME + "='" + LAST_NAME_EXTRA + "'"
				);
		}
	}
	
	protected void setUp() throws Exception {
		super.setUp();
		
		if( 0 != contactIdList.size()) { return; } // Preserving test data (for speed).
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
		
		//
		// Create enough records such that more than 200 contacts exist
		//
		for( int n = 0; n < 200; n++ ) {
			ContactRecord cc = new ContactRecord();
			cc.setFirstName( "Extra_" + n  );
			cc.setLastName(LAST_NAME_EXTRA);
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
		
		if( tearDownTestData ) {
			new SObjectDeleteHelper().delete( testSession, contactIdList );
			contactIdList.clear();
			contactIdSet.clear();
		}
		
	}

	/**
	 * Test the most general of the entry points (most others call this method).
	 * 
	 * @throws Exception
	 */
	public void testFindSObjectsObjectFieldsQuery() throws Exception{
		
		SObjectQueryHelper helper = new SObjectQueryHelper();
		
		final List<ContactRecord> contacts = new ArrayList<ContactRecord>();
		ISObjectQueryCallback callback = new DefaultSObjectQueryCallback() {

			public void addRow(int rowNumber, SObject sObject) {
				contacts.add( new ContactRecord(sObject));
				
			}
			
		};
		
		//
		// Expect to find records
		//
		contacts.clear();
		int nFound = helper.findSObjects( testSession, ContactRecord.SOBJECT_NAME,
				new String[] {ContactRecord.F_ID, ContactRecord.F_FIRST_NAME},
				ContactRecord.F_LAST_NAME + "='" + LAST_NAME + "'",
				callback);
		
		
		assertEquals( contacts.size(), nFound);
		assertEquals( FIRST_NAMES.length, nFound);
		
		for( ContactRecord cc : contacts ) {
			assertTrue( contactIdSet.contains( cc.getId()));
		}
		
		//
		// Expect to not find records.
		//
		contacts.clear();
		nFound = helper.findSObjects( testSession, ContactRecord.SOBJECT_NAME,
				new String[] {ContactRecord.F_ID, ContactRecord.F_FIRST_NAME},
				ContactRecord.F_LAST_NAME + "='" + LAST_NAME + "DoNotFind'",
				callback);
		assertEquals( 0, nFound);
		assertEquals( 0, contacts.size());
		
		//
		// Fetch more than 200 records
		//
		contacts.clear();
		nFound = helper.findSObjects( testSession, ContactRecord.SOBJECT_NAME,
				new String[] {ContactRecord.F_ID, ContactRecord.F_FIRST_NAME},
				ContactRecord.F_LAST_NAME + "='" + LAST_NAME + "'"
				+ " OR " + ContactRecord.F_LAST_NAME + "='" + LAST_NAME_EXTRA + "'",
				callback);
		
		assertTrue( nFound > 200 );
		//
		// Call without field names.
		//
		contacts.clear();
		try {
			nFound = helper.findSObjects( testSession, ContactRecord.SOBJECT_NAME,
					new String[] {},
					ContactRecord.F_LAST_NAME + "='" + LAST_NAME + "'",
					callback);

			fail("No exception when field names not specified");
			
		} catch( Exception e ) {}
		
		contacts.clear();
		try {
			
			nFound = helper.findSObjects( testSession, ContactRecord.SOBJECT_NAME,
					(String[]) null,
					ContactRecord.F_LAST_NAME + "='" + LAST_NAME + "'",
					callback);

			fail("No exception when field names null");
		} catch( Exception e ) {}
		
		//
		// Find all records.
		//
		contacts.clear();
		nFound = helper.findSObjects( testSession, ContactRecord.SOBJECT_NAME,
				new String[] {ContactRecord.F_ID, ContactRecord.F_FIRST_NAME},
				"",
				callback);
		
		assertTrue( contacts.size() > 0 );
		
		contacts.clear();
		nFound = helper.findSObjects( testSession, ContactRecord.SOBJECT_NAME,
				new String[] {ContactRecord.F_ID, ContactRecord.F_FIRST_NAME},
				(String)null,
				callback);
		
		assertTrue( contacts.size() > 0 );
	}

	/**
	 * Find records where all non-null fields will be fetched.
	 * 
	 * @throws Exception
	 */
	public void testFindSObjectsObjectQuery() throws Exception {
		SObjectQueryHelper helper = new SObjectQueryHelper();
		
		final List<ContactRecord> contacts = new ArrayList<ContactRecord>();
		ISObjectQueryCallback callback = new DefaultSObjectQueryCallback() {

			public void addRow(int rowNumber, SObject sObject) {
				contacts.add( new ContactRecord(sObject));
				
			}
			
		};
		
		//
		// Expect to find records
		//
		contacts.clear();
		int nFound = helper.findSObjects( testSession, ContactRecord.SOBJECT_NAME,
				ContactRecord.F_LAST_NAME + "='" + LAST_NAME + "'",
				callback);
		
		
		assertEquals( contacts.size(), nFound);
		assertEquals( FIRST_NAMES.length, nFound);
		assertEquals( LAST_NAME, contacts.get(0).getLastName());
		assertNotNull( contacts.get(0).getFirstName());
	}

	/**
	 * Find a set of records when the id of the records are specified.
	 * 
	 */
	public void testFindSObjectsSessionObjectFieldsIdFieldIdList() throws Exception {
		SObjectQueryHelper helper = new SObjectQueryHelper();
		
		final List<ContactRecord> contacts = new ArrayList<ContactRecord>();
		ISObjectQueryCallback callback = new DefaultSObjectQueryCallback() {

			public void addRow(int rowNumber, SObject sObject) {
				contacts.add( new ContactRecord(sObject));
				
			}
			
		};
		
		//
		// Expect to find records
		//
		contacts.clear();
		int nFound = helper.findSObjects( testSession, ContactRecord.SOBJECT_NAME,
				new String[] {ContactRecord.F_ID, ContactRecord.F_FIRST_NAME},
				ContactRecord.F_ID,
				contactIdList,
				callback);
		
		
		assertEquals( contacts.size(), nFound);
		assertEquals( contactIdSet.size(), nFound);
		
		
	}

	/**
	 * Find records by ID and fetch all fields.
	 * 
	 * @throws Exception
	 */
	public void testFindSObjectsSessionObjectIdFieldIdList() throws Exception {
		SObjectQueryHelper helper = new SObjectQueryHelper();
		
		final List<ContactRecord> contacts = new ArrayList<ContactRecord>();
		ISObjectQueryCallback callback = new DefaultSObjectQueryCallback() {

			public void addRow(int rowNumber, SObject sObject) {
				contacts.add( new ContactRecord(sObject));
				
			}
			
		};
		
		//
		// Expect to find records
		//
		contacts.clear();
		int nFound = helper.findSObjects( testSession, ContactRecord.SOBJECT_NAME,
				ContactRecord.F_ID,
				contactIdList,
				callback);
		
		
		assertEquals( contacts.size(), nFound);
		assertEquals( contactIdSet.size(), nFound);
		assertNotNull( contacts.get(0).getFirstName());
		
	}

	/**
	 * Find ids (and only the ids) of SObjects given a where clause.
	 */
	public void testFindSObjectIds() throws Exception {
		SObjectQueryHelper helper = new SObjectQueryHelper();
		
		final List<ContactRecord> contacts = new ArrayList<ContactRecord>();
		ISObjectQueryCallback callback = new DefaultSObjectQueryCallback() {

			public void addRow(int rowNumber, SObject sObject) {
				contacts.add( new ContactRecord(sObject));
				
			}
			
		};
		
		
		//
		// Expect to find records
		//
		contacts.clear();
		int nFound = helper.findSObjectIds( testSession, ContactRecord.SOBJECT_NAME,
				ContactRecord.F_LAST_NAME + "='" + LAST_NAME + "'",
				callback);
		
		
		assertEquals( contacts.size(), nFound);
		assertNotNull( contacts.get(0).getId());
		assertNull( contacts.get(0).getLastName());
	}

	/**
	 * Find a list of objects where the fields and a where clause are specified.
	 * 
	 * @throws Exception
	 */
	public void testFindSObjectListObjectFieldsWhere() throws Exception {
		
		SObjectQueryHelper helper = new SObjectQueryHelper();
		
		List<SObject> list = helper.findSObjectList(testSession, ContactRecord.SOBJECT_NAME, 
				new String[] {ContactRecord.F_ID, ContactRecord.F_FIRST_NAME},
				ContactRecord.F_LAST_NAME + "='" + LAST_NAME + "'"
				);
		
		assertNotNull(list);
		assertEquals( FIRST_NAMES.length, list.size() );
		assertNotNull( (new ContactRecord(list.get(0)).getFirstName()));
	}

	/**
	 * Find a list of object ids given a where clause.
	 */
	public void testFindSObjectIdList() throws Exception {
		SObjectQueryHelper helper = new SObjectQueryHelper();
		
		List<String> list = helper.findSObjectIdList(testSession, ContactRecord.SOBJECT_NAME, 
				ContactRecord.F_LAST_NAME + "='" + LAST_NAME + "'"
				);
		
		assertNotNull(list);
		assertEquals( FIRST_NAMES.length, list.size() );
		for( String id : list ) {
			assertTrue( contactIdSet.contains(id));
		}
	}

	/**
	 * Find all fields of objects that match a where clause.
	 */
	public void testFindSObjectListObjectWhere() throws Exception {
		SObjectQueryHelper helper = new SObjectQueryHelper();
		
		List<SObject> list = helper.findSObjectList(testSession, ContactRecord.SOBJECT_NAME, 
				ContactRecord.F_LAST_NAME + "='" + LAST_NAME + "'"
				);
		
		assertNotNull(list);
		assertEquals( FIRST_NAMES.length, list.size() );
		assertNotNull( (new ContactRecord(list.get(0)).getFirstName()));
	}

	
	/**
	 * Find all fields of objects that match particular ids.
	 * 
	 */
	public void testFindSObjectListSessionObjectIdFieldIdList() throws Exception {
		SObjectQueryHelper helper = new SObjectQueryHelper();
		
		List<SObject> list = helper.findSObjectList(testSession, ContactRecord.SOBJECT_NAME, 
				ContactRecord.F_ID,
				contactIdList
				);
		
		assertNotNull(list);
		assertNotNull( (new ContactRecord(list.get(0)).getFirstName()));
	}

	/**
	 * Find particular fields given a list of record ids.
	 * 
	 * @throws Exception
	 */
	public void testFindSObjectListSessionObjectFieldsIdFieldIdList() throws Exception {
		SObjectQueryHelper helper = new SObjectQueryHelper();
		
		List<SObject> list = helper.findSObjectList(testSession, ContactRecord.SOBJECT_NAME, 
				new String[] {ContactRecord.F_ID, ContactRecord.F_FIRST_NAME},
				ContactRecord.F_ID,
				contactIdList
				);
		
		assertNotNull(list);
		assertNotNull( (new ContactRecord(list.get(0)).getFirstName()));
		assertNull( (new ContactRecord(list.get(0)).getLastName()));
	}

	/**
	 * Find a particular SObject (and fields) given an id.
	 */
	public void testFindSObjectSessionStringStringStringArray() throws Exception {
		SObjectQueryHelper helper = new SObjectQueryHelper();
		
		SObject sObject = helper.findSObject(testSession, ContactRecord.SOBJECT_NAME, 
				contactIdList.get(0),
				new String[] {ContactRecord.F_ID, ContactRecord.F_FIRST_NAME}
				
				);
		
		assertNotNull(sObject);
		assertEquals( contactIdList.get(0), sObject.getId());
		assertNotNull( (new ContactRecord(sObject).getFirstName()));
		assertNull( (new ContactRecord(sObject).getLastName()));
		
		//
		// Make the find fail.
		//
		SObjectCreateHelper createHelper = new SObjectCreateHelper();
		
		ContactRecord cc = new ContactRecord();
		cc.setFirstName("John"); cc.setLastName(LAST_NAME);
		String missingId = createHelper.create(testSession, cc ).getId();
		new SObjectDeleteHelper().delete(testSession, missingId);
		
		try {
			sObject = helper.findSObject(testSession, ContactRecord.SOBJECT_NAME, 
					missingId,
					new String[] {ContactRecord.F_ID, ContactRecord.F_FIRST_NAME}

			);
			fail("No exception found when failed to find an object");
		} catch( Exception e ) {}
		
	}
	/**
	 * Find a particular SObject (and fields as a list) given an id.
	 */
	public void testFindSObjectSessionStringStringListOfString() throws Exception{
		SObjectQueryHelper helper = new SObjectQueryHelper();
		List<String> fields = new ArrayList<String>();
		
		fields.add( ContactRecord.F_ID );
		fields.add( ContactRecord.F_FIRST_NAME );
		
		SObject sObject = helper.findSObject(testSession, ContactRecord.SOBJECT_NAME, 
				contactIdList.get(0),
				fields
				);
		
		assertNotNull(sObject);
		assertEquals( contactIdList.get(0), sObject.getId());
		assertNotNull( (new ContactRecord(sObject).getFirstName()));
		assertNull( (new ContactRecord(sObject).getLastName()));
	}

	/**
	 * Find a particular SObject and all of its fields given an id.
	 */
	public void testFindSObjectSessionStringString() throws Exception {
		SObjectQueryHelper helper = new SObjectQueryHelper();

		SObject sObject = helper.findSObject(testSession, ContactRecord.SOBJECT_NAME, 
				contactIdList.get(0)
				);
		
		assertNotNull(sObject);
		assertEquals( contactIdList.get(0), sObject.getId());
		assertNotNull( (new ContactRecord(sObject).getFirstName()));
		assertNotNull( (new ContactRecord(sObject).getLastName()));
	}


	public void testGetUserRecords() throws Exception {
		SObjectQueryHelper helper = new SObjectQueryHelper();
		
		List<UserRecord> list = helper.getUserRecords( testSession );
		
		assertNotNull(list);
		assertTrue( list.size() > 0 );
	}

	public void testGetProfileRecords() throws Exception {
		SObjectQueryHelper helper = new SObjectQueryHelper();
		
		List<ProfileRecord> list = helper.getProfileRecords( testSession );
		
		assertNotNull(list);
		assertTrue( list.size() > 0 );
	}

	/**
	 * Find all rows that match a particular query.
	 */
	public void testFindRowsSessionStringISObjectQueryCallback() throws Exception {
		SObjectQueryHelper helper = new SObjectQueryHelper();
		
		final List<ContactRecord> contacts = new ArrayList<ContactRecord>();
		ISObjectQueryCallback callback = new DefaultSObjectQueryCallback() {

			public void addRow(int rowNumber, SObject sObject) {
				contacts.add( new ContactRecord(sObject));
				
			}
			
		};
		
		int nFound = helper.findRows( testSession, 
				"SELECT Id, FirstName, LastName FROM Contact "
				+ " WHERE LastName='" + LAST_NAME + "'",
				callback);
		
		assertEquals( FIRST_NAMES.length, nFound );
		assertEquals( nFound, contacts.size());
		assertEquals( LAST_NAME, contacts.get(0).getLastName());
		
		//
		// Run a bad query.
		//
		try {
			nFound = helper.findRows( testSession, 
					"SELECT Id, FirstName, LastName FROM Contact "
					+ " WHERE LastNameFoo='" + LAST_NAME + "'",
					callback);

			fail("Invalid query did not throw an exception");
		} catch( Exception e ) {}
	}
	

	/**
	 * Run a arbitrary bit of sql where the results are returned as raw strings.
	 */
	public void testFindRowsSessionStringISObjectQuery2Callback() throws Exception {
		SObjectQueryHelper helper = new SObjectQueryHelper();
		
		final List<String[]> contacts = new ArrayList<String[]>();
		ISObjectQuery2Callback callback = new DefaultSObjectQuery2Callback() {

			@Override
			public void addRow(int rowNumber, String[] data) {
				contacts.add(data);
				
			}

			
		};
		
		int nFound = helper.findRows( testSession, 
				"SELECT Id, FirstName, LastName, Title FROM Contact "
				+ " WHERE LastName='" + LAST_NAME + "'",
				callback);
		
		assertEquals( FIRST_NAMES.length, nFound );
		assertEquals( nFound, contacts.size());
		assertEquals( 4, contacts.get(0).length);
		assertEquals( LAST_NAME, contacts.get(0)[2]);
		assertNull( contacts.get(0)[3]);
	}

	/**
	 * Run an arbitrary query return SObjects.
	 */
	public void testRunQuery() throws Exception {
		SObjectQueryHelper helper = new SObjectQueryHelper();
		
		List<SObject> list = helper.runQuery( testSession,
				"SELECT Id, FirstName, LastName, Title FROM Contact "
				+ " WHERE LastName='" + LAST_NAME + "'"
				);
		
		assertEquals( FIRST_NAMES.length, list.size() );
		assertEquals( LAST_NAME, new ContactRecord(list.get(0)).getLastName());
		assertNull( new ContactRecord(list.get(0)).getTitle());
	}

	public void testRunCountQuery() throws Exception {
		SObjectQueryHelper helper = new SObjectQueryHelper();
		
		int count = helper.runCountQuery( testSession,
				"SELECT Count() FROM Contact "
				+ " WHERE LastName='" + LAST_NAME + "'"
				);
		
		assertEquals( FIRST_NAMES.length, count );
	}

	public void testRunQuery2() throws Exception {
		SObjectQueryHelper helper = new SObjectQueryHelper();
		
		List<String[]> list = helper.runQuery2( testSession,
				"SELECT Id, FirstName, LastName, Title FROM Contact "
				+ " WHERE LastName='" + LAST_NAME + "'"
				);
		
		assertEquals( FIRST_NAMES.length, list.size() );
		assertEquals( LAST_NAME, list.get(0)[2]);
		assertNull(list.get(0).clone()[3]);
		
		//
		// Special case -- count records.
		//
		list = helper.runQuery2( testSession,
				"SELECT coUnt() FROM Contact "
				+ " WHERE LastName='" + LAST_NAME + "'"
				);
		
		assertEquals( 1, list.size());
	
	}

	public void testGetDateTimeString() {
		
		SObjectQueryHelper helper = new SObjectQueryHelper();
		
		String dt = helper.getDateTimeString( 1960, 3, 23);
		
		assertEquals("1960-04-23T00:00:00.000Z", dt);
	}

	public void testQuoteSpecial() {
		
		assertEquals( "\\'", SObjectQueryHelper.quoteSpecial("'"));
		assertEquals( "\\\\", SObjectQueryHelper.quoteSpecial("\\"));
		assertNull(SObjectQueryHelper.quoteSpecial((String)null));
	}

	/**
	 * Cancel a query after 2 records are found.
	 * 
	 * @throws Exception
	 */
	public void testCancelWhereQuery() throws Exception {
		SObjectQueryHelper helper = new SObjectQueryHelper();
		
		final List<ContactRecord> contacts = new ArrayList<ContactRecord>();
		ISObjectQueryCallback callback = new DefaultSObjectQueryCallback() {

			

			public void addRow(int rowNumber, SObject sObject) {
				contacts.add( new ContactRecord(sObject));
				
				if( rowNumber == 1 ) { cancel(); }
			}
			
		};
		
		//
		// Find records an cancel after two records are found.
		//
		contacts.clear();
		
		int nFound = helper.findSObjects( testSession, ContactRecord.SOBJECT_NAME,
				new String[] {ContactRecord.F_ID, ContactRecord.F_FIRST_NAME},
				ContactRecord.F_LAST_NAME + "='" + LAST_NAME + "'",
				callback);
		
		
		assertEquals( contacts.size(), 2);
		assertTrue( FIRST_NAMES.length>nFound);
	}
	
	/**
	 * Cancel a search by object id prematurely.
	 * 
	 * @throws Exception
	 */
	public void testCancelIdQuery() throws Exception {
		SObjectQueryHelper helper = new SObjectQueryHelper();
		
		final List<ContactRecord> contacts = new ArrayList<ContactRecord>();
		ISObjectQueryCallback callback = new DefaultSObjectQueryCallback() {

			public void addRow(int rowNumber, SObject sObject) {
				contacts.add( new ContactRecord(sObject));
				if( rowNumber == 1 ) { cancel(); }
			}
			
		};
		
		//
		// Expect to find records
		//
		contacts.clear();
		int nFound = helper.findSObjects( testSession, ContactRecord.SOBJECT_NAME,
				new String[] {ContactRecord.F_ID, ContactRecord.F_FIRST_NAME},
				ContactRecord.F_ID,
				contactIdList,
				callback);
		
		
		assertEquals( 2, nFound);
		assertTrue( contactIdSet.size()> nFound);
		
	}
	
	/**
	 * Cancel a seach that uses an arbitrary query.
	 * 
	 * @throws Exception
	 */
	public void testCancelGeneralQueryCancel() throws Exception {
		SObjectQueryHelper helper = new SObjectQueryHelper();
		
		final List<ContactRecord> contacts = new ArrayList<ContactRecord>();
		ISObjectQueryCallback callback = new DefaultSObjectQueryCallback() {

			public void addRow(int rowNumber, SObject sObject) {
				contacts.add( new ContactRecord(sObject));
				if( rowNumber==1) { cancel(); }
			}
			
		};
		
		int nFound = helper.findRows( testSession, 
				"SELECT Id, FirstName, LastName FROM Contact "
				+ " WHERE LastName='" + LAST_NAME + "'",
				callback);
		
		assertEquals( 2, nFound );
		
		
	}
}
