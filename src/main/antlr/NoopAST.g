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

classDeclaration
	:	^(CLASS TypeIdentifier)
	{ }
	;
