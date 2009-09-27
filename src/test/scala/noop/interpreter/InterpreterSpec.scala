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

package noop.interpreter

import collection.mutable.Stack
import grammar.Parser
import java.io.{ByteArrayOutputStream, File}
import model.{IntLiteralExpression, OperatorExpression}
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import types.NoopInteger

class InterpreterSpec extends Spec with ShouldMatchers {
  def createFixture = {
    val stdLibSourcePath = new File(getClass().getResource("/stdlib").toURI).getAbsolutePath();
    val classLoader = new SourceFileClassLoader(new Parser(), List(stdLibSourcePath));
    val context = new Context(new Stack[Frame], classLoader);
    (classLoader, context);
  }

  describe("the interpreter") {
    it("should evaluate simple arithmetic") {
      val (classLoader, context) = createFixture;
      val expr = new OperatorExpression(new IntLiteralExpression(2), "+", new IntLiteralExpression(3));
      val result = expr.evaluate(context);
      result should be('defined);
      result.get().asInstanceOf[NoopInteger].value should be (5);
    }

    it("should evaluate more complex arithmetic") {
      val source = "{ (1 + 2) * 3 - 10 / 2 % 4; }";
      val (classLoader, context) = createFixture;
      val parser = new Parser();
      val block = parser.buildTreeParser(parser.parseBlock(source)).block();
      val result = block.statements(0).evaluate(context);
      result should be('defined);
      result.get().asInstanceOf[NoopInteger].value should be (8);
    }

    it("should assign variables") {
      val source = "{ Int a; a = 1; }";
      val (classLoader, context) = createFixture;
      val parser = new Parser();
      val block = parser.buildTreeParser(parser.parseBlock(source)).block();
      context.stack.push(new Frame(null, null));
      block.evaluate(context);
      context.stack.top.identifiers should contain key("a");
      context.stack.top.identifiers("a")._2 should not be (null);
      context.stack.top.identifiers("a")._2.asInstanceOf[NoopInteger].value should equal (1);
    }
  }
}
