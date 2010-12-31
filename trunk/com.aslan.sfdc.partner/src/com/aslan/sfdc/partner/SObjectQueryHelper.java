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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.aslan.sfdc.partner.record.ProfileRecord;
import com.aslan.sfdc.partner.record.UserRecord;
import com.sforce.soap.partner.Field;
import com.sforce.soap.partner.QueryResult;
import com.sforce.soap.partner.sobject.SObject;


/**
 * Tool for building queries on SalesForce objects.
 * 
 * 
 * @author snort
 *
 */
public final class SObjectQueryHelper {

	private static SimpleDateFormat sfdcDateTimeMidnightParser = new SimpleDateFormat("yyyy-MM-dd'T00:00:00.000Z'");
	private static Pattern selectCountPattern = Pattern.compile(
			"^\\s*(SELECT\\s+COUNT\\s*\\(\\s*\\)\\s+)(.*)", Pattern.CASE_INSENSITIVE|Pattern.MULTILINE|Pattern.DOTALL);
	
	public SObjectQueryHelper() {}
	
	/**
	 * Find all fields defined for a particular sobject.
	 * 
	 * @param session sfdc database to query.
	 * @param sObjectName find all fields for this guy.
	 * @return list of all field names.
	 * @throws Exception if sfdc croaks.
	 */
	private String[] findFieldNames( LoginManager.Session session, String sObjectName ) throws Exception {
		Field fields[] = session.getDescribeSObjectResult(sObjectName).getFields();
		String names[] = new String[fields.length];
		
		for( int n = 0; n < fields.length; n++ ) {
			names[n] = fields[n].getName();
		}
		
		return names;
	}
	
	/**
	 *  Call a user supplied callback for each record found.
	 * 
	 * @param session sfdc database to query
	 * @param sObjectName table that we will read.
	 * @param fieldList find these fields (null will be the value if the field does not exist or is hidden).
	 * @param whereSQL sql that will be put after the WHERE keyword (to constrain the search. Example: "ID='00023'"
	 * @param callback call this guy when records are found.
	 * @return number of records found
	 * @throws Exception if SFDC throws an error
	 */
	public int findSObjects( LoginManager.Session session, String sObjectName, String[] fieldList, String whereSQL, ISObjectQueryCallback callback ) throws Exception {
		StringBuffer fieldNames = new StringBuffer();
		for( String name : fieldList ) {
			if( session.isFieldAvailable(sObjectName, name)) {
				fieldNames.append( (fieldNames.length()>0?",":"") + name);
			}
		}
		
		if( 0 == fieldNames.length()) {
			throw new Exception("None of the specified fields are defined for " + sObjectName );
		}
		
		String sql = "SELECT " + fieldNames.toString() 
		+ " FROM " + sObjectName 
		+ ((null==whereSQL || 0==whereSQL.trim().length())?"":(" WHERE " + whereSQL))
		;
		
		
		int nRowsFound = 0;
		callback.start();
		
		QueryResult queryResult = session.getBinding().query(sql);
		
		boolean finished = false;
		if( queryResult.getSize() > 0 ) {
			while( !finished ) {
				for( SObject obj : queryResult.getRecords()) {
					callback.addRow( nRowsFound++, obj);
					
					//
					// If the caller cancelled the return of results then
					// return prematurely.
					//
					if( callback.isCancel()) {
						callback.finish();
						return nRowsFound;
					}
				}
				finished = queryResult.isDone();
				if( !finished ) {
					queryResult = session.getBinding().queryMore( queryResult.getQueryLocator());
				}
			}
		}
		
		callback.finish();
		return nRowsFound;
	}
	
