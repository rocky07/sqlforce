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

import java.util.Stack;

/**
 * This is the lexical analyzer used to bust an expression into tokens.
 * 
 * Its major contribution is to isolate the logic for recognizing quoted strings and identifier names.
 * 
 * @author snort
 *
 */
class LexicalAnalyzer {

	private static final char ESCAPE_CHAR = '\\';
	private static final String QUOTE_CHARS = "\"'";
	
	private SourceStack charStack = new SourceStack();
	
	class SourceStack {
		private Stack<Character>	stack = new Stack<Character>();
		private int	tosPosition = 0;
		
		public SourceStack() {
		}
		void setInput( String data ) {
			stack.clear();
			tosPosition = 0;
			char[] allChars = data.toCharArray();
			
			for( int n = allChars.length; n > 0; n-- ) {
				stack.push( allChars[n - 1]);
			}
		}
		Character peek() { return stack.peek(); }
		Character pop() {tosPosition++; return stack.pop(); }
		void push(Character c) {tosPosition--; stack.push(c); }
		boolean isEmpty() { return stack.isEmpty(); }
		int size() { return stack.size(); }
		int getPosition() { return tosPosition; }
	}
	LexicalAnalyzer() {
	}
	
	/**
	 * Eat all leading whitespace from the input stack.
	 *
	 */
	private void eatWhiteSpace() {
		while( !charStack.isEmpty()) {
			
			if( !Character.isWhitespace(charStack.peek())) { break; }
			charStack.pop();
		}
	}
	
	/**
	 * Recognize a quoted string including escaped characters.
	 * 
	 * If we run out of characters before finding the closing quote, pretend it was there.
	 * 
	 * @return resultant string (can be zero length).
	 */
	private String parseQuotedString() {
		
		Character quote = charStack.pop();
		StringBuffer answer = new StringBuffer();
		boolean lastWasEscape = false;
		
		while( !charStack.isEmpty()) {
			Character c = charStack.pop();
			
			if( c.equals(quote) && !lastWasEscape ) { break; } // Got the complete string
			
			boolean isEscape = c.equals(ESCAPE_CHAR );
			
			if( lastWasEscape || !isEscape ) {
				answer.append(c);
			}
			
			lastWasEscape = lastWasEscape?false:isEscape;
		}
		
		return answer.toString();
	}
	
	/**
	 * Recognize a list of strings separated by "," and surrounded by "[" and "]".
	 * 
	 * If we run out of characters before finding the closing "]", pretend it was there.
	 * 
	 * @return resultant string (can be zero length).
	 */
	private String parseList() {
		
		StringBuffer answer = new StringBuffer();
		charStack.pop(); // Remove "["
		while( !charStack.isEmpty()) {
			Character c = charStack.pop();
			if(c==']') { break; } // Got the complete list
			answer.append(c);
		}
		
		return answer.toString();
	}

	/**
	 * Scan for a java style identifier name.
	 * 
	 * When called, we are guarenteed that the first character on the stack is legal
	 * java identifier.
	 * <p>
	 * An identifier can is defined as one of the following:
	 * <ul>
	 * <li>javaIdentifier
	 * <li>javaIdentifier.javaIdentifier
	 * </ul>
	 * 
	 * @return identifier name.
	 */
	private String parseIdentifier() {
		StringBuffer answer = new StringBuffer();
		Character c = charStack.pop();
		boolean haveModule = false;
		
		answer.append(c);
		while( !charStack.isEmpty()) {
			c = charStack.peek();
			if( !Character.isJavaIdentifierPart(c)) { 
				if( haveModule || c != '.' ) { break; }
				
				
				Character saveC = c;
				charStack.pop();
				c = charStack.peek();
				if( Character.isLetter(c)) {
					answer.append(saveC);
					haveModule = true;
				} else { 
					break;
				}
				
			}
			
			answer.append(c);
			charStack.pop();
			
		}
		
		return answer.toString();
	}
	
