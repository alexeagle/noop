grammar Noop;

options {
  output = AST;
  ASTLabelType = CommonTree;
}

tokens {
  CLASS;
  PARAMS;
  PARAM;
}

@header {
  package noop.grammar;
}

@lexer::header {
  package noop.grammar;
}

@rulecatch {
  catch (RecognitionException e) { throw e; }
}

file
	:	classDeclaration
	;

classDeclaration
	: 'class' TypeIdentifier parameterList block
	-> ^(CLASS TypeIdentifier parameterList? block?)
	;

block
	: '{'!  propertyDeclaration* '}'!
	;

propertyDeclaration
  : TypeIdentifier VariableIdentifier ('='^ expression)?
  ;

expression
	: INT
	;

parameterList
	: '('! parameters?  ')'!
	;

parameters
	: parameter (',' parameter)*
	-> ^(PARAMS parameter*)
	;

parameter
	: TypeIdentifier VariableIdentifier
	-> ^(PARAM TypeIdentifier VariableIdentifier)
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

INT
	:	'0'..'9'+
	;
