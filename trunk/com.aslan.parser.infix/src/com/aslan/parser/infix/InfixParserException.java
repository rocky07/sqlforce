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
 * Generic exception thrown on any sort of parser error.
 * 
 * @author snort
 *
 */
public class InfixParserException extends Exception {

	private static final long serialVersionUID = 1L;

	public InfixParserException(String reason) {
		super(reason);
	}
	
	/**
	 * Create an exception where the particular location in an expression is known.
	 * 
	 * @param reason description of the problem
	 * @param expression full infix expression that is being parsed.
	 * @param token token where the problem was found.
	 */
	InfixParserException( String reason, String expression, LexicalToken token ) {
		super( reason + " : " + markupExpression( expression, token));
		
		
	}
	
	private static String markupExpression( String expression, LexicalToken token ) {
		
		final String MARKUP_START = ">>>";
		final String MARKUP_END = "<<<";
		
		if( expression == null || token==null || expression.length() == 0 ) { return expression; }
		
		int	nChars = expression.length();
		int n = token.getPosition();
		
		if( n < 0 ) {
			return expression;
		}
		
		if( n >= nChars ) {
			return expression + MARKUP_START + MARKUP_END;
		}
		
		return expression.substring(0, n) 
			+ MARKUP_START + expression.substring(n,n+1) 
			+ MARKUP_END + expression.substring(n+1);
	}
}
