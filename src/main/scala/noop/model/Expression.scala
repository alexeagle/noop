package noop.model


import interpreter.Context

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

trait Expression {
  def evaluate(c: Context): Unit;
}