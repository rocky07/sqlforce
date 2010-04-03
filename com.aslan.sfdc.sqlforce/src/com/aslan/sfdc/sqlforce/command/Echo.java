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
package com.aslan.sfdc.sqlforce.command;

import com.aslan.sfdc.sqlforce.AbstractSQLForceCommand;
import com.aslan.sfdc.sqlforce.LexicalAnalyzer;
import com.aslan.sfdc.sqlforce.LexicalToken;
import com.aslan.sfdc.sqlforce.SQLForceEnvironment;

/**
 * Echo the contents of a line (with environment substitutions).
 * 
 * The form of ECHO is:
 *  is:
 * <pre>
 * ECHO anything up to the end of line.
 * </pre>
 * @author snort
 *
 */
public class Echo extends AbstractSQLForceCommand {

	@Override
	public String getOneLineHelp() {
		
		return "ECHO anything including ${variable} references";
	}

	@Override
	public String getHelp() {
		return getHelp( Echo.class, "Echo.help");
	}
	
	
	public void execute(LexicalToken token, LexicalAnalyzer lex,
			SQLForceEnvironment env) throws Exception {


		LexicalToken arg = lex.readLine();
		String outline = "";
		if( null != arg ) { outline = env.replaceEnv( arg.getValue().trim()); }
		
		env.println(outline );
		
	}

}
