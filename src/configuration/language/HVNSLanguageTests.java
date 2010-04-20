package configuration.language;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.TreeNode;

import network.entities.Node;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;

import computation.HardwareComputerNode;

import configuration.ConfigurationFileProcessor;
import configuration.language.HVNSLanguage2Parser.script_return;



/**
 * Provides testing for Tree Rules.
 * @author Alex Maskovyak
 *
 */
public class HVNSLanguageTests {

/// Tester
	
	/**
	 * Test driver.
	 * @param args N/A
	 * @throws RecognitionException if the string does not meet the rule's
	 * expectations.
	 * @throws IOException if a problem occurs reading the input.
	 */
	public static void main(String... args) throws RecognitionException, IOException {
		HVNSLanguageTests tester = new HVNSLanguageTests();
		tester.testAssignment();
		//tester.testValue();
		tester.testAssignAndValue();
		tester.testCloneInstantiation();
		tester.testMultiAssign();
		tester.testMath();
		tester.testJavaInstantiation();
		tester.testConnect();
	}

	/**
	 * Obtains the method of the specified name for the given object.
	 * @param object to be interrogated.
	 * @param name of the method to obtain.
	 * @return method of the specified name.
	 */
	public Method getObjectMethod( Object object, String name ) {
		try {
			return object.getClass().getMethod( name, Object.class );
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

	
/// Fields
	
	/** scanner. */
	protected HVNSLanguage2Lexer _lexer;
	/** parser. */
	protected HVNSLanguage2Parser _parser;
	/** tree evaluator. */
	protected HVNSLanguage2TreeEvaluator _treeParser;

	protected String _configBegin = 
		"config Configuration1" +
		"	begin" +
		"	simulator = java simulation.simulator.ComputerNetworkSimulator;";
	protected String _configEnd = "end";
	

/// Construction
	
	/**
	 * Default constructor.
	 */
	public HVNSLanguageTests() {
		init();
	}
	
	protected void init() {
		_lexer = new HVNSLanguage2Lexer();
	}
	
	
/// Tests

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
				new StringReader( 
					String.format("%s stuff = %f;%s", 
						_configBegin, 
						expectedValue,
						_configEnd  )) );
		HVNSLanguage2TreeEvaluator treeParser = 
			new HVNSLanguage2TreeEvaluator( new CommonTreeNodeStream( ast ) );
		treeParser.script();
		try {
			Object result = treeParser.getVariable( "stuff" );
			System.out.printf( 
				(expectedValue.equals( result ) )
				? "Test Assignment/Value Success!\n" 
				: "Test Assignment/Value Failure\n.");
		} catch( Exception e ) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Test the value rule ensuring that it returns the value expected.
	 * @throws RecognitionException if the string does not meet the rule's 
	 * expectations.
	 * @throws IOException if a problem occurs reading the input.
	 */
	/*public void testValue() throws RecognitionException, IOException {
		Double expectedValue = new Double( 13 );
		CommonTree ast = 
			ConfigurationFileProcessor.getAST( 
				new StringReader( 
					String.format(
						"%s %f %s", 
						_configBegin,
						expectedValue,
						_configEnd ) ) );
		_treeParser = 
			new HVNSLanguage2TreeEvaluator( new CommonTreeNodeStream( ast ) );
		Object result = _treeParser.value();
		System.out.printf( 
			(expectedValue.equals( result ) )
			? "Test Value Success!\n" 
			: "Test Value Failure\n.");
	}*/
	
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
						"%s stuff = 13; myStuff = %f; yourStuff = myStuff; %s",
						_configBegin, expectedValue, _configEnd ) ) );
		_treeParser = 
			new HVNSLanguage2TreeEvaluator( new CommonTreeNodeStream( ast ) );
		_treeParser.script();
		Object finalAssignment = _treeParser.getVariable( "yourStuff" );
		System.out.printf( 
				( expectedValue.equals( finalAssignment ) )
				? "Test Assign Several Values Success!\n" 
				: "Test Assign Several Values Failure\n.");
	}
	
	/**
	 * Test the value rule ensuring that it returns the value expected.
	 * @throws RecognitionException if the string does not meet the rule's 
	 * expectations.
	 * @throws IOException if a problem occurs reading the input.
	 */
	public void testJavaInstantiation() throws RecognitionException, IOException {
		String line = 
			String.format(
				"%s javaNode = java computation.HardwareComputerNode { setMaxAllowedOperations : 10; setRefreshInterval : 1; }; %s",
				_configBegin, _configEnd );
		CommonTree ast = 
			ConfigurationFileProcessor.getAST( 
				new StringReader( line ) );
		_treeParser = 
			new HVNSLanguage2TreeEvaluator( new CommonTreeNodeStream( ast ) );
		_treeParser.script();
		HardwareComputerNode node = 
			(HardwareComputerNode)_treeParser.getVariable( "javaNode" );
		System.out.printf( 
			( node != null && node.getMaxAllowedOperations() == 10 && node.getRefreshInterval() == 1 )
			? "Test Java Instantiation and Method Invocation Success!\n" 
			: "Test Java Instantiation and Method Invocation Failure.\n");
	}
	
	/**
	 * Test the value rule ensuring that it returns the value expected.
	 * @throws RecognitionException if the string does not meet the rule's 
	 * expectations.
	 * @throws IOException if a problem occurs reading the input.
	 */
	public void testCloneInstantiation() throws RecognitionException, IOException {
		String className = "network.entities.Node";
		String line = String.format(
				"%s original = java %s; cloned = clone original; %s", 
				_configBegin,
				className,
				_configEnd );
		CommonTree ast = 
			ConfigurationFileProcessor.getAST( 
				new StringReader( line ) );
		_treeParser = 
			new HVNSLanguage2TreeEvaluator( new CommonTreeNodeStream( ast ) );
		_treeParser.script();
		Object original = _treeParser.getVariable( "original" );
		Object cloned = _treeParser.getVariable( "cloned" );
		System.out.printf( 
			( original instanceof Node && cloned instanceof Node && original != cloned )
			? "Test java and clone Instantiation Success!\n" 
			: "Test java and clone Instantiation Failure\n.");
	}	
	
	/**
	 * Test the value rule ensuring that it returns the value expected.
	 * @throws RecognitionException if the string does not meet the rule's 
	 * expectations.
	 * @throws IOException if a problem occurs reading the input.
	 */
	public void testMultiAssign() throws RecognitionException, IOException {
		String line = String.format(
			"%s hey, you, there = java network.entities.Node; %s",
			_configBegin, _configEnd );
		CommonTree ast = 
			ConfigurationFileProcessor.getAST( 
				new StringReader( line ) );
		_treeParser = 
			new HVNSLanguage2TreeEvaluator( new CommonTreeNodeStream( ast ) );
		_treeParser.script();
		
		Object first = _treeParser.getVariable( "hey" );
		Object second = _treeParser.getVariable( "you" );
		Object third = _treeParser.getVariable( "there" );
		
		System.out.printf( 
			( first != null && first != second && first != third 
				&& second != null && second != third
				&& third != null )
			? "Test Multi-Assignment Success!\n" 
			: "Test Multi-Assignment Instantiation Failure\n.");
	}
	
	/**
	 * Test the value rule ensuring that it returns the value expected.
	 * @throws RecognitionException if the string does not meet the rule's 
	 * expectations.
	 * @throws IOException if a problem occurs reading the input.
	 */
	public void testMath() throws RecognitionException, IOException {
		String line = 
			String.format( 
				"%s mathVar = ((1 + -5) * 4); %s",
				_configBegin, _configEnd );
		CommonTree ast = 
			ConfigurationFileProcessor.getAST( 
				new StringReader( line ) );
		_treeParser = 
			new HVNSLanguage2TreeEvaluator( new CommonTreeNodeStream( ast ) );
		_treeParser.script();
		Double result = (Double)_treeParser.getVariable( "mathVar" );
		
		System.out.printf( 
			( result == -16 )
			? "Test Math Success!\n" 
			: "Test Math Failure\n.");
	}
	
	/**
	 * Test the value rule ensuring that it returns the value expected.
	 * @throws RecognitionException if the string does not meet the rule's 
	 * expectations.
	 * @throws IOException if a problem occurs reading the input.
	 */
	public void testConnect() throws RecognitionException, IOException {
		String line = 
			String.format(
				"%s nodeOne, nodeTwo, nodeThree, nodeFour = java network.entities.Node; %s %s",
				_configBegin,
				"(+ nodeOne nodeTwo nodeThree nodeFour);",
				_configEnd );
		CommonTree ast = 
			ConfigurationFileProcessor.getAST( 
				new StringReader( line ) );
		_treeParser = 
			new HVNSLanguage2TreeEvaluator( new CommonTreeNodeStream( ast ) );
		_treeParser.script();

		System.out.println(ast.toStringTree());
		//CommonTree tree = new CommonTree();
		
		//System.out.printf( 
		//	( result == -16 )
		//	? "Test Math Success!\n" 
		//	: "Test Math Failure\n.");
	}
}
	
