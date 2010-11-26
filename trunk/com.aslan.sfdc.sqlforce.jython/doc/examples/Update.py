'''
Update existing records in Salesforce.

SQLForce extends the base SOQL syntax to include the ANSI UPDATE statement.

This example inserts a few Account records, updates a few fields, then deletes them.

@author gsmithfarmer@gmail.com
'''

'''
Step 1: Include the library and connect to Salesforce.
'''
import SQLForce

session = SQLForce.Session("javaforce");

'''
Create records that we will later update.
'''
recordsToInsert = [
                    ["Flintstone Products", "Bedrocker", "Stone Agge"]
                    ,["Rubble Industries", "Bedrocker", "Stone Agge"]
                    ,["Bedrock Mortuary", "Bedrocker", "Stone Agge"]
                   ]

accountIds = session.insert( "Account", ["name", "BillingCity", "BillingCountry"], recordsToInsert )

'''
****************************************************************************************
Case 1: Update records when we can select them using a WHERE clause.

In the sample data we misspelled "Stone Age" as "Stone Agge".
We can use a SQLForce UPDATE statement to fix all records at once.

The "status" returned will be null if the updated succeed and a non-null error message
if it failed.
*****************************************************************************************
'''
sql = """
    UPDATE Account SET
        BillingCity = 'Bedrock'
        , BillingCountry = 'Stone Age'
    WHERE
        BillingCountry = 'Stone Agge'
"""

status = session.runCommands(sql);

print "Status of UPDATE/WHERE: ", status
print "#Rows Updated: ", session.getenv("ROW_COUNT")
'''
Let's prove that the accounts were updated.
'''
for id in accountIds:
    for rec in session.selectRecords("SELECT name, billingCity, billingCountry FROM Account WHERE id='" + id + "'"):
        print "UPDATE/WHERE", id, rec.name, rec.billingCity, rec.billingCountry
        

'''
****************************************************************************************
Case 2: Update records when the change to each record is unique.

In this case we will update the street address of each new account.
Since each account will have a unique street address we cannot use the "UPDATE/WHERE"
technique.

Create an array for each record we want to update. Element[0] MUST be the unique id
of the records to update. The remaining elements are the new field values.

The form of the update statement is:

    session.update( tableToUpdate, fieldsToUpdate, updateData )

where:
    tableToUpdate -- the table where the records live.
    fieldsToUpdate -- the name of the fields to update.
    updateDate -- one row for each record to update. Element[0] MUST be the id of the record.

*****************************************************************************************
'''
recordsToUpdate = [
                    [accountIds[0], "5308 Three Lakes"]
                    ,[accountIds[1], "2323 River Bend"]
                    ,[accountIds[2], "11903 Honey Hill"]
                ]


status = session.update( "Account", ["BillingStreet"], recordsToUpdate )

print "Status of UPDATE/PerRecord: ", status
print "#Rows Updated: ", session.getenv("ROW_COUNT")

'''
Let's prove that the accounts were updated.
'''
for id in accountIds:
    for rec in session.selectRecords("SELECT name, billingStreet, billingCity, billingCountry FROM Account WHERE id='" + id + "'"):
        print "UPDATE/WHERE", id, rec.name, rec.billingStreet, rec.billingCity, rec.billingCountry
        

'''
Delete the accounts we created for the example.
'''
session.delete( "Account", accountIds )