	/**
	 *  Call a user supplied callback for each record found (include all fields).
	 * 
	 * @param session sfdc database to query
	 * @param sObjectName table that we will read.
	 * @param whereSQL sql that will be put after the WHERE keyword (to constrain the search. Example: "ID='00023'"
	 * @param callback call this guy when records are found.
	 * @return number of records found
	 * @throws Exception if SFDC throws an error
	 */
	public int findSObjects( LoginManager.Session session, String sObjectName,  String whereSQL, ISObjectQueryCallback callback ) throws Exception {
		String fieldList[] = findFieldNames(session, sObjectName);
		
		return findSObjects( session, sObjectName, fieldList, whereSQL, callback );
		
	}
	/**
	 * Call a user supplied callback for each record found where the search is restricted by record ids.
	 * 
	 * @param session sfdc session to query.
	 * @param sObjectName fetch this type of object.
	 * @param fieldList specific fields to fetch (cannot be empty or null).
	 * @param idField name of the field which contains the Ids in idlist.
	 * @param idList fetch records with these ids.
	 * @param callback call this guy when records are found.
	 * @return number of records found
	 * @throws Exception if sfdc chokes.
	 */
	public int findSObjects(LoginManager.Session session, String sObjectName, String fieldList[], String idField, List<String> idList, ISObjectQueryCallback callback ) throws Exception {
		int chunkSize = 200;
		int nRowsFound = 0;
		
		callback.start();
		
		for( int n = 0; n < idList.size(); n += chunkSize ) {
			StringBuffer sqlWhere = new StringBuffer();
			
			sqlWhere.append( idField + " IN (");
			String comma = "";
			for( int row = n; row < (n + chunkSize) && row < idList.size(); row++) {
				sqlWhere.append( comma + "'" + idList.get(row) + "'");
				comma = ",";
			}
			sqlWhere.append(")");
			List<SObject> list = findSObjectList( session, sObjectName, fieldList, sqlWhere.toString());
			for( SObject obj : list ) {
				callback.addRow( nRowsFound++, obj);
				//
				// If the caller cancelled the return of results then
				// return prematurely.
				//
				if( callback.isCancel()) {
					callback.finish();
					return nRowsFound;
				}
			}
		}
		callback.finish();
		return nRowsFound;
	}
	
	/**
	 * Call a user supplied callback for each record found (include all fields) where the search is restricted by record ids.
	 * 
	 * @param session sfdc session to query.
	 * @param sObjectName fetch this type of object.
	 * @param idField name of the field which contains the Ids in idlist.
	 * @param idList fetch records with these ids.
	 * @param callback call this guy when records are found.
	 * @return number of records found
	 * @throws Exception if sfdc chokes.
	 */
	public int findSObjects(LoginManager.Session session, String sObjectName,  String idField, List<String> idList, ISObjectQueryCallback callback ) throws Exception {
		String fieldList[] = findFieldNames(session, sObjectName);
		
		return findSObjects( session, sObjectName, fieldList, idField, idList, callback );
	}
	/**
	 * Look for a set of records ids in any SFDC SObject using the query interface.
	 * 
	 * @param session sfdc database to query
	 * @param sObjectName table that we will read.
	 * @param whereSQL sql that will be put after the WHERE keyword (to constrain the search. Example: "ID='00023'"
	 * @param callback call this guy when records are found.
	 * @return number of records found
	 * @throws Exception if SFDC throws an error
	 */
	public int findSObjectIds( LoginManager.Session session, String sObjectName, String whereSQL, ISObjectQueryCallback callback) throws Exception {
		
		return findSObjects( session, sObjectName, new String[] {"ID"}, whereSQL, callback );
			
	}
	/**
	 * Look for a set of records in any SFDC SObject using the query interface.
	 * 
	 * @param session sfdc database to query
	 * @param sObjectName table that we will read.
	 * @param fieldList find these fields (null will be the value if the field does not exist or is hidden).
	 * @param whereSQL sql that will be put after the WHERE keyword (to constrain the search. Example: "ID='00023'"
	 * @return list of records found (may be empty)
	 * @throws Exception if SFDC throws an error
	 */
	public List<SObject> findSObjectList( LoginManager.Session session, String sObjectName, String[] fieldList, String whereSQL) throws Exception {
		
		final List<SObject> resultList = new ArrayList<SObject>();
		
		ISObjectQueryCallback callback = new DefaultSObjectQueryCallback() {

			public void addRow(int rowNumber, SObject row) {
				resultList.add(row);
				
			}
			
		};
		
		findSObjects( session, sObjectName, fieldList, whereSQL, callback );
		return resultList;
	}
	
