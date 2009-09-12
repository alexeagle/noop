package noop.model

import interpreter.Context
import types.NoopObject

/**
 * Converts a function from Context to NoopObject into a Block,
 * so it can behave the same as a list of statements in Noop.
 *
 * @author alexeagle@google.com (Alex Eagle)
 */

class NativeBlock(f: Context => NoopObject) extends Block {
  override def evaluate(c: Context): Option[NoopObject] = Some(f.apply(c));
}