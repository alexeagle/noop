package noop.grammar

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

class PropertiesSpec extends Spec with ShouldMatchers {
  val parser = new OurParser();

  describe("parser") {
    it("should parse a single property declaration") {
      val source = """
        class Foo() {
          Int a = 4;
          Int i;
        }
      """;
      val commonTree = parser.parse(source);

      commonTree.toStringTree() should equal ("(CLASS Foo (PROP Int (= a 4)) (PROP Int i))");
    }
  }
}