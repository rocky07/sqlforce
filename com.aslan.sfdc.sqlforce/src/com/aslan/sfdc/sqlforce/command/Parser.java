package com.aslan.sfdc.sqlforce.command;

import java.util.ArrayList;
import java.util.List;

import com.aslan.sfdc.sqlforce.LexicalAnalyzer;
import com.aslan.sfdc.sqlforce.LexicalToken;
import com.aslan.sfdc.sqlforce.SQLForceEnvironment;

/**
 * Parsers shared by various SQLForce commands.
 * 
 * @author gsmithfarmer@gmail.com
 *
 */
class Parser {

	/**
	 * Parse data of the form (value [,value]*).
	 * 
	 * The first token returned by the lexical analyzer MUST be a left paren.
	 * 
	 * Example: This method can be used to parse values from this form of the INSERT command:
	 * <pre>
	 * INSERT Contact( FirstName, LastName) VALUES("Greg", "Smith"), ("Mary", "Smith")
	 * <pre>
	 * @param lex read tokens from this analyzer
	 * @param env SQLForce environment.
	 * @return the values found in a within parenthesis.
	 * @throws Exception
	 */
	static List<String> parseSQLParenData(  LexicalAnalyzer lex,
			SQLForceEnvironment env) throws Exception {
		
		List<String> values = new ArrayList<String>();
		
		lex.getToken( true, "(");
		while( true ) {
			LexicalToken valueToken = lex.getToken(true);
			
			if( LexicalToken.Type.IDENTIFIER == valueToken.getType() && "null".equalsIgnoreCase(valueToken.getValue())) {
				values.add(null);
			} else {
				if( 0==valueToken.getValue().length()) {
					values.add(null); // Force treats blank as null.
				} else {
					values.add( valueToken.getValue());
				}
			}
			
			LexicalToken delim = lex.getToken(true );
			if( ")".equals( delim.getValue())) { break; } // End of values
			if( !",".equals( delim.getValue())) {
				throw new Exception("Expected a comma after " + valueToken.getValue() + " but found " + delim.getValue());
			}
		}
		
		return values;
	}
}
