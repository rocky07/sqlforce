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

    def testSelectDeepPath(self):
        session = TestEnvironment.getSession()
        session.selectRecords("SELECT account.owner.name, account.lastModifiedBy.alias FROM contact LIMIT 1")[0]
        '''
        The previous line would crash because of a deep path on 15-Sep-10 (release 1.0.7)
        '''
        
if __name__=='__main__':
    unittest.main()
        