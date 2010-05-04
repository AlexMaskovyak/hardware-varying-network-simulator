package configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Scans a client log file to obtain read values for its two epochs.
 * @author Alex Maskovyak
 *
 */
public class ClientLogAnalyzer {
	
/// Bookkeeping Fields
	
	/** time when local reading began. */
	protected Double _localTimeStart;
	/** time when local reading ended, (i.e. last local response was sent.) */
	protected Double _localTimeEnd;
	/** time when remote reading began. */
	protected Double _remoteTimeStart;
	/** time when remote reading ended, (i.e. last remote response received.)*/
	protected Double _remoteTimeEnd;


/// Fields
	
	/** total amount of time it took for the local read process. */
	protected double _totalLocalTime;
	/** total amount of time it took for the remote read process. */
	protected double _totalRemoteTime;
	
	
/// Construction
	
	/** Default constructor. */
	public ClientLogAnalyzer() {
		reset();
	}
	
	
/// Accessor
	
	/**
	 * Obtain the total amount of time on local read operations.
	 * @return total amount of time spent on local read operations.
	 */
	public double getTotalLocalTime() {
		return _totalLocalTime;
	}
	
	/**
	 * Obtains the total amount of time spent requesting and finally receiving
	 * remote data.
	 * @return total amount of time spent to finish remote read operations.
	 */
	public double getTotalRemoteTime() {
		return _totalRemoteTime;
	}
	
	
/// Analysis
	
	/** Reset values. */
	public void reset() {
		_localTimeStart = null;
		_localTimeEnd = null;
		_remoteTimeStart = null;
		_remoteTimeEnd = null;
		
		_totalLocalTime = 0;
		_totalRemoteTime = 0;
	}
	
	/**
	 * Scan several files and average the time values found within.
	 * @param files
	 * @throws FileNotFoundException 
	 */
	public boolean average( List<File> files ) throws FileNotFoundException {
		int logFileTotal = 0;
		double totalLocal = 0;
		double totalRemote = 0;
		for( File f : files ) {
			// note, we use a fileinputstream here due to problems associated
			// with passing a bare file in, this code works in either case as
			// it appears here, oddly, large problems occurred with reading the 
			// file after the simulation was run without it...
			if( scan( new Scanner( new FileInputStream( f ) ) ) ) {
				logFileTotal++;
				totalLocal += getTotalLocalTime();
				totalRemote += getTotalRemoteTime();
			} else {
				return false;
			}
		}
		
		if( logFileTotal > 0 ) {
			_totalLocalTime = totalLocal / logFileTotal;
			_totalRemoteTime = totalRemote / logFileTotal;
			return true;
		}
		
		return false;
	}
	
	/**
	 * Scan for time values.
	 * @param scanner to use for information acquisition.
	 */
	public boolean scan( Scanner scanner ) {
		scanner.reset();
		try {
			while( scanner.hasNext() ) {
				String epoch = scanner.next();
				Double time = scanner.nextDouble();
				// grab start time
				if( epoch.equals("LOCAL") ) {
					if( _localTimeStart == null ) { _localTimeStart = time; }
					// possible end time
					else { _localTimeEnd = time; }
				// grab start time
				} else if( epoch.equals( "REMOTE" ) ) {
					if( _remoteTimeStart == null ) { _remoteTimeStart = time; }
					// possible end time
					else { _remoteTimeEnd = time; }				
				}
				scanner.nextInt();
				scanner.nextInt();
				scanner.nextInt();
				scanner.nextInt();
				scanner.nextInt();
				scanner.nextInt();			
			}
		} catch( Exception e ) {
			e.printStackTrace();
			return false;
		}
		
		if( _localTimeStart == null || _localTimeEnd == null ) {
			return false;
		}

		_totalLocalTime = _localTimeEnd - _localTimeStart;
		_totalRemoteTime = _remoteTimeEnd - _remoteTimeStart;
		
		return true;
	}

	
/// Representation

	/**
	 * Outputs scanned values into the specified file.
	 * @param file to contain the averaged/scanned values.
	 * @throws IOException if a problem occurs trying to write to the file.
	 */
	public void output( File file ) throws IOException {
		if( !file.exists() ) {
			file.getAbsoluteFile().getParentFile().mkdirs();
			file.getAbsoluteFile().createNewFile();
		}
		output( new PrintWriter( file ) );
	}
	
	/**
	 * Outputs scanned values for the to the specified writer.
	 * @param writer to use to write values.
	 */
	public void output( PrintWriter writer ) {
		writer.write( this.toString() );
		writer.flush();
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format(
					"%f\t%f", 
					_localTimeEnd - _localTimeStart, 
					_remoteTimeEnd - _remoteTimeStart  );
	}
	
	
/// Testing
	
	/**
	 * Test driver.
	 * @param args N/A
	 */
	public static void main(String... args) {
		ClientLogAnalyzer client = new ClientLogAnalyzer();
		try {
			client.scan( new Scanner( new File( "C:\\Users\\user\\workspaces\\gradproject\\configurations\\config_set_1_adaptor_speed\\config_1\\run_1\\node_0.log" ) ) );
			System.out.println( client );
			File[] files = new File[2];
			files[0] = new File("C:\\Users\\user\\workspaces\\gradproject\\configurations\\config_set_1_adaptor_speed\\config_1\\run_1\\node_0.log");
			files[1] = new File("C:\\Users\\user\\workspaces\\gradproject\\configurations\\config_set_1_adaptor_speed\\config_1\\run_2\\node_0.log");
			 
			client.reset();
			client.average( Arrays.asList( files ) );
			System.out.println( client );
		} catch( Exception e ) {
			e.printStackTrace();
		}		
	}
}
