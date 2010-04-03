'''
Created on Jan 18, 2010

@author: greg
'''

import TestEnvironment
import unittest

class UpdateTest(unittest.TestCase):
    
    def testUpdateManyRowsOneColumn(self):
        session = TestEnvironment.getSession()
        
        insertData = [ 
              ["GregoryInsertTest", "Smith"], 
              ["MaryInsertTest", "Smith"], 
              ["RebeccaInsertTest", "Smith"]
             ]
        insertedIds = []
        
        try:
            insertedIds = session.insert("Contact", ["FirstName", "LastName"], insertData )
            updateRows = []
            
            for id in insertedIds:
                updateRows.append( [id, ("CEO-" + id) ])
            
            session.update( "Contact", ["title"], updateRows )
            nRows = int(session.getenv("ROW_COUNT"))
            
            self.failIf(0==nRows)
            self.assertEqual( len(insertedIds), nRows)
            
            for row in session.select("SELECT title from Contact where ID IN('" + "','".join(insertedIds) + "')"):
                self.assertTrue( row[0].startswith("CEO-"))
        finally:
            if None != insertedIds:
                session.delete( "Contact", insertedIds )
            
        
if __name__=='__main__':
    unittest.main()