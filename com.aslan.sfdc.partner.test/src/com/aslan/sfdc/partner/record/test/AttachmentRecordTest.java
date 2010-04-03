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
import com.aslan.sfdc.partner.record.AttachmentRecord;
import com.aslan.sfdc.partner.test.SfdcTestEnvironment;
import com.sforce.soap.partner.QueryResult;

public class AttachmentRecordTest extends TestCase {
	
	private LoginManager.Session session = SfdcTestEnvironment.getSession();
	
	public void testReadAccountFromSObject() throws Exception {
		
		QueryResult qr = session.getBinding().query("SELECT Id, Body, Name, BodyLength FROM Attachment LIMIT 1");

		if( qr.getSize() == 0) { return; } // No attachments int eh database.
		
		assertTrue( qr.getSize() == 1 );
		
		AttachmentRecord attachment = new AttachmentRecord( qr.getRecords()[0] );
		
//		System.err.println("Body is : " + attachment.getBody());
//		System.err.println("Name is : " + attachment.getName());
//		
		assertNotNull( attachment.getId());
		assertNotNull( attachment.getBody());
		
	
	}

	
}
