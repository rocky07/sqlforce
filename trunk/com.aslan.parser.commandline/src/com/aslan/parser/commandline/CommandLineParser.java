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
package com.aslan.parser.commandline;

import java.util.*;
import java.io.*;


/**
* Parse a command line args[] array into its corresponding switch/value pairs and
* other arguments. Provide convenience functions for retrieving the value of 
* switch values.
*
<p>This class provides command line parameter parsing. The general usage in main() is: 

<ul>
  <li>define a String[] listing all command line switches (optionally with default values)</li>
  <li>create a command line parser object, using the String[] from the first step</li>
  <li>call the parse method of the command line parser object.</li>
</ul>

<p>Example main():</p>

<blockquote>
  <font FACE="Courier New" SIZE="2"><dl>
    <dd>&nbsp;</dd>
    <dd>public static void main( String[] args )</dd>
    <dd>{<dl>
        <dd>String[] switches = {<dl>
            <dd>&quot;greg:boolean:false::The end of the world is near.&quot;,</dd>
            <dd>&quot;mary:int&quot;,</dd>
            <dd>&quot;rebecca:int:13:11,12,13,14&quot;,</dd>
            <dd>&quot;caleb:string&quot;,</dd>
            <dd>&quot;file:dir::This is the longest switch&quot;</dd>
          </dl>
        </dd>
        <dd>};</dd>
        <dt>&nbsp;</dt>
        <dd>CommandLineParser p = new CommandLineParser(switches);</dd>
        <dd>p.usage(System.out); // Display a a usage message<br>
        </dd>
        <dd>try {<dl>
            <dd>p.parse(args);</dd>
          </dl>
        </dd>
        <dd>} catch (IllegalArgumentException ev) {<dl>
            <dd>System.out.println( &quot;Parse Failed: &quot; + ev.getMessage());</dd>
          </dl>
        </dd>
        <dd>}</dd>
        <dd>String remainingArgs[] = p.getParams(); // Grab arguments which were not parameters</dd>
        <dt>}</dt>
      </dl>
    </dd>
  </dl>
  </font>
</blockquote>

<p>The general form of a switch definition line is:</p>

<blockquote>
  <p>&lt;switch name&gt;:&lt;switch type&gt;:&lt;default value&gt;:&lt;valid
  values&gt;:&lt;description&gt;</p>
</blockquote>

<p>where all values are optional except for the switch name.</p>

<blockquote>
  <table border="1">
    <tr>
      <th valign="top" align="left">Token</th>
      <th valign="top" align="left">Description</th>
    </tr>
    <tr>
      <td valign="top" align="left">switch name</td>
      <td valign="top" align="left">This name can be any series of non-blank characters. Do not
      include the leading dash.</td>
    </tr>
    <tr>
      <td valign="top" align="left">switch type</td>
      <td valign="top" align="left">This optional value indicates the type of data associated
      with the switch. Supported values:<table border="1">
        <tr>
          <th valign="top" align="left">Switch Type</th>
          <th valign="top" align="left">Description</th>
        </tr>
        <tr>
          <td valign="top" align="left">&lt;none&gt;</td>
          <td valign="top" align="left">When a switch type is not specified a switch is assumed not
          to have associated data unless the switch has a default value. If a switch has default
          value it is assumed to be of type String.</td>
        </tr>
        <tr>
          <td valign="top" align="left">string</td>
          <td valign="top" align="left">The switch value is any non-null string.</td>
        </tr>
        <tr>
          <td valign="top" align="left">int</td>
          <td valign="top" align="left">The switch value is any integer.</td>
        </tr>
        <tr>
          <td valign="top" align="left">boolean</td>
          <td valign="top" align="left">The switch does not have a value. The switch is is false if
          it is on the command line as -switch. The switch is true if it is on the command line as
          +switch.</td>
        </tr>
        <tr>
          <td valign="top" align="left">dir</td>
          <td valign="top" align="left">The switch value is a directory name. The directory may (or
          may not) already exist.</td>
        </tr>
        <tr>
          <td valign="top" align="left">file</td>
          <td valign="top" align="left">The switch value is a filename. The file may (or may not)
          already exist.</td>
        </tr>
        <tr>
          <td valign="top" align="left">xdir</td>
          <td valign="top" align="left">The switch vlaue is the name of an existing directory.</td>
        </tr>
        <tr>
          <td valign="top" align="left">xfile</td>
          <td valign="top" align="left">The switch value is the name of an existing file.</td>
        </tr>
      </table>
      </td>
    </tr>
    <tr>
      <td valign="top" align="left">default value</td>
      <td valign="top" align="left">This optional value indicates a default value for the
      switch. If a switch is not specified on the command line it is assumed to have this value.</td>
    </tr>
    <tr>
      <td valign="top" align="left">valid values</td>
      <td valign="top" align="left">This optional value is a comma separated list of legal
      values for the switch.</td>
    </tr>
    <tr>
      <td valign="top" align="left">description</td>
      <td valign="top" align="left">This optional value is displayed in the results of the
      usage() method.</td>
    </tr>
  </table>
</blockquote>

<p>By default, the parser pre-defines two switches:</p>

<blockquote>
  <p>-# displays a usage message and does a System.exit(0)</p>
  <p>-@filename loads command line parameters from the named file.</p>
</blockquote>

* @author gsmithfarmer@gmail.com
* @version 1.0
*/
public class CommandLineParser 
{
	abstract public class SwitchType 
	{

