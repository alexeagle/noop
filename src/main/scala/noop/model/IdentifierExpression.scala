package noop.model;

import interpreter.Context;
import types.NoopObject;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class IdentifierExpression(val identifier: String) extends Expression {

  def evaluate(c: Context): Option[NoopObject] = {
    if (c.stack.top.identifiers.contains(identifier)) {
      return Some(c.stack.top.identifiers(identifier)._2);
    } else if (c.thisRef.parameterInstances.contains(identifier)) {
      return Some(c.thisRef.parameterInstances(identifier));
    }
    return None;
  }
}