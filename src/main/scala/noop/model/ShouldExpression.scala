package noop.model


import interpreter.Context
import interpreter.testing.TestFailedException
import types.NoopObject

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class ShouldExpression(left: Expression, right: Expression) extends Expression {
  def evaluate(c: Context): Option[NoopObject] = {
    if (!right.isInstanceOf[MethodInvocationExpression]) {
      throw new RuntimeException("right-hand side of should must be a matcher");
    }
    val matcherMethod = right.asInstanceOf[MethodInvocationExpression];
    val actual = left.evaluate(c);
    // TODO: wire in matchers
    matcherMethod.name match {
      case "equal" => {
        val expected = matcherMethod.arguments(0).evaluate(c);
        if (actual != expected) {
          throw new TestFailedException("expected " + actual + " to equal " + expected);
        }
      }
    }
    return None;
  }
}