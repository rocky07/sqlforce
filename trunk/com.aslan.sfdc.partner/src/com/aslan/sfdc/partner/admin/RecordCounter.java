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
package com.aslan.sfdc.partner.admin;

import com.aslan.sfdc.partner.LoginManager;
import com.sforce.soap.partner.QueryResult;


/**
 * Count the number of records in any SFDC table.
 * 
 * @author snort
 *
 */
public class RecordCounter {
	private LoginManager.Session session;
	
	/**
	 * Create a gadget for counting records
	 * 
	 * @param session active connection to SFDC.
	 */
	public RecordCounter( LoginManager.Session session ) {
		this.session = session;
	}
	
	/**
	 * Find the total number of records in a SFDC table.
	 * 
	 * @param tableName count records in this table.
	 * @return number of records found.
	 * @throws Exception if anything goes wrong.
	 */
	public int findCount( String tableName ) throws Exception {
		
		QueryResult query = session.getBinding().query("SELECT COUNT() FROM " + tableName );
		
		return query.getSize();
	}
}
