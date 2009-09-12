package noop.model;

import interpreter.Context;
import types.NoopObject;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class IdentifierExpression(val identifier: String) extends Expression {

  def evaluate(c: Context): Option[NoopObject] = {
    val currentFrame = c.stack.top;
    if (currentFrame.identifiers.contains(identifier)) {
      return Some(currentFrame.identifiers(identifier)._2);
    } else if (currentFrame.thisRef.parameterInstances.contains(identifier)) {
      return Some(currentFrame.thisRef.parameterInstances(identifier));
    }
    return None;
  }
}