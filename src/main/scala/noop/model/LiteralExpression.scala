package noop.model


import interpreter.Context

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class LiteralExpression[T](val value: T) extends Expression {

  def evaluate(c: Context) = {

  }
}