			protected String name;
			protected boolean needParameter = true;

			abstract public boolean isValueOK( String value );
			public String getName() { return name; }
			public boolean isNeedParameter() { return needParameter; }
	}

	private HashMap<String,SwitchType> 
                switchTypes = new HashMap<String,SwitchType>();		
                                                                                // All types of switches known by the parser
										// Items like int, string, etc. are in this list.
										// See the constructor.
										// Note that a user can add their own types using
										// defineSwitchType()

	private static boolean showUsageEnabled = true;		// If set, the the -# flag causes a usage message and
										// a program termination.
	private static final String DVALUE_TRUE = "Y";
	private static final String DVALUE_FALSE = "N";
	private static final String READ_FROM_FILE = "-@";	// If used as a switch, the parser reads command
										// line arguments from a file.

	private static final String SHOW_USAGE = "-#";		// If used as a switch, the usage message is displayed
										// and the system exits.

	private String  paramDelimiter = ":";
	private HashMap<String,Switch> definedSwitches = new HashMap<String,Switch>();	// All possible defined switches
	private HashMap<String,Switch> activeSwitches = new HashMap<String,Switch>();	// Switch on the active command line
	private ArrayList<String> activeArgs = new ArrayList<String>();		// Command line arguments which were not switches
/**
* Create an uninitialized CommandLineParser
*/
	public CommandLineParser()
	{
		defineSwitchType( new SwitchTypeNoArg( "none" ));
		defineSwitchType( new SwitchTypeString( "string" ));
		defineSwitchType( new SwitchTypeInt( "int" ));
		defineSwitchType( new SwitchTypeDouble( "double" ));
		defineSwitchType( new SwitchTypeBoolean( "boolean" ));
		defineSwitchType( new SwitchTypeFile( "file" ));
		defineSwitchType( new SwitchTypeExistingFile( "xfile" ));
		defineSwitchType( new SwitchTypeDir( "dir" ));
		defineSwitchType( new SwitchTypeExistingDir( "xdir" ));
	}

/**
* Create a CommandLineParser providing the available switch definitions.
*
* @param switchList definition of all switches which should be recognized by the parser
*
*/
	public CommandLineParser( String[] switchList )
	{
		this();

		if( switchList != null ) { defineSwitches( switchList ); }
	}

	/**
	 * Create a CommandLineParser providing the available switch definitions.
	 * @param switchList definition of all switches which should be recognized by the parser
	 */
	public CommandLineParser( SwitchDef[] switchList ) {
		this();
		
		if( switchList != null ) { defineSwitches( switchList ); }
	}
/*
* Built in switch type for switchs with no arguments
*/
	private class SwitchTypeNoArg extends SwitchType
	{
		SwitchTypeNoArg(String name ) 
		{
			this.name = name;
			this.needParameter = false;
		}

		@Override
		public boolean isValueOK( String value )
		{
			return false; // this type never needs a parameter
		}
	}
/*
* Built in switch type for switches taking a string as their argument
*/

