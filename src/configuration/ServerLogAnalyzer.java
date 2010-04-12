package configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Scans a client log file to obtain read values for its two epochs.
 * @author Alex Maskovyak
 *
 */
public class ServerLogAnalyzer {
	
/// Fields
	
	/** total data sent. */
	protected double _dataSent;
	/** total data received. */
	protected double _dataReceived;
	/** total control information sent. */
	protected double _controlSent;
	/** total control information received. */
	protected double _controlReceived;
	/** total data stored. */
	protected double _dataStored;
	/** total data retrieved. */
	protected double _dataRetrieved;
	/** total time of operation. */
	protected double _time;
	/** name of the item. */
	protected String _name;

/// Construction
	
	/** Default constructor. */
	public ServerLogAnalyzer() {
		reset();
	}
	

/// Analysis
	
	/** Reset values. */
	protected void reset() {
		_dataSent = 0;
		_dataReceived = 0;
		_controlSent = 0;
		_controlReceived = 0;
		_dataStored = 0;
		_dataRetrieved = 0;
	}
	
	/**
	 * Scan several files and average the time values found within.
	 * @param files
	 * @throws FileNotFoundException 
	 */
	public void average( List<File> files ) throws FileNotFoundException {
		int logFileTotal = 0;
		reset();
		try {
			for( File f : files ) {
				if( scan( new Scanner( f ) ) ) {
					_name = f.getName();
					logFileTotal++;
				}
			}
		} catch( Exception e ) {
			reset();
			e.printStackTrace();
		}
		
		_dataSent /= logFileTotal;
		_dataReceived /= logFileTotal;
		_controlSent /= logFileTotal;
		_controlReceived /= logFileTotal;
		_dataStored /= logFileTotal;
		_dataRetrieved /= logFileTotal;
	}
	
	/**
	 * Scan for time values.
	 * @param scanner to use for information acquisition.
	 */
	/**
	 * @param scanner
	 * @return
	 */
	public boolean scan( Scanner scanner ) {
		try {
			while( scanner.hasNext() ) {
				String epoch = scanner.next();
				_time = scanner.nextDouble();
				_dataSent += scanner.nextInt();				
				_dataReceived += scanner.nextInt();
				_controlSent += scanner.nextInt();
				_controlReceived += scanner.nextInt();
				_dataStored += scanner.nextInt();
				_dataRetrieved += scanner.nextInt();
			}
		} catch( Exception e ) {
			e.printStackTrace();
			return false;
		}
		return true;
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
					"%s%f\t%f\t%f\t%f\t%f\t%f\t%f", 
					(_name != null) ? _name + "\t" : "",
					_time,
					_dataSent,
					_dataReceived,
					_controlSent,
					_controlReceived,
					_dataStored,
					_dataReceived);
	}
	
	
/// Testing 
	
	/**
	 * Test driver.
	 * @param args N/A
	 */
	public static void main(String... args) {
		ServerLogAnalyzer server = new ServerLogAnalyzer();
		try {
			server.scan( new Scanner( new File( "C:\\Users\\user\\workspaces\\gradproject\\configurations\\config_set_1_adaptor_speed\\config_1\\run_1\\node_0.log" ) ) );
			System.out.println( server );
			File[] files = new File[2];
			files[0] = new File("C:\\Users\\user\\workspaces\\gradproject\\configurations\\config_set_1_adaptor_speed\\config_1\\run_1\\node_0.log");
			files[1] = new File("C:\\Users\\user\\workspaces\\gradproject\\configurations\\config_set_1_adaptor_speed\\config_1\\run_2\\node_0.log");
			 
			server.reset();
			server.average( Arrays.asList( files ) );
			System.out.println( server );
		} catch( Exception e ) {
			e.printStackTrace();
		}		
	}
}
