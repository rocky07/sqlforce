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
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

/**
 * Parse an infix expression and return an postfix (rpn) expression.
 * 
 * See the package header for details on the type of expressions parsed by this class.
 * (The package header contains the full grammar).
 * 
 * @author snort
 *
 */
public class InfixParser {
	
	private static IParsedExpression EMPTY_EXPRESSION;
	//
	// Create concrete implementations of the type of tokens the parser can return.
	//
	// Note that a few of the concrete implementations are defined externally because
	// they are commonly created by users of this package.
	//
	// See NumberToken, StringToken
	//
	
	class IdentifierToken implements IIdentifierToken {
		private String name;
		
		public IdentifierToken(String name ) { this.name = name;}
		
		public String getName() { return name; }
		public Type getType() { return IToken.Type.IDENTIFIER;}
	}
	
	class FunctionToken implements IFunctionToken {
		private String name;
		private int nArgs;
		private int stackSize;
		
		public FunctionToken(String name, int stackSize ) { this.name = name; this.stackSize = stackSize;}
		
		public String getName() { return name; }
		public Type getType() { return IToken.Type.FUNCTION;}

		public int getNArgs() { return nArgs; }
		void setNArgs( int n ) {nArgs = n; }
		public int getPrecedence() {return OperatorRegistry.LOWEST_PRECEDENCE;}
		int getStackSize() { return stackSize; }
	}
	
	//
	// The following operators are used internally while parsing an infix expression.
	// They will NEVER appear in the resulting postfix expression.
	//
	private final IOperatorToken OP_PAREN = new IOperatorToken() {

		public int getNArgs() { return 0; }
		public int getPrecedence() { return OperatorRegistry.LOWEST_PRECEDENCE; }
		public String getName() { return "("; }
		public Type getType() { return null; }
	};
	
	private final IOperatorToken OP_COMMA = new IOperatorToken() {

		public int getNArgs() { return 0; }
		public int getPrecedence() { return OperatorRegistry.LOWEST_PRECEDENCE; }
		public String getName() { return ","; }
		public Type getType() { return null; }
	};
	

	/**
	 * Create a new infix parser.
	 *
	 */
	public InfixParser() {
		if( EMPTY_EXPRESSION == null ) {
			EMPTY_EXPRESSION = new ParsedExpression(new ArrayList<IToken>());
		}
	}
	
	/**
	 * Remove all operators higher than a specified precendence from the operator stack.
	 * 
	 * @param precedence pop operators until an operator >= this precendence is found.
	 * @param operStack the operator stack.
	 * @param valueStack the values to be consumed by the operators.
	 * @param expression the full expression we are parsing (used to create error messages).
	 * @throws InfixParserException on any error
	 */
	private void popHigherPrecedenceOperators( 
				int precedence,  
				Stack<IOperatorToken> operStack, 
				Stack<List<IToken>> valueStack,
				String expression) throws InfixParserException {
		
		while( !operStack.isEmpty()) {
			IOperatorToken op = operStack.peek();
		
			if( op.getPrecedence() < precedence ) { break; }
			
			if( op.getNArgs() > valueStack.size()) {
				throw new InfixParserException("Too few arguments for '" + op.getName() + "' operator", expression,  null);
			}
			
			op = operStack.pop();
	
			List<IToken> values = valueStack.pop();
	
			for( int n = 1; n < op.getNArgs(); n++ ) {
				values.addAll(0, valueStack.pop() );
			}
			values.add(op);
			valueStack.push(values);
		}
	}
	
