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
package com.aslan.sfdc.sqlforce.test;

import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.partner.test.SfdcTestEnvironment;
import com.aslan.sfdc.sqlforce.SQLForceSession;

/**
 * Generate the SalesForce object needed to run unit tests.
 * 
 * @author greg
 *
 */
public class SQLForceFactory {

	private static SQLForceSession forceSession = null;
	
	/**
	 * Return a SQLForceSession that is connected to SalesForce.
	 * 
	 * @return a logged in session.
	 * @throws Exception if the connection to salesforce fails.
	 */
	public  static SQLForceSession getForceSession() throws Exception {
		if( null != forceSession ) { return forceSession; }
		
		
		LoginManager.Session connection = SfdcTestEnvironment.getTestSession();
		forceSession = new SQLForceSession();
		forceSession.getEnvironment().setSession( connection );
		
		return forceSession;
	}
	
}
