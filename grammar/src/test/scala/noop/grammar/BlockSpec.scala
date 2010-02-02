/**
 * Copyright 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package noop.grammar;

import collection.mutable.ArrayBuffer
import org.scalatest.matchers.ShouldMatchers
import java.io.InputStream
import org.scalatest.Spec
import noop.model.proto.NoopAst._
import noop.model.proto.NoopAst.Expr.Type._

import noop.model._;

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

      returnExpression.expr should be(Expr.newBuilder
              .setType(INT_LITERAL)
              .setIntLiteral(IntLiteral.newBuilder
              .setValue(0)).build());
    }

    it("should allow chained property access on properties") {
      val source = "{ b.c.d; }";
      val blockAst = parser.parseBlock(source);

      blockAst.toStringTree() should be("(. (. b c) d)");
      val block = parser.buildTreeParser(blockAst).block();

      block.statements.size should be(1);
      val expectedExpr: Expr = block.statements(0).asInstanceOf[ExpressionWrapper].data;
      expectedExpr.getType should be (DEREFERENCE);
      expectedExpr.getDeref.getRhs.getType should be (IDENTIFIER);
      expectedExpr.getDeref.getRhs.getIdentifier should be ("d");
      expectedExpr.getDeref.getLhs.getType should be (DEREFERENCE);
      expectedExpr.getDeref.getLhs.getDeref.getLhs.getType should be (IDENTIFIER);
      expectedExpr.getDeref.getLhs.getDeref.getLhs.getIdentifier should be ("b");
      expectedExpr.getDeref.getLhs.getDeref.getRhs.getType should be (IDENTIFIER);
      expectedExpr.getDeref.getLhs.getDeref.getRhs.getIdentifier should be ("c");
    }

    it("should allow a method call on implicit 'this'") {
      val source = "{ a(); }";
      val blockAst = parser.parseBlock(source);

      blockAst.toStringTree() should be ("a ARGS");
      val block = parser.buildTreeParser(blockAst).block();
      block.statements.size should be(1);
      block.statements(0).asInstanceOf[ExpressionWrapper].data should be
        (Expr.newBuilder
                .setType(Expr.Type.METHOD_INVOCATION)
                .setMethodInvocation(MethodInvocation.newBuilder
                  .setTarget(Expr.newBuilder
                    .setType(Expr.Type.IDENTIFIER)
                    .setIdentifier("this"))
                  .setMethodName("a")).build());
    }

    it("should allow calling a method on an identifier") {
      val source = "{ a.b(); }";
      val blockAst = parser.parseBlock(source);

      blockAst.toStringTree() should be("(. a b ARGS)");
      val block = parser.buildTreeParser(blockAst).block();
      block.statements.size should be(1);

      val methodInvocation = block.statements(0).asInstanceOf[ExpressionWrapper].data;
      methodInvocation should be (Expr.newBuilder
              .setType(METHOD_INVOCATION)
              .setMethodInvocation(MethodInvocation.newBuilder
                .setTarget(Expr.newBuilder
                  .setType(IDENTIFIER)
                  .setIdentifier("a"))
                .setMethodName("b")).build())
    }

    it("should allow method chaining") {
      val source = "{ a.b().c(); }";
      val blockAst = parser.parseBlock(source);

      blockAst.toStringTree() should be("(. (. a b ARGS) c ARGS)");
      val block = parser.buildTreeParser(blockAst).block();
      block.statements should have length (1);

      val method = block.statements(0).asInstanceOf[ExpressionWrapper];
      method.data should be (Expr.newBuilder
              .setType(METHOD_INVOCATION)
              .setMethodInvocation(MethodInvocation.newBuilder
                .setTarget(Expr.newBuilder
                  .setType(METHOD_INVOCATION)
                  .setMethodInvocation(MethodInvocation.newBuilder
                    .setTarget(Expr.newBuilder
                      .setType(IDENTIFIER)
                      .setIdentifier("a"))
                    .setMethodName("b")))
                .setMethodName("c")).build());
    }

    it("should allow a method call on a property") {
      val source = "{ a.b.c(); }";
      val blockAst = parser.parseBlock(source);

      blockAst.toStringTree() should be("(. (. a b) c ARGS)");
      val block = parser.buildTreeParser(blockAst).block();
      block.statements should have length (1);

      val method = block.statements(0).asInstanceOf[ExpressionWrapper];
      method.data should be(Expr.newBuilder
              .setType(METHOD_INVOCATION)
              .setMethodInvocation(MethodInvocation.newBuilder
                .setTarget(Expr.newBuilder
                  .setType(DEREFERENCE)
                  .setDeref(Dereference.newBuilder
                    .setLhs(Expr.newBuilder
                      .setType(IDENTIFIER)
                      .setIdentifier("a"))
                    .setRhs(Expr.newBuilder
                      .setType(IDENTIFIER)
                      .setIdentifier("b"))))
                .setMethodName("c")).build());
    }

    it("should allow a method call with arguments") {
      val source = "{ a.b(c, \"d\"); }";
      val blockAst = parser.parseBlock(source);

      blockAst.toStringTree() should be ("(. a b (ARGS c \"d\"))");

      val block = parser.buildTreeParser(blockAst).block();
      block.statements.size should be(1);

      val methodInvocation = block.statements(0).asInstanceOf[ExpressionWrapper].data;
      methodInvocation should be(Expr.newBuilder
              .setType(METHOD_INVOCATION)
              .setMethodInvocation(MethodInvocation.newBuilder
                .setTarget(Expr.newBuilder
                  .setType(IDENTIFIER)
                  .setIdentifier("a"))
                .setMethodName("b")
                .addArgument(Expr.newBuilder
                  .setType(IDENTIFIER)
                  .setIdentifier("c"))
                .addArgument(Expr.newBuilder
                  .setType(STRING_LITERAL)
                  .setStringLiteral(StringLiteral.newBuilder.setValue("d")))).build());
    }
  }
}
