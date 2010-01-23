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
 * @author snort
 *
 */
public class LexicalAnalyzerBasicTest extends TestCase {

	/**
	 * Test parsing string tokens.
	 */
	public void testGetString() {
		LexicalAnalyzer lex = new LexicalAnalyzer();
		LexicalToken token;

		//
		// Very easy cases.
		//
		String expected = "How now brown cow";
		lex.setInput("\"" + expected + "\"");

		token = lex.getToken();
		assertEquals( expected, token.getValue());

		lex.setInput("'" + expected + "'");
		token = lex.getToken();
		assertEquals( expected, token.getValue());

		//
		// Escaped quotes
		//
		expected = "Embedded\"Quote";
		lex.setInput( "\"Embedded\\\"Quote\"" );
		token = lex.getToken();
		assertEquals( expected, token.getValue());


		expected = "Embedded'Quote";
		lex.setInput( "\"Embedded\\'Quote\"" );
		token = lex.getToken();
		assertEquals( expected, token.getValue());

		expected = "Embedded'Quote";
		lex.setInput( "\"Embedded\\'Quote\"" );
		token = lex.getToken();
		assertEquals( expected, token.getValue());

		expected = "Embedded'Quote";
		lex.setInput( "\"Embedded'Quote\"" );
		token = lex.getToken();
		assertEquals( expected, token.getValue());

		//
		// Empty Strings
		//
		expected = "";
		lex.setInput( "\"\"");
		token = lex.getToken();

		expected = "";
		lex.setInput( "''");
		token = lex.getToken();

		//
		// Blank strings
		//
		expected = "   ";
		lex.setInput("\"" + expected + "\"");

		token = lex.getToken();
		assertEquals( expected, token.getValue());


		expected = "  ";
		lex.setInput("'" + expected + "'");

		token = lex.getToken();
		assertEquals( expected, token.getValue());

		//
		// String with missing closing quotes.
		//

		expected = "How now brown cow";
		lex.setInput("\"" + expected );

		token = lex.getToken();
		assertEquals( expected, token.getValue());

		expected = "How now brown cow";
		lex.setInput("'" + expected );

		token = lex.getToken();
		assertEquals( expected, token.getValue());
	}

	/**
	 * Verify that legal identifiers can be parsed.
	 *
	 */
	public void testParseIdentifer() {
		LexicalAnalyzer lex = new LexicalAnalyzer();
		LexicalToken token;

		//
		// Very easy cases.
		//
		String expected = "Mary23";
		lex.setInput(expected);

		token = lex.getToken();
		assertEquals( expected, token.getValue());
		assertEquals( LexicalToken.Type.IDENTIFIER, token.getType());

		expected = "Mary_23";
		lex.setInput("    Mary_23 is a beauty");

		token = lex.getToken();
		assertEquals( expected, token.getValue());
		assertEquals( LexicalToken.Type.IDENTIFIER, token.getType());
		
		expected = "AND"; 		// "AND(" is not function, but operation
		lex.setInput(" AND (");
		token = lex.getToken();
		assertEquals( expected, token.getValue());
		assertEquals( LexicalToken.Type.IDENTIFIER, token.getType());
	}

	/**
	 * Verify that legal function names can be parsed.
	 *
	 */
	public void testParseFunctionName() {
		LexicalAnalyzer lex = new LexicalAnalyzer();
		LexicalToken token;

		//
		// Very easy cases.
		//
		String expected = "Mary23";
		lex.setInput(expected + "(");

		token = lex.getToken();
		assertEquals( expected, token.getValue());
		assertEquals( LexicalToken.Type.FUNCTION, token.getType());

		expected = "Mary_23";
		lex.setInput("    Mary_23 (is a beauty");

		token = lex.getToken();
		assertEquals( expected, token.getValue());
		assertEquals( LexicalToken.Type.FUNCTION, token.getType());
	}

	/**
	 * Verify that multi character punction is parse properly.
	 *
	 */
	public void testParseMultiCharPunction() {
		LexicalAnalyzer lex = new LexicalAnalyzer();
		LexicalToken token;
		String expected;

		String[] toTest = {"==", "!=", ">=", "<=", "&&", "||"};

		for( String punc : toTest ) {
			expected = punc;

			lex.setInput("   " + expected + "  ");

			token = lex.getToken();
			assertEquals( expected, token.getValue());
			assertEquals( LexicalToken.Type.PUNCTUATION, token.getType());

		}
	}

	/**
	 * Verify that legal numbers can be parsed.
	 *
	 */
	public void testParseNumbers() {
		LexicalAnalyzer lex = new LexicalAnalyzer();
		LexicalToken token;
		String expected;

		String[] numList = {"100", "123.23", ".34", "123.", "0.0000034"};
		//
		// Very easy cases.
		//
		for( String ss : numList ) {
			expected = ss;
			lex.setInput("   " + expected + "   ");

			token = lex.getToken();
			assertEquals( expected, token.getValue());
			assertEquals( LexicalToken.Type.NUMBER, token.getType());
		}
	}
}
