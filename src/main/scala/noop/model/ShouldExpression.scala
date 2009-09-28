package noop.model


import interpreter.Context
import types.NoopObject

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class ShouldExpression(left: Expression, right: Expression) extends Expression {
  def evaluate(c: Context): Option[NoopObject] = None;
}