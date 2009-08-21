package noop.grammar;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;

class OurParser() {
  def parseFile(source: String): CommonTree = {
    val input = new ANTLRStringStream(source);
    val noopParser = new NoopParser(new CommonTokenStream(new NoopLexer(input)));
    val file = noopParser.file();

    return file.getTree().asInstanceOf[CommonTree];
  }
  
}
