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
package com.aslan.sfdc.sqlforce.test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import com.aslan.sfdc.sqlforce.LexicalAnalyzer;
import com.aslan.sfdc.sqlforce.LexicalToken;
import junit.framework.TestCase;

/**
 * LexicalAnalyzer Tests
 * @author greg
 *
 */
public class LexicalAnalyzerTest extends TestCase {

	private ByteArrayOutputStream outStream = new ByteArrayOutputStream();;
	private PrintStream outPrintStream = new PrintStream(outStream);

	class  AlwaysFailStream extends InputStream {

		@Override
		public int read() throws IOException {
			throw new IOException("I Always Fail");
		}
		
	}
	protected void setUp() throws Exception {
		
		outPrintStream.flush();
		outStream.reset();
	
	}
	
	private LexicalAnalyzer makeLex( String inputString ) {
		ByteArrayInputStream inStream = new ByteArrayInputStream( inputString.getBytes());
		
		outPrintStream.flush();
		outStream.reset();
		
		return new LexicalAnalyzer(inStream, outPrintStream);
	}
	/**
	 * Test method for {@link com.aslan.sfdc.sqlforce.LexicalAnalyzer#getToken()}.
	 */
	public void testGetToken() {
		LexicalAnalyzer lex;
		LexicalToken token;
		
		lex = makeLex( "Identifier" );
		token = lex.getToken();
		assertEquals( LexicalToken.Type.IDENTIFIER, token.getType());
		assertEquals( "Identifier", token.getValue());
		
		lex = makeLex( "Identifier.Extra" );
		token = lex.getToken();
		assertEquals( LexicalToken.Type.IDENTIFIER, token.getType());
		assertEquals( "Identifier.Extra", token.getValue());
		
		for( String punc : new String[] { ">", "<", ">=", "<=", "!=" } ) {
			lex = makeLex( punc );
			token = lex.getToken();
			assertEquals( LexicalToken.Type.PUNCTUATION, token.getType());
			assertEquals( punc, token.getValue());
		}
		
		lex = makeLex( "" );
		token = lex.getToken();
		assertNull( token );
		
		lex = makeLex( "'Brown Cow'" );
		token = lex.getToken();
		assertEquals( LexicalToken.Type.STRING, token.getType());
		assertEquals( "Brown Cow", token.getValue());
		
		lex = makeLex( "\"Brown Cow\"" );
		token = lex.getToken();
		assertEquals( LexicalToken.Type.STRING, token.getType());
		assertEquals( "Brown Cow", token.getValue());
		
		
		lex = makeLex( "'Brown \\'Cow'" );
		token = lex.getToken();
		assertEquals( LexicalToken.Type.STRING, token.getType());
		assertEquals( "Brown \\'Cow", token.getValue());
		
		
		lex = makeLex( "23" );
		token = lex.getToken();
		assertEquals( LexicalToken.Type.NUMBER, token.getType());
		assertEquals( "23", token.getValue());
		
		lex = makeLex( "23.5" );
		token = lex.getToken();
		assertEquals( LexicalToken.Type.NUMBER, token.getType());
		assertEquals( "23.5", token.getValue());
		
		lex = makeLex( ".5" );
		token = lex.getToken();
		assertEquals( LexicalToken.Type.NUMBER, token.getType());
		assertEquals( ".5", token.getValue());
		
		lex = makeLex( "'hello\\\nworld'" );
		token = lex.getToken();
		assertEquals( LexicalToken.Type.STRING, token.getType());
		assertEquals( "hello\\\nworld", token.getValue());
		
		lex = makeLex( "\\\nHello" );
		token = lex.getToken();
		assertEquals( LexicalToken.Type.IDENTIFIER, token.getType());
		assertEquals( "Hello", token.getValue());
		
		lex = makeLex("");
		try {
			token = lex.getToken();
			assertNull(token);
		} catch( Exception e ) {}
	}

	/**
	 * Test method for {@link com.aslan.sfdc.sqlforce.LexicalAnalyzer#getToken(boolean)}.
	 */
	public void testGetTokenIgnoreEOL() {
		LexicalAnalyzer lex;
		LexicalToken token;
		
		lex = makeLex( "\n" );
		token = lex.getToken(false);
		assertEquals( LexicalToken.Type.END_OF_LINE, token.getType());
		assertEquals( "\n", token.getValue());
		
		lex = makeLex( "\n" );
		token = lex.getToken(true);
		assertNull(token);
	}

