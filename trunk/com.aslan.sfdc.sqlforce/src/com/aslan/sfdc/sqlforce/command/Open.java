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

import java.io.File;
import java.io.FileInputStream;

import com.aslan.sfdc.sqlforce.AbstractSQLForceCommand;
import com.aslan.sfdc.sqlforce.LexicalAnalyzer;
import com.aslan.sfdc.sqlforce.LexicalToken;
import com.aslan.sfdc.sqlforce.SQLForceEnvironment;

/**
 * Include the contents of a file as commands to the current command stream.
 * 
 * The form of OPEN is:
 *  is:
 * <pre>
 * OPEN filename
 * </pre>
 * @author snort
 *
 */
public class Open extends AbstractSQLForceCommand {


	@Override
	public String getOneLineHelp() {
		return "OPEN filename";
	}

	@Override
	public String getHelp() {
		return getHelp( Open.class, "Open.help");
	}

	public void execute(LexicalToken token, LexicalAnalyzer lex,
			SQLForceEnvironment env) throws Exception {


		LexicalToken arg = lex.readLine();
		String filename = env.replaceEnv(arg.getValue().trim());
		
		File file = new File(filename);
		if( !file.exists() ) {
			throw new Exception("File " + filename + " not found");
		}
		
		FileInputStream inStream = new FileInputStream(file);
		lex.include(inStream);
		
		env.log("Include " + filename );
		
	}

}
