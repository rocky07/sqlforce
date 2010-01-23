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

import java.util.HashMap;

import junit.framework.TestCase;

import com.aslan.parser.infix.IFunctionDef;
import com.aslan.parser.infix.InfixParser;
import com.aslan.parser.infix.InfixParserException;
import com.aslan.parser.infix.PostfixEvaluator;

public class PostfixEvaluatorTest extends TestCase {

	private InfixParser parser;
	private PostfixEvaluator evaluator;

	public void setUp() throws Exception {
		parser = new InfixParser();
		evaluator = new PostfixEvaluator();

	}

	public void testMathOperators() throws Exception {

		String[][] expressions = {
				// Infix				Result
				//==========			================
				{"23",					"23"},
				{"-(4+3)",				"-7"},
				{"3.0 * 2",				"6"},
				{"5>2",					"1"},
				{"2>5",					"0"},
				{"5>=4",				"1"},
				{"5>=5",				"1"},
				{"5>=5.0",				"1"},
				{"5>=6.0",				"0"},
				{"5>6",					"0"},
				{"5<2",					"0"},
				{"2<5",					"1"},
				{"5<=4",				"0"},
				{"5<=5",				"1"},
				{"5<=5.0",				"1"},
				{"5<=6",				"1"},
				{"16==16",				"1"},
				{"16==23",				"0"},
				{"16!=14",				"1"},
				{"16!=16",				"0"},
				{"5%2",					"1"},
				{"10%7",				"3"},

		};

		for( String[] ss : expressions ) {

			String answer = evaluator.eval(parser.parse(ss[0]));

			assertEquals( ss[1], answer);
		}
	}

	/**
	 * Test built-in operators that work on strings.
	 *
	 * @throws Exception
	 */
	public void testStringOperators() throws Exception {
		String[][] expressions = {
				// Infix				Result
				//==========			================
				{"'Greg'='Greg'",		"1"},
				{"'23'=23",				"1"},
				{"'Greg'=='GREG'",		"0"},
				{"'Greg'!='Greg'",		"0"},
				{"'23'!=23",			"0"},
				{"'Greg'!='GREG'",		"1"},
				{"'Greg'>'Greg'",		"0"},
				{"'32'>32",				"0"},
				{"'Greg'>'Mary'",		"0"},
				{"'Greg'>'Alfred'",		"1"},
				{"'Greg'>23",			"1"},
				{"23>'Greg'",			"0"},
				{"'Greg'>='Greg'",		"1"},
				{"'32'>=32",			"1"},
				{"'Greg'>='Mary'",		"0"},
				{"'Greg'>='Alfred'",	"1"},
				{"'Greg'>=23",			"1"},
				{"23>='Greg'",			"0"},
				{"'Greg'<'Greg'",		"0"},
				{"'32'<32",				"0"},
				{"'Greg'<'Mary'",		"1"},
				{"'Greg'<'Alfred'",		"0"},
				{"'Greg'<23",			"0"},
				{"23<'Greg'",			"1"},
				{"'Greg'<='Greg'",		"1"},
				{"'32'<=32",			"1"},
				{"'Greg'<='Mary'",		"1"},
				{"'Greg'<='Alfred'",	"0"},
				{"'Greg'<=23",			"0"},
				{"23<='Greg'",			"1"},
				{"'Street'starts'Str'",	"1"},
				{"'Table'starts'TAB'",	"0"},
				{"'Home'ends'me'",		"1"},
				{"'Table'ends'Tab'",	"0"},
				{"'Table'contains'abl'","1"},

		};

		for( String[] ss : expressions ) {

			String answer = evaluator.eval(parser.parse(ss[0]));
			assertEquals( ss[1], answer);
		}
	}

	public void testLogicOperators() throws Exception {
		String[][] expressions = {
				// Infix				Result
				//==========			================
				{"1 and 0",					"0"},
				{"1 and 1",					"1"},
				{"0 and 0",					"0"},
				{"1 or 0",					"1"},
				{"0 or 0",					"0"},
				{"1 or 1",					"1"},
				{"1 && 0",					"0"},
				{"1 && 1",					"1"},
				{"0 && 0",					"0"},
				{"1 || 0",					"1"},
				{"0 || 0",					"0"},
				{"1 || 1",					"1"},
				{"'' and 23",				"0"},
				{"'greg' and 23",			"1"},
				{"'greg' and 'mary'",		"1"},
				{"'greg' and ''",			"0"},
				{"'greg' or 23",			"1"},
				{"'greg' OR 'mary'",		"1"},
				{"'greg' or ''",			"1"},
				{"NOT 23",					"0"},
				{"NOT 0",					"1"},
				{"NOT ''",					"1"},
				{"NOT 'Greg'",				"0"},
				{"! 23",					"0"},
				{"! 0",					"1"},
				{"! ''",					"1"},
				{"! 'Greg'",				"0"},
		};

		for( String[] ss : expressions ) {

			String answer = evaluator.eval(parser.parse(ss[0]));

			assertEquals( ss[1], answer);
		}
	}

	/**
	 * Test the convenience function that returns a java boolean type.
	 *
	 */
	public void testBooleanEvaluation() throws Exception {


		assertTrue( evaluator.evalBoolean( parser.parse("1")));

		assertFalse( evaluator.evalBoolean( parser.parse("0")));

	}

