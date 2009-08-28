package noop.grammar

import org.antlr.runtime.RecognitionException
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

class FileSpec extends Spec with ShouldMatchers {
  val parser = new Parser();

  describe("parser") {
    it("should parse namespace declaration") {
      val source = "namespace noop.test; class Foo() {}";
      parser.parseFile(source).toStringTree() should equal (
        "(namespace noop test) (CLASS Foo)");
    }

    it("should parse import statements") {
      val source = "import noop.test.Test; class Foo() {}";
      parser.parseFile(source).toStringTree() should equal (
        "(import noop test Test) (CLASS Foo)");
    }

    it("should not allow import without a type on the end") {
      val source = "import noop.test;";
      intercept[RecognitionException] {
        parser.parseFile(source);
      }
    }

    it("should parse a realistic looking file header") {
      val source = """
        namespace noop.grammar;
        // TODO: it should fail when a semicolon is missing, but the test still passes
        import org.antlr.runtime.RecognitionException;
        import org.scalatest.Spec;
        import org.scalatest.matchers.ShouldMatchers;

        class FileSpec() {
        }
      """;
      parser.parseFile(source).toStringTree() should equal (
        "(namespace noop grammar) (import org antlr runtime RecognitionException) " +
        "(import org scalatest Spec) (import org scalatest matchers ShouldMatchers) " +
        "(CLASS FileSpec)");
      val file = parser.file(source);
      file.namespace should be ("noop.grammar");
      file.imports(0) should be ("org.antlr.runtime.RecognitionException");
      file.imports(1) should be ("org.scalatest.Spec");
      file.imports(2) should be ("org.scalatest.matchers.ShouldMatchers");
    }
  }
}
