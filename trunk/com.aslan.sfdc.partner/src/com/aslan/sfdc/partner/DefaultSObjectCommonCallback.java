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
 * Default  implementation of {@link ISObjectCommonCallback} that manages operation cancel logic.
 * 
 * All implementations of SObject callbacks in this package extend thsi class.
 * 
 * @author gsmithfarmer@gmail.com
 *
 */
public class DefaultSObjectCommonCallback implements ISObjectCommonCallback {

	private boolean isCancel = false;
	
	public void cancel() {
		isCancel = true;
	}

	public void finish() {

	}

	public void start() {
		isCancel = false;
	}

	public boolean isCancel() {
		
		return isCancel;
	}

}
