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
 * Default implementation of an {@link IExtractionMonitor}.
 * 
 * @author greg
 *
 */
public class DefaultExtractionMonitor implements IExtractionMonitor {


	@Override
	public void reportMessage(String msg) {

	}

	@Override
	public void copyData(String tableName, int nRowsCopied) {
		
	}

	@Override
	public void createTable(String name) {
	}

	@Override
	public void endCopyData(String tableName, int nRowsCopied) {
	}

	@Override
	public void startCopyData(String tableName) {
	}

	@Override
	public void readData(String tableName, int nRowsRead) {
	}

	@Override
	public boolean isCancel() {
		return false;
	}

	@Override
	public void skipData(String tableName, int nRowsSkipped) {
	}

	@Override
	public void skipTable(String name) {
	}

}
