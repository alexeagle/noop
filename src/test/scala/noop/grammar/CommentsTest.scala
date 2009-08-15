package noop.grammar

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

class CommentsTest extends FunSuite with ShouldMatchers {

    test("Should be able to parse comments") {
      val source = "// a comment\n class Foo() {}\n";
      val parser = new OurParser();
      val commonTree = parser.parse(source);

      commonTree.toStringTree() should equal ("(CLASS Foo)");
    }
}
