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
 * 
 * This is the default implementation for the parser String token.
 * 
 * @author snort
 *
 */
public class StringToken implements IStringToken {

	private String value;
	
	/**
	 * Create a new string token.
	 * 
	 * Note that a null value arguement is stored as an empty string.
	 * 
	 * @param value
	 */
	public StringToken(String value ) { 
		this.value = ((value==null)?"":value);
	}
	
	/* (non-Javadoc)
	 * @see com.aslan.parser.infix.IValueToken#getValue()
	 */
	public String getValue() {
		return value;
	}

	/* (non-Javadoc)
	 * @see com.aslan.parser.infix.IToken#getName()
	 */
	public String getName() {
		return value;
	}

	/* (non-Javadoc)
	 * @see com.aslan.parser.infix.IToken#getType()
	 */
	public Type getType() {
		return IToken.Type.STRING;
	}

}