	/**
	 * Look for a set of records ids in any SFDC SObject using the query interface.
	 * 
	 * @param session sfdc database to query
	 * @param sObjectName table that we will read.
	 * @param whereSQL sql that will be put after the WHERE keyword (to constrain the search. Example: "ID='00023'"
	 * @return list of records ids found (may be empty)
	 * @throws Exception if SFDC throws an error
	 */
	public List<String> findSObjectIdList( LoginManager.Session session, String sObjectName, String whereSQL) throws Exception {
		
		final List<String> resultList = new ArrayList<String>();
		
		ISObjectQueryCallback callback = new DefaultSObjectQueryCallback() {

			public void addRow(int rowNumber, SObject row) {
				resultList.add(row.getId());
				
			}
			
		};
		
		findSObjectIds( session, sObjectName, whereSQL, callback );
		
		return resultList;
	}
	/**
	 * Look for a set of records and all fields in each record in any SFDC SObject using the query interface.
	 * 
	 * @param session sfdc database to query
	 * @param sObjectName table that we will read.
	 * @param whereSQL sql that will be put after the WHERE keyword (to constrain the search. Example: "ID='00023'"
	 * @return list of records found (may be empty)
	 * @throws Exception if SFDC throws an error
	 */
	public List<SObject> findSObjectList( LoginManager.Session session, String sObjectName, String whereSQL) throws Exception {
		
		return findSObjectList( session, sObjectName, findFieldNames(session, sObjectName), whereSQL);
		
	}
	/**
	 * Fetch a set of records and all of their fields given their unique ids.
	 * 
	 * @param session sfdc session to query.
	 * @param sObjectName fetch this type of object.
	 * @param idField name of the field which contains the Ids in idlist.
	 * @param idList fetch records with these ids.
	 * @return list of objects fetched
	 * @throws Exception if sfdc chokes.
	 */
	public List<SObject> findSObjectList(LoginManager.Session session, String sObjectName, String idField, List<String> idList ) throws Exception {

		return findSObjectList( session, sObjectName, findFieldNames(session, sObjectName), idField, idList );


	}
	
	/**
	 * Fetch a set of records given their unique ids.
	 * 
	 * @param session sfdc session to query.
	 * @param sObjectName fetch this type of object.
	 * @param fieldList specific fields to fetch (cannot be empty or null).
	 * @param idField name of the field which contains the Ids in idlist.
	 * @param idList fetch records with these ids.
	 * @return list of objects fetched
	 * @throws Exception if sfdc chokes.
	 */
	public List<SObject> findSObjectList(LoginManager.Session session, String sObjectName, String fieldList[], String idField, List<String> idList ) throws Exception {
		final List<SObject> resultList = new ArrayList<SObject>();
		
		ISObjectQueryCallback callback = new DefaultSObjectQueryCallback() {

			public void addRow(int rowNumber, SObject row) {
				resultList.add(row);
				
			}
			
		};
		
		findSObjects(session, sObjectName, fieldList, idField, idList, callback );
		return resultList;
	}
	/**
	 * Find a single SObject and the values of all specified fields where fields that do not exist in the specified
	 * SFDC instance will automatically not included in the query.
	 * 
	 * @param session sfdc session to query.
	 * @param sObjectName table we will search
	 * @param sObjectId specific object we want.
	 * @param fieldList find these fields (null will be the value if the field does not exist or is hidden).
	 * @return the object.
	 * @throws Exception if the record cannot be found.
	 */
	public SObject findSObject( LoginManager.Session session, String sObjectName, String sObjectId, String[] fieldList ) throws Exception {
		
		List<SObject> results = findSObjectList(session, sObjectName,  fieldList, "ID='" + sObjectId + "'");
		
		if( results.size() == 0 ) {
			throw new Exception(sObjectName + " "  + sObjectId + " was not found in the base SFDC instance");
		}
		
		return results.get(0);
		

	}
	
