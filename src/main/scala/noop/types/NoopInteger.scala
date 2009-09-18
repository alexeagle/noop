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
import model.{StringLiteralExpression, IntLiteralExpression, ClassDefinition}
import scala.collection.Map;
import scala.collection.immutable;

class NoopInteger(classDef: ClassDefinition, parameterInstances: Map[String, NoopObject],
    val value: Int) extends NoopObject(classDef, parameterInstances) {

  def other(c: Context): Int = {
    c.stack.top.identifiers("other")._2.asInstanceOf[NoopInteger].value;
  }

  def nativeMethodMap = immutable.Map[String, Context => Option[NoopObject]](
  // TODO: this is an ugly way to make a new NoopInteger
    "plus" -> ((c: Context) => new IntLiteralExpression(value + other(c)).evaluate(c)),
    "minus" -> ((c: Context) => new IntLiteralExpression(value - other(c)).evaluate(c)),
    "multiply" -> ((c: Context) => new IntLiteralExpression(value * other(c)).evaluate(c)),
    "divide" -> ((c: Context) => new IntLiteralExpression(value / other(c)).evaluate(c)),
    "modulo" -> ((c: Context) => new IntLiteralExpression(value % other(c)).evaluate(c)),
    "toString" -> ((c: Context) => new StringLiteralExpression(value.toString).evaluate(c))
  );

  override def nativeMethod(name: String): (Context => Option[NoopObject]) = {
    return nativeMethodMap(name);
  }
}
