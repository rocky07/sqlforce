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
 * An IValueToken marks any parser token that is a primitive value (number or string).
 * 
 * @author snort
 *
 */
public interface IValueToken extends IToken {
	/**
	 * Return the value of the token.
	 * 
	 * @return value
	 */
	String getValue();
}
