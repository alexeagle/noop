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

import org.scalatest.matchers.ShouldMatchers;
import org.scalatest.Spec;

import model.{Block, Method, ClassDefinition};

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class TestRunnerSpec extends Spec with ShouldMatchers {

  val fooClass = new ClassDefinition("Foo", "a class");

  describe("the test runner") {

    it("should find unittests within production classes") {
      val unittest = new Method("name", "Void", new Block(), "doc");
      fooClass.unittests += unittest;
      val classLoader = new ClassSearch() {
        def eachClass(f: ClassDefinition => Unit) = {
          f.apply(fooClass);
        }
      };

      val testRunner = new TestRunner(classLoader, null);
      val testsToRun = testRunner.gatherTests();
      testsToRun should have length (1);
      testsToRun.first.testMethod should be theSameInstanceAs(unittest);
      testsToRun.first.classDef should be theSameInstanceAs(fooClass);
    }

    it("should execute a test") {
      val classLoader = new MockClassLoader();
      val block = new Block();
      val expression = new MockExpression()
      block.statements += expression;
      val testMethod = new Method("should execute me", "Void", block, "doc");
      val test = new TestHolder(fooClass, testMethod);
      new TestRunner(null, classLoader).runTest(test);
      expression.timesCalled should be(1);
    };
  };
}
