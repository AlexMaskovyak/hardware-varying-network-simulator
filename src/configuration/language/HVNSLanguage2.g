grammar HVNSLanguage2;

options {
  language = Java;
  output = AST;
}

tokens {
  	JAVA_INSTANTIATE;
  	CLONE;
}

@header {
	package configuration.language;  
	import network.entities.Node;
}

@lexer::header {
	package configuration.language;
	import network.entities.Node;
}



// top-level items
script
	:	( statement | value )* EOF!
	;

statement
	:	assign 
	;

assign
	:	NAME ASSIGN value ';' -> ^( ASSIGN NAME value )
	;


/**
*	Handles values.
*/
value
	: 	
		'java' NAME -> ^( JAVA_INSTANTIATE NAME )
	|	'clone' NAME -> ^( CLONE NAME )
	|	NUMBER
	| 	NAME // | expression
	;


terminator: NEWLINE | EOF;

//expression
//	:	value
//	;

//
//	LEXER
//

ASSIGN: '=';
LEFT_PAREN: '(';
RIGHT_PAREN: ')';
SIGN: '+' | '-';
NAME: LETTER ( LETTER | DIGIT | '_' | '.' )*;
STRING_LITERAL: '"' NONCONTROL_CHAR* '"';

fragment NONCONTROL_CHAR: LETTER | DIGIT | SYMBOL | SPACE;
fragment LETTER: LOWER | UPPER;
fragment LOWER: 'a'..'z';
fragment UPPER: 'A'..'Z';
fragment DIGIT: '0'..'9';
fragment NON_ZERO_DIGIT: '1'..'9';
fragment SPACE: ' ' | '\t';

fragment SYMBOL: '!' | '#'..'/' | ':'..'@' | '['..']' | '{'..'~';

NUMBER: INTEGER | FLOAT;
fragment FLOAT: INTEGER '.' DIGIT+;
fragment INTEGER: NON_ZERO_DIGIT DIGIT*;

// spacing
fragment NEWLINE: ('\r'? '\n')+;
WHITESPACE: (' ' | '\t' | NEWLINE )+ { $channel = HIDDEN; };

// comments
SINGLE_COMMENT: '//' ~('\r' | '\n')* NEWLINE { skip(); };
MULTI_COMMENT	options { greedy = false; }
	: '/*' .* '*/' NEWLINE? { skip(); };
