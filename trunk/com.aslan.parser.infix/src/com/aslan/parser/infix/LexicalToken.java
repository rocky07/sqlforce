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

/**
 * Representation of a lexical token.
 * 
 * Note THIS IS NOT the type of token present to an user of this package. This is the
 * type used to communication with {@link LexicalAnalyzer}.
 * 
 * @author snort
 *
 */
class LexicalToken {

	/**
	 * All types of tokens recognized by the lexical analyzer.
	 * 
	 */
	enum Type {
		STRING,
		NUMBER,
		IDENTIFIER,
		PUNCTUATION,
		FUNCTION,
		LIST
	}
	
	private Type tokenType;
	private String value;
	private int position;
	
	/**
	 * Create a new lexical token.
	 * 
	 * @param type type of token
	 * @param position index of first char of the value in the lexical input string.
	 * @param value from the expression (for strings, quotes should be removed).
	 */
	public LexicalToken( Type type, int position, String value ) {
		this.tokenType = type;
		this.position = position;
		this.value = value;
	}
	
	/**
	 * Return the token type.
	 * 
	 * @return token type.
	 */
	public Type getType() { return tokenType; }
	
	/**
	 * Return the value of the token.
	 * 
	 * @return token value (from the expression).
	 */
	public String getValue() { return value; }

	/**
	 * Return the index (0 - n-1) of the value of this token in the original lexical input string.
	 * 
	 * This data can help a caller give a better diagnostic message.
	 * 
	 * @return token postion.
	 */
	public int getPosition() { return position; }
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		
		return tokenType.toString() + "[" + value + "]";
	}
	
	
}
