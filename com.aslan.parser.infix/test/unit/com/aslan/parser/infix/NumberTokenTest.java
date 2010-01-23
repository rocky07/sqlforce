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

import com.aslan.parser.infix.IToken;
import com.aslan.parser.infix.NumberToken;

import junit.framework.TestCase;
/**
 * Coverage test of package scope class NumberToken.
 *
 * @author snort
 *
 */
public class NumberTokenTest extends TestCase {

	public void testNumberToken() {
		NumberToken n;

		n = new NumberToken(23.0);
		assertEquals( "23", n.getName());
		assertEquals( "23", n.getValue());
		assertEquals( IToken.Type.NUMBER, n.getType());
		assertEquals( 23.0, n.getDouble());
		assertEquals( 23, n.getInt());

		n = new NumberToken(23);
		assertEquals( "23", n.getName());
		assertEquals( "23", n.getValue());
		assertEquals( IToken.Type.NUMBER, n.getType());
		assertEquals( 23.0, n.getDouble());
		assertEquals( 23, n.getInt());


		n = new NumberToken( "BrownCow");
		assertEquals( "BrownCow", n.getName());
		assertEquals( "BrownCow", n.getValue());
		assertEquals( IToken.Type.NUMBER, n.getType());
		assertEquals( 0.0, n.getDouble());
		assertEquals( 0, n.getInt());


	}

}