	/**
	 * Find a single SObject and the values of all specified fields where fields that do not exist in the specified
	 * SFDC instance will automatically no included in the query.
	 * 
	 * @param session sfdc session to query.
	 * @param sObjectName table we will search
	 * @param sObjectId specific object we want.
	 * @param fieldList find these fields (null will be the value if the field does not exist or is hidden).
	 * @return the object.
	 * @throws Exception if the record cannot be found.
	 */
	public SObject findSObject( LoginManager.Session session, String sObjectName, String sObjectId, List<String> fieldList ) throws Exception {
		return findSObject( session, sObjectName, sObjectId, fieldList.toArray(new String[0]));
	}
	
	/**
	 * Find a single SObject and the values of all of its fields.
	 * 
	* @param session sfdc session to query.
	 * @param sObjectName table we will search
	 * @param sObjectId specific object we want.
	 * @return the object.
	 * @throws Exception if the record cannot be found.
	 */
	public SObject findSObject( LoginManager.Session session, String sObjectName, String sObjectId) throws Exception {
		
		return findSObject( session, sObjectName, sObjectId, findFieldNames(session, sObjectName) );
	}
	
	/**
	 * Return all users defined for a particular database.
	 * 
	 * @param session look in this database.
	 * @return list of users.
	 * @throws Exception if SFDC fails.
	 */
	public List<UserRecord> getUserRecords( LoginManager.Session session ) throws Exception {
		List<SObject> sList = findSObjectList(session, UserRecord.SOBJECT_NAME, (String)null);
		
		List<UserRecord> users = new ArrayList<UserRecord>( sList.size());
		
		for( SObject sObj : sList ) {
			users.add( new UserRecord(sObj));
		}
		return users;
	}
	
	/**
	 * Return all profiles defined for a particular database.
	 * 
	 * @param session look in this database.
	 * @return list of profiles.
	 * @throws Exception if SFDC fails.
	 */
	public List<ProfileRecord> getProfileRecords( LoginManager.Session session ) throws Exception {
		List<SObject> sList = findSObjectList(session, ProfileRecord.SOBJECT_NAME, (String)null);
		
		List<ProfileRecord> list = new ArrayList<ProfileRecord>( sList.size());
		
		for( SObject sObj : sList ) {
			list.add( new ProfileRecord(sObj));
		}
		return list;
	}
	
	/**
	 * Run any query call call user supplied function for each row found.
	 * 
	 * @param session query this sfdc database
	 * @param query run this query
	 * @param callback call this class for each row found.
	 * @return number of rows fetched.
	 * @throws Exception if sfdc croaks of the query is illformed.
	 */
	public int findRows( LoginManager.Session session, String query, ISObjectQueryCallback callback ) throws Exception {
		QueryResult queryResult = session.getBinding().query(query);
		int nRowsFound = 0;
		
		
		boolean finished = false;
		callback.start();
		
		if( queryResult.getSize() > 0 ) {
			while( !finished ) {
				for( SObject obj : queryResult.getRecords()) {
					callback.addRow( nRowsFound++, obj);
					//
					// If the caller cancelled the return of results then
					// return prematurely.
					//
					if( callback.isCancel()) {
						callback.finish();
						return nRowsFound;
					}
				}
				finished = queryResult.isDone();
				if( !finished ) {

					queryResult = session.getBinding().queryMore( queryResult.getQueryLocator());
	
					System.gc();
				}
			}
		}
		
		callback.finish();
		return nRowsFound;
	}
	