	private class SwitchTypeString extends SwitchType
	{
		SwitchTypeString(String name ) 
		{
			this.name = name;
			this.needParameter = true;
		}

		@Override
		public boolean isValueOK( String value )
		{
			return true; // this type is always OK
		}
	}
/*
* Built in switch type for switches which are boolean.
*
*/
	private class SwitchTypeBoolean extends SwitchType
	{
		SwitchTypeBoolean(String name ) 
		{
			this.name = name;
			this.needParameter = false;
		}

		@Override
		public boolean isValueOK( String value )
		{
			return false; // this type never needs a value
		}
	}

/*
* Built in switch type for switches taking an integer as their argument
*/
	private class SwitchTypeInt extends SwitchType
	{
		SwitchTypeInt(String name ) 
		{
			this.name = name;
			this.needParameter = true;
		}

		@Override
		public boolean isValueOK( String value )
		{
			try {
				Integer.parseInt(value);
			} catch( Exception intEv ) {
				return false;
			}
			return true;
		}
	}

	/*
	* Built in switch type for switches taking an integer as their argument
	*/
		private class SwitchTypeDouble extends SwitchType
		{
			SwitchTypeDouble(String name ) 
			{
				this.name = name;
				this.needParameter = true;
			}

			@Override
			public boolean isValueOK( String value )
			{
				try {
					Double.parseDouble(value);
				} catch( Exception intEv ) {
					return false;
				}
				return true;
			}
		}
/*
* Built in switch type for switches taking a directory as their argument
*/

	private class SwitchTypeDir extends SwitchType
	{
		SwitchTypeDir(String name ) 
		{
			this.name = name;
			this.needParameter = true;
		}

		@Override
		public boolean isValueOK( String value )
		{
			return true;
		}
	}
/*
* Built in switch type for switches taking file as their argument
*/

	private class SwitchTypeFile extends SwitchType
	{
		SwitchTypeFile(String name ) 
		{
			this.name = name;
			this.needParameter = true;
		}

		@Override
		public boolean isValueOK( String value )
		{
			return true;
		}
	}
/*
* Built in switch type for switches taking an existing directory as their argument
*/

	private class SwitchTypeExistingDir extends SwitchType
	{
		SwitchTypeExistingDir(String name ) 
		{
			this.name = name;
			this.needParameter = true;
		}

		@Override
		public boolean isValueOK( String value )
		{
			File f = new File(value);
		
			return f.isDirectory();
		}
	}

/*
* Built in switch type for switches taking an existing file as their argument
*/

	private class SwitchTypeExistingFile extends SwitchType
	{
		SwitchTypeExistingFile(String name ) 
		{
			this.name = name;
			this.needParameter = true;
		}

		@Override
		public boolean isValueOK( String value )
		{
			File f = new File(value);
		
			return f.isFile();
		}
	}

/*
* This class holds the definitions of all currently defined switches
*
*/
	private class Switch {
		private HashMap<String,String>	valueMap;
		private String	defaultValue;
		private String	value;
		private String	description;
		private SwitchType swType;

		private Switch( SwitchType swType , String value)
		{
			this(swType,value,null);
		}

		private Switch( SwitchType swType, String value, String description )
		{
			this( swType, null, value, description );
		}

		private Switch( SwitchType swType, 
				String valueOptions[], String value, String description )
		{
			this.swType = swType;
			this.value = value;
			this.defaultValue = value;
			this.description = description;
	
			valueMap = null;
			if( valueOptions != null && valueOptions.length > 0 ) {
				valueMap = new HashMap<String,String>();
				for( int n = 0; n < valueOptions.length; n++ ) {
					valueMap.put( valueOptions[n].toLowerCase(), valueOptions[n] );
				}
			}
		}

		private String getOptionString()
		{
			if( valueMap == null ) { return null; }
			
			Set<?> set = valueMap.keySet();
			StringBuffer b = new StringBuffer();

			for( Iterator<?> ii = set.iterator(); ii.hasNext(); ) {
				if( b.length() > 0 ) { b.append( ", " ); }
				b.append( (String) ii.next() );
			}

			return b.toString();
		}
		
	}

