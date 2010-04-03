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
 * Set a SQLForce environment variable.
 * 
 * @author snort
 *
 */
public class Set extends AbstractSQLForceCommand {

	
	@Override
	public String getOneLineHelp() {
		
		return "SET variable=value";
	}

	@Override
	public String getHelp() {
		return getHelp( Set.class, "Set.help");
	}

	public void execute(LexicalToken token, LexicalAnalyzer lex,
			SQLForceEnvironment env) throws Exception {
		
		LexicalToken name = lex.getToken(LexicalToken.Type.IDENTIFIER);
		lex.getToken("=");
		LexicalToken value = lex.readLine();
		
		String str = env.replaceEnv(value.getValue());
		
		env.setenv( name.getValue(),  str.length()==0?null:str);
	
	}

}
