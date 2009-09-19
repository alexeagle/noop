package noop.grammar


import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class DocumentationSpec extends Spec with ShouldMatchers {
  val parser = new Parser();

  describe("documentation") {
    it("should appear before a class definition, in a string literal") {
      val source = "doc \"This class is awesome\" class Awesome() {}";
      parser.parseFile(source).toStringTree() should equal (
          "(CLASS Awesome (doc \"This class is awesome\"))");
      val file = parser.file(source);
      file.classDef.documentation should equal("This class is awesome");
    }

    it("should appear before a method declaration") {
      val source = "class Foo() { doc \"Here is my great method\" Int doIt() {}}";
      parser.parseFile(source).toStringTree() should equal (
          "(CLASS Foo (METHOD (doc \"Here is my great method\") Int doIt))");
      val file = parser.file(source);
      file.classDef.methods(0).documentation should equal ("Here is my great method");
    }
  }
}