	/**
	 * Recognize punctuation character(s).
	 * 
	 * When called, we are guareenteed that the first character on the stack is legal
	 * punctuation.
	 * 
	 * @return punctuation character.
	 */
	private String parsePunctuation() {
		StringBuffer answer = new StringBuffer();
		Character c = charStack.pop();
		OperatorRegistry opReg = OperatorRegistry.getInstance();
		
		answer.append(c);
		while( !charStack.isEmpty()) {
			c = charStack.peek();
			
			if( !opReg.isOperator( answer.toString() + c.toString())) { break; }
			
			answer.append(c);
			charStack.pop();
		}
		return answer.toString();
	}
	
	/**
	 * Recognize a number.
	 * 
	 * When called, we are guareenteed that the first character on the stack is a digit OR the first two
	 * characters are .Digit
	 * 
	 * @return parsed number.
	 */
	private String parseNumber() {
		StringBuffer answer = new StringBuffer();
		Character c = charStack.pop();
		boolean havePeriod = false;
		
		answer.append(c);
		if( '.' == c ) {
			havePeriod = true;
			answer.append( charStack.pop() );
		}
		
		while( !charStack.isEmpty()) {
			c = charStack.peek();
			if( Character.isDigit(c)) {
				answer.append(charStack.pop());
				continue;
			}
			
			if( !havePeriod && '.' == c ) {
				answer.append(charStack.pop());
				havePeriod = true;
				continue;
			}
			
			break; // 6-Jul-07 Coverage tool reported as not-covered by unit test. print says otherwise.
		}
		return answer.toString();
	}
	/**
	 * Set the input data for the analyzer (forget about any previous data).
	 * 
	 * Note that a copy of the data is made to a local stack.
	 * 
	 * @param data lexically analyze this data.
	 */
	public void  setInput( String data ) {
		charStack.setInput(data);
	}
	
	/**
	 * Return the next lexical token in the stream -- null if there are no more tokens.
	 * 
	 * @return next token or null if there is no more data.
	 */
	public LexicalToken getToken() {
		
		int	position;
		
		eatWhiteSpace();
		if( charStack.isEmpty()) { return null; } // Out of data.
		
		//
		// Look for a quoted string.
		//
		Character firstC = charStack.peek();
		if( QUOTE_CHARS.contains(firstC.toString())) {
			position = charStack.getPosition();
			return new LexicalToken( LexicalToken.Type.STRING, position, parseQuotedString());
		}
		
		//
		// Look for an identifier.
		//
		// If the identifier is followed by a open paren, then consider it a function call.
		// Otherwise it is a normal identifer.
		//
		if( Character.isJavaIdentifierStart(firstC)) {
			position = charStack.getPosition();
			String name = parseIdentifier();
			
			eatWhiteSpace();
			Character c = charStack.isEmpty()?null:charStack.peek();
			OperatorRegistry opRegistry = OperatorRegistry.getInstance();
			
			if( c != null && "(".equals(c.toString()) && !opRegistry.isOperator(name)) {
				charStack.pop();
				return new LexicalToken( LexicalToken.Type.FUNCTION, position, name );
			} else {
				return new LexicalToken( LexicalToken.Type.IDENTIFIER, position, name );
			}
		}
		
		//
		// Look for a number using java built in parser to recognized a variety of number formats.
		//
		if( Character.isDigit(firstC)) {
			position = charStack.getPosition();
			return new LexicalToken( LexicalToken.Type.NUMBER, position, parseNumber());
		} else if( '.'==firstC && charStack.size()>1 ){
			charStack.pop(); // pop firstC off of the stack.
			Character c = charStack.peek();
			charStack.push(firstC);
			
			if( Character.isDigit(c)) {
				position = charStack.getPosition();
				return new LexicalToken( LexicalToken.Type.NUMBER, position, parseNumber());
			}
		} else if( '['==firstC ) {
			position = charStack.getPosition();
			return new LexicalToken( LexicalToken.Type.LIST, position, parseList());
		}
		//
		// If found something that must be punctuation.
		//
		// A few punctuation markers are more than a single character (example: ==, =>, !=, etc.).
		// Recognize these here.
		//
		position = charStack.getPosition();
		String punctuation = parsePunctuation();
		return new LexicalToken(LexicalToken.Type.PUNCTUATION, position, punctuation);

	}
	
}
