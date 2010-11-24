/*******************************************************************************
 * Copyright (c) 2010 Gregory Smith (gsmithfarmer@gmail.com).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Gregory Smith (gsmithfarmer@gmail.com) - initial API and implementation
 ******************************************************************************//**
 * 
 */
package com.aslan.sfdc.partner.example;

import java.util.List;

import com.aslan.sfdc.partner.DefaultSObjectQuery2Callback;
import com.aslan.sfdc.partner.ISObjectQuery2Callback;
import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.partner.SObjectQueryHelper;

/**
 * Select records from Salesforce.
 * 
 * This example illustrates the most useful javaforce methods for reading records from Salesforce.
 * <p>
 * This example describes how to use {@link com.aslan.sfdc.partner.SObjectQueryHelper} - the primary javaforce helper
 * class for running SOQL queries against Salesforce.
 * 
 * @author gsmithfarmer@gmail.com
 *
 */
public class SelectRecords {

	/**
	 * Run a query where a callback method is invoked for each row found.
	 * 
	 * In this example each row is returned as an array of Strings.
	 * <p>
	 * This approach is ideal to minimize memory footprint.
	 * 
	 * @param session a connection to salesforce
	 * @throws Exception if anything goes wrong.
	 */
	public void selectRowsWithCallback(LoginManager.Session session) throws Exception {
		
		//
		// This is the callback method that will be invoked for each row returned
		// by salesforce. In the return array everything is a string (except nulls -- they
		// are returned as null. The data is ordered in the same order as the supplied SOQL query.
		//
		// Always use the DefaultSObjectQuery2Callback and override methods. The interface has
		// as lot of methods that usually will not need.
		//
		ISObjectQuery2Callback callback = new DefaultSObjectQuery2Callback() {

			@Override
			public void addRow(int rowNumber, String[] data) {
				System.err.println("Callback: Row " + rowNumber + " : " + data[0] + ", " + data[1] );
				
			}
		};
		
		//
		// As long as the SOQL would work in APEX it will work here.
		//
		String sql = "SELECT id, name FROM User";
		
		//
		// Run the query.
		//
		new SObjectQueryHelper().findRows( session, sql, callback);
	}
	
	/**
	 * Run a query and return all rows into a list.
	 * 
	 * In this example we suck all rows returned by a SOLQ query into a list.
	 * <p>
	 * Be careful. Some queries return a lot of rows and the callback approach is more appropriate.
	 * 
	 * @param session a connection to salesforce
	 * @throws Exception if anything goes wrong.
	 */
	public void selectRowsIntoArray(LoginManager.Session session) throws Exception {
		
		String sql = "SELECT id, name FROM User";
		
		List<String[]> results = new SObjectQueryHelper().runQuery2(session, sql);
		
		for( String[] record : results ) {
			System.err.println("Array: Row: " + record[0] + "," + record[1] );
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
			SelectRecords driver = new SelectRecords();
			
			driver.selectRowsWithCallback( session );
			driver.selectRowsIntoArray( session );
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
}
