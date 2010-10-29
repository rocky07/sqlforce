/*******************************************************************************
 * Copyright (c) 2010 Gregory Smith (gsmithfarmer@gmail.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/

package com.aslan.sfdc.extract;

import java.io.OutputStream;
import java.io.PrintStream;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Implementation of {@link IExtractionMonitor} that writes messages to stderr.
 * 
 * @author greg
 *
 */
public class OutputStreamExtractionMonitor implements IExtractionMonitor {

	private SimpleDateFormat dateFormat = new SimpleDateFormat( "kk:mm:ss");
	private PrintStream logStream;

	public OutputStreamExtractionMonitor( OutputStream outStream ) {
		logStream = new PrintStream( outStream );
	}
	
	public OutputStreamExtractionMonitor() {
		this(System.out);
	}
	
	private void log( String msg ) {
		StringBuffer timeBuffer = new StringBuffer();
		FieldPosition fieldPos = new FieldPosition(0);
		
		dateFormat.format( new Date(), timeBuffer, fieldPos );
		logStream.println( timeBuffer.toString() + ": " + msg );
		logStream.flush();
	}
	
	@Override
	public void reportMessage(String msg) {
		log( msg );
	}

	@Override
	public void copyData(String tableName, int nRowsCopied) {
		log( "....Copying from " + tableName + "(" + nRowsCopied + " so far)");
	}

	@Override
	public void createTable(String name) {
		log( "Create Table - " + name );
	}


	@Override
	public void endCopyData(String tableName, int nRowsRead, int nRowsSkipped,
			int nRowsCopied) {
		log( "End Copy From " + tableName + " (" + nRowsRead + " read, "  + nRowsSkipped + " skipped, "+ nRowsCopied + " copied)" );
	}
	
	@Override
	public void startCopyData(String tableName) {
		log( "Start Copy From " + tableName );
	}

	@Override
	public void readData(String tableName, int nRowsRead) {
	}

	@Override
	public boolean isCancel() {
		return false;
	}

	@Override
	public void skipData(String tableName, int nRowsSkipped) {
	}

	@Override
	public void skipTable(String name) {
		log( "Table Exists - " + name );
	}

}
