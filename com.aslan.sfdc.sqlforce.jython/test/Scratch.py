'''
Created on Jan 18, 2010

@author: greg
'''

import TestEnvironment

session = TestEnvironment.getSession()

for record in session.selectRecords( "Select firstName, lastName from Contact order by firstName asc limit 10"):
    print "First name: " + record.firstName
    print "Last name: " + record.lastName
    