package noop.model

import collection.mutable.Stack
import interpreter.{MockContext, MockClassLoader, Frame, Context}
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class MethodInvocationExpressionSpec extends Spec with ShouldMatchers with MockContext {

  describe("a method invocation") {
    it("should throw an exception if the method doesn't exist on the type") {
      val context = fixture;
      val target = new StringLiteralExpression("aString");
      val expr = new MethodInvocationExpression(target, "noSuchMethodSorry", List());
      val exception = intercept[NoSuchMethodException] (
        expr.evaluate(context)
      );
      exception.getMessage() should include("noSuchMethodSorry");
      exception.getMessage() should include("String");
    }

    it("should evaluate the method body in a new stack frame") {
      val context = fixture;
      val target = new StringLiteralExpression("aString");
      val expr = new MethodInvocationExpression(target, "length", List());

      expr.evaluate(context);
    }

    it("should evaluate arguments and assign them to local variables indicated by the parameters") {

    }

    it("should throw an exception if the evaluated argument does not match the type of the parameter") {

    }

    it("should restore the original stack frame when finished") {

    }

    it("should evaluate native methods by invoking the native scala implementation") {

    }

    it("should throw an exception if an argument expression returns no value") {

    }
  }
}