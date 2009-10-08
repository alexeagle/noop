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
package noop.types;

import collection.mutable.Map;

import interpreter.InterpreterVisitor;
import model.{ClassDefinition, EvaluatedExpression, MethodInvocationExpression};

/**
 * @author alexeagle@google.com (Alex Eagle)
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
class NoopConsole(classDef: ClassDefinition, parameterInstances: Map[String, NoopObject])
    extends NoopObject(classDef, parameterInstances) {

  def println(args: Seq[NoopObject]): NoopObject = {
    val toPrint = args(0).asInstanceOf[NoopString];
    Console.println(toString.asInstanceOf[NoopString].value);
    return null;
  }

  def nativeMethodMap = Map[String, Seq[NoopObject] => NoopObject] (
    "println" -> println
  );

  override def nativeMethod(name: String): (Seq[NoopObject] => NoopObject) = {
    return nativeMethodMap(name);
  }
}