	/**
	 * Test method for {@link com.aslan.sfdc.sqlforce.LexicalAnalyzer#getNonBlankToken()}.
	 */
	public void testGetNonBlankToken() {
		LexicalAnalyzer lex;
		LexicalToken token;
		
		lex = makeLex( "  fred " );
		try {
			token = lex.getNonBlankToken();
			assertEquals( "fred", token.getValue());
		} catch (Exception e) {
			fail("Did not find next non-blank token");
		}
		
		lex = makeLex( "   " );
		try {
			token = lex.getNonBlankToken();
			assertNull( token );
		} catch (Exception e) {
			;
		}
		
		lex = makeLex( "" );
		try {
			token = lex.getNonBlankToken();
			assertNull(token);
		} catch (Exception e) {
			;
		}
		
	}

	/**
	 * Test method for {@link com.aslan.sfdc.sqlforce.LexicalAnalyzer#getToken(boolean, com.aslan.sfdc.sqlforce.LexicalToken.Type)}.
	 */
	public void testGetTokenSpecificType() throws Exception {
		LexicalAnalyzer lex;
		LexicalToken token;
		
		//
		// Test cases where the scan cannot cross new line barriers.
		//
		lex = makeLex( "fred" );
		token = lex.getToken( false, LexicalToken.Type.IDENTIFIER);
		assertEquals( LexicalToken.Type.IDENTIFIER, token.getType());
		assertEquals( "fred", token.getValue());
		
		lex = makeLex( "'Hello'");
		token = lex.getToken( false, LexicalToken.Type.STRING);
		assertEquals( LexicalToken.Type.STRING, token.getType());
		assertEquals( "Hello", token.getValue());
		
		lex = makeLex( "'Hello");
		token = lex.getToken( false, LexicalToken.Type.STRING);
		assertEquals( LexicalToken.Type.STRING, token.getType());
		assertEquals( "Hello\n", token.getValue());
		
		try {
			lex = makeLex( "fred" );
			token = lex.getToken( false, LexicalToken.Type.NUMBER);
		} catch( Exception e ){}
		
		try {
			lex = makeLex( "\nfred" );
			token = lex.getToken( false, LexicalToken.Type.IDENTIFIER);
		} catch( Exception e ){}
		//
		// Test cases where the scan can cross new line barriers.
		//
		lex = makeLex( "fred" );
		token = lex.getToken( true, LexicalToken.Type.IDENTIFIER);
		assertEquals( LexicalToken.Type.IDENTIFIER, token.getType());
		assertEquals( "fred", token.getValue());
		
		try {
			lex = makeLex( "\n\nfred" );
			token = lex.getToken( true, LexicalToken.Type.NUMBER);
		} catch( Exception e ){}
	}

	/**
	 * Test method for {@link com.aslan.sfdc.sqlforce.LexicalAnalyzer#getToken(com.aslan.sfdc.sqlforce.LexicalToken.Type)}.
	 */
	public void testGetTokenSpecificTypeNoNewLines() throws Exception {
		LexicalAnalyzer lex;
		LexicalToken token;
		
		//
		// Test cases where the scan cannot cross new line barriers.
		//
		lex = makeLex( "fred" );
		token = lex.getToken( LexicalToken.Type.IDENTIFIER);
		assertEquals( LexicalToken.Type.IDENTIFIER, token.getType());
		assertEquals( "fred", token.getValue());
		
		try {
			lex = makeLex( "fred" );
			token = lex.getToken( LexicalToken.Type.NUMBER);
		} catch( Exception e ){}
		
		try {
			lex = makeLex( "\nfred" );
			token = lex.getToken( LexicalToken.Type.IDENTIFIER);
		} catch( Exception e ){}
		
		lex = makeLex("");
		try {
			token = lex.getToken(LexicalToken.Type.IDENTIFIER);
			fail("Found token at end of file");
		} catch( Exception e ) {}
	}

