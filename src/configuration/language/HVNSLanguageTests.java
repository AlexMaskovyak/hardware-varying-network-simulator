package configuration.language;

import java.io.IOException;
import java.io.StringReader;

import javax.swing.tree.TreeNode;

import network.entities.Node;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;

import configuration.ConfigurationFileProcessor;
import configuration.language.HVNSLanguage2Parser.script_return;



/**
 * Provides testing for Tree Rules.
 * @author Alex Maskovyak
 *
 */
public class HVNSLanguageTests {

	/** scanner. */
	protected HVNSLanguage2Lexer _lexer;
	/** parser. */
	protected HVNSLanguage2Parser _parser;
	/** tree evaluator. */
	protected HVNSLanguage2TreeEvaluator _treeParser;
	
	/**
	 * Default constructor.
	 */
	public HVNSLanguageTests() {
		init();
	}
	
	protected void init() {
		_lexer = new HVNSLanguage2Lexer();
	}
	
	public static void main(String... args) throws RecognitionException, IOException {
		HVNSLanguageTests tester = new HVNSLanguageTests();
		tester.testAssignment();
		tester.testValue();
		tester.testAssignAndValue();
		tester.testJavaInstantiation();
		tester.testCloneInstantiation();
	}
	
	/*public void testAssignAndValue() throws IOException, RecognitionException {
		CommonTree ast = 
			ConfigurationFileProcessor.getAST( new StringReader("stuff = 13 myStuff = 15 yourStuff = myStuff") );
		HVNSLanguage2TreeEvaluator treeParser = 
			new HVNSLanguage2TreeEvaluator( new CommonTreeNodeStream( ast ) );
		Object o = treeParser.script();
		System.out.println("ok! : " + o );
	}
	
	public void testAssignment() throws RecognitionException, IOException {
		CommonTree ast = 
			ConfigurationFileProcessor.getAST( new StringReader("stuff = 13") );
		HVNSLanguage2TreeEvaluator treeParser = 
			new HVNSLanguage2TreeEvaluator( new CommonTreeNodeStream( ast ) );
		Object o = treeParser.assign();
		System.out.println("ok! : " + o);
	}*/
	
	/*public void testVaue() throws RecognitionException, IOException {
		CommonTree ast = 
			ConfigurationFileProcessor.getAST( new StringReader("13") );
		HVNSLanguage2TreeEvaluator treeParser = 
			new HVNSLanguage2TreeEvaluator( new CommonTreeNodeStream( ast ) );
		Object o = treeParser.value();
		System.out.println("ok! : " + o);
	}*/

	/**
	 * Test the assignment rule ensuring that it returns the result expected.
	 * @throws RecognitionException if the string does not meet the rule's
	 * expectations.
	 * @throws IOException if a problem occurs reading the input.
	 */
	public void testAssignment() throws RecognitionException, IOException {
		Double expectedValue = new Double( 13 );
		CommonTree ast = 
			ConfigurationFileProcessor.getAST( 
					new StringReader( String.format("stuff = %f;", expectedValue )) );
		HVNSLanguage2TreeEvaluator treeParser = 
			new HVNSLanguage2TreeEvaluator( new CommonTreeNodeStream( ast ) );
		Object result = treeParser.assign();
		System.out.printf( 
			(expectedValue.equals( result ) )
			? "Test Assignment Success!\n" 
			: "Test Assignment Failure\n.");
	}
	
	/**
	 * Test the value rule ensuring that it returns the value expected.
	 * @throws RecognitionException if the string does not meet the rule's 
	 * expectations.
	 * @throws IOException if a problem occurs reading the input.
	 */
	public void testValue() throws RecognitionException, IOException {
		Double expectedValue = new Double( 13 );
		CommonTree ast = 
			ConfigurationFileProcessor.getAST( 
				new StringReader( String.format("%f", expectedValue )) );
		_treeParser = 
			new HVNSLanguage2TreeEvaluator( new CommonTreeNodeStream( ast ) );
		Object result = _treeParser.value();
		System.out.printf( 
			(expectedValue.equals( result ) )
			? "Test Value Success!\n" 
			: "Test Value Failure\n.");
	}
	
	/**
	 * Test the assignment rule whereby we assign another value to reference the
	 * same object.
	 * @throws RecognitionException if the string does not meet the rule's
	 * expectations.
	 * @throws IOException if a problem occurs reading the input.
	 */
	public void testAssignAndValue() throws IOException, RecognitionException {
		Double expectedValue = new Double( 15 );
		CommonTree ast = 
			ConfigurationFileProcessor.getAST( 
				new StringReader(
					String.format( 
						"stuff = 13; myStuff = %f; yourStuff = myStuff;",
						expectedValue ) ) );
		_treeParser = 
			new HVNSLanguage2TreeEvaluator( new CommonTreeNodeStream( ast ) );
		Object result = _treeParser.script();
		System.out.printf( 
				(expectedValue.equals( result ) )
				? "Test Assign and Value Success!\n" 
				: "Test Assign and Value Failure\n.");
	}
	
	/**
	 * Test the value rule ensuring that it returns the value expected.
	 * @throws RecognitionException if the string does not meet the rule's 
	 * expectations.
	 * @throws IOException if a problem occurs reading the input.
	 */
	public void testJavaInstantiation() throws RecognitionException, IOException {
		String className = "network.entities.Node";
		String line = String.format("java %s", className );
		CommonTree ast = 
			ConfigurationFileProcessor.getAST( 
				new StringReader( line ) );
		_treeParser = 
			new HVNSLanguage2TreeEvaluator( new CommonTreeNodeStream( ast ) );
		Object result = _treeParser.value();
		System.out.printf( 
			(result instanceof Node)
			? "Test Java Instantiation Success!\n" 
			: "Test Java Instantiation Failure\n.");
	}	
	
	/**
	 * Test the value rule ensuring that it returns the value expected.
	 * @throws RecognitionException if the string does not meet the rule's 
	 * expectations.
	 * @throws IOException if a problem occurs reading the input.
	 */
	public void testCloneInstantiation() throws RecognitionException, IOException {
		String className = "network.entities.Node";
		String line = String.format("original = java %s; clone original;", className );
		CommonTree ast = 
			ConfigurationFileProcessor.getAST( 
				new StringReader( line ) );
		_treeParser = 
			new HVNSLanguage2TreeEvaluator( new CommonTreeNodeStream( ast ) );
		Object result = _treeParser.script();
		System.out.printf( 
			(result instanceof Node )
			? "Test Java Instantiation Success!\n" 
			: "Test Java Instantiation Failure\n.");
	}	
}
