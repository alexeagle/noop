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
package noop.types

import model.{ClassDefinition, Parameter}
import scala.collection.mutable;
import interpreter.ClassLoader;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class Injector(classLoader: ClassLoader) {

  def boolClass = classLoader.findClass("Boolean");
  def create(value: Boolean) = new NoopBoolean(boolClass, Map.empty[String, NoopObject], value, this);
  def intClass = classLoader.findClass("Int");
  def create(value: Int) = new NoopInteger(intClass, Map.empty[String, NoopObject], value, this);
  def stringClass = classLoader.findClass("String");
  def create(value: String) = new NoopString(stringClass, Map.empty[String, NoopObject], value, this);

  def getInstance(classDef: ClassDefinition): NoopObject = {
    val parameterInstances = mutable.Map.empty[String, NoopObject];

    for (param <- classDef.parameters) {
      val paramClassDef = classLoader.findClass(param.noopType);

      parameterInstances += Pair(param.name, getInstance(paramClassDef));
    }

    //TODO(alexeagle): Injectables really needs work
    classDef.name match {
      case "Console" => new NoopConsole(classLoader.findClass("Console"), parameterInstances);
      case _ => new NoopObject(classDef, parameterInstances);
    }
  }
}