	/**
	 * Run any query call call user supplied function for each row found when the call simply wants the raw column data returned.
	 * 
	 * @param session query this sfdc database
	 * @param query run this query
	 * @param callback call this class for each row found.
	 * @return number of rows fetched.
	 * @throws Exception if sfdc croaks of the query is ill formed.
	 */
	public int findRows( LoginManager.Session session, String query, final ISObjectQuery2Callback callback ) throws Exception {
		
		//
		// Special case -- SELECT COUNT()
		//
		Matcher matcher = selectCountPattern.matcher(query);
		if( matcher.matches()) {
			String[] result = new String[] { Integer.toString( runCountQuery( session, query))};
			callback.addRow(0, result);
			return 1;
		}

		class MyCallback extends DefaultSObjectQueryCallback {
			private QuerySObjectParser rowParser;
			
			MyCallback( String sql ) throws Exception {
				rowParser = new QuerySObjectParser(sql);
			}
			@Override
			public void finish() {
				super.finish();
				callback.finish();
			}

			@Override
			public void start() {
				super.start();
				callback.start();
			}

			public void addRow(int rowNumber, SObject sObject) {
				
				String data[] = rowParser.parseRow( sObject );
				callback.addRow( rowNumber, data);
				

				if( callback.isCancel()) {
					cancel();
				}
			}
		}
		//
		// Regular query that will return rows.
		//
		ISObjectQueryCallback rawCallback = new MyCallback(query);

		
		return findRows( session, query, rawCallback );
		
	}
	/**
	 * Run a query and return all rows in a single list.
	 * 
	 * @param session use this sfdc database.
	 * @param query run thsi query.
	 * @return list of rows found.
	 * @throws Exception if sdfc croaks or the query is illformed.
	 */
	public List<SObject> runQuery( LoginManager.Session session, String query ) throws Exception {
		final List<SObject> resultList = new ArrayList<SObject>();
		
		ISObjectQueryCallback callback = new DefaultSObjectQueryCallback() {

			public void addRow(int rowNumber, SObject row) {
				resultList.add(row);
				
			}
			
		};
		
		findRows(session, query, callback );
		return resultList;
	}
	
	
	/**
	 * Run a query that is counting the number of records in a table.
	 * 
	 * @param session talk to this SFDC
	 * @param query run this query (must be a SELECT COUNT() query);
	 * @return number of records found.
	 * @throws Exception if running the query fails.
	 */
	public int runCountQuery( LoginManager.Session session, String query ) throws Exception {
		QueryResult queryResult = session.getBinding().query(query);
		
		return queryResult.getSize();
		
	}
	/**
	 * Run any query and result to results as a list where each list member is an array of values for one row from the query.
	 * 
	 * @param session talk to this SFDC
	 * @param query run this query.
	 * @return results (may be an empty list).
	 * @throws Exception if running the query fails.
	 */
	public List<String[]> runQuery2( LoginManager.Session session, String query ) throws Exception {
		final List<String[]> resultList = new ArrayList<String[]>();
		
		ISObjectQuery2Callback callback = new DefaultSObjectQuery2Callback() {

			public void addRow(int rowNumber, String[] row) {
				resultList.add(row);
				
			}
			
		};
		
		findRows( session, query, callback );
		return resultList;
		
	}

	/**
	 * Return a Date/Time string if a form suitable for a SalesForce query.
	 * 
	 * @param year the year
	 * @param month the month
	 * @param day the day
	 * @return a date string whose time is midnight.
	 */
	public String getDateTimeString( int year, int month, int day ) {
		Calendar cal = Calendar.getInstance();
		
		cal.set(year, month, day);
		return sfdcDateTimeMidnightParser.format(cal.getTime());
		
	}
	
	/**
	 * Quote all "special" characters in a query.
	 * 
	 * @param orig starting starting
	 * @return orig with stuff like single quotes backslashes quoted.
	 */
	public static String quoteSpecial( String orig ) {
		if( null == orig  ) { return null; }
		
		return orig.replace( "\\", "\\\\").replace( "'", "\\'");
	}
}
