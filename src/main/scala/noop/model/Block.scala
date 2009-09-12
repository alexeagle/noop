package noop.model

import collection.mutable.{Buffer, ArrayBuffer}
import interpreter.Context
import types.NoopObject

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class Block extends Expression {
  var statements: Buffer[Expression] = new ArrayBuffer[Expression]();

  def evaluate(c: Context): Option[NoopObject] = None;
}