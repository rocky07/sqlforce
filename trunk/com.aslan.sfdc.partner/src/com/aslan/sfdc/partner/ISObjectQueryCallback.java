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

import com.sforce.soap.partner.sobject.SObject;

/**
 * Callback method invoked for each row returned from a query.
 * 
 * @author greg
 *
 */
public interface ISObjectQueryCallback extends ISObjectCommonCallback{
	
	/**
	 * Called each time a row is returned from a query.
	 * 
	 * @param rowNumber row index (starting with zero).
	 * @param sObject the row found.
	 */
	void addRow( int rowNumber, SObject sObject );

	
	
}
