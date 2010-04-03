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

import com.sforce.soap.partner.DeleteResult;
import com.sforce.soap.partner.sobject.SObject;

/**
 * Helper methods for deleting objects in SFDC.
 * 
 * @author snort
 *
 */
public class SObjectDeleteHelper {

	private static int DELETE_CHUNK_SIZE = 200;
	
	public SObjectDeleteHelper() {
		
	}
	
	/**
	 * Delete a series of records invoking a user supplied callback for each record deleted.
	 * 
	 * @param session connect to this salesforce
	 * @param idList list of unique record ids to delete
	 * @param callback call for each record deleted (or failed to delete).
	 * @return number of records successfully deleted.
	 * @throws Exception if sfdc croaks.
	 */
	public int delete( LoginManager.Session session, List<String> idList, ISObjectDeleteCallback callback ) throws Exception {
		int nDeleted = 0;
		
		callback.start();
		
		for( int n = 0; n < idList.size(); n += DELETE_CHUNK_SIZE ) {
			int n2Delete = Math.min( DELETE_CHUNK_SIZE, idList.size() - n );

			String idChunk[] = new String[n2Delete];
			for( int k = 0; k < idChunk.length; k++ ) {
				idChunk[k] = idList.get(n + k) ;
				
			}
			
			if( callback.isCancel()) { break; }
			
			DeleteResult batchResults[] = session.getBinding().delete( idChunk );
			session.getBinding().emptyRecycleBin(idChunk); // Remove from the recycle bin.
			
			for( int k = 0; k < batchResults.length; k++ ) {
				
				int myRow = n + k;
				DeleteResult result = batchResults[k];
				String myId = idList.get(n+k);
				
				if( result.isSuccess() ) {
					callback.delete( myRow, myId, result);
					nDeleted++;
				} else {
					callback.error(myRow, myId, result, result.getErrors()[0].getMessage());
				}
				
			}
	
		}
		
		callback.finish();
		return nDeleted;
	}
	
	/**
	 * Delete all records from a table that match records returned by a WHERE statement.
	 * 
	 * @param session use this salesforce database.
	 * @param sObjectType delete objects of this type
	 * @param sqlWhere use this expression to find objects.
	 * @param callback call for each record deleted (or failed to delete).
	 * @return number of records successfully deleted.
	 * @throws Exception if sfdc croaks
	 */
	public int deleteWhere( LoginManager.Session session, String sObjectType, String sqlWhere, ISObjectDeleteCallback callback  ) throws Exception {
		
		class SelectForDeleteCallback extends DefaultSObjectQueryCallback {
			
			private LoginManager.Session session;
			private ISObjectDeleteCallback callback;
			
			private List<String> pendingIds = new ArrayList<String>();
			private SObjectDeleteHelper deleteHelper = new SObjectDeleteHelper();
			private int rowCounter = 0;
			private int nDeleted = 0;
			
			private ISObjectDeleteCallback deleteCallback = new DefaultSObjectDeleteCallback() {
				
				public void delete(int rowNumber, String id,
						DeleteResult deleteResult) {
					
					rowCounter++;
					nDeleted++;
					callback.delete( rowCounter, id, deleteResult );
					
				}

				public void error(int rowNumber, String id,
						DeleteResult deleteResult, String message) {
					
					rowCounter++;
					callback.error( rowCounter, id, deleteResult, message );
					
				}
			};
			
			SelectForDeleteCallback( LoginManager.Session session, ISObjectDeleteCallback callback ) {
				this.session = session;
				this.callback = callback;
			}
			
			private void deletePending() {
				if( 0 == pendingIds.size() ) { return; }
				
				try {
					deleteHelper.delete( session, pendingIds, deleteCallback );
				} catch (Exception e) {
					//
					// Something bad happened with SalesForce. We will not be able to recover.
					//
					throw new Error( e.getMessage());
				}
				pendingIds.clear();
			}
			
			public int getNDeleted() { return nDeleted; }
			@Override
			public void addRow(int rowNumber, SObject sObject) {
	
				pendingIds.add( sObject.getId());
				
				if( DELETE_CHUNK_SIZE == pendingIds.size()) { deletePending(); }
			}

			@Override
			public void finish() {
				deletePending();
				super.finish();
			}
			
		}
		SelectForDeleteCallback selectForDeleteCallback = new SelectForDeleteCallback( session, callback );
		callback.start();
		SObjectQueryHelper queryHelper = new SObjectQueryHelper();
		
		queryHelper.findSObjectIds(session, sObjectType, sqlWhere, selectForDeleteCallback);
		
		callback.finish();
		
		return selectForDeleteCallback.getNDeleted();
	}

