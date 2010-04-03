'''
Created on Jan 18, 2010

@author: greg
'''

import TestEnvironment
import unittest

class InsertTest(unittest.TestCase):
    
        
    def testInsertAndFindIds(self):
        session = TestEnvironment.getSession()
        insertData = [ ["GregoryInsertTest", "Smith"], ["MaryInsertTest", "Smith"], ["RebeccaInsertTest", "Smith"]]
        insertedIds = []
        
        try:
            insertedIds = session.insert("Contact", ["FirstName", "LastName"], insertData )
            
            self.assertEqual(len(insertData), len(insertedIds))
            for id in insertedIds:
                self.assertTrue( None != id )
                
        finally:
            if None != insertedIds:
                session.delete( "Contact", insertedIds )
        

if __name__=='__main__':
    unittest.main()