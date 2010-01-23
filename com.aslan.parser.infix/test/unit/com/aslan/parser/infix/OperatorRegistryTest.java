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

import com.aslan.parser.infix.IListOperatorToken;
import com.aslan.parser.infix.IOperatorToken;
import com.aslan.parser.infix.IToken;
import com.aslan.parser.infix.OperatorRegistry;

import junit.framework.TestCase;

public class OperatorRegistryTest extends TestCase {

	private OperatorRegistry registry;

	public void setUp() throws Exception {
		registry = OperatorRegistry.getInstance();
	}

	public void testAddOperator() {

		//
		// Add a new operator
		//
		registry.addOperator( "@@@", 2, 1001, IToken.Type.OP_ADD );

		IOperatorToken token = registry.getOperator("@@@");
		assertNotNull(token);
		assertEquals( "@@@", token.getName());
		assertEquals( 2, token.getNArgs());
		assertEquals( 1001, token.getPrecedence());

		//
		// Overload an existing operator.
		//
		registry.addOperator( "@@@", 2, 1001, IToken.Type.OP_LT );
		token = registry.getOperator("@@@");
		assertNull(token);

		assertTrue( registry.isOperator( "@@@"));
	}

	public void testIsOperator() {

		assertTrue( registry.isOperator( "+" ));
		assertFalse( registry.isOperator( "freddy"));
		assertTrue( registry.isOperator( "INLIST" ));
	}

	public void testGetOperator() {

		assertNotNull( registry.getOperator("+"));
		assertNotNull( registry.getOperator("-"));
		assertNull( registry.getOperator( "HolyMoly"));
	}
	
	public void testListOperator() {
		IOperatorToken token = registry.getOperator("INLIST");
		assertNotNull( token );
		assertTrue( token instanceof IListOperatorToken);
		IListOperatorToken lOp = ((IListOperatorToken)token).clone();
		lOp.setListValue("1,2");
		assertTrue(lOp.contains("1"));
		assertTrue(lOp.contains("2"));
		assertFalse(lOp.contains("3"));
		assertFalse(lOp.contains("1,2"));
	}

}
