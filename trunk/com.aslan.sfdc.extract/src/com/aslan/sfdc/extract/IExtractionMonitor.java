/*******************************************************************************
 * Copyright (c) 2010 Gregory Smith (gsmithfarmer@gmail.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/

package com.aslan.sfdc.extract;

/**
 * Report on the progress of extraction of data from Salesforce.
 * 
 * @author greg
 *
 */
public interface IExtractionMonitor {

	/**
	 * Report progress to a listener.
	 * 
	 * @param msg indication of progress readable by a human.
	 */
	void reportMessage( String msg );
}
