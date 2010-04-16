tree grammar HVNSTreeEvaluator;

options {
  language = Java;
  tokenVocab = HVNSLanguage;
  ASTLabelType = CommonTree;
}

@header {
  package configuration.language;
  import java.util.Map;
  import java.util.HashMap;
  import simulation.simulator.DESimulator;
}

@members {
  protected Map<String, Object> variables = new HashMap<String, Object>();
}

evaluator returns [ DESimulator result ]
	:	
	;

/*assignment
	:	 ^(':=' IDENT e=expression)
		{ variables.put( $IDENT.text, e ); }
	;
*/
singleAssignmentDecl returns [ String name ]
	:	'var' IDENTIFIER { $name=$IDENTIFIER.text; }
	;
	
arrayAssignmentDecl returns [ String name ]
	:	'var' IDENTIFIER '[' INTEGER '..' INTEGER ']' { $name=$IDENTIFIER.text; }
	;
	

	
rule
	: 		
	;
