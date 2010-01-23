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
 * Parser numeric token type.
 * 
 * @author snort
 *
 */
public interface INumberToken extends IValueToken {

	/**
	 * Return the value of the number as a double.
	 * 
	 * @return the value.
	 */
	double getDouble();
	
	/**
	 * Return the value of the number as an int (truncate if required).
	 * 
	 * @return the value
	 */
	int getInt();
}
