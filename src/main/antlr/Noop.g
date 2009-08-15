grammar Noop;

options {
  output = AST;
  ASTLabelType = CommonTree;
}

tokens {
  CLASS;
}

@header {
  package noop.grammar;
}

@lexer::header {
  package noop.grammar;
}

file
	:	classDeclaration
	;

classDeclaration
	: 'class' TypeIdentifier paraneterList block
	-> ^(CLASS TypeIdentifier)
	;

block
	: '{'! '}'!
	;

paraneterList
	: '('! parameters?  ')'!
	;

parameters
	: parameter (',' parameter)*
	;

parameter
	: TypeIdentifier VariableIdentifier
	;

TypeIdentifier
	: 'A' .. 'Z' ('a' .. 'z' | 'A' .. 'Z' | '0' .. '9')*
	;

VariableIdentifier
	: 'a' .. 'z' ('a' .. 'z' | 'A' .. 'Z' | '0' .. '9')*
	;

WS
  :	(' '|'\r'|'\n')+ {$channel = HIDDEN;} 
  ;

COMMENT
  :   '/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN;}
  ;

LINE_COMMENT
  : '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
  ;


