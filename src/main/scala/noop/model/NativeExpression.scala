package noop.model;

import interpreter.Context;
import types.{NoopConsole, NoopObject, NoopString};

class NativeExpression(instance: NoopConsole) extends Expression {

  def evaluate(c: Context): Option[NoopObject] = {
    instance.println(c.stack.top.identifiers("s")._2.asInstanceOf[NoopString].value);
    return None;
  }
}
