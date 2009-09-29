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
import scala.collection.Map
import scala.collection.immutable

class NoopString(classDef: ClassDefinition, parameterInstances: Map[String, NoopObject],
    val value: String) extends NoopObject(classDef, parameterInstances) {

  def nativeMethodMap = immutable.Map[String, Context => Option[NoopObject]](
    "toString" -> ((c: Context) => new StringLiteralExpression(value).evaluate(c)),
    // TODO: not the right way to make an integer
    "length" -> ((c: Context) => new IntLiteralExpression(value.length).evaluate(c))
  );

  override def nativeMethod(name: String): (Context => Option[NoopObject]) = {
    return nativeMethodMap(name);
  }

  def isComparable(obj: Any) = obj.isInstanceOf[NoopString];
  override def equals(obj: Any) = obj match {
    case that: NoopString => (that isComparable this) &&
            that.value == this.value;
    case _ => false;
  }
  override def hashCode = super.hashCode * 41 + value.hashCode;
  override def toString() = value;
}
