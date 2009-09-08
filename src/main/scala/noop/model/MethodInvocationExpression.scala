package noop.model


import collection.mutable.Buffer
import interpreter.Context

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class MethodInvocationExpression(n: String, a: Buffer[Expression]) extends Expression {
  val name = n;
  val arguments = a;

  def evaluate(c: Context) = {}
}