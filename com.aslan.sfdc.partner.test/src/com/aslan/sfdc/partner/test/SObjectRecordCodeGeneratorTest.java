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
package com.aslan.sfdc.partner.test;

import junit.framework.TestCase;

import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.partner.record.SObjectRecordCodeGenerator;

public class SObjectRecordCodeGeneratorTest extends TestCase {

	private LoginManager.Session session = SfdcTestEnvironment.getSession();
	
	public void testCreateContactCode() throws Exception {
		
		SObjectRecordCodeGenerator generator = new SObjectRecordCodeGenerator(session);
		
		generator.setIgnoreCustomFields(true);
		
		//
		// To create code for  your object, replace the following assignment with name of your SObject.
		//
		String createCodeForSObject = "Attachment";
		
		String code = generator.generateCode(createCodeForSObject);
		
		assertNotNull(code);
	}
	

	
	
}
