package com.aslan.sfdc.extract.ansi.test;

import junit.framework.TestCase;

import com.aslan.sfdc.extract.DefaultExtractionMonitor;
import com.aslan.sfdc.extract.ExtractionManager;
import com.aslan.sfdc.extract.ExtractionRuleset;
import com.aslan.sfdc.extract.ExtractionRuleset.TableRule;
import com.aslan.sfdc.extract.ansi.SQLEmitterDatabaseBuilder;
import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.partner.test.SfdcTestEnvironment;

public class ExtractSchemaTest extends TestCase {

	class MyMonitor extends DefaultExtractionMonitor {
		int nRecords = 0;
		
		@Override
		public void reportMessage(String msg) {
			nRecords++;
			System.err.println(msg);
		}
	}
	
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
	
		
		ExtractionRuleset rules = new ExtractionRuleset();
		
		rules.includeTable(new TableRule("User", true));
		
		MyMonitor monitor = new MyMonitor();
		mgr.extractSchema(rules, monitor);
		
		assertTrue( monitor.nRecords > 0 );

	}
	
	public void testExtractData() throws Exception {
		LoginManager.Session session = SfdcTestEnvironment.getTestSession();
		SQLEmitterDatabaseBuilder builder = new SQLEmitterDatabaseBuilder(System.err);
		ExtractionManager mgr = new ExtractionManager(session, builder);
		
		ExtractionRuleset rules = new ExtractionRuleset();
		
		TableRule rule = new TableRule("Contact");
		rules.includeTable(rule);
		
		MyMonitor monitor = new MyMonitor();
		mgr.extractData(rules, monitor);
		
		assertTrue( monitor.nRecords > 0 );
		
		
		System.err.println("Extract a Single Record");
		rule.setPredicate( "ID!=null LIMIT 1");
		assertTrue( null != rule.getPredicate());
		
		monitor = new MyMonitor();
		mgr.extractData(rules, monitor);
		assertTrue( monitor.nRecords > 0);
		
	}
}
