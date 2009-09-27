package noop.interpreter

import model.{Block, Method, ClassDefinition}
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class TestRunnerSpec extends Spec with ShouldMatchers {
  describe("the test runner") {
    it("should find unittests within production classes") {
      val fooClass = new ClassDefinition("Foo", "a class");
      val unittest = new Method("it should be awesome", "Void", new Block(), "doc");
      fooClass.unittests += unittest;
      var classLoader = new ClassSearch() {
        def eachClass(f: ClassDefinition => Unit) = {
          f.apply(fooClass);
        }
      };

      var testRunner = new TestRunner(classLoader);
      testRunner.gatherTests() should contain (unittest);
    }
  }
}