package noop.grammar

import org.scalatest.FunSuite

class CommentsTest extends FunSuite {

    test("Should be able to parse comments") {
      val source = "// a comment\n";
      val parser = new OurParser();
      val commonTree = parser.parse(source);

      assert(commonTree === null);
    }
}
