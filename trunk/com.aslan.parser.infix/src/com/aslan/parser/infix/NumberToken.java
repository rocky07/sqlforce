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
 * This is the default implementation for a parser number.
 * 
 * This class is used internally by the parser and by the postfix evaluator.
 * @author snort
 *
 */
public class NumberToken implements INumberToken {

	private String value;
	private double dValue;
	private int    iValue;
	
	/**
	 * Create a number.
	 * 
	 * @param d value for the number
	 */
	public NumberToken( double d ) {
		this( Double.toString(d));
		if( value.endsWith(".0")) {
			value = value.substring( 0, value.length() - 2 );
		}
	}
	
	/**
	 * Create a number.
	 * 
	 * @param d value for the number
	 */
	public NumberToken(int d ) {
		this( Integer.toString(d));
	}
	
	/**
	 * Create a number from a string.
	 * 
	 * If the supplied value is NOT a valid number, the internal double and int values will be set to
	 * zero and the error will be silently ignored. This means that the values returned by
	 * {@link #getDouble()} and {@link #getInt()} will return zero even though the value returned
	 * by {@link #getValue()} is not a number. The rationale is:
	 * <UL>
	 * <LI>This class is intended to be used by parsers and other codes that have already verified that the
	 * string is a number. We do not want to clutter up their code with try/catch of {@link java.lang.NumberFormatException}
	 * exceptions.
	 * <LI>This class has package scope.
	 * </UL>
	 * 
	 * @param value value for the number
	 */
	public NumberToken(String value ) { 
		this.value = value;
		
		try {
			dValue = Double.parseDouble(value);
			iValue = (int)dValue;
		} catch( NumberFormatException e ) {
			// Ignore the exception..the lexical analyzer already checked the format.
			dValue = 0;
			iValue = 0;
		}
	}
	/* (non-Javadoc)
	 * @see com.aslan.parser.infix.INumberToken#getDouble()
	 */
	public double getDouble() {
		return dValue;
	}

	/* (non-Javadoc)
	 * @see com.aslan.parser.infix.INumberToken#getInt()
	 */
	public int getInt() {
		return iValue;
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
		return IToken.Type.NUMBER;
	}

}
