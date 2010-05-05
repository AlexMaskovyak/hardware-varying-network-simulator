package configuration;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
		return createManagers( setDirectory, null );
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
	 * Test driver.
	 * @param args N/A.
	 */
	public static void main(String... args) {
		String configSetDirectory = "C:\\Users\\user\\workspaces\\gradproject\\configurations\\config_set_1_adaptor_speed\\";
		String configFile = "C:\\Users\\user\\workspaces\\gradproject\\configurations\\config_set_1_adaptor_speed\\config_1.cfg";
		ConfigurationSetManager manager = 
			new ConfigurationSetManager( configSetDirectory );
		List<ConfigurationManager> managers = manager.getConfigurationManagers();
		for( ConfigurationManager m : managers ) {
			System.out.println( m.getBaseRunName() );
			System.out.println( m.getMaxRunNumber() );
			System.out.println( m.getRunsDirectory() );
			System.out.println( m.getConfigFile() );
		}
	}
}
