package noop.model


import collection.mutable.{ArrayBuffer, Buffer}
import interpreter.Context

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class DereferenceExpression(val left: Expression, val right: Expression) extends Expression {

  def evaluate(c: Context) = {}
}