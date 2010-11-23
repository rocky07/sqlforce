package com.aslan.sfdc.build.ant;

import java.io.File;
import java.io.FilenameFilter;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * Find the root directory or jar of the more recent version of a plugin in an eclipse install.
 * @author snort
 *
 */
public class PluginRoot extends Task {

	private String property;	// Name of the property to set.
	private File eclipse;		// Root directory for Eclipse.
	private String plugin;		// Name of the plugin to find.

	private File findPluginDir() throws BuildException {
		
		//
		// Case 1: Eclipse directory not specified.
		//
		if( null == eclipse ) {
			String antHome = System.getProperty("ant.home", null );
			if( null == antHome ) {
				throw new BuildException( "Failed to discover Eclipse plugin directory");
			}
			File pluginDir = new File((new File(antHome)).getParent());
			return pluginDir;
		}

		//
		// Case 2: Eclipse root explicit.
		//
		if( !eclipse.isDirectory()) {
			throw new BuildException("Eclipse parameter (" + eclipse.toString() + ") is not a directory");
		}
		
		File pluginDir = new File(eclipse, "plugins");
		if( !pluginDir.isDirectory()) {
			throw new BuildException("Eclipse plugin directory (" + pluginDir.toString() + ") not found");
		}
		
		return pluginDir;
	}
	
	private String findPlugin( File pluginDir, final String pluginName ) {
		String candidates[] = pluginDir.list(new FilenameFilter() {
			
			private long lastModTime = 0;
			@Override
			public boolean accept(File root, String name) {
				if( !name.startsWith( pluginName + "_")) { return false; }
				
				long myModTime = (new File( root,name)).lastModified();
				if( myModTime < lastModTime ) { return false;}
				
				lastModTime = myModTime;
				return true;
		
			}
		});
		
		if( 0 == candidates.length ) { return null; }
		
		return candidates[0];
	}
	public void setProperty( String value ) {
		property = value;
	}

	public void setEclipse( File eclipseRoot ) {
		eclipse = eclipseRoot;
	}

	public void setPlugin( String name ) {
		plugin = name;
	}
	
	public void execute() throws BuildException {
		if( null==property || 0==property.trim().length()) {
			throw new BuildException("No property specified");
		}
		
		if( null==plugin || 0==plugin.trim().length()) {
			throw new BuildException("No plugin specified");
		}
		
		File pluginDir = findPluginDir();
		String pluginBase = findPlugin( pluginDir, plugin );
		if( null == pluginBase ) {
			throw new BuildException("Plugin " + pluginBase + " not found");
		}
		
		getProject().setProperty(property, (new File( pluginDir, pluginBase)).toString());
	}
}
