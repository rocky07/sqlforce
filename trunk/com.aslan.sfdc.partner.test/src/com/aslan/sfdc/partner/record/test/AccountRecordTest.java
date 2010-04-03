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

import junit.framework.TestCase;

import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.partner.record.AccountRecord;
import com.aslan.sfdc.partner.test.SfdcTestEnvironment;
import com.sforce.soap.partner.QueryResult;

public class AccountRecordTest extends TestCase {
	
	private LoginManager.Session session = SfdcTestEnvironment.getSession();
	
	public void testReadAccountFromSObject() throws Exception {
		
		QueryResult qr = session.getBinding().query("SELECT Id, Name, LastModifiedDate FROM Account LIMIT 1");

		assertTrue( qr.getSize() == 1 );
		
		AccountRecord account = new AccountRecord( qr.getRecords()[0] );
		
		assertNotNull( account.getId());
		assertNotNull( account.getStringField("name"));
		assertNotNull( account.getStringField("LastModifiedDate"));
		assertNull( account.getStringField( "owner"));
	
	}

	
}
