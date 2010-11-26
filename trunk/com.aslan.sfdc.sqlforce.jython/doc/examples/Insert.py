'''
Insert Records into Salesforce.

SQLForce extends the base SOQL syntax to include the ANSI INSERT statement.

This example inserts a few Account records (then deletes them!).

@author gsmithfarmer@gmail.com
'''

'''
Step 1: Include the library and connect to Salesforce.
'''
import SQLForce

session = SQLForce.Session("javaforce");

'''
Create accounts for the following businesses.

'''
accountNames = ["Flintstone Products", "Rubble Industries", "Bedrock Mortuary"]

'''
Build a simple array for each Account we want to created and stash it away in an array.
'''
rowsToInsert = []
for name in accountNames:
    rowsToInsert.append( [ name, "Bedrock" ] );

'''
Create the accounts returning the unique id of each created account.

The arguments to session.insert() are:
+ Name of the table to get the new records.
+ Name of the columns where data is provided (in the next argument)
+ One String[] for each row to be inserted.
'''    
accountIds = session.insert( "Account", ["name", "BillingCity"], rowsToInsert )

'''
Let's prove that the accounts were created.
'''
for id in accountIds:
    for rec in session.selectRecords("SELECT name, billingCity FROM Account WHERE id='" + id + "'"):
        print id, rec.name, rec.billingCity
'''
Delete the accounts we created for the example.
'''
session.delete( "Account", accountIds )

