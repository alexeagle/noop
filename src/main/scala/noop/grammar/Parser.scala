package noop.grammar;

import noop.model.File
import noop.grammar.antlr.NoopAST
import noop.grammar.antlr.NoopParser
import noop.grammar.antlr.NoopLexer

import org.antlr.runtime.ANTLRInputStream
import org.antlr.runtime.ANTLRStringStream
import org.antlr.runtime.CommonTokenStream
import org.antlr.runtime.RecognitionException
import org.antlr.runtime.tree.CommonTree
import org.antlr.runtime.tree.CommonTreeNodeStream

class Parser() {
  def buildParser(source:String):NoopParser = {
    val input = new ANTLRStringStream(source);
    new NoopParser(new CommonTokenStream(new NoopLexer(input)));
  }
  
  def parseFile(source: String): CommonTree = {
    val file = buildParser(source).file();
    return file.getTree().asInstanceOf[CommonTree];
  }
  
  def parseInterpretable(source:String): CommonTree = {
    val interpretable = buildParser(source).interpretable();
    return interpretable.getTree().asInstanceOf[CommonTree];
  }
  
  def parseBlock(source: String): CommonTree = {
    val noopParser = buildParser(source);
    val block = noopParser.block();
    return block.getTree().asInstanceOf[CommonTree];
  }
  
  def file(source: String): File = {
    val ast = parseFile(source);
    val treeParser = new NoopAST(new CommonTreeNodeStream(ast));
    return treeParser.file();
  }
}
