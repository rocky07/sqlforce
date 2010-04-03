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
package com.aslan.sfdc.partner.record.test;

import java.util.Calendar;

import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.partner.record.AccountRecord;
import com.aslan.sfdc.partner.test.SfdcTestEnvironment;
import com.sforce.soap.partner.QueryResult;

import junit.framework.TestCase;

public class AbstractRecordTest extends TestCase {
	
	private LoginManager.Session session = SfdcTestEnvironment.getSession();
	
	public void testDateTimeModification() throws Exception {
		
		QueryResult qr = session.getBinding().query("SELECT Id, CreatedDate, LastModifiedDate FROM Account LIMIT 1");

		assertTrue( qr.getSize() == 1 );
		
		AccountRecord account = new AccountRecord( qr.getRecords()[0] );
		
		Calendar cc = account.getCreatedDate();
		
		//System.err.println("original creation: " + cc.getTime());
		cc.add( Calendar.MINUTE, 10);
		//System.err.println("edited creation: " + cc.getTime());
		account.setCreatedDate( cc );
		assertTrue( 0 == cc.compareTo( account.getCreatedDate()));
		
		cc = account.getCreatedDate();
		//System.err.println("updated creation: " + cc.getTime());
		
	
	}

	
}
