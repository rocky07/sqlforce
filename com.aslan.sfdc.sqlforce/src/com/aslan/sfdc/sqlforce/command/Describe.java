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

import java.util.TreeMap;

import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.sqlforce.AbstractSQLForceCommand;
import com.aslan.sfdc.sqlforce.LexicalAnalyzer;
import com.aslan.sfdc.sqlforce.LexicalToken;
import com.aslan.sfdc.sqlforce.SQLForceEnvironment;
import com.sforce.soap.partner.Field;

/**
 * Describe a Salesforce SObject 
 * 
 * @author snort
 *
 */
public class Describe extends AbstractSQLForceCommand {

	

	@Override
	public String getOneLineHelp() {
		return "DESCRIBE anySObject";
	}

	@Override
	public String getHelp() {
		return getHelp( Describe.class, "Describe.help");
	}
	
	public void execute(LexicalToken token, LexicalAnalyzer lex,
			SQLForceEnvironment env) throws Exception {


		String objectName = lex.getToken().getValue();

		env.checkSession();
		LoginManager.Session session = env.getSession();
		
		try {
			Field fieldList[] = session.getDescribeSObjectResult( objectName ).getFields();

			TreeMap<String,Field> map = new TreeMap<String,Field>();
			for( Field field : fieldList ) {
				map.put( field.getName(), field);
			}
			
			for( String name : map.keySet()) {
				env.println(name);
			}
			
		} catch( Exception e ) {
			throw new Exception( e.toString());
		}
	}
}
