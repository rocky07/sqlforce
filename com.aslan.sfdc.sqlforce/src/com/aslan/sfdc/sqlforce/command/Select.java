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
package com.aslan.sfdc.sqlforce.command;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.aslan.sfdc.partner.DefaultSObjectQuery2Callback;
import com.aslan.sfdc.partner.LoginManager;
import com.aslan.sfdc.partner.SObjectQueryHelper;
import com.aslan.sfdc.sqlforce.AbstractSQLForceCommand;
import com.aslan.sfdc.sqlforce.LexicalAnalyzer;
import com.aslan.sfdc.sqlforce.LexicalToken;
import com.aslan.sfdc.sqlforce.SQLForceEnvironment;

/**
 * SQLForce SELECT command.
 * 
 * Passthru a SELECT statement to SalesForce and show the results. In addition to native
 * SOQL statements the following alternate forms are also supported:
 * <ul>
 * <li>SELECT DISTINCT ... only show unique rows.
 * <li>SELECT .... UNION SELECT ... UNION SELECT;
 * </ul>
 * <p>
 * Another extension: Any SELECT statement that end with:
 * <ul>
 * <li>OUTPUT "filename"
 * <li>APPEND "filename"
 * </ul>
 * will write the results of the select statement to the specified file. The filename
 * MUST be in quotes (single or double quotes) and environment variable substitution will
 * be applied.
 * <p>
 * Examples:
 * <ul>
 * <li>SELECT LastName FROM Contact WHERE LastName LIKE 'Smith%' OUTPUT '${user.home}/Smiths.txt';
 * <li>SELECT DISTINCT title FROM Contact;
 * <li>SELECT DISTINCT MailingCountry FROM Contact UNION SELECT OtherCountry FROM Contact;
 * </ul>
 * @author snort
 *
 */
public class Select extends AbstractSQLForceCommand {

	/**
	 * This value will be used as the tab character between columns.
	 */
	public static final String SELECT_TAB = "SELECT_TAB";
	
	/**
	 * If this value is set, then the value of all columns will be quoted
	 * using this value.
	 */
	public static final String SELECT_QUOTE = "SELECT_QUOTE";
	
	/**
	 * If this value is set, then rows will be terminated with this character string
	 * instead of the default newline character.
	 */
	public static final String SELECT_EOL = "SELECT_EOL";
	
	private static Pattern selectDistinctPattern = Pattern.compile(
			"^(\\s*SELECT\\s+DISTINCT\\s+)(.*)", Pattern.CASE_INSENSITIVE|Pattern.MULTILINE|Pattern.DOTALL);
	
	private static Pattern selectUnionPattern = Pattern.compile( "\\s+(UNION\\s+)SELECT\\s+", Pattern.CASE_INSENSITIVE|Pattern.MULTILINE);
	
	private static Pattern redirectOutputPattern = Pattern.compile(
			"\\s+(OUTPUT|APPEND)\\s+(['\"])([^'\"]+)\\2\\s*$"
			, Pattern.CASE_INSENSITIVE|Pattern.MULTILINE
			);
	
	/**
	 * A row from a query that can be compared against another row for uniqueness.
	 * 
	 * @author greg
	 *
	 */
	private class UniqueRow {
		private String[] data;
		private int hashCode = 0;
		
		UniqueRow( String[] data ) {
			this.data = data;
			for( String token : data ) {
				if(null==token) { continue; }
				hashCode += token.toUpperCase().hashCode();
			}
		}

		@Override
		public int hashCode() {
			return hashCode;
		}

		@Override
		public boolean equals(Object obj) {
			if( null == obj ) { return false; }
			if( !(obj instanceof UniqueRow )) { return false; }
			
			UniqueRow otherRow = (UniqueRow)obj;
			
			if( data.length != otherRow.data.length ) { return false; }
			
			for( int n = 0; n < data.length; n++ ) {
				String a = data[n];
				String b = otherRow.data[n];
				
				if( null==a && null!=b ) { return false; }
				if( null!=a && null==b ) { return false; }
				if( null==a && null==b ) { continue; }
				if( !a.equalsIgnoreCase(b)) { return false; }
			}
			
			return true;
		}
		
	}
	
	abstract private class BaseQueryCallback extends DefaultSObjectQuery2Callback {

		private Set<UniqueRow> uniqueRowSet;
		private int rowCount = 0;
		
		BaseQueryCallback(  boolean selectDistinct ) {
			if( selectDistinct ) {
				uniqueRowSet = new HashSet<UniqueRow>();
			}
			
		}
		
		
		@Override
		public void start() {
			super.start();
		}

		
		abstract protected void writeRow( String[] data );
		protected void endQuery() {}
		
		
		public void addRow(int rowNumber, String[] data) {
			
			if( null != uniqueRowSet ) {
				UniqueRow uniqueData = new UniqueRow(data);
				
				if( uniqueRowSet.contains(uniqueData)) {
					return;
				}
				uniqueRowSet.add(uniqueData);
			}
			
			
			rowCount++;
			writeRow( data );
			
		}
		
		public int getRowCount() { return rowCount; }
	}
	private class TabularOutputQueryCallback extends BaseQueryCallback {

