package noop.interpreter

import noop.grammar.Parser

class Interpreter(parser:Parser) {
  def evaluate(source:String) = {
    val file = parser.file(source);
    println(file.classDef.name);
  }
}