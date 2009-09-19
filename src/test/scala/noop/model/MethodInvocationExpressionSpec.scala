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

package noop.model

import interpreter.MockContext
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class MethodInvocationExpressionSpec extends Spec with ShouldMatchers with MockContext {

  describe("a method invocation") {
    it("should throw an exception if the method doesn't exist on the type") {
      val context = fixture;
      val target = new StringLiteralExpression("aString");
      val expr = new MethodInvocationExpression(target, "noSuchMethodSorry", List());
      val exception = intercept[NoSuchMethodException] (
        expr.evaluate(context)
      );
      exception.getMessage() should include("noSuchMethodSorry");
      exception.getMessage() should include("String");
    }

    it("should evaluate the method body in a new stack frame") {
      val context = fixture;
      val target = new StringLiteralExpression("aString");
      val expr = new MethodInvocationExpression(target, "length", List());

      expr.evaluate(context);
    }

    it("should evaluate arguments and assign them to local variables indicated by the parameters") {

    }

    it("should throw an exception if the evaluated argument does not match the type of the parameter") {

    }

    it("should restore the original stack frame when finished") {

    }

    it("should evaluate native methods by invoking the native scala implementation") {

    }

    it("should throw an exception if an argument expression returns no value") {

    }
  }
}