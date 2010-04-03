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
package com.aslan.sfdc.partner.record;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

import com.aslan.sfdc.partner.LoginManager;
import com.sforce.soap.partner.Field;

/**
 * Generate Java classes based on {@link ISObjectRecord} that have type safe methods for setting/getting all properties of a particular SObject type.
 * 
 * 
 * @author snort
 *
 */
public class SObjectRecordCodeGenerator {

	private LoginManager.Session session;
	private final String CODE_TEMPLATE_FILE = "SObjectRecordTemplate.txt";
	private static String codeTemplate = null;
	private static Set<String> stringDataTypes;
	private static Set<String> doubleDataTypes;
	private static Set<String> integerDataTypes;
	private static Set<String> dateDataTypes;
	private static Set<String> dateTimeDataTypes;
	private static Set<String> multipickDataTypes;
	private static Set<String> booleanDataTypes;
	
	static {
		
		//
		// Data Types that are passed as String's to setters/getters
		//
		stringDataTypes = new HashSet<String>();
		for( String name : new String[] { "string", "masterrecord", "email", "url" , "reference",
										"combobox", "encryptedstring", "id", "calculated",
										"phone", "picklist", "textarea", "base64"} ) {
			stringDataTypes.add(name);
		}
		
		//
		// Data Types that are passed as String[]'s to setters/getters
		//
		multipickDataTypes = new HashSet<String>();
		for( String name : new String[] { "multipicklist"} ) {
			multipickDataTypes.add(name);
		}
		
		//
		// Data Types that are passed as Boolean's to setters/getters
		//
		booleanDataTypes = new HashSet<String>();
		for( String name : new String[] { "boolean"} ) {
			booleanDataTypes.add(name);
		}
		//
		// Data Types that are passed as Integer's to setters/getters.
		//
		integerDataTypes = new HashSet<String>();
		for( String name : new String[] { "int"} ) {
			integerDataTypes.add(name);
		}
		
		//
		// Data Types that are passed as Double's to setters/getters.
		//
		doubleDataTypes = new HashSet<String>();
		for( String name : new String[] { "double", "percent", "currency"} ) {
			doubleDataTypes.add(name);
		}
		
		//
		// Data Types that are passed as Calendar's to setters/getters.
		//
		dateDataTypes = new HashSet<String>();
		for( String name : new String[] { "date"} ) {
			dateDataTypes.add(name);
		}
		
		//
		// Data Types that are passed as Calendar's to setters/getters.
		//
		dateTimeDataTypes = new HashSet<String>();
		for( String name : new String[] { "datetime"} ) {
			dateTimeDataTypes.add(name);
		}
	}
	/**
	 * Special tokens in the code template that will be substituted by this class.
	 * 
	 */
	private static final String KEY_SOBJECT_TYPE = "\\$\\{SObjectType\\}";
	private static final String KEY_CLASS_NAME = "\\$\\{ClassName\\}";
	private static final String KEY_FIELD_CONSTANTS = "\\$\\{FieldConstants\\}";
	private static final String KEY_SETTERS_AND_GETTERS = "\\$\\{SettersAndGetters\\}";
	
	/**
	 * Determine if custom fields should be included. By default, accessors for custom fields will not be generated.
	 */
	private boolean ignoreCustomFields = true;
	
	public SObjectRecordCodeGenerator( LoginManager.Session session ) {
		this.session = session;
	}
	
	private String replaceAll( String code, String key, String value ) {
		Pattern pattern = Pattern.compile(key, Pattern.MULTILINE);
		
		return pattern.matcher(code).replaceAll(value);
		
	}
	private String getCodeTemplate() throws Exception {
		
		if( null != codeTemplate ) { return codeTemplate; }
		
		InputStream inStream =  this.getClass().getResourceAsStream( CODE_TEMPLATE_FILE );
		
		if( null == inStream ) { 
			throw new Exception("Failed to open code template file: " + CODE_TEMPLATE_FILE);
		}
		
		
		BufferedReader reader = new BufferedReader( new InputStreamReader( inStream ));
		StringBuffer template = new StringBuffer();
		
		try {
			int nRead;
			char buffer[] = new char[1024*40];
			while( -1 != (nRead = reader.read(buffer))) {
				template.append(buffer, 0, nRead );
			}
			
		} catch( Exception e ) {
			; // Ignore on purpose
		} finally {
			try {
				inStream.close();
			} catch (IOException e) {
				// Ignore on purpose
			}
		}
		
		codeTemplate = template.toString();
		return codeTemplate;
	}
	
