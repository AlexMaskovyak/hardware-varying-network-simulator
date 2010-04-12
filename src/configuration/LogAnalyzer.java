package configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Analyzes a single log.
 * @author Alex Maskovyak
 *
 */
public class LogAnalyzer {

/// Fields
	
	/** analyzes and produces baseline / distribution time-centric logs, for
	 * the client. */
	protected ClientLogAnalyzer _client;
	/** analyzes and produces aggregated data sent/received, etc. information 
	 * for a file. */
	protected ServerLogAnalyzer _server;
	/** */
	protected StringBuilder _serverLines;
	
	
	
/// Construction
	
	/** Default constructor. */
	public LogAnalyzer() { 
		init();
	}
	
	/** Externalize instantiation. */
	protected void init() {
		_client = new ClientLogAnalyzer();
		_server = new ServerLogAnalyzer();
		//_clientLines = new StringBuilder();
		_serverLines = new StringBuilder();
	}
	
	
/// Analyze

	/**
	 * Resets aggregated lines.
	 */
	public void reset() {
		//_clientLines.setLength( 0 );
		_serverLines.setLength( 0 );
	}
	
	/**
	 * Analyzes the files with the analyzers.  Creates an average of 
	 * information.
	 * @param scanner wrapping the log of information to analyze.
	 * @throws FileNotFoundException 
	 */
	public void analyze( List<File> files ) throws FileNotFoundException {
		resetAnalyzers();
		_client.average( files );
		_server.average( files );
	}
	
	/**
	 * Resets the analyzer's state so that a new file may be analyzed.
	 */
	public void resetAnalyzers() {
		_client.reset();
		_server.reset();
	}
	
	/**
	 * Outputs files to the output directory.  Clients have two files produced.
	 * Servers produce one file.
	 * @param outputDirectory to hold files.
	 * @param baseName base filename to use to generate our output.
	 * @throws FileNotFoundException 
	 */
	public void outputClient( String outputDirectory ) throws FileNotFoundException {
		
		File clientFile = 
			new File( 
				String.format( 
					"%s%s%s",
					outputDirectory,
					File.separator, 
					"client_read.log") );
		File serverFile =
			new File(
				String.format( 
					"%s%s%s",
					outputDirectory,
					File.separator, 
					"servers.log") );
		PrintWriter client = new PrintWriter( clientFile );
		client.write( _client.toString() );
		client.flush();
		client.close();
		PrintWriter server = new PrintWriter( serverFile );
		//server.write( _serverLines.toString() );
		server.flush();
		server.close();
	}
	
	public void outputServer( String outputDirectory ) {
		
	}
}
