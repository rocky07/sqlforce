/*******************************************************************************
 * Copyright (c) 2010 Gregory Smith (gsmithfarmer@gmail.com).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Gregory Smith (gsmithfarmer@gmail.com) - initial API and implementation
 ******************************************************************************/
/**
 * 
 */
package com.aslan.sfdc.partner.admin;

import java.io.PrintStream;

import com.aslan.sfdc.partner.LoginManager;
import com.sforce.soap.partner.Field;
import com.sforce.soap.partner.PicklistEntry;
import com.sforce.soap.partner.QueryResult;

/**
 * Generate a report, suitable for Excel, that describes all fields on a SObject.
 * @author snort
 *
 */
public class SObjectFieldReport {

	private LoginManager.Session session;
	private String delim = "\t";
	
	public SObjectFieldReport( LoginManager.Session session ) {
		this.session = session;
	}
	
	private int countNonNullRecords( String sObjectName, String fieldName ) throws Exception {
		QueryResult query = session.getBinding().query("SELECT COUNT() FROM " + sObjectName + " WHERE " + fieldName + " != null" );
		
		return query.getSize();
	}
	
	public void generateReport( String sObjectName, boolean showStandardFields, PrintStream outStream ) throws Exception {
		
		outStream.println("Field Name" 
				+ delim + "Data Type" 
				+ delim + "# Used"
				+ delim + "Custom"
				+ delim + "Writable"
				+ delim + "Picklist Values"
				+ delim + "UI Label"
				+ delim + "Description"
				);
		
		for( Field field : session.getDescribeSObjectResult(sObjectName).getFields()) {
	
			if( !field.isCustom()) {
				if( !showStandardFields ) { 
							continue; 
				}
			}
			
			
			PicklistEntry picks[] = field.getPicklistValues();
			String pickListData = "";
			if( null != picks ) {
				for( PicklistEntry pp : picks ) {
					pickListData += ("".equals(pickListData)?"":";") + pp.getLabel();
				}
			}

			
			outStream.println(
					field.getName()
					+ delim + field.getType().getValue()
					+ delim + (field.isFilterable()?countNonNullRecords( sObjectName, field.getName()):-1)
					+ delim + (field.isCustom()?"Y":"N")
					+ delim + (field.isCreateable()?"Y":"N")
					+ delim + pickListData
					+ delim + field.getLabel()
					+ delim + (null==field.getInlineHelpText()?"":field.getInlineHelpText())
					);
		}
	}
}
