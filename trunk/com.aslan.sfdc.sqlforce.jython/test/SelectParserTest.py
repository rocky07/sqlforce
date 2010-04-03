'''
Unit tests for class SQLForce.SelectParser()

Created on Jan 18, 2010

@author: greg
'''

import SQLForce
import unittest

class SelectTest( unittest.TestCase):
    
    def testSelectSimple(self):
        parser = SQLForce.SelectParser()
        
        parser.parse("SELECT A FROM Asset")
        self.assertEquals( "Asset", parser.tableName )
        self.assertEquals( ["A"], parser.columnNames )
        
        parser.parse("\nSELECT\nB\nFROM\nContact")
        self.assertEquals( "Contact", parser.tableName )
        self.assertEquals( ["B"], parser.columnNames )
        
        parser.parse("select A from Asset")
        self.assertEquals( "Asset", parser.tableName )
        self.assertEquals( ["A"], parser.columnNames )
        
        parser.parse("SELECT A FROM Asset WHERE id='hello'")
        self.assertEquals( "Asset", parser.tableName )
        self.assertEquals( ["A"], parser.columnNames )
        
        parser.parse("SELECT A , B FROM Account WHERE id='hello'")
        self.assertEquals( "Account", parser.tableName )
        self.assertEquals( ["A", "B"], parser.columnNames )
        
    def testSelectCompoundColumns(self):
        parser = SQLForce.SelectParser()
        
        parser.parse("SELECT id, account.name, name, owner.id FROM Contact")
        self.assertEquals( ["id", "account.name", "name", "owner.id"], parser.columnNames )
        
        parser.parse("SELECT account.name, owner.id FROM Contact")
        self.assertEquals( ["account.name", "owner.id"], parser.columnNames )
        
    def testSelectX(self):
        parser = SQLForce.SelectParser()
        
        parser.parse("\nSELECTX\nB\nFROM\nContact")
        self.assertEquals( "Contact", parser.tableName )
        self.assertEquals( ["B"], parser.columnNames )
        
        parser.parse("SELECTX account.name, owner.id FROM Contact")
        self.assertEquals( ["account.name", "owner.id"], parser.columnNames )
        
        parser.parse("\nSELECTx\nB\nFROM\nContact")
        self.assertEquals( "Contact", parser.tableName )
        self.assertEquals( ["B"], parser.columnNames )
        
        parser.parse("SELECTx account.name, owner.id FROM Contact")
        self.assertEquals( ["account.name", "owner.id"], parser.columnNames )
        
    def testSelectDistinct(self):
        parser = SQLForce.SelectParser()
        
        parser.parse("SELECT\ndistinct\nid, account.name, name, owner.id FROM Contact")
        self.assertEquals( ["id", "account.name", "name", "owner.id"], parser.columnNames )
        
        parser.parse("SELECT DISTINCT account.name, owner.id FROM Contact")
        self.assertEquals( ["account.name", "owner.id"], parser.columnNames )
        
    def testSelectUnion(self):
        parser = SQLForce.SelectParser()
        
        parser.parse("SELECT id, account.name, name, owner.id FROM Contact UNION SELECT a,b,c,d FRom Fred")
        self.assertEquals( ["id", "account.name", "name", "owner.id"], parser.columnNames )
        
        parser.parse("SELECT account.name, owner.id FROM Contact\nunion\nselect e,f from George")
        self.assertEquals( ["account.name", "owner.id"], parser.columnNames )
        
    def testSelectDistinctUnion(self):
        parser = SQLForce.SelectParser()
        
        parser.parse("SELECT DIstinct id, account.name, name, owner.id FROM Contact UNION SELECT a,b,c,d FRom Fred")
        self.assertEquals( ["id", "account.name", "name", "owner.id"], parser.columnNames )
        
        parser.parse("SELECT distinct account.name, owner.id FROM Contact\nunion\nselect e,f from George")
        self.assertEquals( ["account.name", "owner.id"], parser.columnNames )
    
    def testNotAValidSelect(self):
        parser = SQLForce.SelectParser()
        
        parser.parse("Update Contact Set name='fred'")
        self.assertEqual( None, parser.columnNames )
        self.assertEqual( None, parser.tableName )
        
        parser.parse("Select a,b,c frUm Asset")
        self.assertEqual( ["a", "b", "c"], parser.columnNames )
        self.assertEqual( None, parser.tableName )
        
        
if __name__=='__main__':
    unittest.main()
        