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

@rulecatch {
  catch (RecognitionException e) {
  // TODO: print the line, and show a carot underneath the position of the error
    reportError(e);
    throw(e);
  }
}

@members {
  Stack paraphrases = new Stack(); 

  @Override
  public String getErrorMessage(RecognitionException e, String[] tokenNames) { 
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
}

file returns [File file = new File()]
  scope SourceFile;
  @init { $SourceFile::file = $file;
          paraphrases.push("at top-level in file"); }
  @after { paraphrases.pop(); }
  :	 namespaceDeclaration? importDeclaration* classDeclaration
  ;

namespaceDeclaration
	:	^('namespace' name=namespace)
	{ $SourceFile::file.namespace_$eq($name.text); }
	;

importDeclaration
	: ^('import' name=qualifiedType)
	{ $SourceFile::file.imports().$plus$eq($name.text); }
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
  ClassDefinition classDef = new ClassDefinition();
	$SourceFile::file.classDef_$eq(classDef);
	paraphrases.push("in class definition");
}
@after {
  paraphrases.pop();
}
	:	^(CLASS m=modifiers? t=TypeIdentifier p=parameters? typeSpecifier* classBlock?) 
	{ 
	classDef.name_$eq($t.text);
	if ($p.parameters != null) {
	  classDef.parameters().$plus$plus$eq($p.parameters);
	}
	if ($m.modifiers != null) {
	  classDef.modifiers().$plus$plus$eq($m.modifiers);
	}
	}
	;

parameters returns [Buffer<Parameter> parameters = new ArrayBuffer<Parameter>() ]
	:	^(PARAMS parameter[$parameters]*)
	;

parameter [Buffer<Parameter> parameters]
	:	^(PARAM modifiers? t=TypeIdentifier v=VariableIdentifier)

	{ Parameter param = new Parameter($v.text, $t.text);
	  $parameters.$plus$eq(param);
	}
	;

typeSpecifier
	:	^(IMPL i=qualifiedType)
	{
   $SourceFile::file.classDef().interfaces().$plus$eq($i.text);
	}
	;

modifiers returns [Buffer<Enumeration.Value> modifiers ]
@init { modifiers = new ArrayBuffer<Enumeration.Value>(); }
	: ^(MOD modifier[modifiers]+)
	;
	
modifier [Buffer<Enumeration.Value> mods]
	: m=('mutable' | 'delegate' | 'native')
	{ mods.$plus$eq(Modifier.valueOf($m.text).get()); }
	;

classBlock
	:	identifierDeclaration | methodDeclaration
	;
	
methodDeclaration 
@init { paraphrases.push("in method declaration"); }
@after { paraphrases.pop(); }
  :	^(METHOD m=modifiers? type=TypeIdentifier name=VariableIdentifier p=parameters? b=block)
  { Method method = new Method($name.text, $type.text, $b.block);
    $SourceFile::file.classDef().methods().$plus$eq(method);
    if ($p.parameters != null) {
  	  method.parameters().$plus$plus$eq($p.parameters);
	  }
	  if ($m.modifiers != null) {
  	  method.modifiers().$plus$plus$eq($m.modifiers);
  	}
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
	| identifierDeclaration
	| exp=expression
	{ $Block::block.statements().$plus$eq($exp.exp); }
	;

returnStatement
	: ^('return' ex=expression)
	{ $Block::block.statements().$plus$eq(new ReturnExpression($ex.exp)); }
	;

identifierDeclaration
	:	^(VAR t=TypeIdentifier (^('=' v=VariableIdentifier exp=expression) | v=VariableIdentifier))
	{ IdentifierDeclaration identifierDeclaration = new IdentifierDeclaration($t.text, $v.text);
		$Block::block.statements().$plus$eq(identifierDeclaration);
	  if ($exp.exp != null) {
	    identifierDeclaration.initialValue_$eq(new scala.Some($exp.exp));
	  }
  }
	;
	
assignment
	: ^('=' lhs=expression rhs=expression)
	{ $Block::block.statements().$plus$eq(new AssignmentExpression($lhs.text, $rhs.text)); }
	;

expression returns [Expression exp]
  :	p=primary
  { $exp = $p.exp; }
  | d=dereference
  { $exp = $d.exp; }
  | v=VariableIdentifier 
  { $exp = new IdentifierExpression($v.text); }
  ;

dereference returns [DereferenceExpression exp]
	: ^('.' left=expression right=VariableIdentifier a=arguments?)
	{ 
	  if ($a.args != null) {
	    $exp = new DereferenceExpression($left.exp, new MethodInvocationExpression($right.text, $a.args));
	  } else {
	    $exp = new DereferenceExpression($left.exp, new IdentifierExpression($right.text));
	  }
	}
	;

arguments returns [Buffer<Expression> args]
	: ^(ARGS (exp+=expression)*)
	{ $args = new ArrayBuffer<Expression>();
	  if ($exp != null) {
  	  for (Object item : $exp) {
	      Expression expression = ((expression_return)item).exp;
	      $args.$plus$eq(expression);
	    }
	  }
	}
	;
  
primary returns [Expression exp]
	: i=INT
	{ $exp = new IntegerLiteralExpression(Integer.valueOf($i.text)); }
	| s=StringLiteral
	{ String valueWithQuotes = $s.text;
	  $exp = new StringLiteralExpression(valueWithQuotes.substring(1, valueWithQuotes.length() - 1)); }
	;
