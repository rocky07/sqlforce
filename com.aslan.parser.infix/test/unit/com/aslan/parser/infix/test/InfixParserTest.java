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
package com.aslan.parser.infix.test;

import junit.framework.TestCase;

import com.aslan.parser.infix.IParsedExpression;
import com.aslan.parser.infix.InfixParser;
import com.aslan.parser.infix.InfixParserException;

public class InfixParserTest extends TestCase {

	private InfixParser parser;

	public void setUp() throws Exception {
		parser = new InfixParser();

	}

	/**
	 * Create an expression using variations of null.
	 *
	 * @throws Exception on any error
	 */
	public void testNullExpressions() throws Exception {
		IParsedExpression rpn;

		rpn = parser.parse( null );
		assertNotNull(rpn);
		assertEquals( 0, rpn.getPostfix().size());

		rpn = parser.parse( "" );
		assertNotNull(rpn);
		assertEquals( 0, rpn.getPostfix().size());

		rpn = parser.parse( "     " );
		assertNotNull(rpn);
		assertEquals( 0, rpn.getPostfix().size());
	}
	
	public void testBasicOperators() throws Exception {
		IParsedExpression rpn;
		String[][] expressions = {
				// Infix				Postfix Notation
				//==========			================
				{"23 + Greg",			"23 Greg +"},
				{"6 + 14*8",			"6 14 8 * +"},
				{"4+5-8",				"4 5 + 8 -"},
				{"4 - 6 + 5",			"4 6 - 5 +"},
				{"8/4 + 2",				"8 4 / 2 +"},
				{"Greg % 23 + 18",		"Greg 23 % 18 +"},
				{"Greg MOD 23 - 12",	"Greg 23 MOD 12 -"},
				{"Greg OR Mary AND 23",	"Greg Mary 23 AND OR"},
				{"not Greg or Mary",	"Greg NOT Mary OR"},
				{"8*32/2",				"8 32 * 2 /"},
				{"'Give up' + 14",		"\"Give up\" 14 +"},
				{"\"Fred \\\"\" + 18",	"'Fred \"' 18 +"},

		};

		for( String[] ss : expressions ) {

			rpn = parser.parse(ss[0]);

			assertNotNull(ss[0], rpn);
			assertEquals( ss[1], rpn.toPostfixString());
		}
	}

	public void testParens() throws Exception {
		IParsedExpression rpn;
		String[][] expressions = {
				// Infix				Postfix Notation
				//==========			================
				{"4 * (23 + 17)",		"4 23 17 + *"},
				{"(40 + 20)/10*5",		"40 20 + 10 / 5 *"},
				{"(4+8)*(17+28*(2+3))",	"4 8 + 17 28 2 3 + * + *"},
		};

		for( String[] ss : expressions ) {

			rpn = parser.parse(ss[0]);

			assertNotNull(ss[0], rpn);
			assertEquals( ss[1], rpn.toPostfixString());

			//System.err.println( rpn.getPostfixString() + " = " + (40 + 20)/10*5);
		}
	}

	public void testUnaryMinus() throws Exception {
		IParsedExpression rpn;
		String[][] expressions = {
				// Infix				Postfix Notation
				//==========			================
				{"-23",					"23 -#"},
				{"- (4 + 5)",			"4 5 + -#"},
				{"5 + -(18--3)",		"5 18 3 -# - -# +"},
				{"---4",				"4 -# -# -#"},
		};

		for( String[] ss : expressions ) {

			rpn = parser.parse(ss[0]);

			assertNotNull(ss[0], rpn);
			assertEquals( ss[1], rpn.toPostfixString());


		}
	}

	public void testFunctions() throws Exception {
		IParsedExpression rpn;
		String[][] expressions = {
				// Infix				Postfix Notation
				//==========			================
				{"f(3+4, 7)",			"3 4 + 7 f#2"},
				{"3 + g()",				"3 g#0 +"},
				{"g()",					"g#0"},
				{"f() * g()",			"f#0 g#0 *"},
				{"3 + 4*g(8)",			"3 4 8 g#1 * +"},
				{"f( 3 + g())",			"3 g#0 + f#1"},
				{"f(g(h()))",			"h#0 g#1 f#1"},
				{"f( g(mark) - h(1,2,3))",	"mark g#1 1 2 3 h#3 - f#1"},
		};

		for( String[] ss : expressions ) {

			rpn = parser.parse(ss[0]);

			//System.err.println( rpn.getPostfixString() );
			assertNotNull(ss[0], rpn);
			assertEquals( ss[1], rpn.toPostfixString());


		}
	}

	public void testIllegalExpressions() throws Exception {
		IParsedExpression rpn;
		String[] expressions = {

				"-+45",
				"+45",
				"(4+3",
				"(4+3))",
				"[ how now brown cow]",
				"23 'Brown cow'",
				"(4+3 - 13 + (18",
				"23 + f(12,13,14",
				"+",
				"23)",
				"23+)"

		};

		for( String ss : expressions ) {

			try {
				rpn = parser.parse(ss);
				assertTrue("Illegal expression " + ss + " parsed successfully to " + rpn.toPostfixString(),false );
			} catch (InfixParserException e) {
				;
			}


		}
	}

	/**
	 * Make sure all constructors for an exception are called.
	 *
	 *
	 */
	public void testParserExceptionCreation() {

		new InfixParserException( "Hard Exception to create");
	}
}
