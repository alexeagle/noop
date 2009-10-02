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
package noop.model;

import org.scalatest.matchers.ShouldMatchers;
import org.scalatest.Spec;

import interpreter.{MockContext, InterpreterVisitor};
import types.NoopString;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class IdentifierDeclarationExpressionSpec extends Spec with ShouldMatchers with MockContext {

  describe("an assignment expression") {

    it("should add an identifier to the map of identifiers on the current stack frame") {
      val identifierDeclaration = new IdentifierDeclarationExpression("type", "myString");

      identifierDeclaration.initialValue = Some(new StringLiteralExpression("hello world"));

      val context = fixture;
      val visitor = new InterpreterVisitor(context);

      identifierDeclaration.accept(visitor);
      context.stack.top.identifiers should have size (1);
      context.stack.top.identifiers("myString")._2.asInstanceOf[NoopString].value should
          equal ("hello world");
    };
  };
}
