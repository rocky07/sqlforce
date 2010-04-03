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
package com.aslan.sfdc.sqlforce;

/**
 * A command that can be executed on a SQLForce command line.
 * 
 * @author snort
 *
 */
public interface ISQLForceCommand {

	void execute( LexicalToken token, LexicalAnalyzer lex, SQLForceEnvironment env ) throws Exception;
	
	/**
	 * Get a one-line description of what the command does.
	 * 
	 * @return a one-liner.
	 */
	String getOneLineHelp();
	
	/**
	 * Return a full description of the command.
	 * 
	 * @return full description.
	 */
	String getHelp();
}
