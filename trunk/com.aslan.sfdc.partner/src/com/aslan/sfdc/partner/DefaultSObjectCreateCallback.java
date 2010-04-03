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
 * Default empty implementation of {@link ISObjectCreateCallback}.
 * 
 * @author gsmithfarmer@gmail.com
 *
 */
public class DefaultSObjectCreateCallback extends DefaultSObjectCommonCallback implements ISObjectCreateCallback {

	
	public void addRow(int rowNumber, String id, SaveResult saveResult) {
	
	}

	
	public void error(int rowNumber, SaveResult saveResult, String message) {
		
	}

}
