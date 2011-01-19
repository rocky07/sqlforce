/*******************************************************************************
 * Copyright (c) 2010 Gregory Smith (gsmithfarmer@gmail.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package com.aslan.sfdc.extract;

import javax.swing.JFrame;

/**
 * @author snort
 *
 */
public class SwingExtractionMonitor implements IExtractionMonitor {


	private JFrame frame = new JFrame( "CopyForce Progress Monitor");
	private SwingExtractionMonitorJPanel subMonitor;

	public SwingExtractionMonitor() {
		
		subMonitor = new SwingExtractionMonitorJPanel();
		frame.getContentPane().add(subMonitor);
		frame.pack();
		frame.setVisible(true);
	}

	@Override
	public void createTable(String name) {
		subMonitor.createTable(name);
	}

	@Override
	public void skipTable(String name) {
		subMonitor.skipTable(name);
		
	}

	@Override
	public void startCopyData(String tableName) {
		subMonitor.startCopyData(tableName);
	}

	@Override
	public void copyData(String tableName, int nRowsCopied) {
		subMonitor.copyData(tableName, nRowsCopied);
		
	}

	@Override
	public void skipData(String tableName, int nRowsSkipped) {
		subMonitor.skipData(tableName, nRowsSkipped);
		
	}

	@Override
	public void readData(String tableName, int nRowsRead) {
		subMonitor.readData(tableName, nRowsRead);
		
	}

	@Override
	public void endCopyData(String tableName, int nRowsRead, int nRowsSkipped,
			int nRowsCopied) {
		subMonitor.endCopyData(tableName, nRowsRead, nRowsSkipped, nRowsCopied);
		
	}

	@Override
	public void reportMessage(String msg) {
		subMonitor.reportMessage(msg);
		
	}

	@Override
	public boolean isCancel() {
		return subMonitor.isCancel();
	}
	


}
