'''
/*******************************************************************************
 * Copyright (c) 2010 Gregory Smith (gsmithfarmer@gmail.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/

Run SELECT/INSERT/UPDATE/DELETE commands against a SalesForce database.

This module is simply a wrapper around the SQLForce jar that provides convenient
jython glue. NOTE: YOUR CLASSPATH MUST INCLUDE sqlforce.jar or sqlforce must be in your jre lib/ext directory.

The basic structure of any script using this module will be:

1. Connect to the database.

    import SQLForce

    session = SQLForce.Session()
    session.connect( "production|sandbox", "username", "password", "securityToken" )
    
    or
    
    session = SQLForce.Session( profileName )

2. Select or modify data in SalesForce

    rowList = session.select("Select FirstName, LastName FROM Contact")
    
    def callback(row):
        print row
    session.select2( "SELECT DISTINCT LastName FROM Contact", callback )

    status = session.runCommands( "any SQLForce script" )
    
For a complete understanding of available commands in SQLForce see the
site https://code.google.com/p/sqlforce/

Rather than keep salesforce credentials inside of a script, you will probably want to put them
in a configuration file. On load, this script automatically looks for and load credentials from
$HOME/sqlforce.ini (if the file is found). The format of the file is defined in the documentation
for the method loadProfileFile(filename)

Created on Jan 18, 2010

@author: gsmithfarmer@gmail.com
'''
      
from com.aslan.sfdc.sqlforce import SQLForceSession
from java.io import File
import re
from xml.sax.handler import ContentHandler
from xml.sax import make_parser

_findTablePattern = re.compile(r'FROM\s+(\S+)\s*', re.IGNORECASE|re.DOTALL|re.MULTILINE)
_findColumnsPattern = re.compile(r'^\s*SELECT[X]{0,1}\s+(?:DISTINCT\s*)?((?:[\w\.]+)(?:\s*,\s*(?:[\w\.]+))*)', re.IGNORECASE|re.DOTALL|re.MULTILINE)

class SelectParser():
    '''
    Parse a SOQL SELECT statement and determine the names of the columns and the top level
    table that is being queried.
    '''
    def __init__(self, sql=None ):
        self.columnNames = None
        self.tableName = None
        if sql:
            self.parse(sql)
    
    def parse(self, sql):
        self.columnNames = self._findColumnNames(sql)
        self.tableName = self._findTableName(sql)
        
    def _findColumnNames(self, sql):
        mm = _findColumnsPattern.search(sql)
        if None==mm:
            return None 
        names=[]
        for name in mm.group(1).split(","):
            names.append( name.strip())
        return names
        
    
    def _findTableName(self, sql):
        mm = _findTablePattern.search(sql)
        if mm:
            return mm.group(1)
        else:
            return None

class _SQLForceRecord():
    '''
    Single record for a record returned by selectRecords() or selectRecords2()
    '''
    def __init__(self, table, columns):
        
        self._table = table
        
        for name in columns:
            subNames = name.split(".")
            if 1==len(subNames):
                setattr( self, subNames[0], None)
            else:
                setattr( self, subNames[0], _SQLForceRecord( subNames[0], [".".join(subNames[1:])]))
    
    def setValue(self, column, value ):
        subNames = column.split(".")
        if 1==len(subNames):
            setattr( self, subNames[0], value)
        else:
            record = getattr( self, subNames[0] )
            record.setValue( ".".join(subNames[1:]), value)


