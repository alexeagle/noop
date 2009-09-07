package noop.model


import interpreter.Context

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class AssignmentExpression(left: String, right: String) extends Expression {
  val lhs: String = left;
  val rhs: String = right;
  def evaluate(c: Context): Unit = {

  }
}