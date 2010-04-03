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
import java.util.List;
import java.util.Map;

import org.apache.axis.message.MessageElement;
import org.w3c.dom.Element;

import com.aslan.sfdc.partner.record.ISObjectRecord;
import com.sforce.soap.partner.Field;
import com.sforce.soap.partner.SaveResult;
import com.sforce.soap.partner.sobject.SObject;

/**
 * Helper methods for creating new objects in SFDC.
 * @author snort
 *
 */
public class SObjectCreateHelper {

	private static int CREATE_CHUNK_SIZE = 200;
	
	public SObjectCreateHelper() {
		
	}
	
	private MessageElement newMessageElement(String name, Object value)
	throws Exception {

		MessageElement me = new MessageElement("", name); // , value);
		me.setObjectValue(value);
		Element e = me.getAsDOM();
		e.removeAttribute("xsi:type");
		e.removeAttribute("xmlns:ns1");
		e.removeAttribute("xmlns:xsd");
		e.removeAttribute("xmlns:xsi");

		me = new MessageElement(e);
		return me;
	}
	
	/**
	 * Create an SObject that can be used to create a new record in SFDC.
	 * 
	 * This method scans all set fields in an ISObjectRecord and uses all creatable fields to
	 * create a new SObject.
	 * 
	 * @param session sfdc that will get the new record.
	 * @param sObjectRecord data for the record we want to create.
	 * @param createNullFields if true the null valued fields will be sent to the create, else create will take the default.
	 * @return SObject that can be passed to SFDC for creation.
	 */
	private SObject makeCreatableSObject( LoginManager.Session session, ISObjectRecord sObjectRecord, boolean createNullFields ) {
		List<MessageElement> elementList = new ArrayList<MessageElement>();
		List<String> fieldsToNull = new ArrayList<String>();
		String sObjectName = sObjectRecord.getSObjectName();
		Map<String,String> allFields = sObjectRecord.getAllFields();
		
		for( String key : allFields.keySet() ) {
			if( !session.isFieldAvailable(sObjectName, key)) { continue; }
			
			try {
				Field field = session.getField( sObjectName, key);
				if( !field.isCreateable()) { continue; };
				
				String value = allFields.get(key);
				if( null == value ) {
					if( createNullFields) { fieldsToNull.add(key); }
				} else {
					elementList.add( newMessageElement( key, value ));
				}
				
			} catch (Exception e) {
				; // Will not happen we checked field availability
			}
		}
		
		return new SObject(
				sObjectName,
				fieldsToNull.toArray(new String[0]),
				(String) null, // id
				elementList.toArray( new MessageElement[0])
				);
		
	}
	
	/**
	 * Create a set of records invoking a user supplied callback for each row insert (or failure).
	 * 
	 * @param session use this salesforce
	 * @param recordList records in create
	 * @param callback call for each successful row insert (or failure).
	 * @return number of records successfully created.
	 * @throws Exception if sfdc croaks.
	 */
	public int create(LoginManager.Session session, ISObjectRecord[] recordList, ISObjectCreateCallback callback ) throws Exception { 
		int nCreated = 0;
		
		callback.start();
		
		for( int n = 0; n < recordList.length; n += CREATE_CHUNK_SIZE ) {
			
			if( callback.isCancel()) { break; }
			
			int n2Create = Math.min( CREATE_CHUNK_SIZE, recordList.length - n );
			

			SObject sObjects[] = new SObject[n2Create];
			for( int k = 0; k < sObjects.length; k++ ) {
				sObjects[k] = makeCreatableSObject( session, recordList[n + k], false );
			}
			
			SaveResult batchResults[] = session.getBinding().create( sObjects );
			
			for( int k = 0; k < batchResults.length; k++ ) {
				
				int myRow = n + k;
				SaveResult result = batchResults[k];
				
				if( result.isSuccess() ) {
					callback.addRow( myRow, result.getId(), result);
					nCreated++;
				} else {
					callback.error(myRow,  result, result.getErrors()[0].getMessage());
				}
			}
			
		}
		
		callback.finish();
		return nCreated;
	}
	
	/**
	 * Create a set of records invoking a user supplied callback for each row insert (or failure).
	 * 
	 * @param session use this salesforce
	 * @param recordList records in create
	 * @param callback call for each successful row insert (or failure).
	 * @return number of records successfully created.
	 * @throws Exception if sfdc croaks.
	 */
	public int create(LoginManager.Session session, List<ISObjectRecord> recordList, ISObjectCreateCallback callback ) throws Exception {
		return create( session, recordList.toArray( new ISObjectRecord[0]), callback );
	}
	
	/**
	 * Create a set of new records in a SFDC database.
	 * @param recordList
	 * @throws Exception
	 */
	public List<SaveResult> create( LoginManager.Session session, List<ISObjectRecord> recordList ) throws Exception {
		return create( session, recordList.toArray(new ISObjectRecord[0]));
	}
	
	/**
	 * Create a set of new records in a SFDC database.
	 * 
	 * @param recordList
	 * @throws Exception
	 */
	public List<SaveResult> create( LoginManager.Session session, ISObjectRecord[] recordList ) throws Exception {
		final List<SaveResult> saveResults = new ArrayList<SaveResult>();
		
		ISObjectCreateCallback callback = new DefaultSObjectCreateCallback() {

			@Override
			public void addRow(int rowNumber, String id, SaveResult saveResult) {
				saveResults.add(saveResult );
			}

			@Override
			public void error(int rowNumber, SaveResult saveResult,
					String message) {
				
				saveResults.add(saveResult );
			}
			
		};
		
		create( session, recordList, callback );
		
		return saveResults;
	}
	
	/**
	 * Create a single new record in a SFDC database.
	 * 
	 * @param session write to this SFDC.
	 * @param record the new record.
	 * @return result of the save.
	 * @throws Exception if SalesForce chokes.
	 */
	public SaveResult create( LoginManager.Session session, ISObjectRecord record ) throws Exception {
		List<SaveResult> results = create( session, new ISObjectRecord[] {record});
		
		return results.get(0);
	}
}
