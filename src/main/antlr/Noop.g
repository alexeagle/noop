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
  ARGS;
  VAR;
}

@header {
  package noop.grammar.antlr;
}

@lexer::header {
  package noop.grammar.antlr;
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
	: 'class' TypeIdentifier parameterList classBlock
	-> ^(CLASS TypeIdentifier parameterList? classBlock?)
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
