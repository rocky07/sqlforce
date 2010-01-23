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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.aslan.parser.infix.IToken.Type;

/**
 * Define all built-in operators understood by the parser.
 * 
 * Nothing in the design keeps an user of the package from defining their own
 * operators (except the fact that this class has package scope). Fundamentally,
 * I do not think exposing this feature is a good idea. The need for "externally
 * defined operators" should be satisified by creating new functions (not operators).
 * <P>
 * Bias: I find operator overloading features in C++, Java, etc. intellectually interesting
 * but dangerous in practice.
 * 
 * @author snort
 *
 */
class OperatorRegistry {
	
	/**
	 * Highest Precendence of any operator. No operator in this class actually has this precedence.
	 * The constant is used by the parser.
	 */
	public static final int HIGHEST_PRECEDENCE = 10000;
	
	/**
	 * Lowest Precendence of any operator. No operator in this class actually has this precedence.
	 * The constant is used by the parser.
	 */
	public static final int LOWEST_PRECEDENCE = -10000;
	
	private static OperatorRegistry INSTANCE;
	
	/**
	 * Common concrete class for all operators.
	 * @author snort
	 *
	 */
	class Operator implements IOperatorToken {
		private String name;
		private int nargs;
		private int precedence;
		private Type type;
		
		public Operator( String name, int nargs, int precedence, IToken.Type type ) {
			this.name = name;
			this.nargs = nargs;
			this.precedence = precedence;
			this.type = type;
		}
		
		public int getNArgs() { return nargs; }
		public int getPrecedence() { return precedence; }
		public Type getType() { return type; }
		public String getName() { return name; }
	}
	
	class ListOperator extends Operator implements IListOperatorToken {
		private HashSet<String> set = null;
		public ListOperator(String name, int nargs, int precedence, Type type) {
			super(name, nargs, precedence, type);
		}

		public void setListValue(String value) {
			String[] sA = value.split(",");
			set = new HashSet<String>();
			for(String el: sA) {
				set.add(el);
			}
		}
		
		public IListOperatorToken clone() {
			ListOperator op = new ListOperator(getName(),getNArgs(),getPrecedence(),getType());
			op.set = set;
			return op;
		}


		public boolean contains(String v) {
			return set.contains(v);
		}

	}
	
	private HashMap<String,List<Operator>> operatorNameMap = new HashMap<String,List<Operator>>();
	private HashMap<IToken.Type,Operator> operatorTypeMap = new HashMap<IToken.Type,Operator>();
	
	private OperatorRegistry() {
		
		//
		// Create all operators.
		//
		
		addOperator("-", 1, 100, IToken.Type.OP_UNARY_MINUS);
		
		addOperator( "%", 2, 60, IToken.Type.OP_MOD );
		addOperator( "MOD", 2, 60, IToken.Type.OP_MOD );
		
		addOperator( "*", 2, 50, IToken.Type.OP_MULTIPLY );
		addOperator( "/", 2, 50, IToken.Type.OP_DIVIDE );
		
		addOperator( "+", 2, 40, IToken.Type.OP_ADD );
		addOperator( "-", 2, 40, IToken.Type.OP_SUBTRACT );
		
		addOperator( "==", 2, 30, IToken.Type.OP_EQ );
		addOperator( "=", 2, 30, IToken.Type.OP_EQ );
		addOperator( "!=", 2, 30, IToken.Type.OP_NOT_EQ );
		addOperator( "<>", 2, 30, IToken.Type.OP_NOT_EQ );
		addOperator( ">", 2, 30, IToken.Type.OP_GT );
		addOperator( "<", 2, 30, IToken.Type.OP_LT );
		addOperator( ">=", 2, 30, IToken.Type.OP_GT_EQ );
		addOperator( "<=", 2, 30, IToken.Type.OP_LT_EQ );
		addOperator("STARTS", 2, 30, IToken.Type.OP_STARTS);
		addOperator("ENDS", 2, 30, IToken.Type.OP_ENDS);
		addOperator("CONTAINS", 2, 30, IToken.Type.OP_CONTAINS);
		addOperator("INLIST",1,30,IToken.Type.OP_IN_LIST);

		
		addOperator( "NOT", 1, 25, IToken.Type.OP_NOT );
		addOperator( "!", 1, 25, IToken.Type.OP_NOT );
		addOperator( "AND", 2, 23, IToken.Type.OP_AND );
		addOperator( "&&", 2, 23, IToken.Type.OP_AND );
		addOperator( "OR", 2, 20, IToken.Type.OP_OR );
		addOperator( "||", 2, 20, IToken.Type.OP_OR );
	}

	/**
	 * Register a new operator.
	 * 
	 * @param name textual representation of the operator
	 * @param nargs number of arguments consumed by the operator
	 * @param precedence relative precedence (high == higher precedence)
	 * @param type parser token type.
	 */
	 void addOperator(String name, int nargs, int precedence, IToken.Type type ) {
		Operator op = (type == Type.OP_IN_LIST)? new ListOperator(name, nargs, precedence, type) : new Operator( name, nargs, precedence, type );
		
		operatorTypeMap.put(op.getType(), op );
		//
		// Note that a string may have more than one semantics -- depending on context.
		// Example: Consider unary minus and substraction.
		//
		if( operatorNameMap.containsKey(op.getName()) ) {
			operatorNameMap.get(op.getName()).add(op);
		} else {
			List<Operator> list = new ArrayList<Operator>();
			list.add(op);
			operatorNameMap.put(op.getName(),list);
		}
	}
	
	/**
	 * Return the only instance of the operator registry.
	 * 
	 * @return the operator registry.
	 */
	static public OperatorRegistry getInstance() {
		if(INSTANCE == null ) {
			INSTANCE = new OperatorRegistry();
		}
		return INSTANCE;
	}
	
	/**
	 * Determine if a string is a name of an operator.
	 * 
	 * @param candidate a possible operator
	 * @return true if an operator, else false.
	 */
	public boolean isOperator( String candidate ) {
		return operatorNameMap.containsKey(candidate.toUpperCase() );
	}
	
	/**
	 * Return the descriptor of an operator given its operation type.
	 * 
	 * @param opCode operator type
	 * @return operator descriptor (or null if no such operator  -- this is a operator registration error).
	 */
	public IOperatorToken getOperator( IToken.Type opCode ) {
		IOperatorToken op = operatorTypeMap.get(opCode);
		
		return op;
	}
	
	/**
	 * Return the descriptor of an operator given its name.
	 * 
	 * @param name operator name
	 * @return operator descriptor (or null if no such operator  -- this is a operator registration error).
	 */
	public IOperatorToken getOperator( String name ) {
		
		//
		// There is more than one choice for the the "-" symbol. Unary minus and substraction.
		//
		
		if( "-".equals(name)) {
			return getOperator( IToken.Type.OP_SUBTRACT);
		}
		
		//
		// Normal operator.
		//
		List<Operator> ops = operatorNameMap.get(name.toUpperCase());
		
		if( ops == null ) { return null; }
		if( ops.size() == 1 ) { return ops.get(0); }
		
		
		
		return  null; // Ambigious data initialization error.
	}
}
