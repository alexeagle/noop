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
package noop.interpreter;

import java.io.{ByteArrayOutputStream, File};

import collection.mutable.Stack;

import org.scalatest.matchers.ShouldMatchers;
import org.scalatest.Spec;

import grammar.Parser;
import model.{IntLiteralExpression, OperatorExpression};
import types.{Injector, NoopInteger};

/**
 * @author alexeagle@google.com (Alex Eagle)
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
class InterpreterSpec extends Spec with ShouldMatchers {

  def createFixture = {
    val stdLibSourcePath = new File(getClass().getResource("/stdlib").toURI).getAbsolutePath();
    val classLoader = new SourceFileClassLoader(new Parser(), List(stdLibSourcePath));
    val context = new Context(new Stack[Frame], classLoader);

    context.addRootFrame(null);
    (classLoader, context);
  }

  describe("the interpreter") {

    it("should evaluate simple arithmetic") {
      val (classLoader, context) = createFixture;
      val expr = new OperatorExpression(new IntLiteralExpression(2), "+", new IntLiteralExpression(3));
      val visitor = new InterpreterVisitor(context, new Injector(classLoader));

      expr.accept(visitor);
      val result = context.stack.top.lastEvaluated(0);

      result should not be (null);
      result.asInstanceOf[NoopInteger].value should be (5);
    }

    it("should evaluate more complex arithmetic") {
      val source = "{ (1 + 2) * 3 - 10 / 2 % 4; }";
      val (classLoader, context) = createFixture;
      val parser = new Parser();
      val block = parser.buildTreeParser(parser.parseBlock(source)).block();
      val visitor = new InterpreterVisitor(context, new Injector(classLoader));

      block.statements(0).accept(visitor);
      val result = context.stack.top.lastEvaluated(0);

      result should not be (null);
      result.asInstanceOf[NoopInteger].value should be (8);
    }

    it("should assign variables") {
      val source = "{ Int a; a = 1; }";
      val (classLoader, context) = createFixture;
      val parser = new Parser();
      val block = parser.buildTreeParser(parser.parseBlock(source)).block();
      val visitor = new InterpreterVisitor(context, new Injector(classLoader));

      context.stack.top.blockScopes.inScope("interpreter test") {
        block.accept(visitor);
        context.stack.top.blockScopes.scopes.top should contain key("a");
        context.stack.top.blockScopes.getIdentifier("a")._2 should not be (null);
        context.stack.top.blockScopes.getIdentifier("a")._2.asInstanceOf[NoopInteger].value should equal (1);
      }
    }
  }
}
