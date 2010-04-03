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
package com.aslan.sfdc.partner;

import com.sforce.soap.partner.DeleteResult;

/**
 * Callback used by {@link SObjectDeleteHelper} methods.
 * 
 * @author greg
 *
 */
public interface ISObjectDeleteCallback extends ISObjectCommonCallback {
	
	/**
	 * Called for each successfully deleted record.
	 * 
	 * @param rowNumber row number (starting from zero) passed to a delete method.
	 * @param id the record that was deleted.
	 * @param deleteResult the result returned from salesforce.
	 */
	void delete( int rowNumber, String id, DeleteResult deleteResult );
	
	/**
	 * Called for each record that fails to delete.
	 * 
	 * @param rowNumber row number (starting from zero) passed to a delete method.
	 * @param id the record that failed to delete.
	 * @param deleteResult the result returned from salesforce.
	 * @param message the error message (extracted from saveResult).
	 */
	void error( int rowNumber, String id, DeleteResult deleteResult, String message );
}
