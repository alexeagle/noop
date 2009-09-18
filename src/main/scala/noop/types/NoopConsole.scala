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


import interpreter.Context
import model.{EvaluatedExpression, MethodInvocationExpression, ClassDefinition}
import scala.collection.mutable.Map;

class NoopConsole(classDef: ClassDefinition, parameterInstances: Map[String, NoopObject])
    extends NoopObject(classDef, parameterInstances) {

  def println(c: Context): Option[NoopObject] = {
    val toPrint = c.stack.top.identifiers("s")._2;
    val toString = new MethodInvocationExpression(new EvaluatedExpression(toPrint), "toString", Nil).evaluate(c) match {
      case Some(str) => str.asInstanceOf[NoopString]
      case None => throw new RuntimeException("Internal error: toString of " + toPrint + " returned Void");
    }
    Console.println(toString.value);
    return None;
  }

  def nativeMethodMap = Map[String, Context => Option[NoopObject]](
    "println" -> println
  );

  override def nativeMethod(name: String): (Context => Option[NoopObject]) = {
    return nativeMethodMap(name);
  }
}
