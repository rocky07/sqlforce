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
        
         
        
if __name__=='__main__':
    unittest.main()
        