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

/**
 * NOTE: this test relies on the working directory being the project root when tests are run.
 * @author alexeagle@google.com (Alex Eagle)
 */
class InterpreterSystemTest extends Spec with ShouldMatchers with ConsoleTestFixture {
  InterpreterMain.disableSystemExitForTesting;

  describe("the interpreter") {
    it("should run successfully") {
      InterpreterMain.main(List("helloworld.HelloWorld", "examples/noop").toArray);
      InterpreterMain.exitCodeForTesting should be(0);
    }

    it("should print an error and exit with error status if no arguments are given") {
      withRedirectedStandardOut { (output) => {
        InterpreterMain.main(List().toArray);
        InterpreterMain.exitCodeForTesting should be(1);
        output.toString should include("Usage: InterpreterMain main-class");
      }}
    }

    it("should use the working directory as a source root") {
      InterpreterMain.main(List("examples.noop.helloworld.HelloWorld").toArray);
      InterpreterMain.exitCodeForTesting should be(0);
    }
  }
}