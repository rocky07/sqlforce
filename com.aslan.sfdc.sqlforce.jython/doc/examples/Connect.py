'''
Connect to a Salesforce instance.

This example illustrates how to connect to a Salesforce instance.

@author gsmithfarmer@gmail.com
'''

'''
Step 1: Include the library
'''
import SQLForce

'''
Case 1: Use a profile defined in an external configuration file.

This is the approach almost always used. For it to work you must define
a connection profile in $HOME/sqlforce.ini (or %USERPROFILE%/sqlforce.ini on Windows). The sqlforce.ini must look
like:
    <sqlforce>
        <connection 
            name="javaforce"
            type="Production"
            username="gsmithfarmer@gmail.com"
            password="yourpassword"
            token="yoursecuritytokenfromsalesforce"
        />
    </sqlforce>

Where:
    name - the name we will use in Jython
    type - either Production or Sandbox
    username - the salesforce username to use.
    password - the password to use
    token - the security token provided by salesforce
    
If your organization does not need a security token, use a blank string.
'''

session = SQLForce.Session("javaforce")
n = session.select("SELECT COUNT() FROM User")[0][0]
print "Found #Users = ", n


'''
Case 2: Pass user credentials directly into the session creation method.

If the profile system in SQLForce is not sufficient for you environment then
this is the technique you will need to use.

If your organization does not use security tokens skip the parameter or pass in a blank.
'''
connectionType = "Production"
username = "gsmithfarmer@gmail.com"
password = "yourpassword"
token= "yoursecuritytokenfromsalesforce"

session = SQLForce.Session()
session.connect(connectionType, username, password, token )
n = session.select("SELECT COUNT() FROM Contact")[0][0]
print "Found #Contacts = ", n


