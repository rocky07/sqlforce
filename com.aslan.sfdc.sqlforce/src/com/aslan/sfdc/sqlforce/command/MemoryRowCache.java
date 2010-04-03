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

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * RowCache that keeps all rows in memory.
 * 
 * @author greg
 *
 */
class MemoryRowCache implements IRowCache {

	private int numColumns = 0;
	private Queue<List<String>> allRows = new LinkedList<List<String>>();
	
	public MemoryRowCache(  ) {
		
	}
	public int getNumColumns() {
		return numColumns;
	}

	public void addRow( List<String> row ) {
		if( 0 == allRows.size()) {
			numColumns = row.size();
		}
		
		if( row.size() != numColumns )  {
			throw new IllegalArgumentException("Row should have " + numColumns + " but has " + row.size() + " columns");
		}
		allRows.add( row );
	}
	
	public List<String> nextRow() {
		return allRows.isEmpty()?null:allRows.remove();
	}
	
}
