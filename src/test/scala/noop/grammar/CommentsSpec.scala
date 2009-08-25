package noop.grammar

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

class CommentsSpec extends Spec with ShouldMatchers {

  val parser = new OurParser();

  describe("parser") {
    it("should be able to parse a single line comment") {
      val source = "// a comment\n class Foo() {}\n";
      val commonTree = parser.parseFile(source);

      commonTree.toStringTree() should equal ("(CLASS Foo)");
    }

    it("should be able to parse a multi-line comment") {
      val source = """
        /** hello
        bonjour
        */class Foo() {}
      """;
      var commonTree = parser.parseFile(source);

      commonTree.toStringTree() should equal ("(CLASS Foo)");
    }

    it("should be able to parse nested comments") {
      val source = """
        /** hello
        // bonjour
         *
        class Foo() {} */class Foo() {}
      """;
      var commonTree = parser.parseFile(source);

      commonTree.toStringTree() should equal ("(CLASS Foo)");
    }
  }
}
