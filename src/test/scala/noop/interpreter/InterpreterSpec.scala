package noop.interpreter

import scala.Console
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import noop.grammar.Parser
import java.io.ByteArrayOutputStream

class InterpreterSpec extends Spec with ShouldMatchers {
  
  describe("the interpreter") {
    it("should register Console as a native type") {
      val classLoader = new MockClassLoader(new Parser(), List());
      val interpreter = new Interpreter(classLoader);
      interpreter.registerNativeTypes;
      classLoader.cache should contain key("Console");
    }
  }
}