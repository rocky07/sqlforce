/*******************************************************************************
 * Copyright (c) 2010 Gregory Smith (gsmithfarmer@gmail.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/

package com.aslan.sfdc.extract;

/**
 * Report on the progress of extraction of data from Salesforce.
 * 
 * @author greg
 *
 */
public interface IExtractionMonitor {


	/**
	 * Report just after a table is created.
	 * @param name a table
	 */
	void createTable( String name );
	
	
	/**
	 * Report just before data is copied to a table.
	 * 
	 * @param tableName a table.
	 */
	void startCopyData( String tableName );
	
	/**
	 * Report each time data is copied to a table.
	 * 
	 * @param tableName a table
	 * @param nRowsCopied total number of rows copied to the table.
	 */
	void copyData( String tableName, int nRowsCopied );
	
	/**
	 * Report the number of rows read from Salesforce including those not yet copied to the destination database.
	 * 
	 * @param tableName a table
	 * @param nRowsRead number of rows read.
	 */
	void readData( String tableName, int nRowsRead );

	/**
	 * Report after all rows have been copied to a table.
	 * 
	 * @param tableName a table
	 * @param nRowsCopied total number of rows copied to a table.
	 */
	void endCopyData( String tableName, int nRowsCopied );
	
	/**
	 * Report progress to a listener.
	 * 
	 * @param msg indication of progress readable by a human.
	 */
	void reportMessage( String msg );
}
