package noop.model;

import interpreter.Context;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class IdentifierDeclaration(val noopType: String, val name: String) extends Expression {
  var initialValue: Option[Expression] = None;

  def evaluate(c: Context) = None
}
