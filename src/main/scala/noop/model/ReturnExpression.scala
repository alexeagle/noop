package noop.model


import interpreter.Context

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class ReturnExpression(e: Expression) extends Expression {
  val expr = e;

  def evaluate(c: Context) {

  }
}