	/**
	 * Convenience class for contructing switches in class that use the Command Line Parser.
	 * 
	 * @author greg
	 *
	 */
	public static class SwitchDef {
		private String switchTypeName;
		private String switchName;
		private String defaultValue;
		private String valueOptions[];
		private String description;
		
		public SwitchDef( String switchTypeName, String switchName, String defaultValue, String valueOptions[], String description ) {
			this.switchTypeName = switchTypeName;
			this.switchName = switchName;
			this.defaultValue = defaultValue;
			this.valueOptions = valueOptions;
			this.description = description;
		}
		
		public SwitchDef( String switchTypeName, String switchName, String defaultValue, String description ) {
			this( switchTypeName, switchName, defaultValue, (String[]) null, description );
		}
		
		public SwitchDef( String switchTypeName, String switchName, String description ) {
			this( switchTypeName, switchName, (String) null, description );
		}
	}
/**
* Pad a value on the right with spaces to a specific length.
*
* @param s string to pad
* @param specificLength final length desired for the string
*
* @return s padded on the right with spaces to specificLength
*/
	private String pad( String s, int specificLength )
	{
		s = ((s == null)?"":s);

		int	nonSpace = s.length();

		if( specificLength < nonSpace ) {
			return s;
		}

		if( specificLength == nonSpace ) {
			return s;
		}

		StringBuffer b = new StringBuffer(s);

		for( int n = 0; n < (specificLength - nonSpace); n++ ) {
			b.append(" " );
		}

		return b.toString();
			
	}
/**
* Add a new single switch to the set of defined switches
*
* The logic is a bit tricky because the tokenizer recognizes foo::fee as
* the tokens foo, NULL, and fee.
*
*
* @param switchDef description of a single switch.
* @param delim token which delimits the parts of a parameter
*
* @throws IllegalArgumentException is something is wrong with the switch definition
*/
	private void _defineSwitch( String switchDef, String delim )
	{
		String	token;

		if( switchDef == null ) return;

		StringTokenizer tokens = new StringTokenizer( switchDef, delim, true );

		if( tokens.countTokens() > 9 ) {
			throw new IllegalArgumentException( "Switch definition has more than 4 items: " + switchDef  );
		}

		String theSwitch = tokens.nextToken();
		SwitchType swType = null;
		String defaultValue = null;
		String description = null;
		String valueList[] = null;

		token = (tokens.hasMoreTokens()?tokens.nextToken():null);

		if( token != null ) { 
			token = (tokens.hasMoreTokens()?tokens.nextToken():null);
			String dataType = ((token != null && !token.equals(delim))?token:null);

			if( dataType != null ) {
				swType = switchTypes.get( dataType.toLowerCase() );
				if( swType == null ) {
					throw new IllegalArgumentException( "Unknown switch type: " + dataType );
				}
				if( tokens.hasMoreTokens() ) {token = tokens.nextToken(); }
			} 
		}

		if( token != null ) { 
			token = (tokens.hasMoreTokens()?tokens.nextToken():null);
			defaultValue = ((token != null && !token.equals(delim))?token:null); 
			if( defaultValue != null && tokens.hasMoreTokens() ) {
				token = tokens.nextToken();
			} 

		}

		if( token != null ) { 
			token = (tokens.hasMoreTokens()?tokens.nextToken():null);
			String tmpList = ((token != null && !token.equals(delim))?token:null);

			if( tmpList != null ) {
				StringTokenizer optTokens = new StringTokenizer( tmpList, "," );
				
				valueList = new String[optTokens.countTokens()];

				for( int n = 0; optTokens.hasMoreTokens(); n++ ) {
					valueList[n] = optTokens.nextToken();
				}
				
			}
			if( defaultValue != null && tokens.hasMoreTokens() ) {
				token = tokens.nextToken();
			} 

		}

		if( token != null ) { 
			token = (tokens.hasMoreTokens()?tokens.nextToken():null);
			description = ((token != null && !token.equals(delim))?token:null); 
		}

		if( swType == null ) {
			swType = switchTypes.get( ((defaultValue==null)?"none":"string") );
		}
//
// If the switch is already defined then replace its definition, otherwise
// create a new switch.
//
		theSwitch = theSwitch.toLowerCase();

		if( definedSwitches.containsKey(theSwitch) ) { definedSwitches.remove(theSwitch); }

		definedSwitches.put( theSwitch, new Switch( swType, valueList, defaultValue, description ));

	}

