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
package noop.interpreter;

import collection.mutable.{Stack, ArrayBuffer};
import model.{EvaluatedExpression, Expression, MethodInvocationExpression, StringLiteralExpression,
    ClassDefinition};

/**
 * This class bootstraps the interpretation process, by setting up the ClassLoader with
 * native Scala-implemented Noop types, and starting off the first method invocation.
 *
 * @author alexeagle@google.com (Alex Eagle)
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
class Interpreter(classLoader: ClassLoader) {

  def runApplication(mainClass: ClassDefinition) = {
    val context = new Context(new Stack[Frame], classLoader);

    context.addRootFrame();
    val visitor = new InterpreterVisitor(context);
    var args = new ArrayBuffer[Expression];

    //TODO: pass the list of command line arguments to main() instead
    args += new StringLiteralExpression("something");
    val mainInstance = new EvaluatedExpression(mainClass.getInstance(classLoader));

    new MethodInvocationExpression(mainInstance, "main", args).accept(visitor);
  };
}
