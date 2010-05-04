package configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.antlr.runtime.RecognitionException;

import simulation.simulator.ComputerNetworkSimulator;

import computation.HardwareComputerNode;

/**
 * Handles the directory management for a single configuration file and the runs
 * that will be run for it.
 * @author Alex Maskovyak
 *
 */
public class ConfigurationManager {

/// Fields
	
	/** directory holding the configuration file. */
	protected File _runsDirectory;
	/** file with configuration information. */
	protected File _configFile;
	/** directory which will hold the current run's output. */
	protected File _currentRunDirectory;
	/** all runs directories. */
	protected List<File> _runDirectories;
	
	/** base name for a simulation run, this value gets incremented as additional
	 * runs occur. */
	protected String _baseRunName;
	/** filter to use/reuse for finding run directories. */
	protected RunDirectoryFilter _runDirectoryFilter;
	
	
	/** maximum run number for this configuration directory. */
	protected int _maxSimRunNumber;
	/** constructs a simulator via a configuration file. */
	protected ConfigurationFileProcessor _configFileProcessor;
	

/// Construction

	/**
	 * Constructor.  Uses default directory run name of "run_[0-9]+"
	 * @param runsDirectory path to the directory containing the sim runs.
	 * @param configFile path to the configuration file.
	 */
	public ConfigurationManager( String runsDirectory, String configFile ) {
		this( runsDirectory, configFile, "run_" );
	}
	
	/**
	 * Constructor.  Uses default directory run name of "run_[0-9]+"
	 * @param runsDirectory directory containing the sim runs.
	 * @param configFile configuration file.
	 */
	public ConfigurationManager( File runsDirectory, File configFile ) {
		this( runsDirectory, configFile, "run_" );
	}
	
	/**
	 * Constructor.
	 * @param runsDirectory path to the directory containing the config file,
	 * and simulation run directories.
	 * @param configFile path to configuration file.
	 * @param baseRunName for a simulation run.
	 */
	public ConfigurationManager( 
			String runsDirectory, 
			String configFile, 
			String baseRunName ) {
		this( new File( runsDirectory ), new File( configFile ), baseRunName );
	}
	
	/**
	 * Constructor.
	 * @param configFile the configuration file.
	 * @param runsDirectory the directory containing the configuration file
	 * and simulation run directories.
	 * @param baseRunName for a simulation run.
	 */
	public ConfigurationManager( File runsDirectory, File configFile, String baseRunName ) {
		// assign
		_configFile = configFile;
		_runsDirectory = runsDirectory;
		_baseRunName = baseRunName;
		
		// init
		init();
		
		refreshValues();
	}
	
