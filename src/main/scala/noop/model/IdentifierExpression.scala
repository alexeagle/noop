package noop.model


import interpreter.Context

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class IdentifierExpression(i: String) extends Expression {
  val identifier = i;

  def evaluate(c: Context) = {}
}