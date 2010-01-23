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

import com.aslan.parser.infix.InfixParserException;
import com.aslan.parser.infix.LexicalToken;

import junit.framework.TestCase;

/**
 * Test all constructors of InfixParserException.
 *
 * @author snort
 *
 */
public class InfixParserExceptionTest extends TestCase {

	public void testInfixParserExceptionString() {

		InfixParserException e = new InfixParserException( "brown cow");

		assertEquals( e.getMessage(), "brown cow");

		e = new InfixParserException( null );
		assertNull( e.getMessage());
	}

	public void testInfixParserExceptionStringStringLexicalToken() {
		InfixParserException e;
		LexicalToken	token;
		String 			expr;

		expr = "23 + 18";
		token = new LexicalToken(LexicalToken.Type.NUMBER, 0, "23" );
		e = new InfixParserException( "play", expr, token );

		assertNotNull( e.getMessage());

		token = new LexicalToken(LexicalToken.Type.NUMBER, -100, "23" );
		e = new InfixParserException( "play", expr, token );

		assertNotNull( e.getMessage());

		token = new LexicalToken(LexicalToken.Type.NUMBER, expr.length(), "23" );
		e = new InfixParserException( "play", expr, token );

		assertNotNull( e.getMessage());

		token = new LexicalToken(LexicalToken.Type.NUMBER, expr.length(), "23" );
		e = new InfixParserException( "play", (String)null, token );

		assertNotNull( e.getMessage());

		token = new LexicalToken(LexicalToken.Type.NUMBER, expr.length(), "23" );
		e = new InfixParserException( "play", expr, (LexicalToken)null );

		assertNotNull( e.getMessage());

	}

}
