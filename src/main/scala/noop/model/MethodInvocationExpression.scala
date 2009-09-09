package noop.model


import collection.mutable.Buffer
import interpreter.Context

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class MethodInvocationExpression(val name: String, val arguments: Buffer[Expression]) extends Expression {

  def evaluate(c: Context) = {}
}