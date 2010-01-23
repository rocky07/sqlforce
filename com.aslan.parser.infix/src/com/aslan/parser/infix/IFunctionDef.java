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

/**
 * Define a function that can be invoked when evaluating a postfix expression
 * using {@link PostfixEvaluator}.
 * 
 * 
 * @author snort
 *
 */
public interface IFunctionDef {
	
	/**
	 * Return the name of the function (as it will appear in an postfix (or infix) expression).
	 * 
	 * Example: If "MAX( 12, 13, 14)" is legal, then the name return by this code for the "MAX" function
	 * should be "MAX" (case does not matter).
	 * 
	 * @return function name.
	 */
	String getName();
	
	/**
	 * Return the minimum number of arguments that should be passed to the {@link #eval(IValueToken[])} method.
	 * 
	 * @return minimum number of acceptable arguments.
	 */
	int getMinArgs();
	
	/**
	 * Return the maximum number of arguments that should be passed to the {@link #eval(IValueToken[])} method.
	 * 
	 * @return maximum number of acceptable arguments.
	 */
	int getMaxArgs();
	
	
	/**
	 * Compute a new token for given a list of arguments (possible zero length).
	 * 
	 * It is good practice for a function to deal with the arguments it is given (if at all possible).
	 * Throwing an exceptoin is good practice when:
	 * <UL>
	 * <LI>A argument cannot be converted to the correct type (e.g. the function wants a number but the
	 * caller provided the string 'Grgg'.
	 * <LI>The range of the argument is invalid. Example: the function requires a positive integer and the
	 * argument is negative.
	 * </UL>
	 * <P>
	 * This method can assume that the argList will contains between {@link #getMinArgs()} and
	 * {@link #getMaxArgs()} values.
	 * 
	 * @param argList function parameters (may be zero length).
	 * @return a value computed by the function.
	 * @throws Exception on any sort of error (including invalid arguments).
	 */
	IValueToken eval( IValueToken[] argList ) throws Exception;

}
