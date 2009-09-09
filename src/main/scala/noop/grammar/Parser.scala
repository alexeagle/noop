package noop.grammar

import java.io.InputStream
import model.{Block, File}
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
  def buildParser(input: ANTLRStringStream): NoopParser = {
    return new NoopParser(new CommonTokenStream(new NoopLexer(input)));
  }

  def parseFile(source: InputStream): CommonTree = {
    val file = buildParser(new ANTLRInputStream(source)).file();
    return file.getTree().asInstanceOf[CommonTree];
  }

  def parseFile(source: String): CommonTree = {
    val file = buildParser(new ANTLRStringStream(source)).file();
    return file.getTree().asInstanceOf[CommonTree];
  }

  def parseInterpretable(source: String): CommonTree = {
    val interpretable = buildParser(new ANTLRStringStream(source)).interpretable();
    return interpretable.getTree().asInstanceOf[CommonTree];
  }

  def parseBlock(source: String): CommonTree = {
    val block = buildParser(new ANTLRStringStream(source)).block();
    return block.getTree().asInstanceOf[CommonTree];
  }

  def buildTreeParser(ast: CommonTree): NoopAST = {
    return new NoopAST(new CommonTreeNodeStream(ast));
  }

  def file(source: String): File = {
    return buildTreeParser(parseFile(source)).file();
  }

  def file(source: InputStream): File = {
    return buildTreeParser(parseFile(source)).file();
  }
}
