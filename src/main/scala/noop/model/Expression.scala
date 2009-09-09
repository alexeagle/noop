package noop.model;

import interpreter.Context;

import types.NoopObject;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
trait Expression {
  def evaluate(c: Context): Option[NoopObject];
}
