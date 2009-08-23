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
  METHOD;
  MOD;
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
	:	namespaceDeclaration? importDeclaration* classDeclaration
	;

namespaceDeclaration
	:	'namespace'^ namespace ';'!
	;

importDeclaration
	:	'import'^ qualifiedType ';'!
	;

methodDeclaration
	: TypeIdentifier VariableIdentifier parameterList block
	-> ^(METHOD TypeIdentifier VariableIdentifier parameterList? block)
	;

namespace
	:	VariableIdentifier ('.'! VariableIdentifier)*
	;

qualifiedType
	:	 namespace ('.'! TypeIdentifier)
	;

classDeclaration
	: 'class' TypeIdentifier parameterList block
	-> ^(CLASS TypeIdentifier parameterList? block?)
	;

modifiers
	: modifier+
	-> ^(MOD modifier+)
	;

modifier
	: 'mutable' | 'delegate'
	;

block
	: '{'!  propertyDeclaration* methodDeclaration* '}'!
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
	: modifiers? TypeIdentifier VariableIdentifier
	-> ^(PARAM modifiers? TypeIdentifier VariableIdentifier)
	;

literal
	: INT | StringLiteral
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
