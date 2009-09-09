package noop.interpreter

import noop.grammar.Parser

class Interpreter(parser:Parser) {
  def evaluate(source:String) = {
    val file = parser.buildTreeParser(parser.parseFile(source)).file();
    println(file.classDef.name);
  }
}