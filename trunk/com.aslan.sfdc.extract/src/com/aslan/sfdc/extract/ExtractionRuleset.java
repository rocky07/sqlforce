/*******************************************************************************
 * Copyright (c) 2010 Gregory Smith (gsmithfarmer@gmail.com)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/

package com.aslan.sfdc.extract;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Rules for how data should be extracted from Salesforce.
 * 
 * An ExtractionRuleset is used to control:
 * <ul>
 * <li>Which tables' data is extracted from Salesforce.</li>
 * <li>The order in which table are extracted from Salesforce.</li>
 * </ul>
 * 
 * @author greg
 *
 */
public class ExtractionRuleset {
	
	private List<TableRule> includedTableRules = new ArrayList<TableRule>();
	private List<TableRule> excludedTableRules = new ArrayList<TableRule>();
	
	/**
	 * A rule that will apply to all tables that match a particular pattern.
	 * 
	 * @author greg
	 *
	 */
	public static class TableRule {
		private Pattern pattern;
		private String regexp;
		private boolean includeReferencedTables = false;
	
		public TableRule( String regexp ) {
			this(regexp, false );
			
		}
		
		public TableRule( String regexp, boolean includeReferencedTables ) {
			this.includeReferencedTables = includeReferencedTables;
			pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE);
			this.regexp = regexp;
		}
		
		public boolean isMatch( String name ) {
			return pattern.matcher(name).matches();
		}
		public String getRegexp( ) { return regexp; }
		
		public boolean isIncludeReferencedTables() {
			return includeReferencedTables;
		}
		
		/**
		 * Determine if tables referenced by this table will be automatically extracted
		 * if this table is extracted.
		 * 
		 * @param flag true or false.
		 */
		public void setIncludeReferencedTables( boolean flag ) {
			includeReferencedTables = flag;
		}
	}

	public ExtractionRuleset() {
		
	}
	/**
	 * Include all tables that match the rule with the tables that will be extracted.
	 * 
	 * @param rule
	 */
	public void includeTable( TableRule rule ) {
		includedTableRules.add(rule);
	}
	
	/**
	 * Include all tables that match the rule with the tables that will NOT BE extracted.
	 * 
	 * @param rule
	 */
	public void excludeTable( TableRule rule ) {
		excludedTableRules.add(rule);
	}
	
	/**
	 * Determine if a table has been explicitly excluded by a rule.
	 * 
	 * Note that being excluded does not mean that the table is explicitly on
	 * the included list.
	 * 
	 * @param name the table name
	 * @return true if name is on the excluded list, else false.
	 */
	public boolean isTableExcluded( String name ) {
		for( TableRule rule : excludedTableRules ) {
			if( rule.isMatch(name)) { return true; }
		}
		
		return false;
	}
	
	/**
	 * Return the ordered list of tables that should be extracted.
	 * 
	 * @return list of tables to extract.
	 */
	public List<TableRule> getIncludedTableRules() {
		return includedTableRules;
	}
}
