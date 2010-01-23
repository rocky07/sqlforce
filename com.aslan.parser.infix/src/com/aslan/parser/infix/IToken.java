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
 * The expression return by {@link InfixParser} are constructed of a list of instances of this class.
 * @author snort
 *
 */
public interface IToken {
	/**
	 * All types of tokens returned by the parser.
	 * 
	 */
	enum Type {
		STRING,
		NUMBER,
		
		IDENTIFIER, // Value that must be supplied externally.
		
		FUNCTION,	// A built-in or user defined function.
		
		OP_UNARY_MINUS,
	
		//
		// Comparison operators
		//
		OP_EQ,
		OP_NOT_EQ,
		OP_GT,
		OP_LT,
		OP_GT_EQ,
		OP_LT_EQ,
		OP_IN_LIST,
		
		//
		// Mathematical operators
		//
		OP_MOD,
		
		OP_MULTIPLY,
		OP_DIVIDE,
		
		OP_ADD,			// May also be string concatenation
		OP_SUBTRACT,
		
		//
		// Logical operators
		//
		OP_NOT,
		OP_AND,
		OP_OR,
		
		//
		// String operators
		//
		OP_STARTS,
		OP_ENDS,
		OP_CONTAINS,
	}
	
	/**
	 * Return the basic token type.
	 * 
	 *
	 * @return token type
	 */
	Type getType();
	
	/**
	 * Return the "name" of the token.
	 * 
	 * What this means depends on the token. Operators return their ascii representation. Strings
	 * and numbers return their associated value.
	 * 
	 * @return token name.
	 */
	String getName();
}
