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
import java.util.HashSet;
import java.util.List;
import java.util.Stack;

/**
 * Implementation of an expression parsed by {@link InfixParser}.
 * 
 * @author snort
 *
 */
class ParsedExpression implements IParsedExpression {

	private List<IToken> postfix;
	
	/**
	 * Create a parsed expression.
	 * @param postfix
	 */
	ParsedExpression( List<IToken> postfix ) {
		this.postfix = postfix;
	}
	/* (non-Javadoc)
	 * @see com.aslan.parser.infix.IParsedExpression#getPostfix()
	 */
	public List<IToken> getPostfix() {
		return postfix;
	}
	
	
	/* (non-Javadoc)
	 * @see com.aslan.parser.infix.IParsedExpression#getIdentifiers()
	 */
	public List<String> getIdentifiers() {
		HashSet<String> hash = new HashSet<String>();
		ArrayList<String> list = new ArrayList<String>();
		
		for( IToken token : getPostfix() ) {
			if( IToken.Type.IDENTIFIER != token.getType()) { continue; }
			if( hash.contains( token.getName().toUpperCase())) { continue; }
			
			hash.add( token.getName().toUpperCase());
			list.add(token.getName());
		}
		return list;
	}
	
	/* (non-Javadoc)
	 * @see com.aslan.parser.infix.IParsedExpression#equals(com.aslan.parser.infix.IParsedExpression)
	 */
	public boolean equals(IParsedExpression p) {

		if( p == null ) { return false; }
		
		List<IToken> listA = getPostfix();
		List<IToken> listB = p.getPostfix();
		
		//
		// Check cases where a token by token comparison is not required.
		//
		if( listA==null && listB==null) { return true; }
		if( listA==null && listB!=null) { return false; }
		if( listA!=null && listB==null) { return false; }
		if( listA.size() != listB.size()) {return false; }
		
		//
		// Do a token by token check.
		//
		for( int n=0; n < listA.size(); n++ ) {
			IToken a = listA.get(n);
			IToken b = listB.get(n);
			
			if( a.getType() != b.getType()) { return false; }
			
			String valueA = a.getName();
			String valueB = b.getName();
			
			if( a instanceof INumberToken ) {
				INumberToken aNum = (INumberToken)a;
				INumberToken bNum = (INumberToken)b;
				if( aNum.getDouble() != bNum.getDouble()) { return false; }
				
			} else if( a instanceof IFunctionToken ) {
				
				if( !valueA.equalsIgnoreCase(valueB)) { return false; }
				
			} else if( a instanceof IOperatorToken ) {
				; // IMPORTANT: This check MUST go after IFunctionToken because a function is an operator.
				
			} else if ( a instanceof IIdentifierToken) {
				if( !valueA.equalsIgnoreCase(valueB)) { return false; }
				
			} else { // This covers the IStringToken case and any new token types introduced.
				
				if(!valueA.equals(valueB)) { return false; }
			}
		}
		return true;
	}
	/**
	 * Quote a string using quotes based on the string's content.
	 * 
	 * @param token any parser token
	 * @return string with surrounding quotes.
	 * 
	 */
	private String getQuotedString( IToken token ) {
		String quote = token.getName().contains("\"")?null:"\"";
		quote = quote!=null?quote:(token.getName().contains("'")?null:"'");
		
		if( quote == null ) {
			return "\"" + token.getName().replace("\"", "\\\"") + "\"";
		} else {
			return quote + token.getName() + quote;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.aslan.parser.infix.IParsedExpression#getPostfixString()
	 */
	public String toPostfixString() {
		StringBuffer b = new StringBuffer();
		
		for( IToken token : getPostfix() ) {
			if( token instanceof IStringToken ) {
				b.append( getQuotedString(token));
			} else {
				b.append( (b.length()==0?"":" ") + token.getName());
			}
			
			if( token instanceof IOperatorToken ) {
				IOperatorToken op = (IOperatorToken)token;
				if( op.getType() == IToken.Type.OP_UNARY_MINUS ) {
					b.append("#");
				}
				if( op instanceof IFunctionToken ) {
					IFunctionToken ff = (IFunctionToken)op;
					b.append( "#" + ff.getNArgs());
				}
			}
		}
		return b.toString();
	}
	/* (non-Javadoc)
	 * @see com.aslan.parser.infix.IParsedExpression#getInfixString()
	 */
	public String toInfixString() {
		
		Stack<String> valueStack = new Stack<String>();
		
		for( IToken token : getPostfix()) {
			if( token instanceof IValueToken || token instanceof IIdentifierToken) {
				if( token instanceof IStringToken ) {
					valueStack.push( getQuotedString(token));
				} else {
					valueStack.push(token.getName());
				}
			} else {
				
				if( token instanceof IFunctionToken ) {
					StringBuffer b = new StringBuffer();
					IFunctionToken ftok = (IFunctionToken) token;
					
					b.append( ftok.getName() + "(");
					String[] args = new String[ftok.getNArgs()];
					for( int n = 0; n < ftok.getNArgs(); n++ ) {
						args[ftok.getNArgs() - n - 1] = valueStack.pop();
					}
					for( int n = 0; n < args.length; n++ ) {
						b.append( (n==0?"":", ") + args[n] );
					}
					b.append( ")");
					valueStack.push(b.toString());
				
				} else if( token instanceof IOperatorToken ) {
					IOperatorToken opToken = (IOperatorToken)token;
					StringBuffer b = new StringBuffer();
					
					b.append("(");
					if( opToken.getNArgs() == 1 ) {
						b.append( opToken.getName());
						b.append( valueStack.pop());
					} else {
						String v2 = valueStack.pop();
						String v1 = valueStack.pop();
						
						b.append( v1 + " " + opToken.getName() + " " + v2);
					}
					b.append(")");
					valueStack.push(b.toString());
				}
			}
		}
		
		
		return valueStack.isEmpty()?"":valueStack.pop();
	}

	
}
