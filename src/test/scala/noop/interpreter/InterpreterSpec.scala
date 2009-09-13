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

package noop.interpreter

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

class InterpreterSpec extends Spec with ShouldMatchers {

  describe("the interpreter") {
    it("should register Console as a native type") {
      val classLoader = new MockClassLoader();
      val interpreter = new Interpreter(classLoader);
      interpreter.registerNativeTypes;
      classLoader.cache should contain key("Console");
    }
  }
}