package noop.grammar

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

class ExpressionsSpec extends Spec with ShouldMatchers {
  val parser = new Parser();

  describe("the parser") {
    it("should parse a variable assignment") {
      val source = "{ Int a = 3; }";
      parser.parseBlock(source).toStringTree() should equal ("(VAR Int (= a 3))");
    }
  }
}
