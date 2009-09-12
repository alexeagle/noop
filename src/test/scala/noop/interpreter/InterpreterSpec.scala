package noop.interpreter

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

class InterpreterSpec extends Spec with ShouldMatchers {

  describe("the interpreter") {
    it("should register Console as a native type") {
      val classLoader = new MockClassLoader();
      val interpreter = new Interpreter(classLoader);
      interpreter.registerNativeTypes;
      classLoader.cache should contain key("Console");
    }
  }
}