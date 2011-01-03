/*******************************************************************************
 * Copyright (c) 2010 Gregory Smith (gsmithfarmer@gmail.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package com.aslan.sfdc.extract;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

/**
 * @author snort
 *
 */
public class SwingExtractionMonitor implements IExtractionMonitor {

	private class SalesforceTable {
		private String name;
		private String action = "";
		private Integer nRowsCopied = 0;
		private Integer nRowsRead = 0;
		private Integer nRowsSkipped = 0;
		private Calendar copyStartTime;
		private Calendar copyEndTime;
		
		SalesforceTable( String name ) {
			this.name = name;
		}
	}
	

	private class SalesforceTableModel extends AbstractTableModel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private static final int ACTION_INDEX = 0;
		private static final int NAME_INDEX = 1;
		private static final int NREAD_INDEX = 2;
		private static final int NSKIPPED_INDEX = 3;
		private static final int NROWS_INDEX = 4;
		private static final int COPY_START_INDEX = 5;
		private static final int ELAPSED_INDEX = 6;
		private String[] columnNames = {"Action", "Table Name", "#Read", "#Skipped", "#Copied", "Copy Start", "Elapsed Time"};
		private DateFormat dateFormat = DateFormat.getTimeInstance();
		
		
		private String getTimeDiff( Calendar epoch, Calendar now  ) {
			
			long msDiff = now.getTime().getTime() - epoch.getTime().getTime();
			
			Long ms = msDiff%1000;
			Long seconds = (msDiff/1000)%60;
			Long minutes = (msDiff/(1000*60))%60;
			Long hours = (msDiff/(1000*60*60));
			
			if( hours > 0 ) {
				return String.format("%1$dh %2$dm %3$ds", hours, minutes, seconds );
			}
			
			if( minutes > 0 ) {
				return String.format("%1$dm %2$ds", minutes, seconds );
			}
			
			return String.format("%1$d.%2$ds", seconds, ms );
		}
		@Override
		public int getColumnCount() {
			return columnNames.length;
		}
		
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			switch( columnIndex ) {
			case ACTION_INDEX:
			case NAME_INDEX:
			case COPY_START_INDEX:
			case ELAPSED_INDEX:
				return String.class;
				
			case NROWS_INDEX:
			case NREAD_INDEX:
			case NSKIPPED_INDEX:
				return Integer.class;
			default:
				return Object.class;
			}
		}

		@Override
		public String getColumnName(int column) {
			return columnNames[column];
		}

		

		@Override
		public int getRowCount() {
			return allTables.size();
		}

		@Override
		public Object getValueAt(int row, int columnIndex) {
			
			SalesforceTable table = allTables.get(row);
			
			switch( columnIndex ) {
			case ACTION_INDEX:
				return table.action;
			case NAME_INDEX:
				return table.name;
			
			case COPY_START_INDEX:
				if( null == table.copyStartTime ) {
					return "";
				}
				return dateFormat.format( table.copyStartTime.getTime());
			
			case ELAPSED_INDEX:
				if( null == table.copyStartTime ) {
					return "";
				}
				return getTimeDiff(table.copyStartTime, (null==table.copyEndTime?Calendar.getInstance():table.copyEndTime));
				
			case NROWS_INDEX:
				return table.nRowsCopied;
				
			case NREAD_INDEX:
				return table.nRowsRead;
				
			case NSKIPPED_INDEX:
				return table.nRowsSkipped;
				
			default:
				return "";
			}
		}
	}
	
	private class CopyTimerThread extends Thread {
		private SalesforceTable table;
		private boolean timeToDie = false;
		
		CopyTimerThread( SalesforceTable table ) {
			this.table = table;
		}

		@Override
		public void run() {
			while( !timeToDie ) {
				refreshTable(table);
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					;
				}
			}
		}
		
		
	}
	private JFrame frame = new JFrame( "CopyForce Progress Monitor");
	private List<SalesforceTable> allTables = new ArrayList<SalesforceTable>();
	private Map<String, SalesforceTable> tableNameMap = new HashMap<String, SalesforceTable>();
	private SalesforceTableModel tableModel = new SalesforceTableModel();
	private JTable tableUI;
	private JScrollPane tableScroller;
	private CopyTimerThread copyTimerThread = null;
	private boolean isCancelled = false;
	private JLabel reportMessage;
	
	public SwingExtractionMonitor() {
		initMonitor( frame.getContentPane());
		frame.pack();
		frame.setVisible(true);
	}
	
	private void initMonitor(Container root ) {

		String copyright = "<html><i>Copyright (c) 2011 Gregory Smith (gsmithfarmer@gmail.com)</i></html>";
		
		tableUI = new JTable();
		tableUI.setModel( tableModel );
		
		JPanel myRoot = new JPanel();
		myRoot.setLayout(new BorderLayout());
		
		tableScroller = new JScrollPane( tableUI );
		tableUI.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 300));
		
		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BorderLayout());
		JLabel copyrightLabel = new JLabel(copyright);
		
		southPanel.add( copyrightLabel, BorderLayout.SOUTH);
		
		reportMessage = new JLabel("Progress messages appear here");
		southPanel.add( reportMessage, BorderLayout.CENTER);
		
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout( new BorderLayout());
		controlPanel.add( southPanel, BorderLayout.SOUTH);
		JButton cancelButton = new JButton("Cancel");
		controlPanel.add( cancelButton, BorderLayout.CENTER);
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				SwingExtractionMonitor.this.isCancelled = true;
				
			}
			
		});
		myRoot.add( controlPanel, BorderLayout.SOUTH);
		myRoot.add(tableScroller, BorderLayout.CENTER );
		root.add( myRoot );
		
	}
	
	private SalesforceTable getTable( String name ) {
	
		SalesforceTable table;
		
		if( !tableNameMap.containsKey(name)) {
			table = new SalesforceTable(name);
			allTables.add(table);
			tableNameMap.put( name, table );
			final int whatRow = allTables.size() - 1;
			
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {

					tableModel.fireTableRowsInserted( whatRow, whatRow );
					
					Rectangle rowRect = tableUI.getCellRect(whatRow, 0, true);
					tableUI.scrollRectToVisible(rowRect);
				}
			});
			
		} else {
			table = tableNameMap.get(name);
			final int whatRow = allTables.indexOf(table);
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {			
					Rectangle rowRect = tableUI.getCellRect(whatRow, 0, true);
					tableUI.scrollRectToVisible(rowRect);
				}
			});
		}
		
		Thread.yield();
		return table;
		
		
	}
	
	public void refreshTable( SalesforceTable table ) {
		final int tableIndex = allTables.indexOf(table);

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				tableModel.fireTableRowsUpdated( tableIndex, tableIndex );
			}
		});
		
		Thread.yield();
	}
	/* (non-Javadoc)
	 * @see com.aslan.sfdc.extract.IExtractionMonitor#copyData(java.lang.String, int)
	 */
	@Override
	public void copyData(String tableName, int nRowsCopied) {
		SalesforceTable table = getTable(tableName);
		table.nRowsCopied = nRowsCopied;
		refreshTable( table );

	}

	/* (non-Javadoc)
	 * @see com.aslan.sfdc.extract.IExtractionMonitor#createTable(java.lang.String)
	 */
	@Override
	public void createTable(String name) {
		SalesforceTable table = getTable(name);
		table.action = "Schema Created";
		refreshTable( table );
		log( "Create Table - " + name );

	}


	@Override
	public void endCopyData(String tableName, int nRowsRead, int nRowsSkipped,
			int nRowsCopied) {
		SalesforceTable table = getTable(tableName);
		table.action = "Copied";
		table.nRowsCopied = nRowsCopied;
		table.nRowsSkipped = nRowsSkipped;
		table.nRowsRead = nRowsRead;
		table.copyEndTime = Calendar.getInstance();
		
		if( null != copyTimerThread ) {
			copyTimerThread.timeToDie = true;
			copyTimerThread = null;
		}
		refreshTable( table );

		log( "End Copy From " + tableName + " (" + nRowsRead + " read, "  + nRowsSkipped + " skipped, "+ nRowsCopied + " copied)" );
		
	}

	private SimpleDateFormat dateFormat = new SimpleDateFormat( "kk:mm:ss");
	private void log( String msg ) {
		final StringBuffer timeBuffer = new StringBuffer();
		FieldPosition fieldPos = new FieldPosition(0);
		
		dateFormat.format( new Date(), timeBuffer, fieldPos );
		timeBuffer.append(" : " + msg );
	
		SwingUtilities.invokeLater(new Runnable() {

		@Override
		public void run() {
			reportMessage.setText(timeBuffer.toString());
		}
	});

	}
	/* (non-Javadoc)
	 * @see com.aslan.sfdc.extract.IExtractionMonitor#reportMessage(java.lang.String)
	 */
	
	@Override
	public void reportMessage(String msg) {
		log(msg);
	}

	/* (non-Javadoc)
	 * @see com.aslan.sfdc.extract.IExtractionMonitor#startCopyData(java.lang.String)
	 */
	@Override
	public void startCopyData(String tableName) {
		SalesforceTable table = getTable(tableName);
		table.action = "Copying";
		table.copyStartTime = Calendar.getInstance();
		refreshTable( table );
		
		copyTimerThread = new CopyTimerThread(table);
		copyTimerThread.start();

		log( "Start Copy From " + tableName );

	}

	@Override
	public void readData(String tableName, int nRowsRead) {
		SalesforceTable table = getTable(tableName);
		table.nRowsRead = nRowsRead;
		refreshTable( table );
		
	}

	@Override
	public boolean isCancel() {
		return isCancelled;
	}

	@Override
	public void skipData(String tableName, int nRowsSkipped) {
		SalesforceTable table = getTable(tableName);
		table.nRowsSkipped = nRowsSkipped;
		refreshTable( table );
		
	}

	@Override
	public void skipTable(String name) {
		SalesforceTable table = getTable(name);
		table.action = "Schema OK";
		refreshTable( table );
		
		log( "Table Exists - " + name );
		
	}

}
