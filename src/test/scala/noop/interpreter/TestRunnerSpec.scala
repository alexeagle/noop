package noop.interpreter

import model.{MethodInvocationExpression, StringLiteralExpression, ShouldExpression, Block, Method, ClassDefinition}
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import testing.{TestRunner, TestHolder, TestFailedException}
/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class TestRunnerSpec extends Spec with ShouldMatchers with MockContext {
  val fooClass = new ClassDefinition("Foo", "a class");

  describe("the test runner") {
    it("should find unittests within production classes") {
      val unittest = new Method("name", "Void", new Block(), "doc");
      fooClass.unittests += unittest;
      val classLoader = new ClassSearch() {
        def eachClass(f: ClassDefinition => Unit) = {
          f.apply(fooClass);
        }
      };

      val testRunner = new TestRunner(classLoader, null);
      val testsToRun = testRunner.gatherTests();
      testsToRun should have length (1);
      testsToRun.first.testMethod should be theSameInstanceAs(unittest);
      testsToRun.first.classDef should be theSameInstanceAs(fooClass);
    }

    it("should execute a test") {
      val classLoader = new MockClassLoader();
      val block = new Block();
      val expression = new MockExpression();
      block.statements += expression;
      val testMethod = new Method("should execute me", "Void", block, "doc");
      val test = new TestHolder(fooClass, testMethod);
      new TestRunner(null, classLoader).runTest(test);
      expression.timesCalled should be(1);
    }
  }

  describe("the 'should' operator") {
    it("should be silent if the lefthand side matches an equals matcher") {
      val matcher = new MethodInvocationExpression(null, "equal", List(new StringLiteralExpression("hello")));
      val shouldExpr = new ShouldExpression(new StringLiteralExpression("hello"), matcher);
      val context = fixture;
      shouldExpr.evaluate(context) should be(None);
    }

    it("should throw a test failed exception if an equals matcher is not satisfied") {
      val matcher = new MethodInvocationExpression(null, "equal", List(new StringLiteralExpression("goodbye")));
      val shouldExpr = new ShouldExpression(new StringLiteralExpression("hello"), matcher);
      val context = fixture;
      intercept[TestFailedException] {
        shouldExpr.evaluate(context);
      }
    }
  }
}