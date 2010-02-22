/**
 * Copyright 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package noop.model

import proto.NoopAst.{Operation, MethodInvocation}

/**
 * An expression with a binary operator, like + , with two operands.
 * This is sugar for the named method on the left operand, with the right operand as the argument.
 *
 * @author alexeagle@google.com (Alex Eagle)
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
class OperatorExpression(val left: Expression, val operator: String, val right: Expression) extends Expression {
  // Proto-based constructor
  def this(data: Operation) =
    this(new ExpressionWrapper(data.getLhs).getTypedExpression,
      data.getOperator, new ExpressionWrapper(data.getRhs).getTypedExpression);

  override def accept(visitor: Visitor) = {
    val methodName = operator match {
      case "+" => "plus";
      case "-" => "minus";
      case "*" => "multiply";
      case "/" => "divide";
      case "%" => "modulo";
      case "==" => "equals";
      case "!=" => "doesNotEqual";
      case ">" => "greaterThan";
      case "<" => "lesserThan";
      case ">=" => "greaterOrEqualThan";
      case "<=" => "lesserOrEqualThan";
    }

    new MethodInvocationExpression(left, methodName, List(right)).accept(visitor);
    visitor.visit(this);
  }
}
