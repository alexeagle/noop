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
import model.ClassDefinition
import scala.collection.mutable.Map;

class NoopConsole(classDef: ClassDefinition, parameterInstances: Map[String, NoopObject])
    extends NoopObject(classDef, parameterInstances) {

  def println(c: Context): Option[NoopObject] = {
    Console.println(c.stack.top.identifiers("s")._2.asInstanceOf[NoopString].value);
    return None;
  }

  def nativeMethodMap = Map[String, Context => Option[NoopObject]](
    "println" -> println
  );

  override def nativeMethod(name: String): (Context => Option[NoopObject]) = {
    return nativeMethodMap(name);
  }
}