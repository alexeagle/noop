package noop.model


import collection.mutable.{ArrayBuffer, Buffer}
import interpreter.Context

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class DereferenceExpression(l: Expression, r: Expression) extends Expression {
  val left = l;
  val right = r;

  def evaluate(c: Context) = {}
}