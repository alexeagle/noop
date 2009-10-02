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

import collection.mutable.Stack;
import model.{Method, Modifier, ClassDefinition};

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
trait MockContext {

  def fixture = {
    val stack = new Stack[Frame];
    val classLoader = new MockClassLoader();
    val classDefinition = new ClassDefinition("String", "");
    val method = new Method("length", "Int", null, "");
    method.modifiers += Modifier.native;
    classDefinition.methods += method;

    classLoader.classes += Pair("String", classDefinition);
    classLoader.classes += Pair("Int", new ClassDefinition("Int", ""));
    classLoader.classes += Pair("Boolean", new ClassDefinition("Boolean", ""));
    val context = new Context(stack, classLoader);

    context.addRootFrame();
    context;
  };
}
