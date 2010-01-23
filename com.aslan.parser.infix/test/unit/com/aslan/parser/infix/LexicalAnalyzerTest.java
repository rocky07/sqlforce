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

import com.aslan.parser.infix.LexicalAnalyzer;
import com.aslan.parser.infix.LexicalToken;

import junit.framework.TestCase;
/**
 *
 * Test complex lexical analysis.
 * @author snort
 *
 */
public class LexicalAnalyzerTest extends TestCase {

	/**
	 * Test parsing string tokens.
	 */
	public void testParse() {
		LexicalAnalyzer lex = new LexicalAnalyzer();
		LexicalToken token;

		lex.setInput( "A + B/23");

		token = lex.getToken();
		assertNotNull(token);
		assertEquals( LexicalToken.Type.IDENTIFIER, token.getType());
		assertEquals( "A", token.getValue());

		token = lex.getToken();
		assertNotNull(token);
		assertEquals( LexicalToken.Type.PUNCTUATION, token.getType());
		assertEquals( "+", token.getValue());

		token = lex.getToken();
		assertNotNull(token);
		assertEquals( LexicalToken.Type.IDENTIFIER, token.getType());
		assertEquals( "B", token.getValue());

		token = lex.getToken();
		assertNotNull(token);
		assertEquals( LexicalToken.Type.PUNCTUATION, token.getType());
		assertEquals( "/", token.getValue());

		token = lex.getToken();
		assertNotNull(token);
		assertEquals( LexicalToken.Type.NUMBER, token.getType());
		assertEquals( "23", token.getValue());

		lex.setInput( ".23 0.23 23. 23.0");
		token = lex.getToken();
		assertNotNull(token);
		assertEquals( LexicalToken.Type.NUMBER, token.getType());
		assertEquals( ".23", token.getValue());

		token = lex.getToken();
		assertNotNull(token);
		assertEquals( LexicalToken.Type.NUMBER, token.getType());
		assertEquals( "0.23", token.getValue());

		token = lex.getToken();
		assertNotNull(token);
		assertEquals( LexicalToken.Type.NUMBER, token.getType());
		assertEquals( "23.", token.getValue());

		token = lex.getToken();
		assertNotNull(token);
		assertEquals( LexicalToken.Type.NUMBER, token.getType());
		assertEquals( "23.0", token.getValue());

		lex.setInput( ".23A 0.23B 23.C");

		token = lex.getToken();
		assertNotNull(token);
		assertEquals( LexicalToken.Type.NUMBER, token.getType());
		assertEquals( ".23", token.getValue());

		token = lex.getToken();
		assertNotNull(token);
		assertEquals( LexicalToken.Type.IDENTIFIER, token.getType());
		assertEquals( "A", token.getValue());

		token = lex.getToken();
		assertNotNull(token);
		assertEquals( LexicalToken.Type.NUMBER, token.getType());
		assertEquals( "0.23", token.getValue());

		token = lex.getToken();
		assertNotNull(token);
		assertEquals( LexicalToken.Type.IDENTIFIER, token.getType());
		assertEquals( "B", token.getValue());

		token = lex.getToken();
		assertNotNull(token);
		assertEquals( LexicalToken.Type.NUMBER, token.getType());
		assertEquals( "23.", token.getValue());

		token = lex.getToken();
		assertNotNull(token);
		assertEquals( LexicalToken.Type.IDENTIFIER, token.getType());
		assertEquals( "C", token.getValue());
		
		
		lex.setInput( "ROWID !INLIST [1,5,15]");

		token = lex.getToken();
		assertNotNull(token);
		assertEquals( LexicalToken.Type.IDENTIFIER, token.getType());
		assertEquals( "ROWID", token.getValue());

		token = lex.getToken();
		assertNotNull(token);
		assertEquals( LexicalToken.Type.PUNCTUATION, token.getType());
		assertEquals( "!", token.getValue());

		token = lex.getToken();
		assertNotNull(token);
		assertEquals( LexicalToken.Type.IDENTIFIER, token.getType());
		assertEquals( "INLIST", token.getValue());

		token = lex.getToken();
		assertNotNull(token);
		assertEquals( LexicalToken.Type.LIST, token.getType());
		assertEquals( "1,5,15", token.getValue());

	}


}
