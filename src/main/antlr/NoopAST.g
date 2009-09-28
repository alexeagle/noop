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

tree grammar NoopAST;

options {
  tokenVocab = Noop;
  ASTLabelType = CommonTree;
}

scope SourceFile {
  File file;
}

scope Block {
  Block block;
}

@header {
  package noop.grammar.antlr;

  import noop.model.*;
  import scala.Enumeration;
  import scala.collection.mutable.Buffer;
  import scala.collection.mutable.ArrayBuffer;
}

@members {
  Stack paraphrases = new Stack();
  public boolean hadErrors = false;
  
  @Override
  public String getErrorMessage(RecognitionException e, String[] tokenNames) {
    hadErrors = true;
    String msg = super.getErrorMessage(e, tokenNames);
    if ( paraphrases.size()>0 ) {
      String paraphrase = (String)paraphrases.peek();
      msg = msg+" "+paraphrase;
    }
    return msg;
  }

  public String join(String delim, List strings) {
    if (strings.isEmpty()) {
      return "";
    }
    String first = ((CommonTree) strings.get(0)).getText();
    if (strings.size() == 1) {
      return first;
    }
    StringBuilder builder = new StringBuilder(first);
    for (int i=1; i<strings.size(); i++) {
      builder.append(delim);
      builder.append(((CommonTree)strings.get(i)).getText());
    }
    return builder.toString();
  }
  
  //TODO(alexeagle): when we have escape sequences, we need to do more work here
  // Probably anything acceping raw strings should take a RawString type which 
  // knows how to propertly un-escape things.
  String stripQuotes(String withQuotes) {
    return (withQuotes.startsWith("\"\"\"") ?
        withQuotes.substring(3, withQuotes.length() - 3) :
        withQuotes.substring(1, withQuotes.length() - 1));
  }
}

file returns [File file = new File()]
  scope SourceFile;
  @init { $SourceFile::file = $file;
          paraphrases.push("at top-level in file"); }
  @after { paraphrases.pop(); }
  :	 namespaceDeclaration? importDeclaration* (classDeclaration | test)
  ;

namespaceDeclaration
	:	^('namespace' name=namespace)
	{ $SourceFile::file.namespace_\$eq($name.text); }
	;

importDeclaration
	: ^('import' name=qualifiedType)
	{ $SourceFile::file.imports().\$plus\$eq($name.text); }
	;

namespace returns [String text]
	:	v+=VariableIdentifier+
	{ $text = join(".", $v); }
	;

qualifiedType returns [String text]
	:	 n=namespace? t=TypeIdentifier
	{ $text = ($n.text == null) ? $t.text : $n.text + "." + $t.text; }
	;

classDeclaration
@init {
	paraphrases.push("in class definition");
	Buffer<Method> methodCollector = new ArrayBuffer<Method>();
	Buffer<Method> unittestCollector = new ArrayBuffer<Method>();
	Buffer<String> interfaceCollector = new ArrayBuffer<String>();
}
@after {
  paraphrases.pop();
}
	:	^(CLASS m=modifiers? t=TypeIdentifier p=parameters? 
	          typeSpecifier[interfaceCollector]* 
	          classBlock[methodCollector, unittestCollector]? 
	          d=doc?)
	{
	ClassDefinition classDef = new ClassDefinition($t.text, $d.doc);
	classDef.methods().\$plus\$plus\$eq(methodCollector);
	classDef.unittests().\$plus\$plus\$eq(unittestCollector);
	classDef.interfaces().\$plus\$plus\$eq(interfaceCollector);
	if ($p.parameters != null) {
	  classDef.parameters().\$plus\$plus\$eq($p.parameters);
	}
	if ($m.modifiers != null) {
	  classDef.modifiers().\$plus\$plus\$eq($m.modifiers);
	}
	$SourceFile::file.classDef_\$eq(classDef);
	}
	;

parameters returns [Buffer<Parameter> parameters = new ArrayBuffer<Parameter>() ]
	:	^(PARAMS parameter[$parameters]*)
	;

parameter [Buffer<Parameter> parameters]
	:	^(PARAM modifiers? t=TypeIdentifier v=VariableIdentifier)

	{ Parameter param = new Parameter($v.text, $t.text);
	  $parameters.\$plus\$eq(param);
	}
	;

typeSpecifier [Buffer<String> interfaces]
	:	^(IMPL i=qualifiedType)
	{
   $interfaces.\$plus\$eq($i.text);
	}
	;

modifiers returns [Buffer<Enumeration.Value> modifiers ]
@init { modifiers = new ArrayBuffer<Enumeration.Value>(); }
	: ^(MOD modifier[modifiers]+)
	;

modifier [Buffer<Enumeration.Value> mods]
	: m=('mutable' | 'delegate' | 'native')
	{ mods.\$plus\$eq(Modifier.valueOf($m.text).get()); }
	;

