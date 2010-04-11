package configuration;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Handles the directory management for a single configuration file and the runs
 * that will be run for it.
 * @author Alex Maskovyak
 *
 */
public class ConfigurationManager {

/// Fields
	
	/** directory holding the configuration file. */
	protected File _configDirectory;
	/** file with configuration information. */
	protected File _configFile;
	/** directory which will hold the current run's output. */
	protected File _currentRunDirectory;
	/** base name for a simulation run, this value gets incremented as additional
	 * runs occur. */
	protected String _baseRunName;
	
	/** maximum run number for this configuration directory. */
	protected int _maxSimRunNumber;


/// Construction

	/**
	 * Constructor.  Uses default directory run name of "run_[0-9]+"
	 * @param configDirectory path to the directory containing the sim runs.
	 * @param configFile path to the configuration file.
	 */
	public ConfigurationManager( String configDirectory, String configFile ) {
		this( configDirectory, configFile, "run_" );
	}
	
	/**
	 * Constructor.  Uses default directory run name of "run_[0-9]+"
	 * @param configDirectory directory containing the sim runs.
	 * @param configFile configuration file.
	 */
	public ConfigurationManager( File configDirectory, File configFile ) {
		this( configDirectory, configFile, "run_" );
	}
	
	/**
	 * Constructor.
	 * @param configDirectory path to the directory containing the config file,
	 * and simulation run directories.
	 * @param configFile path to configuration file.
	 * @param baseRunName for a simulation run.
	 */
	public ConfigurationManager( 
			String configDirectory, 
			String configFile, 
			String baseRunName ) {
		this( new File( configDirectory ), new File( configFile ), baseRunName );
	}
	
	/**
	 * Constructor.
	 * @param configFile the configuration file.
	 * @param configDirectory the directory containing the configuration file
	 * and simulation run directories.
	 * @param baseRunName for a simulation run.
	 */
	public ConfigurationManager( File configDirectory, File configFile, String baseRunName ) {
		// assign
		_configFile = configFile;
		_configDirectory = configDirectory;
		_baseRunName = baseRunName;
		
		// get the maximum run for this directory
		RunFilenameFilter filter = new RunFilenameFilter( _baseRunName );
		_configDirectory.listFiles( filter );
		_maxSimRunNumber = filter.getMaxRunNumber();
	}
	
	
/// Accessors
	
	/**
	 * Obtains the configuration file being managed.
	 * @return configuration file.
	 */
	public File getConfigFile() {
		return _configFile;
	}
	
	/**
	 * Obtains the configuration directory being managed.
	 * @return configuration directory.
	 */
	public File getConfigDirectory() {
		return _configDirectory;
	}
	
	/**
	 * Obtains the maximum run number in this directory.
	 * @return current maximum run number.
	 */
	public int getMaxRunNumber() {
		return _maxSimRunNumber;
	}
	
	/**
	 * Obtains the maximum run number in this directory.
	 * @param maxRunNumber new maximum run number.
	 * @return current maximum run number.
	 */
	protected void setMaxRunNumber(int maxRunNumber) {
		_maxSimRunNumber = maxRunNumber;
	}
	
	/**
	 * Obtains the base name used to construct a directory to hold log files for
	 * a simulation run.
	 * @return base name for holding log files from a simulation run.
	 */
	public String getBaseRunName() {
		return _baseRunName;
	}
	
	
/// Management

	/**
	 * Creates a new directory to hold simulation log files.
	 * @return the new directory to hold simulation log files, null if there was
	 * a problem creating the directory.
	 */
	public File makeNewRunDirectory() {
		File newRunDir = 
			new File( 
				String.format( 
					"%s%s%s%d", 
					_configDirectory.getAbsolutePath(), 
					File.separator, 
					getBaseRunName(), 
					getMaxRunNumber() + 1 ) );
		if( newRunDir.mkdirs() ) {
			setMaxRunNumber( getMaxRunNumber() + 1 );
			return newRunDir;
		}
		return null;
	}
	
	
/// Utility
	
	
/// Tester

	/**
	 * Test driver.
	 * @param args N/A
	 */
	public static void main(String... args) {
		String configDirectory = "C:\\Users\\user\\workspaces\\gradproject\\configurations\\config_set_1_adaptor_speed\\config_1\\";
		String configFile = "C:\\Users\\user\\workspaces\\gradproject\\configurations\\config_set_1_adaptor_speed\\config_1.cfg";
		ConfigurationManager manager = new ConfigurationManager( configDirectory, configFile );
		System.out.println( manager.getConfigFile() );
		System.out.println( manager.getConfigDirectory() );
		System.out.println( manager.getBaseRunName() );
		System.out.println( manager.getMaxRunNumber() );
		System.out.println( manager.makeNewRunDirectory() );
	}
	
}

/**
 * Identifies the maximum simulation run number from a configuration 
 * directory.
 * @author Alex Maskovyak
 *
 */
class RunFilenameFilter implements FilenameFilter {

/// Fields
	
	/** base filename which is appended with _# */
	protected String _baseName;
	/** maximum run number encountered. */
	protected int _maxRunNumber;

	
/// Construction
	
	/** 
	 * Default constructor. 
	 * @param baseName to identify.
	 */
	public RunFilenameFilter(String baseName) {
		_baseName = baseName;
		reset();
	}

/// Methods
	
	/**
	 * Resets this FilenameFilter so it can be used to process a directory 
	 * again.
	 */
	public void reset() {
		_maxRunNumber = 0;
	}
	
	/**
	 * Obtains the maximum run number calculated against the directory.
	 * @return maximum run currently in the directory.
	 */
	public int getMaxRunNumber() {
		return _maxRunNumber;
	}
	
	/**
	 * Gets the next maximum run number calculated against the directory.
	 * @return next available run number.
	 */
	public int getNextRunNumber() {
		return _maxRunNumber + 1;
	}
	
	
/// FilenameFilter
	
	/**
	 * This implementation keeps track of matches as well, allowing this filter
	 * to be used by others to obtain a value.
	 * (non-Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	@Override
	public boolean accept(File dir, String name) {
		if( name.startsWith( _baseName ) ) {
			String numeric = name.substring( _baseName.length() );
			try {
				int runNumber = Integer.parseInt( numeric );
				_maxRunNumber = 
					( _maxRunNumber > runNumber ) 
					? _maxRunNumber 
					: runNumber;
				return true;
			} catch( Exception e ) {}
		}
		return false;
	}
}