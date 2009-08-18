package noop.grammar

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

class CommentsTest extends FunSuite with ShouldMatchers {

    test("Should be able to parse single line comment") {
      val source = "// a comment\n class Foo() {}\n";
      val parser = new OurParser();
      val commonTree = parser.parse(source);

      commonTree.toStringTree() should equal ("(CLASS Foo)");
    }

    test("Should be able to parse multilines comment") {
      val source = "/** hello\nbonjour\n */class Foo() {}";
      val parser = new OurParser();
      var commonTree = parser.parse(source);

      commonTree.toStringTree() should equal ("(CLASS Foo)");
    }

    test("Should be able to parse nested comments") {
      val source = "/** hello\n// bonjour\n *\n class Foo() {} */class Foo() {}";
      val parser = new OurParser();
      var commonTree = parser.parse(source);

      commonTree.toStringTree() should equal ("(CLASS Foo)");
    }
}
