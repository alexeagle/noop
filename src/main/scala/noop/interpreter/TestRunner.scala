package noop.interpreter


import collection.mutable.{Stack, ArrayBuffer, Buffer}
import model.{MethodInvocationExpression, EvaluatedExpression, StringLiteralExpression, Method, ClassDefinition}
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
      test.testMethod.execute(context);
    } finally {
      stack.pop();
    }
  }
}