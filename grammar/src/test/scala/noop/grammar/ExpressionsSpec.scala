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

import org.scalatest.matchers.ShouldMatchers
import noop.model.{ExpressionWrapper, OperatorExpression, IntLiteralExpression, IdentifierDeclarationExpression}
import org.scalatest.Spec
import noop.model.proto.NoopAst.{IntLiteral, Operation, Expr};
import noop.model.proto.NoopAst.Expr.Type.{OPERATION,INT_LITERAL}

/**
 * @author alexeagle@google.com (Alex Eagle)
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
class ExpressionsSpec extends Spec with ShouldMatchers {

  val parser = new Parser();

  describe("the parser") {

    it("should parse a variable assignment") {
      val source = "{ Int a = 3; }";
      parser.parseBlock(source).toStringTree() should equal ("(VAR Int (= a 3))");
    }

    it("should parse arithmetic") {
      val source = "{ Float a = 3 + 4 / (5 * (6 + 7)) % 8; }";
      parser.parseBlock(source).toStringTree() should equal (
          "(VAR Float (= a (+ 3 (% (/ 4 (* 5 (+ 6 7))) 8))))");
      val block = parser.buildTreeParser(parser.parseBlock(source)).block();
      block.statements should have length(1);
      block.statements(0).getClass should be(classOf[IdentifierDeclarationExpression]);
      val declaration = block.statements(0).asInstanceOf[IdentifierDeclarationExpression];
      declaration.initialValue should be ('defined);
      val expr:Expr = declaration.initialValue.get().asInstanceOf[Expr];
      expr.getType should be (OPERATION);
      expr.getOperation.getLhs.getType should be (INT_LITERAL);
      expr.getOperation.getLhs.getIntLiteral.getValue should be (3);
      expr.getOperation.getOperator should be ("+");
      expr.getOperation.getRhs.getType should be (OPERATION);
      expr.getOperation.getRhs.getOperation.getOperator should be ("%")
    }
  }
}
