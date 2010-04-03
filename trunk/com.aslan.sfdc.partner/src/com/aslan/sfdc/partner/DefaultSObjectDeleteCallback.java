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
 * Default empty implementation of {@link ISObjectDeleteCallback}.
 * 
 * @author gsmithfarmer@gmail.com
 *
 */
public class DefaultSObjectDeleteCallback extends DefaultSObjectCommonCallback implements ISObjectDeleteCallback {

	
	public void delete(int rowNumber, String id, DeleteResult deleteResult) {

	}

	public void error(int rowNumber, String id, DeleteResult deleteResult,
			String message) {

	}

}
