package com.aslan.sfdc.extract.ansi.test;

import com.aslan.sfdc.extract.DefaultExtractionMonitor;
import com.aslan.sfdc.extract.ExtractionManager;
import com.aslan.sfdc.extract.IExtractionMonitor;
import com.aslan.sfdc.extract.ansi.SQLEmitterDatabaseBuilder;
import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.partner.test.SfdcTestEnvironment;

import junit.framework.TestCase;

public class ExtractSchemaTest extends TestCase {

	IExtractionMonitor monitor = new DefaultExtractionMonitor() {

		@Override
		public void reportMessage(String msg) {
			System.err.println(msg);
		}
		
	};
	
	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testExtractSchema() throws Exception {
		LoginManager.Session session = SfdcTestEnvironment.getTestSession();
		SQLEmitterDatabaseBuilder builder = new SQLEmitterDatabaseBuilder(System.err);
		ExtractionManager mgr = new ExtractionManager(session, builder);
		
//		mgr.extractSchema( "Account");
//		mgr.extractSchema( );
	}
	
	public void testExtractData() throws Exception {
		LoginManager.Session session = SfdcTestEnvironment.getTestSession();
		SQLEmitterDatabaseBuilder builder = new SQLEmitterDatabaseBuilder(System.err);
		ExtractionManager mgr = new ExtractionManager(session, builder);
		
		mgr.extractData("Account", monitor);
//		mgr.extractSchema( );
	}
}
