/*
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
package noop.model

import collection.jcl.Buffer
import proto.Noop.{Unittest, Property, Method, ConcreteClass}

/**
 * An AST element representing a concrete class. To the user, this is a "class", but to us,
 * a class may be an interface or a binding as well. This is similar to Java's confusion
 * but it's familiar.
 *
 * @author Alex Eagle (alexeagle@google.com)
 */

class ConcreteClassDefinition(val data: ConcreteClass) extends ClassDefinition {
  def properties: Seq[Property] = Buffer(data.getPropertyList);
  def name: String = data.getName;
  def unittests: Seq[Unittest] = Buffer(data.getUnittestList);
  
  def findMethod(methodName: String): MethodDefinition = {
    val methods: Seq[Method] = Buffer(data.getMethodList());
    methods.find((method: Method) => method.getSignature.getName == methodName) match {
      case Some(method) => return new MethodDefinition(method);
      case None => throw new NoSuchMethodException(
          "Method " + methodName + " is not defined on class " + data.getName);
    }
  }
}