test
@init {
  Buffer<Method> unittestCollector = new ArrayBuffer<Method>();
}
	:	^(TEST StringLiteral statement* test? unittest[unittestCollector]?)
	{}
	;
	
unittest [Buffer<Method> unittests]
	:	^(UNITTEST name=StringLiteral b=block)
	{ Method method = new Method(stripQuotes($name.text), "Void", $b.block, stripQuotes($name.text));
	  $unittests.\$plus\$eq(method);
	}
	;

classBlock [Buffer<Method> methods, Buffer<Method> unittests]
	:	(identifierDeclaration | methodDeclaration[methods] | unittest[unittests])+
	;

methodDeclaration [Buffer<Method> methods]
@init { paraphrases.push("in method declaration"); }
@after { paraphrases.pop(); }
  :	^(METHOD d=doc? m=modifiers? type=TypeIdentifier name=VariableIdentifier p=parameters? b=block)
  { Method method = new Method($name.text, $type.text, $b.block, $d.doc);
    if ($p.parameters != null) {
  	  method.parameters().\$plus\$plus\$eq($p.parameters);
	  }
	  if ($m.modifiers != null) {
  	  method.modifiers().\$plus\$plus\$eq($m.modifiers);
  	}
  	$methods.\$plus\$eq(method);
  }
  ;

block returns [Block block]
  scope Block;
  @init { $block = new Block();
          $Block::block = $block; }
  :	statement*
  ;

statement
@init { paraphrases.push("in statement"); }
@after { paraphrases.pop(); }
	:	returnStatement
	| w=whileStatement
	{ $Block::block.statements().\$plus\$eq($w.exp); }
	| identifierDeclaration
	| should=shouldStatement
	{ $Block::block.statements().\$plus\$eq($should.exp); }
	| exp=expression
	{ $Block::block.statements().\$plus\$eq($exp.exp); }
	;

shouldStatement returns [Expression exp]
	:	^('should' left=expression right=expression)
	{ $exp = new ShouldExpression($left.exp, $right.exp); }
	;

whileStatement returns [Expression exp]
	: ^(WHILE term=expression b=block)
	{ $exp = new WhileLoop($term.exp, $b.block); }
	;

returnStatement
	: ^('return' ex=expression)
	{ $Block::block.statements().\$plus\$eq(new ReturnExpression($ex.exp)); }
	;

identifierDeclaration
	:	^(VAR t=TypeIdentifier (^('=' v=VariableIdentifier exp=expression) | v=VariableIdentifier))
	{ IdentifierDeclarationExpression identifierDeclaration = new IdentifierDeclarationExpression($t.text, $v.text);
		$Block::block.statements().\$plus\$eq(identifierDeclaration);
	  if ($exp.exp != null) {
	    identifierDeclaration.initialValue_\$eq(new scala.Some($exp.exp));
	  }
  }
	;

assignment returns [Expression exp]
	: ^('=' lhs=expression rhs=expression)
	{ $exp = new AssignmentExpression($lhs.exp, $rhs.exp); }
	;

expression returns [Expression exp]
  :	l=literal
  { $exp = $l.exp; }
  |	d=dereference
  { $exp = $d.exp; }
  | o=operatorExpression
  { $exp = $o.exp; }
  | ass=assignment
  { $exp = $ass.exp; }
  | right=(VariableIdentifier|TypeIdentifier) a=arguments?
  { Expression left = new IdentifierExpression("this");
    if ($a.args != null) {
	    $exp = new MethodInvocationExpression(left, $right.text, $a.args);
	  } else {
	    $exp = new IdentifierExpression($right.text);
	  }
  }
  ;
  
operatorExpression returns [Expression exp]
	: ^(op=('+'|'-'|'*'|'/'|'%') left=expression right=expression)
	{ $exp = new OperatorExpression($left.exp, $op.text, $right.exp); }
	;

dereference returns [Expression exp]
	: ^('.' left=expression right=VariableIdentifier a=arguments?)
	{
	  if ($a.args != null) {
	    $exp = new MethodInvocationExpression($left.exp, $right.text, $a.args);
	  } else {
	    $exp = new DereferenceExpression($left.exp, new IdentifierExpression($right.text));
	  }
	}
	;

arguments returns [Buffer<Expression> args]
@init { $args = new ArrayBuffer<Expression>(); }
	: ^(ARGS argument[args]*)
	;

argument[Buffer<Expression> args]
  : exp=expression
  {
    $args.\$plus\$eq($exp.exp);
  }
	;

literal returns [Expression exp]
	: i=INT
	{ $exp = new IntLiteralExpression(Integer.valueOf($i.text)); }
	| s=StringLiteral
	{ $exp = new StringLiteralExpression(stripQuotes($s.text)); }
	| b=('true' | 'false')
	{ $exp = new BooleanLiteralExpression(Boolean.valueOf($b.text)); }
	;

doc returns [String doc]
	:	^('doc' s=StringLiteral)
	{ $doc = stripQuotes($s.text); }
	;
