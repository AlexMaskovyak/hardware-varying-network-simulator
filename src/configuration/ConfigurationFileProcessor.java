package configuration;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;

import simulation.simulator.ComputerNetworkSimulator;

import configuration.language.HVNSLanguageLexer;
import configuration.language.HVNSLanguageParser;
import configuration.language.HVNSLanguageTreeEvaluator;


/**
 * Processes a Configuration file, which will result in a run of the simulation
 * with the settings specified.
 * @author Alex Maskovyak
 *
 */
public class ConfigurationFileProcessor {

/// Driver
	
	/**
	 * Configuration / Simulation Driver.
	 * @param args [configuration file path]
	 * @throws RecognitionException if there are problems parsing the AST 
	 * generated from the configuration file.
	 * @throws IOException if a problem occurs reading from the configuration
	 * source.
	 */
	public static void main(String... args) 
			throws RecognitionException, IOException {
		if( args.length != 1 ) {
			System.err.println( 
				"usage: java configuration.ConfigurationFileProcessor [file-name]" );
		} else {
			ConfigurationFileProcessor.processFile( args[ 0 ] );
		}
	}
	

/// Construction
	
	/** Default constructor. */
	public ConfigurationFileProcessor() {}
	
	
/// Utility
	
	/**
	 * Creates a new AST for the configuration file and processes the tree.
	 * @param filePath containing the configuration file.
	 * @throws RecognitionException if there are problems parsing the AST 
	 * generated from the configuration file.
	 * @throws IOException if a problem occurs reading from the configuration
	 * source.
	 */
	public static ComputerNetworkSimulator processFile( String filePath ) 
			throws RecognitionException, IOException {
		return processFile( new File( filePath ) );
	}
	
	/**
	 * Processes the AST generated from a file.
	 * @param filePath containing the configuration file.
	 * @throws RecognitionException if there are problems parsing the AST 
	 * generated from the configuration file.
	 * @throws IOException if a problem occurs reading from the configuration
	 * source.
	 */
	public static ComputerNetworkSimulator processFile( File filePath ) 
			throws RecognitionException, IOException {
		CommonTree ast = getAST( new FileReader( filePath ) );
		return processAST( ast );
	}

	/**
	 * Creates the AST from the configuration information.
	 * @param reader containing configuration information.
	 * @return AST from the configuration information.
	 * @throws IOException if a problem occurs during stream reading.
	 * @throws RecognitionException if there are problems parsing the tokens 
	 * generated from the configuration file into an AST.
	 */
	public static CommonTree getAST(Reader reader) 
			throws IOException, RecognitionException {
		HVNSLanguageParser tokenParser = 
			new HVNSLanguageParser( getTokenStream( reader ) );
		HVNSLanguageParser.script_return result = tokenParser.script();
		reader.close();
		return (CommonTree)result.getTree();
	}
	
	/**
	 * Uses the Lexer to create a token stream.
	 * @param reader containing the non-scanned configuration information.
	 * @return CommonTokenStream from the scanned configuration information.
	 * @throws IOException if a problem occurs during stream reading.
	 */
	public static CommonTokenStream getTokenStream( Reader reader ) 
			throws IOException {
		HVNSLanguageLexer lexer = 
			new HVNSLanguageLexer( new ANTLRReaderStream( reader ) );
		return new CommonTokenStream( lexer );
	}
	
	/**
	 * Processes an AST.
	 * @param ast from the HVNSLanguage.
	 * @throws RecognitionException if a problem occurs recognizing the aspects
	 * of the structure on the CommonTree.
	 */
	public static ComputerNetworkSimulator processAST(CommonTree ast) throws RecognitionException {
		HVNSLanguageTreeEvaluator treeParser = 
			new HVNSLanguageTreeEvaluator( new CommonTreeNodeStream( ast ) );
		return treeParser.script();
	}
}
