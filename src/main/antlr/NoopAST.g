tree grammar NoopAST;

options {
  tokenVocab = Noop;
  ASTLabelType = CommonTree;
}

scope Modifiers {
  List<String> mods;
}

scope SourceFile {
  File file;
}

@header {
  package noop.grammar;

  import noop.model.*;
}

@members {
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
  @init { $SourceFile::file = $file; }
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
	:	 n=namespace t=TypeIdentifier
	{ $text = $n.text + "." + $t.text; }
	;

classDeclaration returns [ClassDefinition classDef = new ClassDefinition()]
	:	^(CLASS t=TypeIdentifier p=parameters? block?) 
	{ 
	$SourceFile::file.classDef_$eq($classDef);
	$classDef.name_$eq($t.text);
	if ($p.parameters != null) {
  	for (Parameter param : $p.parameters) {
	    $classDef.parameters().$plus$eq(param);
	  }
	}
	}
	;

parameters returns [List<Parameter> parameters = new ArrayList<Parameter>() ]
	:	^(PARAMS parameter[$parameters]*)
	{ }
	;

parameter [List<Parameter> parameters]
	:	^(PARAM modifiers? t=TypeIdentifier v=VariableIdentifier)

	{ Parameter param = new Parameter();
	  param.noopType_$eq($t.text);
	  param.name_$eq($v.text);
	  $parameters.add(param);
	}
	;

modifiers
	: ^(MOD modifier+)
	;
	
modifier
	: 'mutable' | 'delegate'
	;

block
	:	^(PROP TypeIdentifier propertyDeclarator)
	| ^(METHOD TypeIdentifier VariableIdentifier parameters? block)
	;
	
propertyDeclarator
	: VariableIdentifier ('='^ expression)?
	;

expression
	: INT | StringLiteral
	;
