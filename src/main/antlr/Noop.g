grammar Noop;

options {
  output = AST;
  ASTLabelType = CommonTree;
}

tokens {
  CLASS;
  INTERFACE;
  PARAMS;
  PARAM;
  PROP;
  METHOD;
  MOD;
  ARGS;
  VAR;
  IMPL;
}

@header {
  package noop.grammar.antlr;
}

@lexer::header {
  package noop.grammar.antlr;
}

@rulecatch {
  catch (RecognitionException e) { 
    reportError(e);
    throw e;
  }
}

// A line of user input in the interactive interpreter
interpretable
  :	(importDeclaration 
     | classDeclaration
     | statement)+
	;

file
	:	namespaceDeclaration? importDeclaration* (classDeclaration | interfaceDeclaration)
	;

namespaceDeclaration
	:	'namespace'^ namespace ';'!
	;

importDeclaration
	:	'import'^ qualifiedType ';'!
	;

methodDeclaration
	: methodSignature block
	-> ^(METHOD methodSignature block)
	;

namespace
	:	VariableIdentifier ('.'! VariableIdentifier)*
	;

qualifiedType
	:	 (namespace '.'!)? TypeIdentifier
	;

classDeclaration
	: 'class' TypeIdentifier parameterList typeSpecifiers? classBlock
	-> ^(CLASS TypeIdentifier parameterList? typeSpecifiers? classBlock?)
	;

typeSpecifiers
	: 'implements' qualifiedType (',' qualifiedType)*
	-> ^(IMPL qualifiedType)*
	;

interfaceDeclaration
  : 'interface' TypeIdentifier interfaceBlock
  -> ^(INTERFACE TypeIdentifier interfaceBlock?)
  ;

modifiers
	: modifier+
	-> ^(MOD modifier+)
	;

modifier
	: 'mutable' | 'delegate'
	;

classBlock
	:	'{'!  propertyDeclaration* methodDeclaration* '}'!
	;

interfaceBlock
  : '{'! methodDefinition* '}'!
	;

methodSignature
  : TypeIdentifier VariableIdentifier parameterList
  ;

methodDefinition
  : methodSignature ';'
  -> ^(METHOD methodSignature)
  ;


propertyDeclaration
  : TypeIdentifier propertyDeclarator ';'
  -> ^(PROP TypeIdentifier propertyDeclarator)
  ;

variableDeclaration
	:	TypeIdentifier propertyDeclarator ';'
	-> ^(VAR TypeIdentifier propertyDeclarator)
	;

propertyDeclarator
	: VariableIdentifier ('='^ expression)?
	;

block
	: '{'!  statement* '}'!
	;

statement
	:	variableDeclaration
	| 'return'^ expression ';'!
	| expression ';'!
	;

expression
	: primary ('='^ expression)?
	;
	
primary
	:	'('! expression ')'!
	| (VariableIdentifier|TypeIdentifier|literal) ('.'^ (VariableIdentifier|TypeIdentifier))* arguments?
	;
	
arguments
	:	'(' expression? ')'
	-> ^(ARGS expression?)
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