	/**
	 * Test method for {@link com.aslan.sfdc.sqlforce.LexicalAnalyzer#getToken(boolean, java.lang.String)}.
	 */
	public void testGetTokenExpectedValue() throws Exception {
		LexicalAnalyzer lex;
		LexicalToken token;
		
		//
		// Test cases where the scan cannot cross new line barriers.
		//
		lex = makeLex( "23" );
		token = lex.getToken( false, "23");
		assertEquals( "23", token.getValue());
		
		try {
			lex = makeLex( "\nfred" );
			token = lex.getToken( false, "fred");
			fail("Unexpected cross of newline barrier");
		} catch( Exception e ){}
		
		try {
			lex = makeLex( "gfred" );
			token = lex.getToken( false, "fred");
			fail("Found token with name did not match.");
		} catch( Exception e ){}
		
		lex = makeLex("");
		try {
			token = lex.getToken(false, "fred");
			fail("Found token at end of file");
		} catch( Exception e ) {}
		
		//
		// Test cases where the scan can cross new line barriers.
		//
		lex = makeLex( "\nfred" );
		token = lex.getToken( true, "fred");
		assertEquals( "fred", token.getValue());
		
		try {
			lex = makeLex( "\nfoo" );
			token = lex.getToken( true, "fred");
			fail("Found token with name did not match.");
		} catch( Exception e ){}
	}

	/**
	 * Test method for {@link com.aslan.sfdc.sqlforce.LexicalAnalyzer#getToken(java.lang.String)}.
	 */
	public void testGetTokenExpectedValueNoNewLine() throws Exception {
		
		LexicalAnalyzer lex;
		LexicalToken token;
		
		//
		// Test cases where the scan cannot cross new line barriers.
		//
		lex = makeLex( "100" );
		token = lex.getToken( "100");
		assertEquals( "100", token.getValue());
		
		try {
			lex = makeLex( "\nfred" );
			token = lex.getToken( "fred");
			fail("Unexpected cross of newline barrier");
		} catch( Exception e ){}
		
		try {
			lex = makeLex( "gfred" );
			token = lex.getToken( "fred");
			fail("Found token with name did not match.");
		} catch( Exception e ){}
		
		
	}

	/**
	 * Test method for {@link com.aslan.sfdc.sqlforce.LexicalAnalyzer#getToken(boolean, java.lang.String[])}.
	 */
	public void testGetTokenOneOfASet() throws Exception {
		
		LexicalAnalyzer lex;
		LexicalToken token;
		String[] keywords = new String[] { "A", "B", "C" };
		
		//
		// All on one line cases.
		//
		lex = makeLex("A");
		token = lex.getToken( false, keywords );
		assertEquals( "A", token.getValue());
		
		lex = makeLex("a");
		token = lex.getToken( false, keywords );
		assertEquals( "a", token.getValue());
		
		try {
			lex = makeLex("D");
			token = lex.getToken( false, keywords );
			fail("Got a token when input should not match");
		} catch( Exception e ) {}
		
		try {
			lex = makeLex("");
			token = lex.getToken( false, keywords );
			fail("Got a token when at end of file");
		} catch( Exception e ) {}
		
		//
		// Cross end of linen cases
		//
		lex = makeLex("\nA");
		token = lex.getToken( true, keywords );
		assertEquals( "A", token.getValue());
		
		
	}

	/**
	 * Test method for {@link com.aslan.sfdc.sqlforce.LexicalAnalyzer#getToken(java.lang.String[])}.
	 */
	public void testGetTokenOneOfASetNoNewLine() throws Exception{
		LexicalAnalyzer lex;
		LexicalToken token;
		String[] keywords = new String[] { "A", "B", "C" };
		
		lex = makeLex("A");
		token = lex.getToken( keywords );
		assertEquals( "A", token.getValue());
		
		lex = makeLex("a");
		token = lex.getToken( keywords );
		assertEquals( "a", token.getValue());
		
		//
		// Cross end of linen cases
		//
		try {
			lex = makeLex("");
			token = lex.getToken( keywords );
			fail("Got a token when at end of line");
		} catch( Exception e ) {}
	}

	/**
	 * Test method for {@link com.aslan.sfdc.sqlforce.LexicalAnalyzer#getKeyword(boolean, java.lang.String)}.
	 */
	public void testGetKeywordBooleanString() throws Exception {
		LexicalAnalyzer lex;
		LexicalToken token;
		String KEYWORD = "MARY";
		
		//
		// All on one line cases.
		//
		lex = makeLex(KEYWORD);
		token = lex.getKeyword( false, KEYWORD );
		assertEquals( KEYWORD, token.getValue());
		
		lex = makeLex(KEYWORD.toLowerCase());
		token = lex.getKeyword( false, KEYWORD );
		assertEquals( KEYWORD.toLowerCase(), token.getValue());
		
		try {
			lex = makeLex("NOTHEKEYWORD");
			token = lex.getKeyword( false, KEYWORD );
			fail("Got a token when input should not match");
		} catch( Exception e ) {}
		
		try {
			lex = makeLex("");
			token = lex.getKeyword( false, KEYWORD );
			fail("Got a token when at end of file");
		} catch( Exception e ) {}
		
		//
		// Cross end of linen cases
		//
		lex = makeLex("\n" + KEYWORD);
		token = lex.getKeyword( true, KEYWORD );
		assertEquals( KEYWORD, token.getValue());
		
	}

