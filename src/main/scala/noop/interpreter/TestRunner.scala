package noop.interpreter


import collection.mutable.{ArrayBuffer, Buffer}
import model.{Method, ClassDefinition}

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class TestRunner(classSearch: ClassSearch) {
  def gatherTests():Buffer[Method] = {
    var tests:Buffer[Method] = new ArrayBuffer[Method];
    classSearch.eachClass((c:ClassDefinition) => tests ++= c.unittests);
    return tests;
  }

  def runTests() = {
    println("Running 0 tests");
  }
}