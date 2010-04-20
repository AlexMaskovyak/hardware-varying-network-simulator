grammar HVNSLanguage2;

options {
  language = Java;
  output = AST;
}

tokens {
  	JAVA_INSTANTIATE;
	MULTI_ASSIGN;
  	CLONE;
  	NEGATION;
  	SERIES_CONNECT_OP;
  	MESH_CONNECT_OP;
  	RANDOM_CONNECT_OP;
  	RING_CONNECT_OP;
  	JAVA_INVOKE;
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
		( statement | value )* 
		'end'! EOF!
	;

/**
*	Special assignment for the simulator to use.
*/
simulatorAssignment
	:	sim='simulator' ASSIGN ( expression ) SEMI -> ^( ASSIGN NAME[$sim] expression )
	;

/**
*	Statements.
*/
statement
	:	assign 
	|	seriesConnectStatement
	|	meshConnectStatement
	|	randomConnectStatement
	|	ringConnectStatement
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
innerAssign
	:	LEFT_BRACE ( NAME COLON expression SEMI )+ RIGHT_BRACE -> ^( NAME expression )+
	;

/**
*	Handles value creation, including insantiation.
*/
value
	: 	'java' NAME -> ^( JAVA_INSTANTIATE NAME )
	|	'java' NAME inner=innerAssign -> ^( JAVA_INVOKE ^( JAVA_INSTANTIATE NAME ) ^($inner) )+
	|	'clone' NAME -> ^( CLONE NAME )
	|	NAME 
	|	LEFT_PAREN! expression RIGHT_PAREN!
	|	NUMBER
// | expression
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

unary
	:   ('+'! | negation^)* value  // --2  (- ( - 2 )
	;

negation
	:	'-' -> NEGATION
	;

seriesConnectStatement
	:	//LEFT_PAREN! '+'^ NAME+ RIGHT_PAREN! ';'!
		//^((LEFT_PAREN '+' -> ^(SERIES_CONNECT_OP) ) ((NAME+ RIGHT_PAREN ';' ) -> NAME+))
		//LEFT_PAREN '+' (names+=NAME)+ RIGHT_PAREN ';' { System.out.println( $names ); } -> SERIES_CONNECT_OP $names
		//LEFT_PAREN '+' (names+=NAME)+ RIGHT_PAREN ';' { System.out.println( $names ); } -> ^(SERIES_CONNECT_OP $names)
		//LEFT_PAREN '+' NAME+ RIGHT_PAREN ';' { System.out.println( $NAME ); } -> ^(SERIES_CONNECT_OP NAME )
		LEFT_PAREN '+' NAME+ RIGHT_PAREN SEMI { System.out.println( $NAME ); } -> ^( SERIES_CONNECT_OP NAME+ )
	;

meshConnectStatement
	:	LEFT_PAREN '#' NAME+ RIGHT_PAREN SEMI { System.out.println( $NAME ); } -> ^( MESH_CONNECT_OP NAME+ )
	;
	
randomConnectStatement
	:	LEFT_PAREN '&' NAME+ RIGHT_PAREN SEMI { System.out.println( $NAME ); } -> ^( RANDOM_CONNECT_OP NAME+ )
	;

ringConnectStatement
	:	LEFT_PAREN '@' NAME+ RIGHT_PAREN SEMI { System.out.println( $NAME ); } -> ^( RING_CONNECT_OP NAME+ )
	;


terminator: NEWLINE | EOF;

//
//	LEXER
//

ASSIGN: '=';
LEFT_PAREN: '(';
RIGHT_PAREN: ')';
LEFT_BRACE: '{';
RIGHT_BRACE: '}';
COLON: ':';
COMMA: ',';
SEMI: ';';


NAME: LETTER ( LETTER | DIGIT | '_' | '.' )*;
STRING_LITERAL: '"' NONCONTROL_CHAR* '"';


fragment NONCONTROL_CHAR: LETTER | DIGIT | SYMBOL | SPACE;
fragment LETTER: LOWER | UPPER;
fragment LOWER: 'a'..'z';
fragment UPPER: 'A'..'Z';
fragment SPACE: ' ' | '\t';

fragment SYMBOL: '!' | '#'..'/' | ':'..'@' | '['..']' | '{'..'~';

// numerics
NUMBER: INTEGER | FLOAT;
fragment DIGIT: '0'..'9';
fragment NON_ZERO_DIGIT: '1'..'9';
fragment FLOAT: INTEGER '.' DIGIT+;
fragment INTEGER: NON_ZERO_DIGIT DIGIT*;

// spacing
fragment NEWLINE: ('\r'? '\n')+;
WHITESPACE: (' ' | '\t' | NEWLINE )+ { $channel = HIDDEN; };

// comments
SINGLE_COMMENT: '//' ~('\r' | '\n')* NEWLINE { skip(); };
MULTI_COMMENT	options { greedy = false; }
	: '/*' .* '*/' NEWLINE? { skip(); };
