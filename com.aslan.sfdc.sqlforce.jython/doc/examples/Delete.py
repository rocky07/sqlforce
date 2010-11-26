'''
Delete existing records in Salesforce.

SQLForce extends the base SOQL syntax to include the ANSI DELETE statement.

This example inserts a few Account records and deletes them.

@author gsmithfarmer@gmail.com
'''

'''
Step 1: Include the library and connect to Salesforce.
'''
import SQLForce

session = SQLForce.Session("javaforce");

'''
Define a function to create test records.
'''
def createTestRecords():
    recordsToInsert = [
                    ["Flintstone Products", "Bedrock", "Stone Age"]
                    ,["Rubble Industries", "Bedrock", "Stone Age"]
                    ,["Bedrock Mortuary", "Bedrock", "Stone Age"]
                   ]

    accountIds = session.insert( "Account", ["name", "BillingCity", "BillingCountry"], recordsToInsert )
    return accountIds

'''
****************************************************************************************
Case 1: Delete records when we can select this dynamically using a WHERE clause.

WARNING: This approach can be deadly if you run the wrong command. The following command could 
delete ALL accounts in your salesforce instance (since it has no WHERE clause all records are deleted).

    DELETE FROM Account

The "status" returned will be null if the delete succeeded and a non-null error message
if it failed.
*****************************************************************************************
'''
sql = """
    DELETE FROM Account WHERE BillingCity='Bedrock' AND BillingCountry='Stone Age'
"""

'''
First, let's delete before inserting test accounts into Salesforce.
'''
status = session.runCommands(sql);

print "Status DELETE/WHERE with No Records: ", status
print "#Rows Deleted: ", session.getenv("ROW_COUNT")

'''
Second: Create test data and then delete it.
'''
accountIds = createTestRecords()
status = session.runCommands(sql);

print "Status DELETE/WHERE with Records: ", status
print "#Rows Deleted: ", session.getenv("ROW_COUNT")

'''
****************************************************************************************
Case 2: Delete records using their unique Salesforce Ids.

For this case we know the Ids of the records to delete. This is the safest way to delete
records.

The "status" returned will be null if the delete succeeded and a non-null error message
if it failed.
*****************************************************************************************
'''

accountIds = createTestRecords()
status = session.delete("Account", accountIds)
print "Status DELETE with IDs: ", status
print "#Rows Deleted: ", session.getenv("ROW_COUNT")