	/**
	 * Pop the stack when a function is the top operator on the operator stack.
	 * 
	 * @param operStack operators
	 * @param valueStack values
	 */
	private void popFunction(Stack<IOperatorToken> operStack, 
			Stack<List<IToken>> valueStack) {
		
		IFunctionToken opFunc = (IFunctionToken) operStack.pop();
		int nValuesToPop = opFunc.getNArgs();
		
		//
		// If the function takes no arguments, then there is not a value on top of the stack
		// we can use to create the list of function arguments.
		//
		if( nValuesToPop == 0 ) {
			ArrayList<IToken> vList = new ArrayList<IToken>();
			vList.add(opFunc);
			valueStack.push(vList);
		} else {
			//
			// The function takes 1 or more arguments. Build the concatenated arguement list.
			//
			List<IToken> values = valueStack.pop();
			
			for( int n = 1; n < nValuesToPop; n++ ) {
				values.addAll(0, valueStack.pop() );
			}
			values.add(opFunc);
			valueStack.push(values);
			
		}
	}
	/**
	 * Parse an infix expression into a postfix (reverse polish) expression.
	 * 
	 * If you are not familiar with postfix notation, search for "postfix notation"
	 * or "reverse polish notation" on the net (they are the same thing).
	 * 
	 * @param expression an infix expression
	 * @return a postfix expression
	 * @throws InfixParserException on any parser error.
	 */
	public IParsedExpression parse( String expression ) throws InfixParserException {
		
		if( expression ==null || expression.trim().length()==0 ) {
			return EMPTY_EXPRESSION; // Simple case
		}
		
		Stack<IOperatorToken> operStack = new Stack<IOperatorToken>();
		Stack<List<IToken>> valueStack = new Stack<List<IToken>>();
		
		OperatorRegistry opRegistry = OperatorRegistry.getInstance();
		
		LexicalAnalyzer lex = new LexicalAnalyzer();
		
		lex.setInput(expression);
		LexicalToken lexToken;
		boolean previousWasOperator = true; // Used to detect unary minus.
		
		while( null != (lexToken = lex.getToken())) {

			switch( lexToken.getType() ) {
			
			case STRING:
			{
					ArrayList<IToken> vList = new ArrayList<IToken>();
					vList.add(new StringToken(lexToken.getValue()));
					valueStack.push(vList);
					previousWasOperator = false;
			}
				break;
				
			case NUMBER:
			{
				ArrayList<IToken> vList = new ArrayList<IToken>();
				vList.add(new NumberToken(lexToken.getValue()));
				valueStack.push(vList);
				previousWasOperator = false;
			}
				break;
				
			case IDENTIFIER:
			{
				previousWasOperator = false;
				//
				// Check for identifiers that are operator keywords.
				//
				if( opRegistry.isOperator( lexToken.getValue())) {
					IOperatorToken op = opRegistry.getOperator(lexToken.getValue());
					
					popHigherPrecedenceOperators( op.getPrecedence(), operStack, valueStack, expression );
					operStack.push(op);
				} else {
					ArrayList<IToken> vList = new ArrayList<IToken>();
					vList.add(new IdentifierToken(lexToken.getValue()));
					valueStack.push(vList);
				}
			}
				break;
				
			case FUNCTION:
				previousWasOperator = true;
				operStack.push( new FunctionToken(lexToken.getValue(), valueStack.size()));
				break;
				
			case PUNCTUATION:
				String value = lexToken.getValue();
				if( "(".equals(value)) {
					operStack.push(OP_PAREN);
					previousWasOperator = true;
				} else if( ")".equals(value)) {
					previousWasOperator = false;
					//
					// The right paren can indicate the end of a function call OR a closing paren.
					//
					
					popHigherPrecedenceOperators( OP_PAREN.getPrecedence() + 1, operStack, valueStack, expression );
					if( operStack.size() == 0 ) {
						throw new InfixParserException( "Extra right parentheses", expression, lexToken );
					}
					IOperatorToken op = operStack.peek();
					
					if( op == OP_PAREN ) { // Simple case. A plain closing parentheses.
						operStack.pop();
						break;
					}
					
					//
					// If I have a function at this point, then the value stack contains the arguments to
					// the function. We want to pop just the function reference and its arguments off
					// of their respective stacks.
					//
					if( op instanceof FunctionToken ) {
						FunctionToken ff = (FunctionToken)op;
						ff.setNArgs( valueStack.size() - ff.getStackSize());
						
						popFunction( operStack, valueStack );
						
						break;
					} 
					
					throw new InfixParserException("Missing right parentheses", expression, lexToken);
					
					
				} else if (",".equals(value)) {
					previousWasOperator = true;
					popHigherPrecedenceOperators( OP_COMMA.getPrecedence() + 1, operStack, valueStack, expression );
				} else if( previousWasOperator && "-".equals(value)) {
					previousWasOperator = true;
					IOperatorToken op = opRegistry.getOperator(IToken.Type.OP_UNARY_MINUS);
					operStack.push(op);
				} else if( opRegistry.isOperator(value) ) {
					previousWasOperator = true;
					IOperatorToken op = opRegistry.getOperator(value);
					popHigherPrecedenceOperators( op.getPrecedence(), operStack, valueStack, expression );
					operStack.push(op);
				} else {
					throw new InfixParserException("Unexpected character", expression, lexToken );
				}
			    break;
			case LIST:
				try {
					if(operStack.peek().getType().equals(IToken.Type.OP_IN_LIST)) {
						IListOperatorToken lt = ((IListOperatorToken)operStack.pop()).clone();
						lt.setListValue(lexToken.getValue());
						operStack.push(lt);
					}
				} catch (EmptyStackException e) {
					throw new InfixParserException("stack is empty");
				}
				break;
			default:
				throw new InfixParserException("Logic error: missing lex token type in InfixParser.parse()");
				
			}
		}
		

		popHigherPrecedenceOperators( OP_PAREN.getPrecedence() + 1, operStack, valueStack, expression );
		
		if( operStack.size() != 0 ) {
			if( operStack.peek() == OP_PAREN ) {
				throw new InfixParserException("Missing closing parentheses", expression, null);
			} else if( operStack.peek() instanceof IFunctionToken ) {
				throw new InfixParserException("Function call missing closing parentheses", expression, null);
			} else {
				throw new InfixParserException("Extra operators in the expression", expression, null);
			}
		}
		if( valueStack.size() != 1 ) {
			throw new InfixParserException("Extra values in the expression", expression, null);
		}
		
		//
		// I have a syntactically correct expression.
		//
		return new ParsedExpression(valueStack.pop());
	}
}
