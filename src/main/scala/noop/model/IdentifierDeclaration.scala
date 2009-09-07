package noop.model


import interpreter.Context

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class IdentifierDeclaration(t: String, n: String) extends Expression {
  val noopType = t;
  val name = n;
  var initialValue: Option[String] = None;

  def evaluate(c: Context) = {

  }
}
