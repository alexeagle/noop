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

import model.IdentifierDeclarationExpression;
import org.scalatest.matchers.ShouldMatchers;
import org.scalatest.Spec;

import types.{NoopString, Injector};

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class InterpreterVisitorSpec extends Spec with ShouldMatchers with MockContext {
  describe("the interpreter visitor") {
    it("should visit an identifier declaration and get the initial value from the eval stack") {
      val context = fixture;
      val injector = new Injector(context.classLoader);
      val visitor = new InterpreterVisitor(context, injector);
      val identifierDeclaration = new IdentifierDeclarationExpression("String", "s");

      context.stack.top.lastEvaluated += injector.create(true);
      context.stack.top.lastEvaluated += injector.create("hello");

      context.stack.top.blockScopes.inScope("interpreter test") {
        visitor.visit(identifierDeclaration);
        context.stack.top.blockScopes.getIdentifier("s")._2.getClass() should be(classOf[NoopString]);
      }
    }
  }
}