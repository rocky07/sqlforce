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
package com.aslan.sfdc.sqlforce;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;


/**
 * Read SQLForce commands from an input stream until the stream is exhausted.
 * 
 * @author snort
 *
 */
public class SQLForceCommandLineReader {

	private Map<String, ISQLForceCommand> commandMap = new TreeMap<String,ISQLForceCommand>();
	private String prompt = null;
	private ISQLForceCommand helpCommand = new HelpCommand();
	private ISQLForceCommand exitCommand = new ExitCommand();
	
	class ExitCommand implements ISQLForceCommand {
		public void execute(LexicalToken token, LexicalAnalyzer lex,
				SQLForceEnvironment env) throws Exception {
			
			System.exit(0);
			
		}

		public String getOneLineHelp() {
			return "[EXIT|QUIT]";
		}

		public String getHelp() {
			return "[EXIT|QUIT]";
		}
		
	}
	
	class HelpCommand implements ISQLForceCommand {

		public void execute(LexicalToken token, LexicalAnalyzer lex,
				SQLForceEnvironment env) throws Exception {
			
			LexicalToken nextToken = lex.getToken(false);
			
			if( null==nextToken || LexicalToken.Type.END_OF_LINE==nextToken.getType()) {
				for( String cmd : commandMap.keySet()) {
					String[] helpText = commandMap.get(cmd).getOneLineHelp().split("\n");
					
					env.println(String.format("%-12s %s", cmd, helpText[0]));
					for( int n = 1; n < helpText.length; n++ ) {
						env.println(String.format("%-12s %s", "", helpText[n]));
					}
				}
			} else {
				ISQLForceCommand cmd = commandMap.get( nextToken.getValue().toUpperCase());
				String help = null==cmd?("No help for found for " + nextToken.getValue()):cmd.getHelp();

				if( null == help ) {
					help = (null==cmd)?("No help found for " + nextToken.getValue()):cmd.getOneLineHelp();
				}
				
				env.println(help);
			}
			
		}

		public String getOneLineHelp() {
			return "HELP [command]";
		}

		public String getHelp() {
			return "HELP [command]";
		}
		
		
	}
	
	
	public SQLForceCommandLineReader() {
		
		//
		// Load built-in commands
		//
		commandMap.put("HELP", helpCommand );
		commandMap.put("EXIT", exitCommand );
		commandMap.put("QUIT", exitCommand );

		//
		// Load command given in a configuration file.
		//
		
		loadCommands();
	}
	
	private void loadCommands() {
		//
		// Load the experiments we can do.
		//
		InputStream inStream = null;
		
		try {
			inStream = SQLForceCommandLineReader.class.getResourceAsStream("Command.properties");
			Properties props = new Properties();
			props.load(inStream);
			
			for( Object obj : props.keySet()) {
				String command = obj.toString();
				String className = props.getProperty( command );
				
				Class<?> clazz = Class.forName( className );
				
				ISQLForceCommand cmdInstance = (ISQLForceCommand) clazz.newInstance();
			
				commandMap.put( command.toUpperCase(), cmdInstance);
				
			}
			
		} catch( Exception e) {
			throw new Error(e);
		} finally {
			if( null != inStream ) {
				try {
					inStream.close();
				} catch (Exception e) {
					;
				}
			}
		}

	}
	
	/**
	 * Return map of all commands registered with the reader.
	 * 
	 * @return map of command to corresponding java class instance.
	 */
	public Map<String, ISQLForceCommand> getCommands() {
		return commandMap;
	}
	/**
	 * Set the prompt that will be used when we run out of data to read (null for no prompt).
	 * 
	 * @param prompt use this value to prompt a user for data (null for no prompt).
	 */
	public void setPrompt( String prompt ) {
		if( null != prompt && 0==prompt.trim().length()) { prompt=null; }
		this.prompt = prompt;
	}
	
	public void execute( LexicalAnalyzer lex, SQLForceEnvironment env ) {
		LexicalToken token;
		
		while( null != (token = lex.getToken())) {
			
			switch( token.getType()) {
			case IDENTIFIER: {
				ISQLForceCommand cmd = commandMap.get(token.getValue().toUpperCase());
				
				if( null == cmd ) { 
					env.logError("Unrecognized command: " + token.getValue() );
					lex.readLine();
				} else {
					try {
						if( null != prompt) { lex.setPrompt("> "); }
						
						String oldStatus = env.getenv("STATUS");
						cmd.execute( token, lex, env);
						
						String newStatus = env.getenv("STATUS");
						if( newStatus != null ) {
							if( oldStatus!=null && oldStatus.equals(newStatus)) {
								env.setenv( SQLForceEnvironment.STATUS, (String)null);
							}
						}
						
					} catch( Exception e ) {
						String message = e.getMessage();
						
						if( null==message) {
							message = e.toString();
						}
						
						env.logError(token.getValue().toUpperCase() + ": " + message);
					}
					lex.setPrompt(prompt);
				}
			
			} break;
			
			case END_OF_LINE: {
			} break;
			
			case PUNCTUATION: {
				if( "#".equals( token.getValue())) {
					lex.readLine(); // Ignore the line.
					break;
				}
				
				if( "?".equals( token.getValue())) {
					
					try {
						helpCommand.execute( null, lex, env);
					} catch (Exception e) {
						//
					}
					break;
				}
			};
			
			default: {
				env.logError("Unrecognized Command" + ": " + token.getValue() );
				lex.readLine();
			}
			}
			
			
		}
	}
	public void execute( InputStream inStream, OutputStream outStream, OutputStream errStream ) {
		
		PrintStream outPrintStream = new PrintStream( outStream );
		PrintStream errPrintStream = new PrintStream( errStream );
		
		SQLForceEnvironment env = new SQLForceEnvironment(outPrintStream, errPrintStream );
		LexicalAnalyzer lex = new LexicalAnalyzer(inStream, outPrintStream );
		
		execute( lex, env );
	}
}
