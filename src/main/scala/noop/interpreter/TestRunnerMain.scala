package noop.interpreter


import grammar.Parser

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

object TestRunnerMain {
  def main(args: Array[String]) {
    val classLoader = new SourceFileClassLoader(new Parser(), args.toList);

    new TestRunner(classLoader).runTests();
  }
}