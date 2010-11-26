'''
Select DISTINCT records from a Salesforce instance.

SQLForce extends the base SOQL syntax to include support for SELECT DISTINCT (e.g. only return distinct
records). 

@author gsmithfarmer@gmail.com
'''

'''
Step 1: Include the library and connect to Salesforce.
'''
import SQLForce

session = SQLForce.Session("javaforce");

'''
Select unique countries from the Contact table.

'''
recordCount = session.select("SELECT COUNT() FROM Contact")[0][0]
countryRecords = session.selectRecords("SELECT DISTINCT mailingCountry FROM Contact ORDER BY mailingCountry");

print "Total # of Contacts:", recordCount
print "# of Distinct Countries:", len(countryRecords)

print "The Countries...."
for rec in countryRecords:
    print "\t", rec.mailingCountry


