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
 * Helper methods for updating  objects in SFDC.
 * @author snort
 *
 */
public class SObjectUpdateHelper {

	private static int UPDATE_CHUNK_SIZE = 200;
	
	public SObjectUpdateHelper() {
		
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
	 * Create an SObject that can be used to update a  record in SFDC.
	 * 
	 * This method scans all set fields in an ISObjectRecord and uses all updateable fields to
	 * create a new SObject.
	 * 
	 * @param session sfdc that will get the updated record.
	 * @param sObjectRecord data for the record we want to update.
	 * @param updateNullFields if true the null valued fields will be sent to the update.
	 * @return SObject that can be passed to SFDC for creation.
	 */
	private SObject makeUpdateableSObject( LoginManager.Session session, ISObjectRecord sObjectRecord, boolean updateNullFields ) {
		List<MessageElement> elementList = new ArrayList<MessageElement>();
		List<String> fieldsToNull = new ArrayList<String>();
		String sObjectName = sObjectRecord.getSObjectName();
		Map<String,String> allFields = sObjectRecord.getAllFields();
		
		for( String key : allFields.keySet() ) {
			if( !session.isFieldAvailable(sObjectName, key)) { continue; }
	
			try {
				Field field = session.getField( sObjectName, key);
				if( !field.isUpdateable()) { continue; };
				
				String value = allFields.get(key);
				if( null == value ) {
					if( updateNullFields) { fieldsToNull.add(key); }
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
				(String) sObjectRecord.getId(),
				elementList.toArray( new MessageElement[0])
				);
		
	}

	
	/**
	 * Update an array of records calling a user supplied callback on each successful or failed row.
	 * 
	 * @param session use this sfdc
	 * @param recordList records to update.
	 * @param callback call this guy once for each row.
	 * @return number of records successfully updated.
	 * @throws Exception if sfdc croaks.
	 */
	public int update( LoginManager.Session session, ISObjectRecord[] recordList, ISObjectUpdateCallback callback ) throws Exception {
		int nUpdated = 0;
		callback.start();
		
		for( int n = 0; n < recordList.length; n += UPDATE_CHUNK_SIZE ) {
			
			if( callback.isCancel()) { break; }
			
			int n2Update = Math.min( UPDATE_CHUNK_SIZE, recordList.length - n );

			SObject sObjects[] = new SObject[n2Update];
			for( int k = 0; k < sObjects.length; k++ ) {
				sObjects[k] = makeUpdateableSObject( session, recordList[n + k], true );
			}
			
			SaveResult batchResults[] = session.getBinding().update( sObjects );
			
			for( int k = 0; k < batchResults.length; k++ ) {
				
				int myRow = n + k;
				SaveResult result = batchResults[k];
				
				if( result.isSuccess() ) {
					callback.update( myRow, recordList[n + k], result);
					nUpdated++;
				} else {
					callback.error(myRow, recordList[n + k], result, result.getErrors()[0].getMessage());
				}
			}
	
		}
		callback.finish();
		return nUpdated;
	}
	
	/**
	 * Update a list of  records in a SFDC database.
	 * 
	 * @param session sfdc instance
	 * @param recordList list of records to update.
	 * @param callback called once for each record updated.
	 * @throws Exception if sfdc croaks.
	 */
	public int update( LoginManager.Session session, List<ISObjectRecord> recordList, ISObjectUpdateCallback callback ) throws Exception {
		return update( session, recordList.toArray(new ISObjectRecord[0]), callback);
	}
	
	/**
	 * Update a list of new records in a SFDC database.
	 * @param recordList
	 * @throws Exception
	 */
	public List<SaveResult> update( LoginManager.Session session, List<ISObjectRecord> recordList ) throws Exception {
		return update( session, recordList.toArray(new ISObjectRecord[0]));
	}
	
	/**
	 * Create a set of new records in a SFDC database.
	 * 
	 * @param recordList
	 * @throws Exception
	 */
	public List<SaveResult> update( LoginManager.Session session, ISObjectRecord[] recordList ) throws Exception {
		final List<SaveResult> saveResults = new ArrayList<SaveResult>();
		
		ISObjectUpdateCallback callback = new DefaultSObjectUpdateCallback() {

			public void error(int rowNumber, ISObjectRecord sObjRecord,
					SaveResult saveResult, String message) {
				saveResults.add(saveResult);
				
			}

			@Override
			public void update(int rowNumber, ISObjectRecord sObjRecord,
					SaveResult saveResult) {
				saveResults.add(saveResult);
				
			}
			
		};
		
		update( session, recordList, callback );
		return saveResults;
	
	}
	
	/**
	 * Update a single record.
	 * 
	 * @param session a SalesForce session.
	 * @param record update this record.
	 * @return result of the update.
	 * @throws Exception if SalesForce chokes.
	 */
	public SaveResult update( LoginManager.Session session, ISObjectRecord record ) throws Exception {
		ISObjectRecord recordList[] = {record};
		
		List<SaveResult> results = update(session, recordList );
		
		return results.get(0);
	}
}
