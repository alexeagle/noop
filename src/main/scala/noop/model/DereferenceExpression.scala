package noop.model;

import collection.mutable.{ArrayBuffer, Buffer};
import interpreter.Context;
import types.NoopObject;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class DereferenceExpression(val left: Expression, val right: Expression) extends Expression {

  def evaluate(c: Context): Option[NoopObject] = {
    val saveThisRef = c.thisRef;

    try {
      c.thisRef = left.evaluate(c) match {
        case Some(r) => r;
        case None => throw new RuntimeException("What the hell just happened?");
      }
      right.evaluate(c);
    } finally {
      c.thisRef = saveThisRef;
    }
  }
}