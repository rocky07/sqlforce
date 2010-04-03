'''
Created on Jan 18, 2010

@author: greg
'''

import unittest
import TestEnvironment

class ConnectTest( unittest.TestCase):

    def testBasicConnect(self):
        session = TestEnvironment.getSession()
        self.assertTrue( session )
        
        n = session.select("SELECT COUNT() FROM Contact")[0][0]
        self.assertTrue(n)
        
if __name__=='__main__':
    unittest.main()
        

