package noop.interpreter

import scala.Console
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import noop.grammar.Parser
import java.io.ByteArrayOutputStream

class InterpreterSpec extends Spec with ShouldMatchers {
  
  describe("the interactive interpreter") {
    it("should read a class declaration and echo the class name") {
      val out = new ByteArrayOutputStream();
      Console.setOut(out);
      val interpreter:Interpreter = new Interpreter(new Parser());
      interpreter.evaluate("class Foo() {}");
      out.toString() should be ("Foo\n");
    }
    
    it("should parse an interpretable") {
      
    }
  }
}