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
package com.aslan.parser.infix;

import com.aslan.parser.infix.LexicalToken;

import junit.framework.TestCase;

/**
 * Insure code coverage for this package level class.
 *
 * @author snort
 *
 */
public class LexicalTokenTest extends TestCase {

	public void testLexicalToken() {
	;
		LexicalToken token = new LexicalToken(LexicalToken.Type.IDENTIFIER, 0, "Hello");

		assertEquals( "Hello", token.getValue());
		assertEquals( LexicalToken.Type.IDENTIFIER, token.getType());
		assertEquals( 0, token.getPosition());
		assertNotNull( token.toString() );
	}

}