	private boolean isParamDataTypeOK( Switch sw, String value )
	{
		if( !sw.swType.isNeedParameter() ) { return true; }
		return sw.swType.isValueOK( value );
	}

/**
* Determine if a value is of the appriate type for a parameter switch.
*
* @param arg sw parameter definition.
* @param value the value to check.
*
* @return true if the value is OK, else false.
*/
	private boolean isParamValueOK( Switch sw, String value )
	{
//
// First verify that the basic data type is OK
//
		if( ! isParamDataTypeOK( sw, value )) { return false; }

//
// The basic data type is OK. If there is a range restriction, verify this.
//
		if( sw.valueMap != null ) {
			if( ! sw.valueMap.containsKey( value.toLowerCase() )) { return false; }
		}	
		return true;
	}

/**
* Tell the parser about a new switch type.
*<p>The constructor automatically defines a few switch types (int, string, none, file, etc.). This method
* can be used to define additional base switch types.
*
* @see SwitchType
*
* @param swType
*/
	public void defineSwitchType( SwitchType swType )
	{
		String name = (swType.getName()).toLowerCase();

		if( switchTypes.containsKey( name ) ) {
		} else {
			switchTypes.put( name, swType );
		}
	}
/**
* Tell the parser about available switches.
*
* <P>If the parse encounters a switch not in this list, it will throw an error.
*
* <P>Each switch should be specified as a switch name followed by an optional data type followed by
* an optional default value.
* Examples:<BR>
*<UL>
*<LI>nseconds:int
*<LI>debug:boolean:true
*</UL>
* Supported data types included the basic Java types plus the following:
*<UL>File - name of an existing file
*<LI>
*
* @param switchList array of switches
*/
	public void defineSwitches( String[] switchList )
	{
		for( int n = 0; n < switchList.length; n++ ) {
			_defineSwitch( switchList[n], paramDelimiter );
		}
	}
	
	/**
	 * Tell the parser about available switches.
	 * <p>If any of the switch types references in the argument are not known, then an error will be thrown.
	 * 
	 * @param switchList list of switches to define.
	 */
	public void defineSwitches( SwitchDef switchList[]) {
		for( SwitchDef sw : switchList ) {
			
			SwitchType swType;
			if( null == sw.switchTypeName || 0==sw.switchTypeName.trim().length()) {
				
					swType = switchTypes.get( ((sw.defaultValue==null)?"none":"string") );
			} else {
				if(!switchTypes.containsKey(sw.switchTypeName.toLowerCase())) {
					throw new IllegalArgumentException("Unrecognized switch type : " + sw.switchTypeName );
				}
				swType = switchTypes.get(sw.switchTypeName.toLowerCase());
			}
			
			
			String name = sw.switchName.toLowerCase();

			if( definedSwitches.containsKey(name) ) { definedSwitches.remove(name); }

			definedSwitches.put( name, new Switch( swType, sw.valueOptions, sw.defaultValue, sw.description ));
		}
	}
/**
* Add a single new switch the list of available switches. Typically the switches are
* defined as an argument to the constructor. However, this method provides an alternate
* way to define a single.
*
* @param newSwitch 
*/
	public void addSwitch( String newSwitch ) { _defineSwitch(newSwitch, paramDelimiter); }

/**
* Display an summay of switch usage to an output stream.
*
* @param out stream to receive the output.
*/

