package configuration.language;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import network.entities.IConnectionMedium;
import network.entities.INode;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;
import org.apache.log4j.PropertyConfigurator;

import simulation.simulatable.ISimulatable;
import simulation.simulator.ComputerNetworkSimulator;

import computation.HardwareComputerNode;

import configuration.ConfigurationFileProcessor;


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
		PropertyConfigurator.configure("log4j.properties"); // disable it
		HVNSLanguageTests tester = new HVNSLanguageTests();
		tester.testAssignment();
		//tester.testValue();
		tester.testAssignAndValue();
		tester.testCloneInstantiation();
		tester.testMultiAssign();
		tester.testMath();
		tester.testJavaInstantiation();
		tester.testSimulatorSetup();
		tester.testConnect();
	}

	
/// Fields
	
	/** scanner. */
	protected HVNSLanguageLexer _lexer;
	/** parser. */
	protected HVNSLanguageParser _parser;
	/** tree evaluator. */
	protected HVNSLanguageTreeEvaluator _treeParser;

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
		_lexer = new HVNSLanguageLexer();
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
		HVNSLanguageTreeEvaluator treeParser = 
			new HVNSLanguageTreeEvaluator( new CommonTreeNodeStream( ast ) );
		treeParser.script();
		try {
			Object result = treeParser.getVariable( "stuff" );
			System.out.printf( 
				(expectedValue.equals( result ) )
				? "Test Assignment/Value Success!\n" 
				: "Test Assignment/Value Failure.\n");
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
			new HVNSLanguageTreeEvaluator( new CommonTreeNodeStream( ast ) );
		_treeParser.script();
		Object finalAssignment = _treeParser.getVariable( "yourStuff" );
		System.out.printf( 
				( expectedValue.equals( finalAssignment ) )
				? "Test Assign Several Values Success!\n" 
				: "Test Assign Several Values Failure.\n");
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
				"%s javaNode = java computation.HardwareComputerNode { setMaxAllowedOperations : 10; setRefreshInterval : 1.0; }; %s",
				_configBegin, _configEnd );
		CommonTree ast = 
			ConfigurationFileProcessor.getAST( 
				new StringReader( line ) );
		_treeParser = 
			new HVNSLanguageTreeEvaluator( new CommonTreeNodeStream( ast ) );
		//System.out.println( ast.toStringTree() );
		_treeParser.script();
		HardwareComputerNode node = 
			(HardwareComputerNode)_treeParser.getVariable( "javaNode" );
		System.out.printf( 
			( node != null && node.getMaxAllowedOperations() == 10 && node.getRefreshInterval() == 1.0 )
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
		String className = "computation.HardwareComputerNode";
		String line = String.format(
				"%s original = java %s { setMaxAllowedOperations : 10; setRefreshInterval : 1.0; } ; cloned = clone original; %s", 
				_configBegin,
				className,
				_configEnd );
		CommonTree ast = 
			ConfigurationFileProcessor.getAST( 
				new StringReader( line ) );
		_treeParser = 
			new HVNSLanguageTreeEvaluator( new CommonTreeNodeStream( ast ) );
		_treeParser.script();
		Object original = _treeParser.getVariable( "original" );
		Object cloned = _treeParser.getVariable( "cloned" );
		System.out.printf( 
			( original instanceof HardwareComputerNode 
					&& cloned instanceof HardwareComputerNode 
					&& original != cloned 
					&& ((HardwareComputerNode)original).getMaxAllowedOperations() == 10 
					&& ((HardwareComputerNode)cloned).getMaxAllowedOperations() == 10 )
			? "Test java and clone Instantiation Success!\n" 
			: "Test java and clone Instantiation Failure.\n");
	}	
	
	/**
	 * Test the value rule ensuring that it returns the value expected.
	 * @throws RecognitionException if the string does not meet the rule's 
	 * expectations.
	 * @throws IOException if a problem occurs reading the input.
	 */
	public void testMultiAssign() throws RecognitionException, IOException {
		String line = String.format(
			"%s hey, you, there = java computation.HardwareComputerNode { setMaxAllowedOperations : 10; }; %s",
			_configBegin, _configEnd );
		CommonTree ast = 
			ConfigurationFileProcessor.getAST( 
				new StringReader( line ) );
		_treeParser = 
			new HVNSLanguageTreeEvaluator( new CommonTreeNodeStream( ast ) );
		_treeParser.script();
		
		HardwareComputerNode first = (HardwareComputerNode)_treeParser.getVariable( "hey" );
		HardwareComputerNode second = (HardwareComputerNode)_treeParser.getVariable( "you" );
		HardwareComputerNode third = (HardwareComputerNode)_treeParser.getVariable( "there" );
		
		System.out.printf( 
			( first != null && first != second && first != third 
				&& second != null && second != third
				&& third != null 
				&& (first.getMaxAllowedOperations() == second.getMaxAllowedOperations()) 
				&& (second.getMaxAllowedOperations() == third.getMaxAllowedOperations())
				&& third.getMaxAllowedOperations() == 10 )
			? "Test Multi-Assignment Instantiation Success!\n" 
			: "Test Multi-Assignment Instantiation Failure.\n");
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
			new HVNSLanguageTreeEvaluator( new CommonTreeNodeStream( ast ) );
		_treeParser.script();
		Integer result = (Integer)_treeParser.getVariable( "mathVar" );
		
		System.out.printf( 
			( result == -16 )
			? "Test Math Success!\n" 
			: "Test Math Failure.\n");
	}
	
	/**
	 * Test the value rule ensuring that it returns the value expected.
	 * @throws RecognitionException if the string does not meet the rule's 
	 * expectations.
	 * @throws IOException if a problem occurs reading the input.
	 */
	public void testSimulatorSetup() throws RecognitionException, IOException {
		String className = "computation.HardwareComputerNode";
		String line = String.format(
				"%s original = java %s { setMaxAllowedOperations : 10; setRefreshInterval : 1.0; }; medium = java network.entities.ConnectionMedium; simulator { setBaseNode : original; setBaseMedium : medium; }; %s",
				_configBegin,
				className,
				_configEnd );
		CommonTree ast = 
			ConfigurationFileProcessor.getAST( 
				new StringReader( line ) );
		_treeParser = 
			new HVNSLanguageTreeEvaluator( new CommonTreeNodeStream( ast ) );
		//System.out.println( ast.toStringTree() );
		_treeParser.script();
		ComputerNetworkSimulator simulator = _treeParser.getSimulator();
		INode baseNode = simulator.getBaseNode();
		INode originalNode = (INode)_treeParser.getVariable( "original" );
		
		IConnectionMedium baseMedium = simulator.getBaseMedium();
		IConnectionMedium originalMedium  = (IConnectionMedium)_treeParser.getVariable( "medium" );
		System.out.printf( 
			( baseNode == originalNode && baseMedium == originalMedium )
			? "Test Simulator set Baseline Success!\n" 
			: "Test Simulator set Baseline Failure.\n");
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
				"%s nodeOne, nodeTwo, nodeThree, nodeFour = make Node; %s %s",
				_configBegin,
				"(+ nodeOne nodeTwo nodeThree nodeFour);",
				_configEnd );
		CommonTree ast = 
			ConfigurationFileProcessor.getAST( 
				new StringReader( line ) );
		//System.out.println(ast.toStringTree());
		_treeParser = 
			new HVNSLanguageTreeEvaluator( new CommonTreeNodeStream( ast ) );
		ComputerNetworkSimulator simulator = _treeParser.script();
		
		List<IConnectionMedium> mediums = new ArrayList<IConnectionMedium>();
		for( ISimulatable simulatable : simulator ) {
			if( simulatable instanceof IConnectionMedium ) {
				mediums.add( (IConnectionMedium)simulatable );
			}
		}
		
		boolean oneTwoConnected = false;
		boolean twoThreeConnected = false;
		boolean threeFourConnected = false;
		
		INode one = (INode)_treeParser.getVariable( "nodeOne" );
		INode two = (INode)_treeParser.getVariable( "nodeTwo" );
		INode three = (INode)_treeParser.getVariable( "nodeThree" );
		INode four = (INode)_treeParser.getVariable( "nodeFour" );
		
		for( IConnectionMedium medium : mediums ) {
			oneTwoConnected = (!oneTwoConnected) ? medium.areConnected( one, two ) : oneTwoConnected;
			twoThreeConnected = (!twoThreeConnected) ? medium.areConnected( two, three ) : twoThreeConnected;
			threeFourConnected = (!threeFourConnected) ? medium.areConnected( three, four ) : threeFourConnected;
		}
		
		System.out.printf( 
			( oneTwoConnected && twoThreeConnected && threeFourConnected )
			? "Test Connect Success!\n" 
			: "Test Connect Failure.\n");
	}
}