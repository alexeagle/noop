package noop.grammar

import collection.mutable.ArrayBuffer
import model._
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class BlockSpec extends Spec with ShouldMatchers {

  val parser = new Parser();

  describe("a block") {

    it("should allow a return statement") {
      val blockAst = parser.parseBlock("{ return 0; }");

      blockAst.toStringTree() should be("(return 0)");
      val block = parser.buildTreeParser(blockAst).block();

      block.statements.size should be(1);
      block.statements(0).getClass() should be(classOf[ReturnExpression]);
      val returnExpression = block.statements(0).asInstanceOf[ReturnExpression];

      returnExpression.expr.getClass() should be(classOf[LiteralExpression[Int]]);
      returnExpression.expr.asInstanceOf[LiteralExpression[Int]].value should be(0);
    }

    it("should allow chained property access on properties") {
      val source = "{ b.c.d; }";
      val blockAst = parser.parseBlock(source);

      blockAst.toStringTree() should be("(. (. b c) d)");
      val block = parser.buildTreeParser(blockAst).block();

      block.statements.size should be(1);
      block.statements(0).getClass() should be(classOf[DereferenceExpression]);
      val deref1 = block.statements(0).asInstanceOf[DereferenceExpression];
      deref1.left.getClass() should be(classOf[DereferenceExpression]);
      deref1.right.getClass() should be(classOf[IdentifierExpression]);
      deref1.right.asInstanceOf[IdentifierExpression].identifier should be("d");

      val deref2 = deref1.left.asInstanceOf[DereferenceExpression];
      deref2.left.getClass() should be(classOf[IdentifierExpression]);
      deref2.right.getClass() should be(classOf[IdentifierExpression]);
      deref2.left.asInstanceOf[IdentifierExpression].identifier should be("b");
      deref2.right.asInstanceOf[IdentifierExpression].identifier should be("c");
    }

    it("should allow calling a method") {
      val source = "{ a.b(); }";
      val blockAst = parser.parseBlock(source);

      blockAst.toStringTree() should be("(. a b ARGS)");
      val block = parser.buildTreeParser(blockAst).block();
      block.statements.size should be(1);

      block.statements(0).getClass() should be(classOf[DereferenceExpression]);
      val deref1 = block.statements(0).asInstanceOf[DereferenceExpression];
      deref1.left.getClass() should be(classOf[IdentifierExpression]);
      deref1.left.asInstanceOf[IdentifierExpression].identifier should be("a");

      deref1.right.getClass() should be(classOf[MethodInvocationExpression]);
      val methodInvocation = deref1.right.asInstanceOf[MethodInvocationExpression]
      methodInvocation.name should be("b");
      methodInvocation.arguments should be(new ArrayBuffer[Expression]);
      methodInvocation.arguments.isEmpty should be (true);
    }

    it("should allow method chaining") {
      val source = "{ a.b().c(); }";
      val blockAst = parser.parseBlock(source);

      blockAst.toStringTree() should be("(. (. a b ARGS) c ARGS)");
      val block = parser.buildTreeParser(blockAst).block();
      block.statements should have length (1);

      val deref1 = block.statements(0).asInstanceOf[DereferenceExpression];
      deref1.left.getClass() should be(classOf[DereferenceExpression]);
      val deref2 = deref1.left.asInstanceOf[DereferenceExpression];

      deref2.left.getClass() should be(classOf[IdentifierExpression]);
      deref2.left.asInstanceOf[IdentifierExpression].identifier should be("a");
      deref2.right.getClass() should be(classOf[MethodInvocationExpression]);
      deref2.right.asInstanceOf[MethodInvocationExpression].name should be("b");
      deref2.right.asInstanceOf[MethodInvocationExpression].arguments should be ('empty);

      deref1.right.getClass() should be(classOf[MethodInvocationExpression]);
      deref1.right.asInstanceOf[MethodInvocationExpression].name should be("c");
      deref1.right.asInstanceOf[MethodInvocationExpression].arguments should be('empty);
    }

    it("should allow a method call on a property") {
      val source = "{ a.b.c(); }";
      val blockAst = parser.parseBlock(source);

      blockAst.toStringTree() should be("(. (. a b) c ARGS)");
      val block = parser.buildTreeParser(blockAst).block();
      block.statements should have length (1);

      val deref1 = block.statements(0).asInstanceOf[DereferenceExpression];
      deref1.left.getClass() should be(classOf[DereferenceExpression]);
      val deref2 = deref1.left.asInstanceOf[DereferenceExpression];

      deref2.left.getClass() should be(classOf[IdentifierExpression]);
      deref2.left.asInstanceOf[IdentifierExpression].identifier should be("a");
      deref2.right.getClass() should be(classOf[IdentifierExpression]);
      deref2.right.asInstanceOf[IdentifierExpression].identifier should be("b");

      deref1.right.getClass() should be(classOf[MethodInvocationExpression]);
      deref1.right.asInstanceOf[MethodInvocationExpression].name should be("c");
      deref1.right.asInstanceOf[MethodInvocationExpression].arguments should be('empty);
    }
  }
}
