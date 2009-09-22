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
