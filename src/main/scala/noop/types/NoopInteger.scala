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

import interpreter.Context;
import model.ClassDefinition;

/**
 * @author alexeagle@google.com (Alex Eagle)
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
class NoopInteger(classDef: ClassDefinition, parameterInstances: Map[String, NoopObject],
    val value: Int) extends NoopObject(classDef, parameterInstances) {

  def other(c: Context): Int = {
    c.stack.top.identifiers("other")._2.asInstanceOf[NoopInteger].value;
  }

  def nativeMethodMap = immutable.Map[String, Context => Option[NoopObject]](

    // TODO: this is an ugly way to make a new NoopInteger
    "plus" -> ((c: Context) => Some(new NoopInteger(classDef,
        Map.empty[String, NoopObject], value + other(c)))),
    "minus" -> ((c: Context) => Some(new NoopInteger(classDef,
        Map.empty[String, NoopObject], value - other(c)))),
    "multiply" -> ((c: Context) => Some(new NoopInteger(classDef,
        Map.empty[String, NoopObject], value * other(c)))),
    "divide" -> ((c: Context) => Some(new NoopInteger(classDef,
        Map.empty[String, NoopObject], value / other(c)))),
    "modulo" -> ((c: Context) => Some(new NoopInteger(classDef,
        Map.empty[String, NoopObject], value % other(c)))),
    "toString" -> ((c: Context) => {
      val classLoader = c.classLoader;
      val classDef = classLoader.findClass("String");

      Some(new NoopString(classDef, Map.empty[String, NoopObject],
          value.toString));
    })
  );

  override def nativeMethod(name: String): (Context => Option[NoopObject]) = {
    return nativeMethodMap(name);
  }
}