		private PrintStream outStream;
		private String tab;
		private String quote;
		private String eol;
		
		
		TabularOutputQueryCallback(  SQLForceEnvironment env, PrintStream rowOutputStream, boolean selectDistinct ) {
			super( selectDistinct );
			
			this.outStream = rowOutputStream;
			tab = env.getenv( SELECT_TAB);
			if(null == tab ) { tab = "\t"; }
			
			quote = env.getenv( SELECT_QUOTE);
			if(null == quote ) { quote = ""; }
			
			eol = env.getenv( SELECT_EOL );
			if( null==eol ) { eol = "\n"; }
		}
		

		protected void writeRow( String[] data ) {
			boolean firstCol = true;
			
			for( String v : data ) {

				outStream.print( (firstCol?"":tab) + quote + (null==v?"":v) + quote);
				firstCol = false;
			}
			outStream.print(eol);
		}
		
		
	}

	private class XMLOutputQueryCallback extends BaseQueryCallback {

		TransformerHandler handler;
		
		XMLOutputQueryCallback(  SQLForceEnvironment env, PrintStream rowOutputStream, boolean selectDistinct ) {
			super( selectDistinct );
			
			try {
				SAXTransformerFactory tf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
				handler = tf.newTransformerHandler();
				Transformer serializer = handler.getTransformer();
				serializer.setOutputProperty(OutputKeys.INDENT,"no");
				serializer.setOutputProperty( OutputKeys.OMIT_XML_DECLARATION, "yes");
				
				StreamResult streamResult = new StreamResult( rowOutputStream );

				handler.setResult( streamResult );
				AttributesImpl atts = new AttributesImpl();
				handler.startDocument();
				handler.startElement("", "", "Table", atts);
			} catch( Exception e ) {
				throw new Error(e);
			}
		}
		
		
		@Override
		protected void endQuery() {
			super.endQuery();
			try {
				handler.endElement("", "", "Table");
				handler.endDocument();
			} catch (SAXException e) {
				throw new Error(e);
			}
			
		}


		protected void writeRow( String[] data ) {
			
			AttributesImpl attrs = new AttributesImpl();
			try {
				handler.startElement("", "", "Row", attrs );
				for( String v : data ) {
					String preparedData = (null==v)?"":v;
					
					attrs.clear();
					handler.startElement("", "", "C", attrs );
					handler.characters(preparedData.toCharArray(),0,preparedData.length());
					handler.endElement("", "", "C");
					
					
				}
				handler.endElement("", "", "Row");
			} catch (SAXException e) {
				throw new Error(e);
			}
		}
		
		
	}

	@Override
	public String getOneLineHelp() {
		return "SELECT [DISTINCT] col1 [,col2]* FROM table [WHERE predicate];"
			+ "SELECT [DISTINCT] col1 [,col2]* ... UNION SELECT col1,[,col2]*"
			+ "SELECTX [DISTINCT] col1 [,col2]* FROM table [WHERE predicate];"
			+ "SELECTX [DISTINCT] col1 [,col2]* ... UNION SELECT col1,[,col2]*";
	}

	@Override
	public String getHelp() {
		return getHelp( Select.class, "Select.help");
	}
	
	public void execute(LexicalToken token, LexicalAnalyzer lex,
			SQLForceEnvironment env) throws Exception {

		PrintStream rowOutputStream = null;
		boolean isXMLSelect = "SELECTX".equalsIgnoreCase(token.getValue());
		
		try {
			
			String sql = readSQL(env, lex).trim();


			sql = "SELECT " + sql;

			//
			// Look for the SELECT DISTINCT case
			//
			Matcher matcher = selectDistinctPattern.matcher( sql );
			boolean selectDistinct = matcher.matches();

			if( selectDistinct ) {
				sql = "SELECT " + matcher.group(2);
			}

			//
			// See if the caller wants to redirect the output to a file.
			//
			Matcher outputMatcher = redirectOutputPattern.matcher(sql);
			if( outputMatcher.find() ) {
				int nFirst = outputMatcher.start();
				String outputType = outputMatcher.group(1).toUpperCase();
				String filename = outputMatcher.group(3);
				sql = sql.substring( 0, nFirst ).trim();

				rowOutputStream = new PrintStream(
						new FileOutputStream( filename, "APPEND".equals(outputType))
				);
			}
			//
			// Look for UNION queries
			//
			List<String> finalQueryList = new ArrayList<String>();
			Matcher unionMatcher = selectUnionPattern.matcher( sql );
			int	nStartQuery = 0;
			while( unionMatcher.find()) {

				int nFirst = unionMatcher.start(); // First character in the match. Will be a blank.
				String query = sql.substring(nStartQuery, nFirst );
				finalQueryList.add(query);

				nStartQuery = unionMatcher.end(1);


			}

			finalQueryList.add( sql.substring( nStartQuery ));

			BaseQueryCallback callback;
			PrintStream queryOutStream = (null==rowOutputStream?env.getStdoutStream():rowOutputStream);
			if( isXMLSelect ) {
				callback = new XMLOutputQueryCallback( env,queryOutStream,selectDistinct);
			} else {
				callback = new TabularOutputQueryCallback( env,queryOutStream,selectDistinct);
			}

			env.checkSession();
			LoginManager.Session session = env.getSession();

			for( String query : finalQueryList ) {
				(new SObjectQueryHelper()).findRows( session, query, callback);
			}
			if( isXMLSelect ) {
				callback.endQuery();
			}
			int nRows = callback.getRowCount();
			if( 0 == nRows ) {
				env.log("No Rows");
			}

			env.setSQLRowResultCounts(nRows, 0);
		} finally {
			if( null != rowOutputStream ) {
				rowOutputStream.close();
			}
		}
	}

}
