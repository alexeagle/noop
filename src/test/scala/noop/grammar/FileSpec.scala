package noop.grammar

import org.antlr.runtime.RecognitionException
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

class FileSpec extends Spec with ShouldMatchers {
  val parser = new OurParser();

  describe("parser") {
    it("should parse package declaration") {
      val source = "package noop.test; class Foo() {}";
      parser.parse(source).toStringTree() should equal ("(package noop test) (CLASS Foo)");
    }

    it("should parse import statements") {
      val source = "import noop.test.Test; class Foo() {}";
      parser.parse(source).toStringTree() should equal ("(import noop test Test) (CLASS Foo)");
    }

    it("should not allow import without a type on the end") {
      val source = "import noop.test;";
      intercept[RecognitionException] {
        parser.parse(source);
      }
    }

    it("should parse a realistic looking file header") {
      val source = """
        package noop.grammar;
        // TODO: it should fail when a semicolon is missing, but the test still passes
        import org.antlr.runtime.RecognitionException;
        import org.scalatest.Spec;
        import org.scalatest.matchers.ShouldMatchers;

        class FileSpec() {
        }
      """;
      parser.parse(source);
    }
  }
}
