package noop.model


import interpreter.Context
import types.NoopObject

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class EvaluatedExpression(value: NoopObject) extends Expression {
  def evaluate(c: Context): Option[NoopObject] = Some(value);
}