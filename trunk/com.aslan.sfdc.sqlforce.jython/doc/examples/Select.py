'''
Select records from a Salesforce instance.

Records can be selected from Salesforce using standard SOQL plus extensions
for SELECT DISTINCT and SELECT/UNION. This example file illustrates how it is done.

@author gsmithfarmer@gmail.com
'''

'''
Step 1: Include the library and connect to Salesforce.
'''
import SQLForce

session = SQLForce.Session("javaforce");

'''
Case 1: Select each record into a String[]

This is the simplest and most memory intensive way of selecting records.

'''
records = session.select("SELECT id, name, profile.name FROM User")
for rec in records:
    print "Case 1: ", rec[0], rec[1], rec[2]

'''
Case 2: Select each record into a custom object.

This a simple memory intensive approach that returns a custom object for each record.
It is preferred it to the String[] approach because of the limited type safety.

This is the approach I almost always use.
'''
records = session.selectRecords("SELECT id, name, profile.name FROM User")
for rec in records:
    print "Case 2: ", rec.id, rec.name, rec.profile.name
    
'''
Case 3: Use a callback to return each record. Each record is a String[]

When lots of records are returned the previous approaches can blow the java memory limits.
This approach is analogous to session.select() where each record (a String[]) is returned
via a user defined function.
'''

def callback3( record ):
    print "Case 3:", record[0], record[1], record[2]

session.select2("SELECT id, name, profile.name FROM User", callback3)

'''
Case 4: Use a callback to return each record. Each record is a custom object.

When lots of records are returned the previous approaches can blow the java memory limits.
This approach is analogous to session.selectRecords() where each record (a custom object) is returned
via a user defined function.
'''
def callback4( record ):
    print "Case 4:", record.id, record.name, record.profile.name

session.selectRecords2("SELECT id, name, profile.name FROM User", callback4)
