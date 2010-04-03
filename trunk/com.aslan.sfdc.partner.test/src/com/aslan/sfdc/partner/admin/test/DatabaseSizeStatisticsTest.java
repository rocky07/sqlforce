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
package com.aslan.sfdc.partner.admin.test;

import junit.framework.TestCase;

import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.partner.admin.DatabaseSizeStatistics;
import com.aslan.sfdc.partner.test.SfdcTestEnvironment;

public class DatabaseSizeStatisticsTest extends TestCase {

	private LoginManager.Session session = SfdcTestEnvironment.getSession();
	
	public void testGetTableStatistics() throws Exception {
		
		DatabaseSizeStatistics mgr = new DatabaseSizeStatistics( session );
		
		String tableNames[] = {
				"Account",
				"Contact",
		};
		
		for( String name : tableNames ) {
			DatabaseSizeStatistics.TableStatistics stats = mgr.getTableStatistics( name );
			assertTrue( stats.getRecordCount() > 0 );
		}
	}

	//
	// This method takes a while to run.....that is why it is commented out!
//	public void testGetAllTableStatistics() throws Exception {
//		DatabaseSizeStatistics mgr = new DatabaseSizeStatistics( session );
//		
//		List<DatabaseSizeStatistics.TableStatistics> statList = mgr.getAllTableStatistics();
//		
//		assertTrue( statList.size() > 0 );
//		
//		for( TableStatistics stats : statList ) {
//			System.err.println(stats.getName() 
//					+ "," + stats.isCustom() 
//					+ "," + stats.getRecordCount()
//					+ "," + stats.getCustomFieldCount()
//					);
//		}
//	}
}
