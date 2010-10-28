/*******************************************************************************
 * Copyright (c) 2010 Gregory Smith (gsmithfarmer@gmail.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/

/**
 * Extract attachments (lots of them).
 */
package com.aslan.sfdc.extract.test;

import java.util.List;

import junit.framework.TestCase;

import com.aslan.sfdc.extract.DefaultExtractionMonitor;
import com.aslan.sfdc.extract.ExtractionManager;
import com.aslan.sfdc.extract.ExtractionRuleset;
import com.aslan.sfdc.extract.IDatabaseBuilder;
import com.aslan.sfdc.extract.ExtractionRuleset.TableRule;
import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.partner.test.SfdcTestEnvironment;
import com.sforce.soap.partner.DescribeSObjectResult;
import com.sforce.soap.partner.Field;

public class ExtractAttachmentsTest extends TestCase {

	private class DatabaseScanner implements IDatabaseBuilder {

		private int recNo = 0;
		@Override
		public void createTable(DescribeSObjectResult sfdcTable)
				throws Exception {
			System.err.println("Create Table: " + sfdcTable.getName());
			
		}

		@Override
		public int insertData(DescribeSObjectResult sfdcTable, Field[] fields,
				List<String[]> dataRows) throws Exception {
			for( String[] row : dataRows ) {
				System.err.println(recNo++ + " : " + row[0] + " : " + sfdcTable.getName() );
			}
			return 0;
		}

		@Override
		public boolean isTableNew(DescribeSObjectResult sfdcTable)
				throws Exception {
			return false;
		}
		
	}
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetAttachments() throws Exception {
		LoginManager.Session session = SfdcTestEnvironment.getSession("Readonly");
		//LoginManager.Session session = SfdcTestEnvironment.getTestSession();
		DatabaseScanner scanner = new DatabaseScanner();
		ExtractionManager mgr = new ExtractionManager(session, scanner);
		ExtractionRuleset rules = new ExtractionRuleset();
		
		
		rules.includeTable(new TableRule("Attachment", false));
		mgr.extractData( rules, new DefaultExtractionMonitor());
			

	}



}