	public void testVariableSubstiution() throws Exception {

		HashMap<String, Object> variables = new HashMap<String, Object>();

		variables.put( "N50", 50 );
		variables.put( "Mary", "Mary Deane");
		variables.put( "TRUE", true );
		variables.put( "FALSE", false );
		variables.put( "CLASS", this );
		variables.put( "NULL", null );
		variables.put( "EMPTY", "" );

		String[][] expressions = {
				// Infix				Result
				//==========			================
				{"3*N50",					"150"},
				{" 100/N50 + 4",			"6"},
				{"Mary + N50",				"Mary Deane50"},
				{"Mary - 23",				"-22"},
				{"true and false",			"0"},
				{"true or false",			"1"},
				{"NOT CLASS", 				"0"},
				{"NOT NULL",				"1"},
				{"NOT EMPTY",				"1"},
				{"(NoSuch + 45)*2",			"90"},
		};

		for( String[] ss : expressions ) {

			String answer = evaluator.eval(parser.parse(ss[0]), variables);

			//System.err.println(ss[0] + " = " + answer );
			assertEquals( ss[1], answer);
		}
	}

	public void testBuiltinFunctions() throws Exception {

		String[][] expressions = {
				// Infix				Result
				//==========			================
				{"length(234)",			"3"},
				{"length('')",			"0"},
				{"abs(-23)",			"23"},
				{"abs(3*6)",			"18"},
				{"ceil(14.52)",			"15"},
				{"ceil(-14.52)",		"-14"},
				{"floor(14.52)",		"14"},
				{"floor(-14.52)",		"-15"},
				{"min(100)",			"100"},
				{"min(4,5,1+1)",		"2"},
				{"min(45, 'greg')",		"45"},
				{"min('greg', 'mary', 'caleb')",		"caleb"},
				{"max(100)",			"100"},
				{"max(4,5,1+1)",		"5"},
				{"max(45, 'greg')",		"greg"},
				{"max('greg', 'mary', 'caleb')",		"mary"},
				{"tohex( 15)",			"f"},
				{"tohex( 255 )",		"ff"},
				{"tohex(rgb(255,255,255))",	"ffffff"},
				{"tohex(rgb( -100, 300, 0))", "ff00"},
				{"tohex(rgb(255,0,0))",	"ff0000"},
				{"tohex(rgb(0,255,0))",	"ff00"},
				{"tohex(rgb(0,0,255))",	"ff"},
				{"substring(random(100),0,5)",			"0.722"},
				{"substring('brown',0,3)",	"bro"},
				{"substring('brown',0,100)",	"brown"},
				{"substring('brown',1,8)",	"rown"},
				{"substring('brown',100,3)",	""},
				{"substring('brown',-100,2)",	"br"},
				{"substring('brown',1,0)",	""},
				{"substring('brown',1,1)",	""},
				{"substring('brown',1,2)",	"r"},
				{"substring('brown', 2)",	"own"},
				{"if('Y', 'Greg')",	"Greg"},
				{"if('', 'Greg')",	""},
				{"if('Y', 'Greg', 'Mary')",	"Greg"},
				{"if('', 'Greg', 'Mary')",	"Mary"},
				{"if(1==1, 'Greg')",	"Greg"},
				{"if(0, 'Greg')",	""},
				{"if(15, 'Greg')",	"Greg"},
				{"if(1==1, 'Greg', 'Mary')",	"Greg"},
				{"if(1>3, 'Greg', 'Mary')",	"Mary"},
				{"if(1 AND 2, 'Greg', 'Mary')",	"Greg"},
				{"if(1 AND 0, 'Greg', 'Mary')",	"Mary"},
		};

		for( String[] ss : expressions ) {

			String answer = evaluator.eval(parser.parse(ss[0]));

			//System.err.println(ss[0] + " = " + answer );
			assertEquals( ss[1], answer);
		}
	}

	/**
	 * Invoke operators in a ways that should throw execeptions.
	 *
	 * @throws Exception
	 */
	public void testOperatorErrors() {

		String[] expressions = {
				" 55 % 0",
				" 100/0",
				"NoSuchFunction()",
				"RGB(23)",
				"RGB(100,100,100,233)",
				"ceil('greg')",
				"abs('greg')",
				"floor('greg')",
				"rgb('h', 'i', 'g')",
				"toHex('greg')",
				"substring( 'hello', 'world')",
				"substring( 'hello', 2, 'world')",

		};

		for( String ss : expressions ) {
			try {
				evaluator.evalBoolean(parser.parse(ss));
				assertTrue( "Expression " + ss+ " should have had a runtime error", false );
			} catch (InfixParserException e) {
				assertTrue( "Unexpected parser error on " + ss, false );
			} catch (Exception e) {
				; // Exception expected
			}
		}
	}

	/**
	 * Remove a function from the system.
	 *
	 */
	public void testRemoveFunction() {
		IFunctionDef func = evaluator.findFunction("MIN");

		assertNotNull(func);
		evaluator.removeFunction("MIN");

		func = evaluator.findFunction("MIN");
		assertNull(func);

	}

}
