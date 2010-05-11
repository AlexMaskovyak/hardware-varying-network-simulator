package configuration;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

/**
 * Handles a set of configuration files.  These files each have a configuration
 * manager.  A set of configuration files corresponds to all of the 
 * configurations needed to create on graph.  They share a common independent
 * variable which is modified between them.
 * @author Alex Maskovyak
 *
 */
public class ConfigurationSetManager {

/// Fields
	
	/** configuration management underneath this set. */
	protected List<ConfigurationManager> _managers;
	/** directory of configurations. */
	protected File _setDirectory;
	
	
/// Construction
	
	/**
	 * Constructor.
	 * @param setPath to the configuration set directory.
	 */
	public ConfigurationSetManager( String setPath ) {
		this( new File( setPath ) );
	}
	
	/**
	 * Constructor.
	 * @param setDirectory containing a set of configuration files which are 
	 * themselves associated with Managers.
	 */
	public ConfigurationSetManager( File setDirectory ) {
		init(); 
		_setDirectory = setDirectory;
		_managers.addAll( createManagers( setDirectory ) );
	}
	
	/** Externalize instantiation. */
	protected void init() {
		_managers = new ArrayList<ConfigurationManager>();
	}
	
	/**
	 * Scans for configuration files, creates ConfigurationManagers for those
	 * files.  Grabs all non-directory files.
	 * @param setDirectory to scan for files.
	 * @return list of ConfigurationManagers created.
	 */
	public Collection<ConfigurationManager> createManagers( File setDirectory ) {
		return createManagers( 
			setDirectory, 
			new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {
					return !name.endsWith( ".log" );
				}
			}  );
	}
	
	/**
	 * Get the configuration managers managed by this set.
	 * @return configuration managers.
	 */
	public List<ConfigurationManager> getConfigurationManagers() {
		return _managers;
	}
	
	/**
	 * Scans for configuration files, creates ConfigurationManagers for those
	 * files.  Grabs files specified by the filter.
	 * @param setDirectory to scan for files.
	 * @return list of ConfigurationManagers created.
	 */
	public Collection<ConfigurationManager> createManagers( 
			File setDirectory, 
			FilenameFilter filter ) {
		
		// result
		List<ConfigurationManager> managers = 
			new ArrayList<ConfigurationManager>();
		
		// apply filter
		File[] configurationFiles = setDirectory.listFiles( filter );
		if( configurationFiles != null ) {
			for( File configFile : configurationFiles ) {
				if( configFile.isDirectory() ) { continue; } // only files
				File configDirectory = 
					new File( String.format(
						"%s%s%s",
						setDirectory.getAbsolutePath(),
						File.separator,
						stripExtension( configFile.getName() ) ) );
				configDirectory.mkdirs();
				managers.add( 
					new ConfigurationManager(configDirectory, configFile) );
			}
		}
		return managers;
	}
	
	/**
	 * Removes the extension from a filename.
	 * @param fullName of the file, containing the extension to be removed.
	 * @return name without the extension.
	 */
	protected String stripExtension( String fullName ) {
		return fullName.substring( 0, fullName.lastIndexOf( "." ) );
	}
	
	/**
	 * Aggregates the averages files for each manager.
	 */
	public void aggregateClientAverages() {
		try {
			List<File> clientLogs = new ArrayList<File>();
			for( ConfigurationManager manager : _managers ) {
				File avg = new File( String.format(
					"%s%savg%sclient.log",
					manager.getRunsDirectory().getAbsolutePath(),
					File.separator,
					File.separator ) );
				clientLogs.add( avg );
			}
			
			StringBuilder aggregate = new StringBuilder();
			// open each file, read its line and concatenate it
			for( File clientLog : clientLogs ) {
				if( clientLog.exists() ) {
					Scanner s = new Scanner( new FileInputStream( clientLog ) );
					if( s.hasNextLine() ) {
						aggregate.append( s.nextLine() );
						aggregate.append( "\n" );
					}
					s.close();
				}
			}
			
			File aggregateFile = 
				new File( 
					String.format(
						"%s%sclient_set_averages.log", 
						_setDirectory,
						File.separator ) );
			FileWriter writer = new FileWriter( aggregateFile );
			writer.write( aggregate.toString() );
			writer.close();
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Test driver.
	 * @param args N/A.
	 */
	public static void main(String... args) {
		
		String configSetDirectory = "C:\\Users\\user\\workspaces\\gradproject\\configurations-algorithm1\\config_set_5_cache_size";
		String configFile = "C:\\Users\\user\\workspaces\\gradproject\\configurations-1\\config_set_5_cache_size\\config_5.cfg";
		ConfigurationSetManager manager = 
			new ConfigurationSetManager( configSetDirectory );
		List<ConfigurationManager> managers = manager.getConfigurationManagers();
		for( ConfigurationManager m : managers ) {
			System.out.println( m.getBaseRunName() );
			System.out.println( m.getMaxRunNumber() );
			System.out.println( m.getRunsDirectory() );
			System.out.println( m.getConfigFile() );
		}
		manager.aggregateClientAverages();
	}
}
