package noop.model


import interpreter.Context

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class ReturnExpression(val expr: Expression) extends Expression {

  def evaluate(c: Context) { }
}
