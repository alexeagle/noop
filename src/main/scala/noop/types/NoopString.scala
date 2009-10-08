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

import collection.immutable;

import model.ClassDefinition;

/**
 * @author alexeagle@google.com (Alex Eagle)
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
class NoopString(classDef: ClassDefinition, parameterInstances: Map[String, NoopObject],
    val value: String, injector: Injector) extends NoopObject(classDef, parameterInstances) {

  def nativeMethodMap = immutable.Map[String, Seq[NoopObject] => NoopObject](
    "toString" -> ((args: Seq[NoopObject]) => injector.create(value)),
    "length" -> ((args: Seq[NoopObject]) => injector.create(value.length))
  );

  override def nativeMethod(name: String): (Seq[NoopObject] => NoopObject) = {
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
