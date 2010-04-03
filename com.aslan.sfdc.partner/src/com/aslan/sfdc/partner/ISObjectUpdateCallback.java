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

import com.aslan.sfdc.partner.record.ISObjectRecord;
import com.sforce.soap.partner.SaveResult;

/**
 * Callback used by {@link SObjectUpdateHelper} methods.
 * 
 * @author greg
 *
 */
public interface ISObjectUpdateCallback extends ISObjectCommonCallback {

	/**
	 * Called for each successfully updated record.
	 * 
	 * @param rowNumber row number (starting from zero) passed to an update method.
	 * @param sObjRecord the record used for the update.
	 * @param saveResult the result returned from salesforce.
	 */
	void update( int rowNumber, ISObjectRecord sObjRecord, SaveResult saveResult );
	
	/**
	 * Called for each record that fails to update.
	 * 
	 * @param rowNumber row number (starting from zero) passed to an update method.
	 * @param sObjRecord the record that failed to update.
	 * @param saveResult the result returned from salesforce.
	 * @param message the error message (extracted from saveResult).
	 */
	void error( int rowNumber, ISObjectRecord sObjRecord, SaveResult saveResult, String message );
}
