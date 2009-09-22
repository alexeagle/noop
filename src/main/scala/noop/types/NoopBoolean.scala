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
import model.{StringLiteralExpression, BooleanLiteralExpression, ClassDefinition}
import scala.collection.Map;
import scala.collection.immutable;

class NoopBoolean(classDef: ClassDefinition, parameterInstances: Map[String, NoopObject],
    val value: Boolean) extends NoopObject(classDef, parameterInstances) {

  def other(c: Context): Boolean = {
    c.stack.top.identifiers("other")._2.asInstanceOf[NoopBoolean].value;
  }

  def nativeMethodMap = immutable.Map[String, Context => Option[NoopObject]](
  // TODO: this is an ugly way to make a new NoopBoolean
    "and" -> ((c: Context) => new BooleanLiteralExpression(value && other(c)).evaluate(c)),
    "or" -> ((c: Context) => new BooleanLiteralExpression(value || other(c)).evaluate(c)),
    "xor" -> ((c: Context) => new BooleanLiteralExpression(value ^ other(c)).evaluate(c)),
    "not" -> ((c: Context) => new BooleanLiteralExpression(! value).evaluate(c)),
    "toString" -> ((c: Context) => new StringLiteralExpression(value.toString).evaluate(c))
  );

  override def nativeMethod(name: String): (Context => Option[NoopObject]) = {
    return nativeMethodMap(name);
  }
}