	/**
	 * Test method for {@link com.aslan.sfdc.sqlforce.LexicalAnalyzer#getKeyword(java.lang.String)}.
	 */
	public void testGetKeywordString() throws Exception {
		LexicalAnalyzer lex;
		LexicalToken token;
		String KEYWORD = "MARY";
		
		lex = makeLex(KEYWORD);
		token = lex.getKeyword( KEYWORD );
		assertEquals( KEYWORD, token.getValue());
		
		//
		// Cross end of linen cases
		//
		try {
			lex = makeLex("\n" + KEYWORD);
			token = lex.getKeyword( KEYWORD );
			fail("Got a token that crossed a line");
		} catch( Exception e ) {}
	}

	public void testGetLetterDigitToken() throws Exception {
		LexicalAnalyzer lex;
		LexicalToken token;
		
		lex = makeLex("003abc=fred");
		token = lex.getLetterDigitToken(false);
		assertEquals( "003abc", token.getValue());
		
		
		lex = makeLex("                003abc =fred");
		token = lex.getLetterDigitToken(false);
		assertEquals( "003abc", token.getValue());
		
		lex = makeLex("                003abc\nhello =fred");
		token = lex.getLetterDigitToken(false);
		assertEquals( "003abc", token.getValue());
		
		//
		// Cross end of line cases
		//
		
		lex = makeLex("\n\n003abc=fred");
		token = lex.getLetterDigitToken(true);
		assertEquals( "003abc", token.getValue());
		
		lex = makeLex("\n\n003abc\bdef=fred");
		token = lex.getLetterDigitToken(true);
		assertEquals( "003abc", token.getValue());
		try {
			lex = makeLex("\n\n003abc=fred");
			token = lex.getLetterDigitToken(false);
			fail("Got a letterDigit token that crossed a line");
		} catch( Exception e ) {}

	}
	
	public void testGetDate() throws Exception {
		LexicalAnalyzer lex;
		LexicalToken token;
		
		lex = makeLex("2010-12-03");
		token = lex.getToken(false);
		assertEquals( "2010-12-03", token.getValue());
		assertEquals( LexicalToken.Type.DATE, token.getType());
		
		lex = makeLex("2010-1-3");
		token = lex.getToken(false);
		assertEquals( "2010-01-03", token.getValue());
		assertEquals( LexicalToken.Type.DATE, token.getType());

	}
	
	public void testGetDateTime() throws Exception {
		LexicalAnalyzer lex;
		LexicalToken token;
		
		lex = makeLex("2010-12-03T12:13:45Z");
		token = lex.getToken(false);
		assertEquals( "2010-12-03T12:13:45Z", token.getValue());
		assertEquals( LexicalToken.Type.DATETIME, token.getType());
		
		lex = makeLex("2010-1-03T8:1:4Z");
		token = lex.getToken(false);
		assertEquals( "2010-01-03T08:01:04Z", token.getValue());
		assertEquals( LexicalToken.Type.DATETIME, token.getType());
		
		lex = makeLex("2010-1-03T8:1:4.023Z");
		token = lex.getToken(false);
		assertEquals( "2010-01-03T08:01:04.023Z", token.getValue());
		assertEquals( LexicalToken.Type.DATETIME, token.getType());
		
		lex = makeLex("2010-12-03T12:13:45+08:23");
		token = lex.getToken(false);
		assertEquals( "2010-12-03T12:13:45+08:23", token.getValue());
		assertEquals( LexicalToken.Type.DATETIME, token.getType());
		
		lex = makeLex("2010-12-03T12:13:45-8:05");
		token = lex.getToken(false);
		assertEquals( "2010-12-03T12:13:45-08:05", token.getValue());
		assertEquals( LexicalToken.Type.DATETIME, token.getType());
		
		lex = makeLex("2010-12-03T12:13:45.101-8:05");
		token = lex.getToken(false);
		assertEquals( "2010-12-03T12:13:45.101-08:05", token.getValue());
		assertEquals( LexicalToken.Type.DATETIME, token.getType());
		
		

	}
	/**
	 * Test method for {@link com.aslan.sfdc.sqlforce.LexicalAnalyzer#readLine()}.
	 */
	public void testReadLine() throws Exception{
		LexicalAnalyzer lex;
		LexicalToken token;
		
		
		//
		// All on one line cases.
		//
		lex = makeLex("hello world");
		token = lex.readLine();
		assertEquals( "hello world", token.getValue());
		
		lex = makeLex("hello world\n");
		token = lex.readLine();
		assertEquals( "hello world", token.getValue());
		
		lex = makeLex("\n");
		token = lex.readLine();
		assertEquals( "", token.getValue());
		
		lex = makeLex("");
		token = lex.readLine();
		assertNull(token);
		
		lex = makeLex("Hello\\\nWorld");
		token = lex.readLine();
		assertEquals("Hello\nWorld", token.getValue());
	}