	public void usage( PrintStream out )
	{
		Set<String> keySet = definedSwitches.keySet();
		String[] keys = new String[keySet.size()];
		int	maxKeyLength = 0;

		keySet.toArray(keys);

		Arrays.sort(keys);

		for( int n = 0; n < keys.length; n++ ) {
			if( keys[n].length() > maxKeyLength ) { maxKeyLength = keys[n].length(); }
		}

		int keyColLength = maxKeyLength + 4;
		int dataTypeColLength = 10;
		int defaultColLength = 12;

		out.println( pad("Option", keyColLength) + 
						pad("Data Type", dataTypeColLength) + 
						pad("Default", defaultColLength ) + 
					"Description" );
		out.println( pad("======", keyColLength) + 
						pad("=========", dataTypeColLength) + 
						pad("========", defaultColLength ) +
					"===========" );

		for( int n = 0; n < keys.length; n++ ) {
			String key = keys[n];

			Switch sw = definedSwitches.get(key);
			String valueList = sw.getOptionString();
			String description = (sw.description==null?"":sw.description);
			
			if( valueList != null ) {
				description = description + (description.equals("")?"":", ") + "Options: " + valueList;
			}

			if( sw.swType instanceof SwitchTypeBoolean ) {
				key = "-+" + key;
			} else {
				key = "-" + key;
			}

			out.println( pad(key, keyColLength) + 
						pad(sw.swType.getName(), dataTypeColLength) + 
						pad(sw.defaultValue, defaultColLength ) +
					description );
		}
	}

/**
* Set the token used to delimit the parts of a parameter definition (default is :)
*
* @param delim
*/
	public void setParamDelimiter( String delim )
	{
		paramDelimiter = delim;
	}

/**
* Return the token used to deliminate tokens in a switch definition
*
* @return switch definition token delimiter
*/
	public String getParamDelimiter() { return paramDelimiter; }

/**
* Parse arguments in a command line array using the defined set of switches.
*
* @param args typically the args[] array from main.
*
* @throws IllegalArgumentException if any switch restriction is violated.
*/
	public void parse( String[] args ) 
	{
//
// Forget about any previously set switch arguments
//
		activeSwitches.clear();
		activeArgs.clear();
//
// Create a stack of the argument list.
//
		ArrayList<String> argStack = new ArrayList<String>();

		for( int n=0; n < args.length; n++) {
			if( (args[n]).startsWith( READ_FROM_FILE ) ) {
				try {
					String filename = args[n].substring(2);
					String inputLine;
					BufferedReader in = new BufferedReader( new FileReader( filename ));

					while( (inputLine = in.readLine()) != null ) {
						StringTokenizer tt = new StringTokenizer( inputLine );
						String token;

						while( tt.hasMoreTokens() ) {
							token = tt.nextToken();
							argStack.add(token);
						}
					}

					in.close();

				} catch (IOException ioev) {
					throw new IllegalArgumentException( "Failed to read arguments from " +
						args[n].substring(2) );
				}
			} else if( args[n].equals( SHOW_USAGE) && showUsageEnabled ){
				usage(System.out);
				System.exit(0);

			} else {  
				argStack.add(args[n]);
			} 
		}

		while( argStack.size() > 0 ) {
			String token = argStack.get(0);

			if( token.startsWith("-") || token.startsWith("+")) {

				String theSwitch = token.substring(1).toLowerCase();
				Switch sw = definedSwitches.get(theSwitch);

				if( sw == null ) {
					throw new IllegalArgumentException( "Unrecognized switch: " + token );
				}
//
// We have a potentially good switch. Remove it from the stack.
//
				argStack.remove(0);
//
// If the switch has already appeared, delete it.
//
				if( activeSwitches.containsKey(theSwitch) ) { activeSwitches.remove(theSwitch); }

				if( sw.swType instanceof SwitchTypeBoolean ) {
					activeSwitches.put( theSwitch, 
						new Switch( sw.swType, 
						(token.startsWith("-")?DVALUE_FALSE:DVALUE_TRUE)));

				} else if( ! sw.swType.isNeedParameter() ) {
					activeSwitches.put( theSwitch, new Switch( sw.swType, DVALUE_TRUE));

//
// Handle the cases where the switch requires an argument.
//					
				} else {
					if( argStack.size()== 0 ) {
						throw new IllegalArgumentException( "Missing value for switch " + theSwitch + 
							". Expected " + sw.swType.getName() );
					}

					String theArg = argStack.get(0);
					argStack.remove(0);

					if( ! isParamValueOK( sw, theArg )) {

						String valueList = sw.getOptionString();
						String expected = "Expected " + 
									((valueList==null)?sw.swType.getName():("one of " + valueList));

						throw new IllegalArgumentException( "Value for switch " + theSwitch + 
							" is invalid. " + expected);
					} 
					activeSwitches.put( theSwitch, new Switch( sw.swType, theArg ));
				}
			} else {
				argStack.remove(0);
				activeArgs.add(token);
			}
		}
//
// If were reach this point, the command line was successfuly parsed.
// Take any parameter which have default values but did not appear on the command
// line and add them as if they appeared explictly.
//
		Set<String> keySet = definedSwitches.keySet();
		String[] keys = new String[keySet.size()];

		keySet.toArray(keys);
		for( int n=0; n < keys.length; n++ ) {
			Switch sw = definedSwitches.get(keys[n]);
			if( sw.value == null ) continue;

			if( !activeSwitches.containsKey(keys[n]) ) {
				activeSwitches.put( keys[n], new Switch( sw.swType, sw.value ));
			} 
		}		
	}

//////////////////////////////////////////////////////////////////////////////////////////////////////
///
/// Methods for retrieving switch and parameter values after a parse.
///
//////////////////////////////////////////////////////////////////////////////////////////////////////

/**
* Return command line parameters which were not part of a switch
*
* @return args[] minus all switches and their parameters.
*/
	public String[] getParams()
	{
		if( activeArgs.size() == 0) {return new String[0];}

		String[] s = new String[activeArgs.size()];

		activeArgs.toArray(s);
		return s;
	}

/**
* Determine if a specific switch was on the command line.
*
* @return true if the switch was on the command line, else false.
*
*/
	public boolean isSet( String name )
	{
		String	lName = name.toLowerCase();
		Switch	sw = activeSwitches.get(lName);

		return (sw==null)?false:true;
	}

/**
* Return the value of any switch.
*
* @param name
*
* @return the string value of switch if it is set, else null.
*/
	public String getAny( String name )
	{
		String	lName = name.toLowerCase();
		Switch	sw = activeSwitches.get(lName);

		if( sw == null ) return null;
	
		return sw.value;
	}

/**
* Return the value of a switch declared as type boolean.
*
* @param name name of a boolean switch
*
* @return true/false if the switch was explicitly set or has a default value. Otherwise return null.
*/
	public Boolean getBoolean( String name )
	{
		String value = getAny(name);
	
		if( value == null ) return null;	
		return new Boolean((value.equals(DVALUE_TRUE)?true:false));
	}
 
/**
* Return the value of a switch declared as type int.
*
* @param name name of a int switch
*
* @return value if the switch was explicitly set or has a default value. Otherwise return null.
*/

