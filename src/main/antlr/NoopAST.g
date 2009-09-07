tree grammar NoopAST;

options {
  tokenVocab = Noop;
  ASTLabelType = CommonTree;
}

scope SourceFile {
  File file;
}

@header {
  package noop.grammar.antlr;

  import noop.model.*;
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
	:	^(CLASS t=TypeIdentifier p=parameters? typeSpecifier* classBlock?) 
	{ 
	classDef.name_$eq($t.text);
	if ($p.parameters != null) {
  	for (Parameter param : $p.parameters) {
	    classDef.parameters().$plus$eq(param);
	  }
	}
	}
	;

parameters returns [List<Parameter> parameters = new ArrayList<Parameter>() ]
	:	^(PARAMS parameter[$parameters]*)
	;

parameter [List<Parameter> parameters]
	:	^(PARAM modifiers? t=TypeIdentifier v=VariableIdentifier)

	{ Parameter param = new Parameter();
	  param.noopType_$eq($t.text);
	  param.name_$eq($v.text);
	  $parameters.add(param);
	}
	;

typeSpecifier
	:	^(IMPL i=qualifiedType)
	{
   $SourceFile::file.classDef().interfaces().$plus$eq($i.text);
	}
	;

modifiers
	: ^(MOD modifier+)
	;
	
modifier
	: 'mutable' | 'delegate'
	;

classBlock
	:	propertyDeclaration | methodDeclaration
	;
	
methodDeclaration 
@init { paraphrases.push("in method declaration"); }
@after { paraphrases.pop(); }
  :	^(METHOD type=TypeIdentifier name=VariableIdentifier p=parameters? statement*)
  { Method method = new Method($name.text, $type.text);
    $SourceFile::file.classDef().methods().$plus$eq(method);
    if ($p.parameters != null) {
  	  for (Parameter param : $p.parameters) {
	      method.parameters().$plus$eq(param);
	    }
	  }
  }
  ;

statement
@init { paraphrases.push("in statement"); }
@after { paraphrases.pop(); }
	:	variableDeclaration
	;

propertyDeclaration
	:	^(PROP TypeIdentifier propertyDeclarator)
	;

variableDeclaration
	:	^(VAR TypeIdentifier propertyDeclarator)
	;
	
propertyDeclarator
	: ^('=' VariableIdentifier expression)
	| VariableIdentifier
	;

expression
	: INT | StringLiteral
	;
