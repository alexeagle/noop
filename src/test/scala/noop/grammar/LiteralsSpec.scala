package noop.grammar

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

class LiteralsSpec extends Spec with ShouldMatchers {
  val parser = new Parser();

  describe("parser") {
    it("should parse integer literals") {
      val source = """
        class Foo() {
          Int a = 123;
          Int b = -123;
        }
      """;
      val commonTree = parser.parseFile(source);

      commonTree.toStringTree() should equal (
          "(CLASS Foo (VAR Int (= a 123)) (VAR Int (= b -123)))");
    }

    it("should parse a string literal") {
      val source = """
        class Foo() {
          String a = "hello, world!";
        }
      """;
      val commonTree = parser.parseFile(source);
      commonTree.toStringTree() should equal (
          "(CLASS Foo (VAR String (= a \"hello, world!\")))");
    }
  }
}