class Session:
    '''
    Manage a connection to a single SalesForce instance.
    '''


    def __init__(self, profileName=None):
        '''
         Initialize a SQLForce session optionally connecting to salesforce.
         
         profileName -- if provided, the name of connection parameters that have been previously registered with this module.
        '''
        self._session = SQLForceSession()
        
        if(profileName):
            self.connectProfile(profileName)

            
    def connectProfile(self, profileName):
        '''
        Connect to a Salesforce session using a pre-defined connection profile.
        
        If the connection is successful then the function will return
        None. Otherwise the reason the connection failed will be returned.
        '''
        self.runCommands("CONNECT PROFILE " + profileName)
        return self.getenv("STATUS")
      
    def connect(self, sessionType, username, password, securityToken):
        '''
        Connect to a Salesforce session.

        If the connection is successful then the function will return
        None. Otherwise the reason the connection failed will be returned.
        '''
        self._session.runCommands("CONNECT " \
            + sessionType + \
            " " + username \
            + " " + password \
            + " " + securityToken)

    
        return self.getenv("STATUS")
    
    def getenv(self, token):
        '''
        Get a SQLForce environment variable.

        The most useful variables include:
        ROW_COUNT -- number of rows returned/effect by the most recent SOQL statement
        STATUS -- None if the most recent command worked, otherwise the error from the most recent command.
        '''
        return self._session.getenv(token)
    
    
    
    class SelectHandler( ContentHandler ):
        
        def __init__(self, rowCallback):
            self.callback = rowCallback
            self.lastData = ""
            self.row = []
            
        def characters(self, data):
            self.lastData += data
            
        def endElement(self, name):
            if "C" == name:
                self.row.append(self.lastData)
                self.lastData = ""
            if "Row" == name:
                self.callback(self.row)
                self.row = []
        
    def select2(self, sql, rowCallback):
        '''
        Select rows calling a user supplied method for each row.
    
        The callback method must have a single argument -- a list
        of values -- one for each column.

        Returns the number of rows found.
        '''
        tmpFile = File.createTempFile("SQLForceSelect", ".tmp")
        tmpFile.deleteOnExit();

        sql = sql.lstrip()
        selectxPattern = re.compile(r'^SELECTX', re.IGNORECASE)
        if not selectxPattern.match(sql):
            sql = "SELECTX" + sql[6:]
            
        self._session.runCommands(sql + " OUTPUT '" \
                + tmpFile.getAbsolutePath() + "'")
        
        fSelect = open(tmpFile.getAbsolutePath(), "r");
        
        parser = make_parser()
        handler =  self.SelectHandler(rowCallback)
        parser.setContentHandler(handler)
        parser.parse(fSelect)
        
        fSelect.close()
        tmpFile.delete();
        return self.getenv("ROW_COUNT")
    
    def select(self, sql):
        '''
        Return all rows found by a SELECT query as a list of lists.

        '''
        results = []
        self.select2(sql, lambda row: results.append(row))
        return results
            
    def selectRecords2(self, sql, callback ):
        '''
        Select records calling a user supplied callback for each row (each record contains attributes
        for each column in the select statement).
        
        Example: SELECT id, name, Account.name From Contact
        will return records with attributes:
        + id
        + name
        + Account.name
        '''
        parser = SelectParser()
        parser.parse(sql)
        table = parser.tableName
        columns = parser.columnNames
       
        
        def myCallback(row ):
            record = _SQLForceRecord( table, columns )
            n = 0
            for value in row:
                record.setValue( columns[n], value )
                n = n + 1
            callback(record)
            
        return self.select2(sql, myCallback)
        
    def selectRecords(self, sql ):
        '''
        Select records where each column in the sql is returned as an attribute of an object.
        
        Example: SELECT id, name, Account.name From Contact
        will return records with attributes:
        + id
        + name
        + Account.name
        
        Return a list of the records found or an empty list if no records are found.
        '''
        
        records = []
        def myCallback(record ):
            records.append(record)
        
        self.selectRecords2( sql, myCallback )
        return records
       
    def runCommands(self, cmd):
        '''
        Run one or more SQLForce commands returning the status of the last
        command.
        
        DELETE, UPDATE, and INSERT commands are generally run using this method.
        
        Example:
            runCommands( "DELETE FROM Contact WHERE LastName='PleaseDeleteMe' )
        '''
        self._session.runCommands(cmd)
        return self.getenv("STATUS")
        
    
    def _valueListToString(self, list, delim=",", quote="'"):
        '''
        Convert a list of values to a string of delimiter separated tokens.
        Recognized an empty value as the special value null.
        '''
        result = None
       
        qq = None
        if None!=quote and len(quote)>0:
            qq = quote
            
        for value in list:
            if None == value:
                quotedValue = "null"
            else:
                vv = str(value)
                if None != qq:
                    vv = vv.replace(qq, "\\")
                    quotedValue = qq + vv.replace(qq, "\\") + qq
                else:
                    quotedValue = vv
            
            if None == result:
                result = quotedValue
            else:
                result += delim + quotedValue
        return result

    def insert(self, table, columns, data):
        '''
        Insert a set of rows into a table, building the appropriate SQLForce command from the supplied data.
        
        The SQL force command executed by this method will be the same as calling runCommands() with:
        
            INSERT table( ','.join(columns) ) VALUES( ','.join(data[0] ), (','.join(data[0])), etc.
        
        or more concretely:
        
            INSERT Contact(FirstName, LastName) VALUE('Gregory', 'Smith'), ('Mary', 'Smith'), etc.
            
        table -- table to receive the rows
        columns -- name of the columns to fill in with data.
        data -- one row (a list) for each new row to create. The column order must match the order on the arg "columns"
        
        return a list of the ids inserted into Salesforce
        '''
        insertPrefix = "INSERT INTO " + table + "(" + ','.join(columns) + ")"
    
        nPending = 0;
        sql = insertPrefix
    
        for row in data:
            if 0 == nPending:
                sql += " VALUES"
            else:
                sql += ","
            
            sql += "(" + self._valueListToString(row, ",", '"') + ")"
            nPending += 1;
            
        self.runCommands(sql)
       
        return self._session.getenv("INSERT_IDS").split(",")
        
    def delete(self, table, idsList ):
        '''
        Delete a set of records from a table given a list of unique record ids.
        '''
        sql = "DELETE FROM " + table + " WHERE ID IN (" + self._valueListToString(idsList) + ")"
        return self.runCommands( sql )

    def update(self, table, columns, data ):
        '''
        Update a set of records where the id of each record is known.
        
        table -- table to update
        columns -- name of columns to update
        data -- one row for each update where row[0]=id of row to update and remaining
                elements contain data for each column.
        '''
        updatePrefix = "UPDATE " + table + "(" + ','.join(columns) + ") "
         
        nPending = 0
        sql = updatePrefix
         
        for row in data:
            if nPending > 0:
                sql += ","
                 
            sql += row[0] + "=(" + self._valueListToString(row[1:]) + ")"
            nPending += 1
          
        return self.runCommands(sql)
