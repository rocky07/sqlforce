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
package com.aslan.sfdc.sqlforce.command;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * RowCache that keeps all rows in a temporary disk file.
 * 
 * @author greg
 *
 */
class DiskRowCache implements IRowCache {

	private int numColumns = 0;
	File tmpFile;
	ObjectOutputStream writeStream;
	ObjectInputStream readStream = null;
	
	public DiskRowCache(  ) throws IOException {
		
		tmpFile = File.createTempFile("DCache", ".tmp");
		tmpFile.deleteOnExit();
		writeStream = new ObjectOutputStream( new FileOutputStream(tmpFile));
	}
	
	private void switchToReadStream() {
		if( null == writeStream ) { return; }
		
		try {
			writeStream.close();
			writeStream = null;
			readStream = new ObjectInputStream( new FileInputStream(tmpFile));
		} catch (IOException e) {
			e.printStackTrace();
			;
		}

	}
	public int getNumColumns() {
		return numColumns;
	}

	public void addRow( List<String> row ) throws Exception {
		if( 0 == numColumns) {
			numColumns = row.size();
		}
		
		if( row.size() != numColumns )  {
			throw new IllegalArgumentException("Row should have " + numColumns + " but has " + row.size() + " columns");
		}
		
		if( null == writeStream ) {
			throw new Exception( "Rows cannot be added once the first row is read via nextRow()");
		}
		writeStream.writeObject( row );
	}
	
	@SuppressWarnings("unchecked")
	private  List<String> readObject() {
		try {
			List<String> row = (List<String>) readStream.readObject();
			return row;
		} catch (Exception e) {

			try {
				readStream.close();
				tmpFile.delete();
			} catch (IOException e1) {
				;
			}
			readStream = null;
			return null;
		} 
	}
	public List<String> nextRow() {
		
		switchToReadStream();
		
		if( null == readStream) { return null; }
		
		return readObject();
		
	}
	
}
