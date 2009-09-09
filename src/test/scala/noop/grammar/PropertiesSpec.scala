package noop.grammar

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

class PropertiesSpec extends Spec with ShouldMatchers {
  val parser = new Parser();

  describe("parser") {
    it("should parse a single property declaration") {
      val source = """
        class Foo() {
          Int a = 4;
          Int i;
        }
      """;

      parser.parseFile(source).toStringTree() should equal ("(CLASS Foo (VAR Int (= a 4)) (VAR Int i))");
    }
  }
}