	/** Externalize instantiation. */
	protected void init() {
		_runDirectories = new ArrayList<File>();
		_runDirectoryFilter = new RunDirectoryFilter( _baseRunName );
		_configFileProcessor = new ConfigurationFileProcessor();
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
	public File getRunsDirectory() {
		return _runsDirectory;
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

	/**
	 * Creates a simulator from the configuration file specified.
	 * @return ComputerNetworkSimulator constructed from a HVNS language file,
	 * null if a problem occurred accessing, reading, scanning, parsing or 
	 * acting upon the directives in the file.
	 */
	public ComputerNetworkSimulator configureSimulator() {
		try {
			return _configFileProcessor.processFile( _configFile );
		} catch (RecognitionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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
					_runsDirectory.getAbsolutePath(), 
					File.separator, 
					getBaseRunName(), 
					getMaxRunNumber() + 1 ) );
		if( newRunDir.mkdirs() ) {
			setMaxRunNumber( getMaxRunNumber() + 1 );
			return newRunDir;
		}
		return null;
	}

	/**
	 * Runs the RunAnalyzer which in turn runs the LogAnalyzers.  This will 
	 * create aggregated data files of this run in the sum directory of the 
	 * configuration directory.
	 * @throws FileNotFoundException if a file cannot be found by the scanner.
	 */
	public void makeAveragesDirectory() throws FileNotFoundException {
		// check if the averages directory exists
		System.out.println("make averages...");
		
		// create analyzer
		ClientLogAnalyzer clientAnalyzer = new ClientLogAnalyzer();
		ServerLogAnalyzer serverAnalyzer = new ServerLogAnalyzer();
		refreshValues();
		File runDir1 = _runDirectories.get( 0 );
		StringBuilder serverLines = new StringBuilder();
		
		// for each file in this run directory, find all in that series across
		// runs
		System.out.println(runDir1.listFiles().length);
		for( File baseLog : runDir1.listFiles() ) {
			List<File> logs = getLogsAcrossRunsFor( baseLog.getName() );
			clientAnalyzer.reset();
			if( clientAnalyzer.average( logs ) ) {
				try {
					clientAnalyzer.output( 
						new File( 
							String.format( 
								"%s%savg%sclient.log",
								_runsDirectory, 
								File.separator, 
								File.separator ) ) );
				} catch( Exception e ) { e.printStackTrace(); }
			}
			serverAnalyzer.reset();
			serverAnalyzer.average( logs ); 
			serverLines.append( serverAnalyzer );
			serverLines.append("\n");
		}
		PrintWriter writer = 
			new PrintWriter(
				new File( 
					String.format( 
						"%s%savg%sserver.log",
						_runsDirectory, 
						File.separator, 
						File.separator ) ) );
		writer.write( serverLines.toString() );
		writer.flush();
		writer.close();
	}
	
	/**
	 * Gets a list of files that correspond to the filename provided across all
	 * runs.
	 * @param fileName corresponding to one node's log.
	 * @return all log files associated with this particular filename across all
	 * runs.
	 */
	public List<File> getLogsAcrossRunsFor( String fileName ) {
		List<File> logsForFilename = new ArrayList<File>();
		for( File runDir : _runDirectories ) {
			File logFile = new File( String.format( "%s%s%s", runDir.getAbsolutePath(), File.separator, fileName ) );
			logsForFilename.add( logFile );
		}
		return logsForFilename;
	}
	
/// Utility

	/**
	 * Gets a list of run directories.
	 * @return list of run directories.
	 */
	public List<File> scanForRunDirectories() {
		_runDirectoryFilter.reset();
		File[] runDirectories = _runsDirectory.listFiles( _runDirectoryFilter );
		return Arrays.asList( runDirectories );
	}
	
	/**
	 * Refreshes the values being managed.  Ensures that the directories we are
	 * refereing are up to date, etc.
	 */
	public void refreshValues() {
		_runDirectories.clear();
		_runDirectories.addAll( scanForRunDirectories() );
		_maxSimRunNumber = _runDirectoryFilter.getMaxRunNumber();
	}

	
/// Tester

	/**
	 * Test driver.
	 * @param args N/A
	 * @throws FileNotFoundException 
	 */
	public static void main(String... args) throws FileNotFoundException {
		String runsDirectory = "C:\\Users\\user\\workspaces\\gradproject\\configurations\\config_set_1_adaptor_speed\\config_1\\";
		String configFile = "C:\\Users\\user\\workspaces\\gradproject\\configurations\\config_set_1_adaptor_speed\\config_1\\config_1.cfg";
		ConfigurationManager manager = new ConfigurationManager( runsDirectory, configFile );
		//System.out.println( manager.makeNewRunDirectory() );
		System.out.println( manager.getConfigFile() );
		System.out.println( manager.getRunsDirectory() );
		System.out.println( manager.getBaseRunName() );
		System.out.println( manager.getMaxRunNumber() );
		manager.makeAveragesDirectory();
	}
	
}

/**
 * Identifies the maximum simulation run number from a configuration 
 * directory.
 * @author Alex Maskovyak
 *
 */
class RunDirectoryFilter implements FilenameFilter {

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
	public RunDirectoryFilter(String baseName) {
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