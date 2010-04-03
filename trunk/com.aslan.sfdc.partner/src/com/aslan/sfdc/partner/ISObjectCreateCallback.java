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

import com.sforce.soap.partner.SaveResult;

/**
 * Callback used by {@link SObjectCreateHelper} methods.
 * 
 * @author greg
 *
 */
public interface ISObjectCreateCallback extends ISObjectCommonCallback {

	/**
	 * Called for each successfully inserted record.
	 * 
	 * @param rowNumber row number (starting from zero) inserted
	 * @param id the record that was inserted.
	 * @param saveResult the result returned from salesforce.
	 */
	void addRow( int rowNumber, String id, SaveResult saveResult );
	
	/**
	 * Called for each record that fails to delete.
	 * 
	 * @param rowNumber row number (starting from zero) inserted
	 * @param saveResult the result returned from salesforce.
	 * @param message the error message (extracted from saveResult).
	 */
	void error( int rowNumber, SaveResult saveResult, String message );
}
