package noop.grammar;

import org.scalatest.matchers.ShouldMatchers;
import org.scalatest.Spec;

class IfSpec extends Spec with ShouldMatchers {

  val parser = new Parser();

  describe("an if expression") {

    it("should parse correctly equality expression on two literals") {
      val source = "{ if (1 == 1) { } }";
      val ifBlock = parser.parseBlock(source);

      ifBlock.toStringTree() should be("(IF (EQ 1 1))");
    }

    it("should parse correctly an inequality expression on two literals") {
      val source = "{ if (1 != 2) { } }";
      val ifBlock = parser.parseBlock(source);

      ifBlock.toStringTree() should be("(IF (NEQ 1 2))");
    }

    it("should parse correctly an inequality expression followed by an or'ed equality expression on literals") {
      val source = "{ if (1 != 2 || 1 == 1) { } }";
      val ifBlock = parser.parseBlock(source);

      ifBlock.toStringTree() should be("(IF (NEQ 1 2) || (EQ 1 1))");
    }

    it("should parse correctly an inequality expression followed by an and'ed equality expression on literals") {
      val source = "{ if (1 != 2 && 1 == 1) { } }";
      val ifBlock = parser.parseBlock(source);

      ifBlock.toStringTree() should be("(IF (NEQ 1 2) && (EQ 1 1))");
    }

    it("should parse correctly complex conditional with or and and primaries") {
      val source = "{ if (1 != a.getValue() && \"hello\" != \"wolrd\" || 50 == myVar) { } }";
      val ifBlock = parser.parseBlock(source);

      ifBlock.toStringTree() should be("(IF (NEQ 1 (. a getValue ARGS)) && (NEQ \"hello\" \"wolrd\") || (EQ 50 myVar))");
    }    
  }
}
