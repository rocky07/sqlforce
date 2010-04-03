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
package com.aslan.sfdc.sqlforce.command;

import java.util.List;

/**
 * Access to (potentially) many rows.
 * 
 * @author greg
 *
 */
interface IRowCache {

	/**
	 * Fetch the number of columns in each row.
	 * 
	 * @return number of columns in each row.
	 */
	int getNumColumns();
	
	/**
	 * Return the next available row (or null) if no more rows are available.
	 * 
	 * @return next row or null is out of data.
	 */
	List<String> nextRow();
	
}
