package noop.model


import interpreter.Context

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class AssignmentExpression(val lhs: String, val rhs: String) extends Expression {

  def evaluate(c: Context): Unit = {

  }
}