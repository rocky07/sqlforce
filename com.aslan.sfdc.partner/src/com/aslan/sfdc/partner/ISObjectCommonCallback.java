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


/**
 * Methods common to all SObject Helper callbacks.
 * 
 * @author greg
 *
 */
public interface ISObjectCommonCallback {

	/**
	 * Called just before the first row is effected.
	 */
	void start();
	
	/**
	 * Called just after the last row is effected.
	 */
	void finish();
	
	/**
	 * Cancel the current operation after any active batch is processed.
	 * 
	 * For a query, this will immediately cause the current operation to stop.
	 * For database update operations, the status for any data already processed (but not reported
	 * via th callback) will be processed before the cancel is honored.
	 */
	void cancel();
	
	/**
	 * Determine if the user has requested for the current operation to stop before it is finished.
	 * 
	 * @return true if we are to stop, else false.
	 */
	boolean isCancel();
}
