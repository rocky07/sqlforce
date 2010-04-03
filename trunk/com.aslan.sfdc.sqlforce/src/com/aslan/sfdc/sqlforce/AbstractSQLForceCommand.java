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
package com.aslan.sfdc.sqlforce;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Complete SQLForce command minus the execute method.
 * @author snort
 *
 */
public abstract class AbstractSQLForceCommand implements ISQLForceCommand {

	/**
	 * Make sure that all unquoted single quote marks in a string are escaped.
	 * 
	 * @param base modify this string.
	 * @return original string with all problem single quote marks escaped.
	 */
	public String quoteQuotes( String base ) {
		StringBuffer fixed = new StringBuffer();
		boolean lastWasEscape = false;
		
		for( char cc : base.toCharArray()) {
			
			if( cc == '\'') {
				if( !lastWasEscape ) {
					fixed.append('\\');
				}
			}
			fixed.append(cc);
			
			boolean isEscape = (cc == '\\');
			
			//
			// Handle the case where a backslash is used to escape a backslash.
			//
			if( (!isEscape) || (isEscape && lastWasEscape)) {
				lastWasEscape = false;
			} else {
				lastWasEscape = isEscape;
			}
		}
		return fixed.toString();
	}
	@Override
	public String getHelp() {
		return "No Help Specified";
	}

	protected String getHelp( Class<?> clazz, String helpFile ) {

		InputStream in = null;
		try {
			in = clazz.getResourceAsStream(helpFile);
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			BufferedInputStream bIn = new BufferedInputStream(in);
			BufferedOutputStream bOut = new BufferedOutputStream(out);
			byte[] buffer = new byte[1024*1024];
			int nRead;
			while( -1 != (nRead = bIn.read(buffer, 0, buffer.length))) {
				if( nRead > 0 ) {
					bOut.write( buffer, 0, nRead );
				}
			}
			bOut.flush();
			return out.toString();
		} catch( Exception e ) {
			return e.getMessage();
		} finally {
			if( null != in ) { try {
				in.close();
			} catch (IOException e) {
				;
			} }

		}
	}


	/**
	 * Return everything up to the SQL EndOfLine Marker.
	 * 
	 * Note that thsi method will automatically substitute values for environment
	 * variable references.
	 * 
	 * @param lex a lexical analyzer.
	 * @return found sql (not including the EOL character);
	 */
	protected String readSQL( SQLForceEnvironment env, LexicalAnalyzer lex ) {
		
		String EOL = ";";
		StringBuffer sql = new StringBuffer();

		for( LexicalToken var = lex.getToken(); null!=var; var = lex.getToken()) {
			if( EOL.equals( var.getValue())) {
				break;
			}

			String value = var.getValue();

			switch( var.getType() ) {
			case STRING: {
				
				value = "'" + quoteQuotes(value) + "'";
				value = env.replaceEnv(value);
			}break;
			
			case IDENTIFIER: {
				if( value.startsWith("$")) {
					value = env.getenv( value.substring(1));
					if( null == value ) { value = ""; }
				}
			} break;
			}
			
			sql.append( " " + value );

			
		}

		return sql.toString();
	}

}
