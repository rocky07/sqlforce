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
 * A boolean parser token is a number whose value is always 1 (true) or 0 (false).
 * 
 *
 * 
 * @author gsmithfarmer@gmail.com
 *
 */
public interface IBooleanToken extends INumberToken {
	public static final int TRUE = 1;
	public static final int FALSE = 0;
}
