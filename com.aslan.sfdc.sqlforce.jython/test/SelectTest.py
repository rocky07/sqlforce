'''
Created on Jan 18, 2010

@author: greg
'''

import TestEnvironment
import unittest

class SelectTest( unittest.TestCase):
    

    def testSelectCount(self):
        session = TestEnvironment.getSession()
        n = session.select("SELECT COUNT() FROM Contact")[0][0]
        self.assertTrue(n)
        
    def testSelectWithCallback(self):
        session = TestEnvironment.getSession()
        n = session.select("SELECT COUNT() FROM Contact")[0][0]
        self.assertTrue(n)
        
if __name__=='__main__':
    unittest.main()
        