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

import java.util.List;

import com.aslan.parser.infix.IParsedExpression;
import com.aslan.parser.infix.InfixParser;
import com.aslan.parser.infix.ParsedExpression;

import junit.framework.TestCase;

public class ParsedExpressionTest extends TestCase {

	private InfixParser parser;

	public void setUp() throws Exception {
		parser = new InfixParser();
	}

	public void testGetPostfix() throws Exception {

		IParsedExpression expr= parser.parse( "5+18" );
		assertNotNull(expr);

	}

	/**
	 * Validate that all code in the getPostfixString can be executed.
	 *
	 */
	public void testGetPostfixString() throws Exception {

		String[][] testData = {
				// Infix				Postfix Notation
				//==========			================
				{"'Hello'",				"\"Hello\""},
				{"'Hel\"lo'",			"'Hel\"lo'"},
				{"'H\\\'el\"lo'",		"\"H'el\\\"lo\""},
				{"Hello + World",		"Hello World +"},
				{"-24",					"24 -#"},
				{"f()",					"f#0"},
				{"f(1,2)",				"1 2 f#2"},
				{"ROWID !INLIST [1,2]", "ROWID INLIST !"},

		};

		for( String[] ss : testData ) {
			IParsedExpression expr = parser.parse( ss[0] );
			String result = expr.toPostfixString();

			assertEquals( ss[1], result );
		}

	}

	/**
	 * Validate that all code in the getInfixString can be executed.
	 *
	 */
	public void testGetInfixString() throws Exception {

		String[][] testData = {
				// Infix				Postfix Notation
				//==========			================
				{"'Hello'",				"\"Hello\""},
				{"'Hel\"lo'",			"'Hel\"lo'"},
				{"'H\\\'el\"lo'",		"\"H'el\\\"lo\""},
				{"Hello + World",		"(Hello + World)"},
				{"-24",					"(-24)"},
				{"f()",					"f()"},
				{"f(1,2)",				"f(1, 2)"},
				{"a + b*c",				"(a + (b * c))"},
				{"",					""},
				{"ROWID !INLIST [1,2]", "(!(INLISTROWID))"},

		};

		for( String[] ss : testData ) {
			IParsedExpression expr = parser.parse( ss[0] );

			String result = expr.toInfixString();

			assertEquals( ss[1], result );
		}

	}

	/**
	 * Grab the list of identifiers from an expression.
	 *
	 * @throws Exception
	 */
	public void testGetIdentifiers() throws Exception {

		List<String> list;

		list = parser.parse( "23 + 18").getIdentifiers();
		assertNotNull(list);
		assertEquals( 0, list.size());

		list = parser.parse( "23 + Mark").getIdentifiers();
		assertNotNull(list);
		assertEquals( 1, list.size());
		assertEquals( "Mark", list.get(0));

		list = parser.parse( "23 + Mark + MARK").getIdentifiers();
		assertNotNull(list);
		assertEquals( 1, list.size());
		assertEquals( "Mark", list.get(0));

		list = parser.parse( "23 + Mark + Greg").getIdentifiers();
		assertNotNull(list);
		assertEquals( 2, list.size());
		assertEquals( "Mark", list.get(0));
		assertEquals( "Greg", list.get(1));
	}

	/**
	 * Compare parsed expressions for equality.
	 *
	 * @throws Exception
	 */
	public void testEquals() throws Exception {
		IParsedExpression a;
		IParsedExpression b;

		//
		// Cover all the cases where a token by token comparison is not required.
		//
		a = parser.parse( "" );
		b = parser.parse( "" );
		assertTrue( a.equals(b));

		a = new ParsedExpression(null);
		b = new ParsedExpression(null);
		assertTrue( a.equals(b) );

		a = parser.parse("");
		b = new ParsedExpression(null);
		assertFalse( a.equals(b));
		assertFalse( b.equals(a));

		assertFalse( a.equals(null));

		a = parser.parse( "23");
		b = parser.parse( "23 + 18");
		assertFalse( a.equals(b));

		//
		// Cover token by token comparison cases.
		//

		a = parser.parse( "23 + 18.0");
		b = parser.parse( "23 + 18");
		assertTrue( a.equals(b));

		a = parser.parse( "fred + 18.0");
		b = parser.parse( "Fred + 18");
		assertTrue( a.equals(b));

		a = parser.parse( "fred(14) + 18.0");
		b = parser.parse( "Fred(14.0) + 18");
		assertTrue( a.equals(b));

		a = parser.parse( "'Hello' + 18.0");
		b = parser.parse( "'Hello' + 18");
		assertTrue( a.equals(b));

		a = parser.parse( "'Hello' + 18.0");
		b = parser.parse( "'HELLO' + 18");
		assertFalse( a.equals(b));

		a = parser.parse( "23.1 + 18.0");
		b = parser.parse( "23 + 18");
		assertFalse( a.equals(b));

		a = parser.parse( "freddy + 18.0");
		b = parser.parse( "Fred + 18");
		assertFalse( a.equals(b));

		a = parser.parse( "freddy(14) + 18.0");
		b = parser.parse( "Fred(14.0) + 18");
		assertFalse( a.equals(b));

		a = parser.parse( "fred + 19");
		b = parser.parse( "18 + fred");
		assertFalse( a.equals(b));



	}
}
