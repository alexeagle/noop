grammar Noop;

options {
  output = AST;
  ASTLabelType = CommonTree;
}

tokens {
  CLASS;
  PARAMS;
  PARAM;
  PROP;
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
	:	packageDeclaration? importDeclaration* classDeclaration
	;

packageDeclaration
	:	'package'^ packageName ';'!
	;
	
importDeclaration
	:	'import'^ qualifiedType ';'!
	;
	
packageName
	:	VariableIdentifier ('.'! VariableIdentifier)*
	;

qualifiedType
	:	 packageName ('.'! TypeIdentifier)
	;
	

classDeclaration
	: 'class' TypeIdentifier parameterList block
	-> ^(CLASS TypeIdentifier parameterList? block?)
	;

block
	: '{'!  propertyDeclaration* '}'!
	;

propertyDeclaration
  : TypeIdentifier propertyDeclarator ';'
  -> ^(PROP TypeIdentifier propertyDeclarator)
  ;

propertyDeclarator
	: VariableIdentifier ('='^ expression)?
	;

expression
	: literal
	;

parameterList
	: '('! parameters? ')'!
	;

parameters
	: parameter (',' parameter)*
	-> ^(PARAMS parameter*)
	;

parameter
	: TypeIdentifier VariableIdentifier
	-> ^(PARAM TypeIdentifier VariableIdentifier)
	;

literal
	:	INT | StringLiteral
	;

/* Lexer rules */

TypeIdentifier
	: 'A' .. 'Z' ('a' .. 'z' | 'A' .. 'Z' | '0' .. '9')*
	;

VariableIdentifier
	: 'a' .. 'z' ('a' .. 'z' | 'A' .. 'Z' | '0' .. '9')*
	;

StringLiteral
	:	'"' ~('\\'|'"')* '"'
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
	:	'-'? '0'..'9'+
	;
