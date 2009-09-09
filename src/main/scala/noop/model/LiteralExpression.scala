package noop.model


import interpreter.Context

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class LiteralExpression[T](v: T) extends Expression {
  val value = v;

  def evaluate(c: Context) = {

  }
}
