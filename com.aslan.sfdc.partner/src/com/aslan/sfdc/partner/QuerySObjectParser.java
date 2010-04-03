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
package com.aslan.sfdc.partner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.axis.message.MessageElement;

import com.sforce.soap.partner.sobject.SObject;

/**
 * Parse the SObject result of a query to find all primitive columns.
 * 
 * This class knows how to flatten a tree of MessageElements returned from a SalesForce
 * query to get the list of columns specified in a SELECT statement. The only tricky
 * part is for child relationships -- SalesForce returns all columns for a child relationship
 * into a single top level MessageElement (thus losing the order of the original columns in 
 * the SELECT statement).  The key method in this class, {@link #parseRow(SObject)}, returns all values
 * in the original column order before returning them to a caller.
 * 
 * @author gsmithfarmer@gmail.com
 *
 */
public class QuerySObjectParser {

	//
	// Pattern to isolate column names from a Select statement.
	//
	private static Pattern columnFinderPattern = Pattern.compile(
			"^\\s*SELECT\\s+(([\\w\\.]+)(\\s*,\\s*([\\w\\.]+))*)\\s+FROM\\s+.*$",
			Pattern.MULTILINE|Pattern.CASE_INSENSITIVE|Pattern.DOTALL
			);
	
	//
	// Pattern to pick off individual column names in a comma separated list.
	//
	private static Pattern columnNamePattern = Pattern.compile(
			"[\\w\\.]+",
			Pattern.MULTILINE|Pattern.CASE_INSENSITIVE|Pattern.DOTALL
	);
	

	//
	// The query that is generated results.
	//
	private String theQuery = null; 
	//
	// Map of a column name (uppercase) in a select statement to the ordinal
	// position of the column in a query.
	//
	private Map<String,Integer> nameToOrderMap = new HashMap<String,Integer>();
	
	
	/**
	 * Prepare to parse results for a given query.
	 * 
	 * @param sql the query that generated the results -- WARNING if this is not the query then column
	 * values will not be returned in the correct order and some column values may be ignored.
	 * 
	 * @throws Exception if the SQL cannot be parsed for column names.
	 */
	public QuerySObjectParser( String sql ) throws Exception {
		findSQLColumns(sql);
		theQuery = sql;
	}
	
	/**
	 * Look at a bit of SQL to determine the order of columns.
	 * 
	 * 
	 * @param sql
	 */
	private void findSQLColumns( String sql ) throws Exception {
		Matcher colMatcher = columnFinderPattern.matcher(sql);
		
		if( !colMatcher.find()) {
			throw new Exception("Failed to find any columns referenced in SQL " + sql );
		}
		
		Matcher nameMatcher = columnNamePattern.matcher( colMatcher.group(1));
		while( nameMatcher.find()) {
			String name = nameMatcher.group(0);
			
			
			nameToOrderMap.put( name.toUpperCase(), nameToOrderMap.size());
		}
		
		
	}
	
	@SuppressWarnings({"unchecked" })
	private List<Object> getMessageElementChildren( MessageElement root ) {
		return root.getChildren();
	}
	
	/**
	 * Take a MessageElement returned from a SalesForce query and flatten it to a map of (columnName,Value).
	 * 
	 * The key part of this method is that if will recurse to flatten child and children of children relationships.
	 * 
	 * @param owner relationship that owns the root (null for columns owned by the FROM table of the query).
	 * @param root an element returned from a SalesForce query.
	 * @return map of (columnName,value) for each primitive value in the messageElement.
	 */
	private Map<String,String> getElementColumns( String owner, MessageElement root )  {
		
		Map<String,String> valueMap = new HashMap<String,String>();
		
		List<Object> children =  getMessageElementChildren(root);
		
		if( null!=children ) {

			if( 0 == children.size()) {
				throw new Error("Not Implemented: 0 children of a MessageElement");
			} else if( 1 == children.size()) {
				String fieldName = root.getName();
				String value = root.getValue();

				if( null != value ) { // Will be null for subrelationships with no data
					valueMap.put((null==owner?"":(owner + ".")) + fieldName, value);
				}
			} else if( 2== children.size()) {
				throw new Error("Not Implemented: 2 children of a MessageElement");
				
			} else {
				String subOwner = (null==owner?"":(owner + ".")) + root.getName(); 
				//String sObjectType = ((MessageElement)children.get(0)).getValue();
				//String sObjectId = ((MessageElement)children.get(1)).getValue();

				for( int nChild = 2; nChild < children.size(); nChild++ ) {
					MessageElement child = (MessageElement) children.get(nChild);

					Map<String,String> cValueMap = getElementColumns(subOwner, child );

					for( String key : cValueMap.keySet()) {
						valueMap.put( key, cValueMap.get(key));
					}

				}
			}
		} else {
			String fieldName = root.getName();
			String value = root.getValue();

			if( null != value ) { // Will be null for subrelationships with no data
				valueMap.put((null==owner?"":(owner + ".")) + fieldName, value);

			}
		}

		return valueMap;
	}
	
	/**
	 * Parse a row return by the SalesForce query method returning column values in the same
	 * order that they appeared in the original SELECT statement.
	 * 
	 * @param sObject a row from a SalesForce query() method.
	 * @return values for each column in the query.
	 */
	public String[] parseRow( SObject sObject ) {
		MessageElement tokens[] = sObject.get_any();
		
		String[] results = new String[nameToOrderMap.size()];
		
		for( int n = 0; n < tokens.length; n++ ) {
			Map<String,String> tmpMap = getElementColumns((String)null, tokens[n]);
			
			for( String key : tmpMap.keySet()) {
				String value = tmpMap.get(key);
				String upperKey = key.toUpperCase();
				
				if( !nameToOrderMap.containsKey(upperKey)) {
					throw new Error("Query result contained a column not in the query: " + key + ", Query = " + theQuery);
				}
				
				int keyIndex = nameToOrderMap.get(upperKey);
				results[keyIndex] = value;
				
			}
		}
		
		return results;
	}
}