	/**
	 * Delete a bunch of records and report the status of each delete.
	 * 
	 * @param session delete from this SFDC Session.
	 * @param idList list of ids to delete.
	 * @return list of deletion results in the same order as the provide idList
	 * @throws Exception only if SFDC throws an error (no error will be thrown if a delete for a particular record fails).
	 */
	public List<DeleteResult> deleteWithStatus(LoginManager.Session session, List<String> idList ) throws Exception {
		
		final List<DeleteResult> deleteResults = new ArrayList<DeleteResult>();
		
		ISObjectDeleteCallback callback = new DefaultSObjectDeleteCallback() {

			public void delete(int rowNumber, String id,
					DeleteResult deleteResult) {
				
				deleteResults.add(deleteResult);
				
			}

			public void error(int rowNumber, String id,
					DeleteResult deleteResult, String message) {
				deleteResults.add(deleteResult);
				
			}
			
		};
		
		delete( session, idList, callback );
		
		return deleteResults;
	}

	public List<DeleteResult> deleteWhereWithStatus( LoginManager.Session session, String sObjectType, String sqlWhere ) throws Exception {
		final List<DeleteResult> deleteResults = new ArrayList<DeleteResult>();
		
		ISObjectDeleteCallback callback = new DefaultSObjectDeleteCallback() {

			public void delete(int rowNumber, String id,
					DeleteResult deleteResult) {
				
				deleteResults.add(deleteResult);
				
			}

			public void error(int rowNumber, String id,
					DeleteResult deleteResult, String message) {
				deleteResults.add(deleteResult);
				
			}
			
		};
		
		deleteWhere( session, sObjectType, sqlWhere, callback );
		
		return deleteResults;
	}
	
	/**
	 * Delete a set of records and throw an exception if any of the deletes fail.
	 * 
	 * @param session delete from this SFDC session.
	 * @param idList delete these ids.
	 * @throws Exception if any delete fails or if SFDC throws an exception.
	 */
	public void delete(LoginManager.Session session, List<String> idList ) throws Exception {
		
		List<DeleteResult> deleteResults = deleteWithStatus( session, idList );
		
		for( int n = 0; n < deleteResults.size(); n++ ) {
			DeleteResult result = deleteResults.get(n);
			
			if( !result.isSuccess() ) {
				throw new Exception("Failed to delete record: " + idList.get(n) + ": " 
						+ result.getErrors()[0].getMessage());
			}
		}
		
	}
	
	/**
	 * Delete a single record throwing an exception if the delete fails.
	 * 
	 * @param session use this salesforce.
	 * @param id delete this record.
	 * @throws Exception if record cannot be deleted.
	 */
	public void delete( LoginManager.Session session, String id ) throws Exception {
		List<String> idList = new ArrayList<String>();
		idList.add(id);
		
		DeleteResult result = deleteWithStatus( session, idList ).get(0);
		if( !result.isSuccess() ) {
			throw new Exception("Failed to delete record: " + id + ": " 
					+ result.getErrors()[0].getMessage());
		}
	}
	/**
	 * Delete all records associate in a particular SFDC Table.
	 * 
	 * @param session delete from this sfdc
	 * @param sObjectType delete all records from this table.
	 * @throws Exception if any records were not deleted. You will have to scan the table to see which records were not deleted.
	 */
	public void deleteAll( LoginManager.Session session, String sObjectType ) throws Exception {
		
		deleteWhere( session, sObjectType, (String)null);
		
	}
	
	/**
	 * Delete all records associate in a particular SFDC Table.
	 * 
	 * @param session delete from this sfdc
	 * @param sObjectType delete records from this table.
	 * @param sqlWhere delete records returned by this clause on the table.
	 * @throws Exception if any records were not deleted. You will have to scan the table to see which records were not deleted.
	 */
	public void deleteWhere( LoginManager.Session session, String sObjectType, String sqlWhere ) throws Exception {
		
		List<DeleteResult> resultList = deleteWhereWithStatus( session, sObjectType, sqlWhere );
		
		for( DeleteResult result : resultList ) {
			if( !result.isSuccess() ) {
				throw new Exception( result.getErrors()[0].getMessage());
			}
		}
		
	}
	
}
