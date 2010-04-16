package configuration.language;

import java.io.IOException;
import java.io.StringReader;

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
		tester.testVaue();
		tester.testAssignAndValue();
	}
	
	public void testAssignAndValue() throws IOException, RecognitionException {
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
	}
	
	public void testVaue() throws RecognitionException, IOException {
		CommonTree ast = 
			ConfigurationFileProcessor.getAST( new StringReader("13") );
		HVNSLanguage2TreeEvaluator treeParser = 
			new HVNSLanguage2TreeEvaluator( new CommonTreeNodeStream( ast ) );
		Object o = treeParser.value();
		System.out.println("ok! : " + o);
	}
}
