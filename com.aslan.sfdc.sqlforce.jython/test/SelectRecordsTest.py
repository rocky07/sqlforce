'''
Created on Jan 18, 2010

@author: greg
'''

import TestEnvironment
import unittest

class SelectRecordsTest( unittest.TestCase):
    
        
    def testSelectRecordsWithCallback(self):
        
        firstName = "SelectRecords2UnitTest"
        session = TestEnvironment.getSession()
        insertData = [ [firstName, "Smith"], [firstName, "Hoiles"], [firstName, "Jones"]]
        insertedIds = []
        
        try:
            insertedIds = session.insert("Contact", ["FirstName", "LastName"], insertData )
            
            def callback(record):
                self.assertEquals( firstName, record.firstName)
                self.assertTrue( None != record.lastName )
            
            session.selectRecords2("SELECT firstName, lastName FROM Contact where firstName='" + firstName + "'", callback)
                
        finally:
            if None != insertedIds:
                session.delete( "Contact", insertedIds )
        
           
    def testSelectRecords(self):
        
        firstName = "SelectRecords2UnitTest"
        session = TestEnvironment.getSession()
        insertData = [ [firstName, "Smith"], [firstName, "Hoiles"], [firstName, "Jones"]]
        insertedIds = []
        
        try:
            insertedIds = session.insert("Contact", ["FirstName", "LastName"], insertData )
            
            records = session.selectRecords("SELECT firstName, lastName FROM Contact where firstName='" + firstName + "'")
            self.assertEquals( len(insertData), len(records))
            for rr in records:
                self.assertEquals( firstName, rr.firstName)
                self.assertTrue( None != rr.lastName )
                
                
        finally:
            if None != insertedIds:
                session.delete( "Contact", insertedIds )
        
         
    def testSelectDeepRecords(self):
        firstName = "SelectRecords2UnitTest"
        session = TestEnvironment.getSession()
        
        insertedAccountId = None
        insertedIds = []
        
        try:
            insertedAccountId = session.insert("Account", ["Name"], [["SelectRecordIndusty"]] )[0]

            insertData = [ [insertedAccountId, firstName, "Smith"]]
            insertedIds = session.insert("Contact", ["accountId", "FirstName", "LastName"], insertData )

            records = session.selectRecords("SELECT name, account.name, account.owner.alias FROM Contact where id='" + insertedIds[0] + "'")
            for rec in records:
                self.assertTrue( None != rec.account.owner.alias )
                
                
        finally:
            if None != insertedIds:
                session.delete( "Contact", insertedIds )
            if None != insertedAccountId:
                session.delete( "Account", [insertedAccountId])
        
if __name__=='__main__':
    unittest.main()
        