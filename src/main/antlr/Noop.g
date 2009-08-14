grammar Noop;

options {
  output = AST;
  ASTLabelType = CommonTree;
}

@header {
  package noop.grammar;
}

@lexer::header {
  package noop.grammar;
}

program :	
  COMMENT*;

WS  : (' '|'\r'|'\n')+ {$channel = HIDDEN;} ;

COMMENT
    :   '/*' ( options {greedy=false;} : . )* '*/' {$channel=HIDDEN;}
    ;

LINE_COMMENT
    : '//' ~('\n'|'\r')* '\r'? '\n' {$channel=HIDDEN;}
    ;
