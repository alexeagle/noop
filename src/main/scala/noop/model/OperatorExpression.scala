package noop.model


import interpreter.Context
import types.NoopObject

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class OperatorExpression(val left: Expression, val operator: String, val right: Expression) extends Expression {

  def evaluate(context: Context): Option[NoopObject] = {
    val methodName = operator match {
      case "+" => "plus";
      case "-" => "minus";
      case "*" => "multiply";
      case "/" => "divide";
      case "%" => "modulo";
    }
    return new MethodInvocationExpression(left, methodName, List(right)).evaluate(context);
  }
}