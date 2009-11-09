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
package noop.inject

/**
 * An implementation of our Noop fixture which uses Guice to store the bindings and create objects.
 * @author alexeagle@google.com (Alex Eagle)
 */

import model.ClassDefinition;
import interpreter.ClassLoader;
import types.{NoopConsole, NoopObject};

import com.google.inject.{AbstractModule, Guice};
import scala.collection.mutable;

class GuiceBackedInjector(classLoader: ClassLoader, injector: com.google.inject.Injector) extends Injector {
  // A pointer to the youngest child fixture
  var currentInjector: com.google.inject.Injector = injector;

  def getInstance(classDef: ClassDefinition): NoopObject = {
    val propertyMap = mutable.Map.empty[String, NoopObject];

    for (param <- classDef.parameters) {
      val paramClassDef = classLoader.findClass(param.noopType);

      propertyMap += Pair(param.name, getInstance(paramClassDef));
    }

    //TODO(alexeagle): Injectables still really needs work
    classDef.name match {
      case "Console" => new NoopConsole(classLoader.findClass("Console"), propertyMap);
      case _ => new NoopObject(classDef, propertyMap);
    }
  }

  def withBindings(bindings: Map[String, NoopObject])(f: => Any): Unit = {
    val module = new AbstractModule() {
      def configure() = {
        // TODO: add the bindings into this module
      }
    };
    currentInjector = injector.createChildInjector(module);
    f;
    currentInjector = currentInjector.getParent();
  }
}