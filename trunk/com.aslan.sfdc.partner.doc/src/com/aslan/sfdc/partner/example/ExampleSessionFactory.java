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
package com.aslan.sfdc.partner.example;

import com.aslan.sfdc.partner.LoginCredentials;
import com.aslan.sfdc.partner.LoginCredentialsRegistry;
import com.aslan.sfdc.partner.LoginManager;

/**
 * Factory for establishing connections to Salesforce used only by the sample code in this package.
 * 
 * @author gsmithfarmer@gmail.com
 *
 */
public class ExampleSessionFactory {

	private static LoginManager.Session session;
	private static final String profileName = "javaforce";
	
	public static LoginManager.Session getSession() throws Exception {
		if( null == session ) {
			LoginCredentials credentials = LoginCredentialsRegistry.getInstance().getCredentials(profileName);
		
			if( null == credentials ) {
				throw new Exception("Failed to find Salesforce credentials called '" + profileName + "'");
			}

			session = (new LoginManager()).login( credentials );
		}
		
		return session;
	}
}
