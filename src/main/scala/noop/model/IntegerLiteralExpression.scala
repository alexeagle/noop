package noop.model;

import interpreter.Context;
import types.{NoopInteger,NoopObject};

import scala.collection.mutable.Map;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class IntegerLiteralExpression(val value: Int) extends Expression {

  def evaluate(c: Context): Option[NoopInteger] = {
    val noopIntegerClassDef = new ClassDefinition();

    noopIntegerClassDef.name = "Int";
    return Some(new NoopInteger(noopIntegerClassDef, Map.empty[String, NoopObject], value));
  }
}
