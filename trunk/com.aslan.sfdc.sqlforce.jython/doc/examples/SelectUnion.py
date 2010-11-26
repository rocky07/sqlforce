'''
Combine SOQL statements using SELECT/UNION.

SQLForce extends the base SOQL syntax to include support for SELECT/UNION (e.g. ANSI syntax).

In this example we look for all unique country names used in Salesforce. We look in Contacts and Accounts.

@author gsmithfarmer@gmail.com
'''

'''
Step 1: Include the library and connect to Salesforce.
'''
import SQLForce

session = SQLForce.Session("javaforce");

'''
Select unique countries from Contacts and Accounts.

'''
sql = '''
    SELECT  MailingCountry FROM Contact WHERE MailingCountry!=null
    UNION SELECT  ShippingCountry FROM Account WHERE ShippingCountry!=null
    UNION SELECT  BillingCountry FROM Account WHERE BillingCountry!=null
'''

countryRecords = session.selectRecords(sql);

uniqueCountries = {}
for rec in countryRecords:
    if not rec.MailingCountry in uniqueCountries.keys():
        print rec.MailingCountry
        uniqueCountries[rec.MailingCountry] = True


