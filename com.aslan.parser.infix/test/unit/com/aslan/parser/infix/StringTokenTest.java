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
package com.aslan.parser.infix;

import com.aslan.parser.infix.IToken;
import com.aslan.parser.infix.StringToken;

import junit.framework.TestCase;

/**
 * Coverage tests for package local class StringToken
 * @author snort
 *
 */
public class StringTokenTest extends TestCase {

	/**
	 * Test method for {@link com.aslan.parser.infix.StringToken#getType()}.
	 */
	public void testStringToken() {

		StringToken token;

		token = new StringToken( "fred");
		assertEquals( "fred", token.getName());
		assertEquals( IToken.Type.STRING, token.getType());
		assertEquals( "fred", token.getValue());

		token = new StringToken( (String)null);
		assertEquals( "", token.getName());
		assertEquals( IToken.Type.STRING, token.getType());
		assertEquals( "", token.getValue());

	}

}
