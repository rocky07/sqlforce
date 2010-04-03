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
/**
 * 
 */
package com.aslan.sfdc.partner.test;

import com.aslan.sfdc.partner.QuerySObjectParser;

import junit.framework.TestCase;

/**
 * @author greg
 *
 */
public class QuerySObjectParserTest extends TestCase {

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for {@link com.aslan.sfdc.partner.QuerySObjectParser#QuerySObjectParser(java.lang.String)}.
	 */
	public void testQuerySObjectParser() throws Exception {
		
		new QuerySObjectParser("SELECT LastName, FirstName FROM Contact");
		
		new QuerySObjectParser("SELECT \nLastName,\nFirstName \nFROM Contact");
		
		new QuerySObjectParser("SELECT LastName, FirstName, Account.Id \nFROM Contact");
	}

	/**
	 * Test method for {@link com.aslan.sfdc.partner.QuerySObjectParser#parseRow(com.sforce.soap.partner.sobject.SObject)}.
	 */
	public void testParseRow() {
		;
	}

}
