'''
Manage a Salesforce connection that can be reused by all unit tests.

@author: greg
'''

import SQLForce

_session = SQLForce.Session("Sandbox")

def getSession():
    return _session

if __name__ == '__main__':
    pass