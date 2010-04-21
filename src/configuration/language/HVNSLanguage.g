grammar HVNSLanguage;

options {
  language = Java;
  output = AST;
  backtrack = true;
}

tokens {
  	JAVA_INSTANTIATE;
	MULTI_ASSIGN;
  	CLONE;
  	NEGATION;
  	SERIES_CONNECT_OP;
  	BUS_CONNECT_OP;
  	MESH_CONNECT_OP;
  	RANDOM_CONNECT_OP;
  	RING_CONNECT_OP;
  	JAVA_INVOKE;
  	SIMULATOR_CREATE;
  	NODE;
  	MEDIUM;
  	ADAPTOR;
}

@header {
	package configuration.language;  
	import network.entities.Node;
}

@lexer::header {
	package configuration.language;
	import network.entities.Node;
}

///
///	PARSER
///

/**
*	Start-rule.  
*/
script
	:	'config'! NAME!
		'begin'!
		simulatorAssignment
		( statement )* 
		'end'! EOF!
	;

/**
*	Special assignment for the simulator to use.
*/
simulatorAssignment
	:	NAME ASSIGN ( expression ) SEMI { "simulator".equals($NAME.text ) }? -> ^( ASSIGN NAME["simulator"] expression )
	;

/**
*	Statements.
*/
statement
	:	assign 
	|	connectStatement
	|	value SEMI -> ^( value )
	;

/**
*	Assignment of values to variables.
*/
assign
	:	NAME (COMMA NAME)* ASSIGN ( expression ) SEMI -> ^( ASSIGN NAME expression )+
	;

/**
*	Calls to getter methods and object fields.
*/
innerAssigns
	:	LEFT_BRACE ( NAME COLON expression SEMI )+ RIGHT_BRACE -> ( NAME expression )+
	;

/**
*	Handles value creation, including insantiation.
*/
value
	: 	'make' 'Node' -> ^( SIMULATOR_CREATE NODE )
	|	'make' 'Medium' -> ^( SIMULATOR_CREATE MEDIUM )
	|	'make' 'Adaptor' -> ^( SIMULATOR_CREATE ADAPTOR )
	|	'java' NAME -> ^( JAVA_INSTANTIATE NAME )
	|	'java' NAME in=innerAssigns -> ^( JAVA_INVOKE ^(JAVA_INSTANTIATE NAME) $in )
	|	'clone' NAME -> ^( CLONE NAME )
	|	'clone' NAME in=innerAssigns -> ^( JAVA_INVOKE ^(CLONE NAME) $in )
	|	NAME 
	|	NAME in=innerAssigns -> ^( JAVA_INVOKE NAME $in )
	|	LEFT_PAREN! expression RIGHT_PAREN!
	|	FLOAT
	|	INTEGER 
	;


/**
*	Addition and subtraction operations.
*/
expression
	:	mult ( ('+'^ | '-'^ ) mult )*
	;

/**
*	Multiplication operations.
*/
mult:	unary ( ( '*'^ | '/'^ ) unary )*
	;

/**
*	Handles unary positive and negative operators for numerics.
*/
unary
	:   ('+'! | negation^)* value  // --2  (- ( - 2 )
	;

/**
*	Convert unary '-' to a negation token.
*/
negation
	:	'-' -> NEGATION
	;

/**
*	Handles the base of a connect statement.
*/
connectStatement
	:	LEFT_PAREN! ( nodeConnectOperator^ NAME+ ) RIGHT_PAREN! SEMI!
	;

/**
*	Converts the connect operator into the proper token that the tree evaluator
*	understands and which is unique from the math operators.
*/
nodeConnectOperator
	:	'+' -> SERIES_CONNECT_OP
	|	'*' -> BUS_CONNECT_OP
	|	'#' -> MESH_CONNECT_OP
	|	'&' -> RANDOM_CONNECT_OP
	|	'@' -> RING_CONNECT_OP
	;

//
//	LEXER
//

// special control cahra
ASSIGN: '=';
LEFT_PAREN: '(';
RIGHT_PAREN: ')';
LEFT_BRACE: '{';
RIGHT_BRACE: '}';
COLON: ':';
COMMA: ',';
SEMI: ';';

// strings
NAME: LETTER ( LETTER | DIGIT | '_' | '.' )*;
STRING_LITERAL: '"' NONCONTROL_CHAR* '"';
fragment NONCONTROL_CHAR: LETTER | DIGIT | SYMBOL | SPACE;
fragment LETTER: LOWER | UPPER;
fragment LOWER: 'a'..'z';
fragment UPPER: 'A'..'Z';
fragment SPACE: ' ' | '\t';
fragment SYMBOL: '!' | '#'..'/' | ':'..'@' | '['..']' | '{'..'~';

// numerics
fragment DIGIT: '0'..'9';
fragment NON_ZERO_DIGIT: '1'..'9';
FLOAT: INTEGER '.' DIGIT+;
INTEGER: NON_ZERO_DIGIT DIGIT*;

// spacing
fragment NEWLINE: ('\r'? '\n')+;
WHITESPACE: (' ' | '\t' | NEWLINE )+ { $channel = HIDDEN; };

// comments
SINGLE_COMMENT: '//' ~('\r' | '\n')* NEWLINE { skip(); };
MULTI_COMMENT	options { greedy = false; }
	: '/*' .* '*/' NEWLINE? { skip(); };
