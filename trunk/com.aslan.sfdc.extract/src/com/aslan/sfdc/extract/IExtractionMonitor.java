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
	 * Called at the very beginning of the extaction process.
	 * 
	 * This happens before any database connections are attempted.
	 */
	void start();
	
	/**
	 * Called when the extraction process is complete.
	 * 
	 * @param e if an error occurred e will be non-null; on success this value will be null.
	 */
	void end(Exception e);
	
	/**
	 * Called just before the system starts creating target database schema.
	 */
	void startSchema();
	
	/**
	 * Called just after the system finishes creating target database schema.
	 */
	void endSchema();
	
	/**
	 * Called just before the system starts copying data from SFDC tables to the destination.
	 */
	void startTables();
	
	/**
	 * Called just after the system finishes copying data from SFDC tables to the destination.
	 */
	void endTables();
	
	/**
	 * Report just after a table is created.
	 * @param name a table
	 */
	void createTable( String name );
	
	/**
	 * Report when it is determined that a table already exists in the destination database.
	 * 
	 * @param name a table
	 */
	void skipTable( String name );
	
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
	 * Report each time data is skipped because the destination database is already up to date.
	 * 
	 * @param tableName a table
	 * @param nRowsSkipped total number of rows skipped for this table (cumulative).
	 */
	void skipData( String tableName, int nRowsSkipped );
	
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
	 * @param nRowsRead total number of records read from salesforce.
	 * @param nRowsSkipped total number of records read but not copied (already up to date).
	 * @param nRowsCopied total number of rows copied to a table.
	 */
	void endCopyData( String tableName, int nRowsRead, int nRowsSkipped, int nRowsCopied );
	
	/**
	 * Report progress to a listener.
	 * 
	 * @param msg indication of progress readable by a human.
	 */
	void reportMessage( String msg );
	
	/**
	 * If true the cancel the current run in an orderly fashion.
	 * 
	 * @return true if current run should be cancelled.
	 */
	boolean isCancel();
}
