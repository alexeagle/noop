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
package noop.interpreter.testing;

import collection.mutable.{ArrayBuffer, Buffer, Stack};

import model.{ClassDefinition, EvaluatedExpression, Method, MethodInvocationExpression,
    StringLiteralExpression}
import types.Injector;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class TestRunner(classSearch: ClassSearch, classLoader: ClassLoader) {

  def gatherTests(): Buffer[TestHolder] = {
    var tests = new ArrayBuffer[TestHolder];
    classSearch.eachClass((c:ClassDefinition) => {
      for (testMethod <- c.unittests) {
        tests += new TestHolder(c, testMethod);
      }
    });
    return tests;
  }

  def runTests() = {
    val startTime = System.currentTimeMillis;
    val tests = gatherTests;
    for (test <- tests) {
      runTest(test);
    }
    val elapsed = (System.currentTimeMillis - startTime) / 1000;
    println("Ran " + tests.size + " test(s) in " + elapsed + " seconds.");
  }

  /**
   * Run a single test
   */
  def runTest(test: TestHolder) = {
    val instance = test.classDef.getInstance(classLoader);
    val stack = new Stack[Frame];
    val context = new Context(stack, classLoader);

    stack.push(new Frame(instance, test.testMethod));
    try {
      new InterpreterVisitor(context, new Injector(classLoader)).visit(test.testMethod);
    } finally {
      stack.pop();
    }
  }
}
