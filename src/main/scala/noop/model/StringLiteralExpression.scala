package noop.model;

import interpreter.Context
import types.{NoopInteger, NoopObject, NoopString};


import scala.collection.mutable.Map;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class StringLiteralExpression(val value: String) extends Expression {

  def evaluate(c: Context): Option[NoopString] = {
    val noopStringClassDef = new ClassDefinition();
    val lengthBlock = new NativeBlock((c: Context) => new NoopInteger(null, null, "a".length));
    noopStringClassDef.methods += new Method("length", "Int", lengthBlock);

    noopStringClassDef.name = "String";
    return Some(new NoopString(noopStringClassDef, Map.empty[String, NoopObject], value));
  }
}
