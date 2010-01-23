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
 * Built-in operators (==, !=, BETWEEN, etc) are represented by this class.
 * 
 * Unlike functions, the list of operators is not intended to be extended.
 * 
 * @author snort
 *
 */
public interface IOperatorToken extends IToken {
	
	/**
	 * Return the number of arguments consumed by this operator.
	 * 
	 * @return number of arguments consumed.
	 */
	int getNArgs();
	
	/**
	 * Return the precendence of an operator where "high integer values" have higher precendence
	 * than "low integer values".
	 * 
	 * @return precendence of an operator.
	 */
	int getPrecedence();

}
