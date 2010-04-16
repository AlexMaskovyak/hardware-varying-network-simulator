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

import configuration.language.HVNSLanguage2Lexer;
import configuration.language.HVNSLanguage2Parser;
import configuration.language.HVNSLanguage2TreeEvaluator;


/**
 * Processes a Configuration file, which will result in a run of the simulation
 * with the settings specified.
 * @author Alex Maskovyak
 *
 */
public class ConfigurationFileProcessor {

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
			new ConfigurationFileProcessor().processFile( args[ 0 ] );
		}
	}

	/** Default constructor. */
	public ConfigurationFileProcessor() {
		
	}
	
	/**
	 * Creates a new AST for the configuration file and processes the tree.
	 * @param filePath containing the configuration file.
	 * @throws RecognitionException if there are problems parsing the AST 
	 * generated from the configuration file.
	 * @throws IOException if a problem occurs reading from the configuration
	 * source.
	 */
	public static void processFile( String filePath ) 
			throws RecognitionException, IOException {
		processFile( new File( filePath ) );
	}
	
	/**
	 * Processes the AST generated from a file.
	 * @param filePath containing the configuration file.
	 * @throws RecognitionException if there are problems parsing the AST 
	 * generated from the configuration file.
	 * @throws IOException if a problem occurs reading from the configuration
	 * source.
	 */
	public static void processFile( File filePath ) 
			throws RecognitionException, IOException {
		CommonTree ast = getAST( new FileReader( filePath ) );
		processAST( ast );
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
		HVNSLanguage2Parser tokenParser = 
			new HVNSLanguage2Parser( getTokenStream( reader ) );
		HVNSLanguage2Parser.script_return result = tokenParser.script();
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
		HVNSLanguage2Lexer lexer = 
			new HVNSLanguage2Lexer( new ANTLRReaderStream( reader ) );
		return new CommonTokenStream( lexer );
	}
	
	/**
	 * Processes an AST.
	 * @param ast from the HVNSLanguage.
	 * @throws RecognitionException if a problem occurs recognizing the aspects
	 * of the structure on the CommonTree.
	 */
	public static void processAST(CommonTree ast) throws RecognitionException {
		HVNSLanguage2TreeEvaluator treeParser = 
			new HVNSLanguage2TreeEvaluator( new CommonTreeNodeStream( ast ) );
		treeParser.script();
	}
}