	public Integer getInt( String name )
	{
		String value = getAny(name);
	
		if( value == null ) return null;
		try {
			return new Integer(value);
		} catch (Exception ev ) {
			return null;
		}
	}

/**
* Return the value of a switch declared as type string.
* This method is the same as getAny().
*
* @param name name of a string switch
*
* @return value if the switch was explicitly set or has a default value. Otherwise return null.
*/

	public String getString( String name ) { return getAny(name); }

/**
* Return the value of a switch declared as type file or xfile.
*
* @param name name of a file switch
*
* @return value if the switch was explicitly set or has a default value. Otherwise return null.
*/

	public File getFile( String name )
	{ 
		String value = getAny(name);

		if( value == null ) return null;

		return new File(value);
	}
/**
* Return the value of a switch declared as type dir or xdir.
*
* @param name name of a dir switch
*
* @return value if the switch was explicitly set or has a default value. Otherwise return null.
*/
	public File getDir( String name ) { return getFile(name); }

/*
* Test Driver Program

	public static void main( String[] args )
	{
		String[] switches = {
			"greg:boolean:false::The end of the world is near.",
			"mary:int",
			"rebecca:int:13:13,14,15,16,17",
			"caleb:string",
			"file:dir:::This is the longest switch"
		};

		CommandLineParser p = new CommandLineParser(switches);

		try {
			p.parse(args);
		} catch (IllegalArgumentException ev) {
			System.out.println( "Parse Failed: " + ev.getMessage());
		}

		String remainingArgs[] = p.getParams(); // Grab arguments which were not parameters

		System.out.println( "Rebecca is : " + p.getInt("rebecca" ));
		System.out.println( "Greg is : " + p.getBoolean("greg" ));

	}
*/	
}
