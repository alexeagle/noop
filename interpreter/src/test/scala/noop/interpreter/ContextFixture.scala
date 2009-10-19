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

import collection.mutable.Stack
import types.Injector;
import model.{Method, Modifier, ClassDefinition};

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
trait ContextFixture {

  def fixture = {
    val stack = new Stack[Frame];
    val classLoader = new MockClassLoader();
    val classDefinition = new ClassDefinition("String", "");
    val length = new Method("length", "Int", null, "");
    length.modifiers += Modifier.native;
    classDefinition.methods += length;

    classLoader.classes += Pair("String", classDefinition);
    classLoader.classes += Pair("Int", new ClassDefinition("Int", ""));
    classLoader.classes += Pair("Boolean", new ClassDefinition("Boolean", ""));
    val context = new Context(stack, classLoader);

    val injector = new Injector(classLoader);
    context.addRootFrame(injector.create("aClazz"));
    (context, injector);
  }
}
