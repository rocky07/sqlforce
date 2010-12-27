package com.aslan.sfdc.extract.oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class PracticeConnect {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String sql = "CREATE TABLE Account(Id CHAR(18) PRIMARY KEY,IsDeleted DECIMAL(1,0)  NULL ,MasterRecordId CHAR(18) NULL ,Name VARCHAR2(765)  NULL ,Type VARCHAR2(120)  NULL ,ParentId CHAR(18) NULL ,BillingStreet VARCHAR2(765)  NULL ,BillingCity VARCHAR2(120)  NULL ,BillingState VARCHAR2(60)  NULL ,BillingPostalCode VARCHAR2(60)  NULL ,BillingCountry VARCHAR2(120)  NULL ,ShippingStreet VARCHAR2(765)  NULL ,ShippingCity VARCHAR2(120)  NULL ,ShippingState VARCHAR2(60)  NULL ,ShippingPostalCode VARCHAR2(60)  NULL ,ShippingCountry VARCHAR2(120)  NULL ,Phone VARCHAR2(120)  NULL ,Fax VARCHAR2(120)  NULL ,AccountNumber VARCHAR2(120)  NULL ,Website VARCHAR2(765)  NULL ,Sic VARCHAR2(60)  NULL ,Industry VARCHAR2(240)  NULL ,AnnualRevenue NUMBER(18, 0)  NULL ,NumberOfEmployees DECIMAL(38,0)  NULL ,Ownership VARCHAR2(120)  NULL ,TickerSymbol VARCHAR2(60)  NULL ,Description CLOB  NULL ,Rating VARCHAR2(120)  NULL ,Site VARCHAR2(240)  NULL ,OwnerId CHAR(18) NULL ,CreatedDate DATE  NULL ,CreatedById CHAR(18) NULL ,LastModifiedDate DATE  NULL ,LastModifiedById CHAR(18) NULL ,SystemModstamp DATE  NULL ,LastActivityDate DATE  NULL ,IsCustomerPortal DECIMAL(1,0)  NULL ,gsmithforce__CustomerPriority_ VARCHAR2(765)  NULL ,gsmithforce__SLA__c VARCHAR2(765)  NULL ,gsmithforce__Active__c VARCHAR2(765)  NULL ,gsmithforce__NumberofLocations NUMBER(3, 0)  NULL ,gsmithforce__UpsellOpportunity VARCHAR2(765)  NULL ,gsmithforce__SLASerialNumber__ VARCHAR2(30)  NULL ,gsmithforce__SLAExpirationDate DATE  NULL );";
		
		
		try {
			Class.forName( "oracle.jdbc.driver.OracleDriver");
			Connection con = DriverManager.getConnection(
						"jdbc:oracle:thin:@localhost:1521:xe", "salesforce", "aslan"
					);
			
			Statement stmt = con.createStatement();
			
			stmt.executeUpdate(sql);
			
			stmt.close();
			con.close();
		} catch( Exception e ) {
			e.printStackTrace();
		}

	}

}
