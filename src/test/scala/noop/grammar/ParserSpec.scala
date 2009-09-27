package noop.grammar


import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class ParserSpec extends Spec with ShouldMatchers {
  describe("our parser") {
    it("should throw an exception if there's an error parsing the AST") {
      val source = "class MyClass() { Int foo() { Int result = List.List(List()); } }";
      val parser = new Parser();
      parser.parseFile(source).toStringTree() should be (
          "(CLASS MyClass (METHOD Int foo (VAR Int (= result (. List List (ARGS List ARGS))))))");
      intercept[ParseException] {
        val classDef = parser.file(source);
      }
    }
  }
}