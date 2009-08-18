package noop.grammar

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

class PropertiesTest extends FunSuite with ShouldMatchers {

    test("Should parse a single property declaration") {
      val source = """
        class Foo() {
          Int a = 4;
          Int i;
        }
      """;
      val parser = new OurParser();
      val commonTree = parser.parse(source);
      // TODO
      // commonTree.toStringTree() should equal ("(CLASS Foo (= Int a 4) (= Int i))");
    }
}