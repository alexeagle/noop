package noop.grammar

import model.{LiteralExpression, ReturnExpression, Block}
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class BlockSpec extends Spec with ShouldMatchers {

  val parser = new Parser();

  describe("a block") {

    it("should allow a return statement") {
      val block = parser.parseBlock("{ return 0; }");

      block.toStringTree() should be("(return 0)");
      val b = parser.buildTreeParser(block).block();

      b.statements.size should be(1);
      b.statements(0).getClass() should be(classOf[ReturnExpression]);
      val returnExpression = b.statements(0).asInstanceOf[ReturnExpression];

      returnExpression.expr.getClass() should be(classOf[LiteralExpression[Int]]);
      returnExpression.expr.asInstanceOf[LiteralExpression[Int]].value should be(0);
    }

  }
}

