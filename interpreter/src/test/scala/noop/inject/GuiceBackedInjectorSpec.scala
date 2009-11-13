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


import interpreter.InterpreterModule;
import java.io.File;
import noop.types.NoopTypesModule;


import com.google.inject.Guice
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class GuiceBackedInjectorSpec extends Spec with ShouldMatchers {
  describe("the injector") {
    it("should create a boolean") {
      val guiceInjector = Guice.createInjector(new NoopTypesModule(), new InterpreterModule(List()));
      val noopInjector: Injector = guiceInjector.getInstance(classOf[Injector]);
      //TODO(alexeagle): finish the test!
    }
  }
}