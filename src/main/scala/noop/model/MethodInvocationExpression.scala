package noop.model;

import interpreter.{Context,Frame};
import types.{NoopObject,NoopType};

/**
 * @author alexeagle@google.com (Alex Eagle)
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
class MethodInvocationExpression(val left: Expression, val name: String,
    val arguments: Seq[Expression]) extends Expression {

  def evaluate(context: Context): Option[NoopObject] = {
    val stack = context.stack;
    val thisRef = left.evaluate(context) match {
      case Some(r) => r;
      case None => throw new RuntimeException(
          "Expression has no value, cannot dispatch method to it: " + left);
    }
    val method = thisRef.classDef.findMethod(name);
    val frame = new Frame(thisRef, method);

    if (method.parameters.size != arguments.size) {
      throw new RuntimeException("Method " + method.name + " takes " + method.parameters.size +
          " arguments but " + arguments.size + " were provided");
    }
    for (i <- 0 until arguments.size) {
      var value = arguments(i).evaluate(context) match {
        case Some(v) => v;
        case None => throw new RuntimeException("Argument " + i + " to method " + name + " evaluated to Void");
      }
      val identifier = method.parameters(i).name;

      frame.addIdentifier(identifier, new Tuple2[NoopType, NoopObject](null, value));
    }

    stack.push(frame);
    try {
      return method.execute(context);
    } finally {
      stack.pop();
    }
  }
}