	/**
	 * Test method for {@link com.aslan.sfdc.sqlforce.LexicalAnalyzer#getLastToken()}.
	 */
	public void testGetLastToken() {
		LexicalAnalyzer lex;
		LexicalToken token;
		
		lex = makeLex("Mary");
		assertNull( lex.getLastToken());
		token = lex.getToken();
		
		assertNotNull( lex.getLastToken());
		assertEquals( token, lex.getLastToken());
		
	}

	/**
	 * Test method for {@link com.aslan.sfdc.sqlforce.LexicalAnalyzer#consume(java.lang.String)}.
	 */
	public void testConsume() throws Exception {
		LexicalAnalyzer lex;
		LexicalToken token;
		
		lex = makeLex("Mary; Greg");
		lex.consume( ";");
		token = lex.getToken();
		assertEquals( "Greg", token.getValue());
		
		lex = makeLex("");
		lex.consume(";");
		
		lex = makeLex( ";Hello");
		lex.getToken();
		lex.consume(";");
		token = lex.getToken();
		assertEquals( "Hello", token.getValue());
		
		lex.consume(";"); // Consume after end of input.
		

	}

	/**
	 * Test method for {@link com.aslan.sfdc.sqlforce.LexicalAnalyzer#include(java.io.InputStream)}.
	 */
	public void testIncludeInputStream() throws Exception {
		LexicalAnalyzer lex;
		LexicalToken token;
		ByteArrayInputStream inStream = new ByteArrayInputStream("Hello".getBytes());
		
		lex = makeLex("World");
		lex.include(inStream);
		token = lex.getToken();
		assertEquals( "Hello", token.getValue());
		
		lex.consume("\n");
		token = lex.getToken();
		assertEquals( "World", token.getValue());
	}

	/**
	 * Test method for {@link com.aslan.sfdc.sqlforce.LexicalAnalyzer#include(java.lang.String)}.
	 */
	public void testIncludeString() {
		LexicalAnalyzer lex;
		LexicalToken token;
		
		lex = makeLex("World");
		lex.include("Hello");
		token = lex.getToken();
		assertEquals( "Hello", token.getValue());
		
		lex.consume("\n");
		token = lex.getToken();
		assertEquals( "World", token.getValue());
		
	}

	/**
	 * Test method for {@link com.aslan.sfdc.sqlforce.LexicalAnalyzer#setPrompt(java.lang.String)}.
	 */
	public void testSetPrompt() {
		LexicalAnalyzer lex;
		
		lex = makeLex("");
		lex.setPrompt( "Hello");
		assertEquals( "Hello", lex.getPrompt());
		
		lex.setPrompt( (String)null);
		assertNull( lex.getPrompt());
		
		lex.setPrompt( "" );
		assertNull( lex.getPrompt());

	}

	/**
	 * Test method for {@link com.aslan.sfdc.sqlforce.LexicalAnalyzer#stripQuotes(java.lang.String)}.
	 */
	public void testStripQuotes() {
		LexicalAnalyzer lex;
		
		lex = makeLex("");
		
		assertEquals( "Hello", lex.stripQuotes("\"Hello\""));
		assertEquals( "Hello", lex.stripQuotes("'Hello'"));
		assertEquals( "Hello", lex.stripQuotes("Hello"));
		assertEquals( "'", lex.stripQuotes("'"));
		assertEquals( "", lex.stripQuotes(""));
		assertNull( lex.stripQuotes( (String)null));
	}

	/**
	 * Force an unexpected IO Exception in the lex reader.
	 */
	public void testForceIOException() {
		
		outPrintStream.flush();
		outStream.reset();
		
		LexicalAnalyzer lex =  new LexicalAnalyzer(new AlwaysFailStream(), outPrintStream);
		outPrintStream.close();
		
		try {
			lex.getToken();
		} catch( Exception e ) {}
	}
}
