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

import model.{Block, ClassDefinition, Method, MethodInvocationExpression, ShouldExpression,
              StringLiteralExpression};
import interpreter.testing.{TestFailedException, TestHolder, TestRunner};
import types.Injector;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class TestRunnerSpec extends Spec with ShouldMatchers with ContextFixture {

  val fooClass = new ClassDefinition("Foo", "a class");

  describe("the test runner") {

    it("should find unittests within production classes") {
      val unittest = new Method("name", "Void", new Block(), "doc");
      fooClass.unittests += unittest;
      val classLoader = new ClassSearch() {
        def eachClass(f: ClassDefinition => Unit) = {
          f.apply(fooClass);
        }
      }

      val testRunner = new TestRunner(classLoader, null);
      val testsToRun = testRunner.gatherTests();
      testsToRun should have length (1);
      testsToRun.first.testMethod should be theSameInstanceAs(unittest);
      testsToRun.first.classDef should be theSameInstanceAs(fooClass);
    }

    it("should execute a test") {
      val classLoader = new MockClassLoader();
      val block = new Block();
      val expression = new MockExpression();

      block.statements += expression;
      val testMethod = new Method("should execute me", "Void", block, "doc");
      val test = new TestHolder(fooClass, testMethod);

      new TestRunner(null, classLoader).runTest(test);
      expression.timesCalled should be(1);
    }
  }

  describe("the 'should' operator") {

    it("should be silent if the lefthand side matches an equals matcher") {
      val matcher = new MethodInvocationExpression(null, "equal", List(new StringLiteralExpression("hello")));
      val shouldExpr = new ShouldExpression(new StringLiteralExpression("hello"), matcher);
      val (context, injector) = fixture;
      val visitor = new InterpreterVisitor(context, injector);

      shouldExpr.accept(visitor);

      // TODO(jeremiele): no idea if it is the correct behavior
      context.stack.top.lastEvaluated.size should be (0);
    }

    it("should throw a test failed exception if an equals matcher is not satisfied") {
      val matcher = new MethodInvocationExpression(null, "equal", List(new StringLiteralExpression("goodbye")));
      val shouldExpr = new ShouldExpression(new StringLiteralExpression("hello"), matcher);
      val (context, injector) = fixture;
      val visitor = new InterpreterVisitor(context, injector);

      intercept[TestFailedException] {
        shouldExpr.accept(visitor);
      }
    }
  }
}
