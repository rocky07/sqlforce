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

import java.util.List;

/**
 * An IParsedExpression is the result of an infix expression that has been parsed by {@link InfixParser}.
 * 
 * @author snort
 *
 */
public interface IParsedExpression {
	
	/**
	 * Return the parsed expression as a list of tokens in postfix order.
	 * 
	 * @return tokens in postfix order.
	 */
	List<IToken> getPostfix();
	
	/**
	 * Determine if another expression is semantically identical to the given expression.
	 * 
	 * @param p check for equality with this expression.
	 * 
	 * @return true if expressions are equal, else false.
	 */
	boolean equals( IParsedExpression p );
	
	/**
	 * Return a list of all identifiers referenced in the expression.
	 * 
	 * Note identifiers are case insensitive. Only the first reference to an indentifier
	 * will be returned.
	 * 
	 * @return identifiers in the expression, empty list if no identifiers.
	 */
	List<String> getIdentifiers();
	
	/**
	 * Return a "psuedo" postfix expression that includes markup that indicates how many arguments a function will consume.
	 * 
	 * The values returned is the precise postfix the for the given infix expression IF the expression does not contain
	 * function calls or unary minus. Examples:
	 * <UL>
	 * <LI> 23 + 14 - 8 returned as 23 14 + 8 -
	 * <LI> Greg * 23 returned as Greg 23 *
	 * </UL>
	 * <P>
	 * If the expression contains a function or unary minus, expect something like:
	 * <UL>
	 * <LI> -23 + 45 returned as 23 -# 45 +
	 * <LI> 23 + max(18,7) returned as 23 18 7 max#2 +
	 * </UL>
	 * <P>
	 * The conversion rules are simple:
	 * <UL>
	 * <LI>Unary minus is returned as -#
	 * <LI>Then number of consumed arguments is appended to function names.
	 * </UL>
	 * <P>
	 * This method is designed to help in debugging code.
	 * 
	 * @return postfix string expression
	 */
	public String toPostfixString();
	

	/**
	 * Return the fully parenthesized infix version of the expression.
	 * 
	 * 
	 * @return infix expression.
	 */
	public String toInfixString();
}
