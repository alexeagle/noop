// Copyright 2009 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

grammar Noop;

options {
  backtrack = true;
  output = AST;
  ASTLabelType = CommonTree;
}

tokens {
  CLASS;
  INTERFACE;
  PARAMS;
  PARAM;
  METHOD;
  MOD;
  ARGS;
  VAR;
  IMPL;
  IF;
  EQ;
  NEQ;
}

@header {
  package noop.grammar.antlr;
}

@lexer::header {
  package noop.grammar.antlr;
}

@lexer::rulecatch {
  catch (RecognitionException e) {
    reportError(e);
    throw new RuntimeException(e);
  }
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

ifExpression
  : 'if' '(' conditionalExpression ')' block* ('else' block*)?
  -> ^(IF conditionalExpression* block*)
  ;

conditionalExpression
  : (conditionalOrExpression | conditionalAndExpression)+
  ;

conditionalOrExpression
  : conditionalAndExpression ('||' conditionalAndExpression)*
  ;

conditionalAndExpression
  : (equalityExpression | inequalityExpression) ('&&' (equalityExpression | inequalityExpression))*
  ;

equalityExpression
  : primary '==' primary
  -> ^(EQ primary primary)
  ;

inequalityExpression
  : primary '!=' primary
  -> ^(NEQ primary primary)
  ;

methodDeclaration
	: methodSignature block
	-> ^(METHOD methodSignature block?)
	;

namespace
	:	VariableIdentifier ('.'! VariableIdentifier)*
	;

qualifiedType
	:	 (namespace '.'!)? TypeIdentifier
	;

classDeclaration
	: doc? modifiers? 'class' TypeIdentifier parameterList typeSpecifiers? classBlock
	-> ^(CLASS modifiers? TypeIdentifier parameterList? typeSpecifiers? classBlock? doc?)
	;

doc
	:	'doc'^ StringLiteral
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
	: 'mutable' | 'delegate' | 'native'
	;

classBlock
	:	'{'!  identifierDeclaration* methodDeclaration* '}'!
	;

interfaceBlock
  : '{'! methodDefinition* '}'!
	;

methodSignature
  : doc? modifiers? TypeIdentifier VariableIdentifier parameterList
  ;

methodDefinition
  : methodSignature ';'
  -> ^(METHOD methodSignature)
  ;


identifierDeclaration
	:	TypeIdentifier identifierDeclarator ';'
	-> ^(VAR TypeIdentifier identifierDeclarator)
	;

identifierDeclarator
	: VariableIdentifier ('='^ expression)?
	;

block
	: '{'!  statement* '}'!
	;

statement
	:	identifierDeclaration
	| 'return'^ expression ';'!
	| expression ';'!
	| ifExpression
	;

expression
	: additiveExpression ('='^ expression)?
	;
	
additiveExpression
	:	multiplicativeExpression ( ('+' | '-')^ multiplicativeExpression )*
	;

multiplicativeExpression
	:	primary ( ( '*' | '/' | '%' )^ primary )*
	;	

primary
	:	'('! expression ')'!
	| (VariableIdentifier|TypeIdentifier|literal) ('.'^ (VariableIdentifier|TypeIdentifier) arguments?)*
	;

arguments
	:	'(' expressionList? ')'
	-> ^(ARGS expressionList?)
	;

expressionList
	:	expression (','! expression)*
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
	:	'"' ~('"'|'\\'|'\n'|'\r')* '"'
	| '"""' (options {greedy=false;}:.)* '"""'
	;


WS
  :	(' '|'\r'|'\n'|'\t')+ {$channel = HIDDEN;}
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