	private String makeConstantName( String sfdcName ) {
		StringBuffer name = new StringBuffer();
		
		
		for( char c : sfdcName.toCharArray()) {
			if( Character.isUpperCase(c) && name.length() > 0 ) {
				name.append("_");
			}
			if( Character.isJavaIdentifierPart(c)) {
				name.append( Character.toUpperCase(c));
			} else {
				name.append("_");
			}
		}
		
		return "F_" + name.toString();
	}
	
	private String makeMethodBaseName( String sfdcName ) {
		StringBuffer name = new StringBuffer();
		
		
		for( char c : sfdcName.toCharArray()) {
			
			if( Character.isJavaIdentifierPart(c)) {
				name.append( 0==name.length()?Character.toUpperCase(c):c);
			} else {
				name.append("_");
			}
		}
		
		return name.toString();
	}
	
	/**
	 * Determine if accessor methods will be generated for custom object fields.
	 * 
	 * @param newState if true ignore custom fields, else generate accessors for them.
	 */
	public void setIgnoreCustomFields( boolean newState ) {
		ignoreCustomFields = newState;
	}
	
	
	public String generateCode( String sObjectType ) throws Exception {
		
		Field fieldList[] = session.getDescribeSObjectResult( sObjectType ).getFields();
		
		ByteArrayOutputStream setterStream = new ByteArrayOutputStream();
		PrintStream setterOut = new PrintStream( setterStream );
		
		ByteArrayOutputStream constStream = new ByteArrayOutputStream();
		PrintStream constOut = new PrintStream( constStream );
		
		//
		// March thru the fields in alphabetical order.
		//
		TreeMap<String,Field> map = new TreeMap<String,Field>();
		for( Field field : fieldList ) {
			map.put( field.getName().toUpperCase(), field);
		}
		
		for( Field field : map.values() ) {
			
			if( "id".equalsIgnoreCase(field.getName())) { continue; }
			if( ignoreCustomFields && field.isCustom()) { continue; }
			
			String fieldConstant = makeConstantName( field.getName());
			constOut.println("\tpublic static final String " + fieldConstant + " = \"" + field.getName() + "\";");
			
			String type = field.getType().toString().toLowerCase();
			String methodName = makeMethodBaseName( field.getName());
			
			String methodType = null; // Base name of method to use for getters/setters
			String valueType = null;  // Strongly typed java type to get/set.
			
			if( stringDataTypes.contains(type)) {
				methodType = "String";
				valueType = "String";
			}
			
			if( integerDataTypes.contains(type)) {
				methodType = "Integer";
				valueType = "Integer";
			}
			
			if( doubleDataTypes.contains(type)) {
				methodType = "Double";
				valueType = "Double";
			}
			
			if( dateDataTypes.contains(type)) {
				methodType = "Date";
				valueType = "Calendar";
			}
			
			if( multipickDataTypes.contains(type)) {
				methodType = "MultiPickList";
				valueType = "String[]";
			}
			
			if( booleanDataTypes.contains(type)) {
				methodType = "Boolean";
				valueType = "Boolean";
			}
			
			if( dateTimeDataTypes.contains(type)) {
				methodType = "DateTime";
				valueType = "Calendar";
			}
			
			if( null != methodType) {
				setterOut.println("\n\tpublic " + valueType +  " get" + methodName + "() { return get" + methodType + "Field(" + fieldConstant + "); }");
				if( field.isCreateable()) {
					setterOut.println("\tpublic void set" + methodName + "(" + valueType + " value) { set" + methodType + "Field(" + fieldConstant + ", value); }");
				}
			} 
		}
		
		constOut.flush();
		setterOut.flush();
		
		String code = getCodeTemplate().replaceAll( KEY_CLASS_NAME, sObjectType + "Record");
		code = replaceAll( code, KEY_SOBJECT_TYPE, sObjectType);
		code = replaceAll( code, KEY_FIELD_CONSTANTS, new String( constStream.toByteArray()));
		code = replaceAll( code, KEY_SETTERS_AND_GETTERS, new String( setterStream.toByteArray()));
		
		return code;
	}
}
