package noop.grammar


import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class ParserSpec extends Spec with ShouldMatchers {
  describe("our parser") {
    it("should throw an exception if there's an error parsing the AST") {
      // This source currently parses fine but blows up the tree parser. If it starts working
      // in the tree parser, this test will need to be fixed by finding another source meeting
      // that property.
      val source = "class MyClass() { Int foo() { Int result = main(List()); } }";
      val parser = new Parser();
      intercept[ParseException] {
        val classDef = parser.file(source);
      }
    }
  }
}