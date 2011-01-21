/*******************************************************************************
 * Copyright (c) 2010 Gregory Smith (gsmithfarmer@gmail.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package com.aslan.sfdc.extract;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * Report the progress of an extraction in a top level frame.
 * 
 * Warning: When the frame is closed, the application will exit.
 * 
 * To embed the same monitoring in a JPanel see {@link SwingExtractionMonitorJPanel}.
 * @author snort
 *
 */
public class SwingExtractionMonitor implements IExtractionMonitor {


	private JFrame frame = new JFrame( "CopyForce Progress Monitor");
	private SwingExtractionMonitorJPanel subMonitor;

	public SwingExtractionMonitor() {
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent ev) {
				System.exit(0);
			}
		});
		
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
	
	@Override
	public void start() {
		subMonitor.start();
	}

	@Override
	public void end(Exception e) {
		subMonitor.end(e);
	}

	@Override
	public void startSchema() {
		subMonitor.startSchema();
	}

	@Override
	public void endSchema() {
		subMonitor.endSchema();
	}

	@Override
	public void startTables() {
		subMonitor.startTables();
	}

	@Override
	public void endTables() {
		subMonitor.endTables();
	}

}
