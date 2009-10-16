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
package noop.grammar

import model.{StringLiteralExpression, IdentifierExpression};
import org.scalatest.matchers.ShouldMatchers;
import org.scalatest.Spec;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class BindingSpec extends Spec with ShouldMatchers {
  val parser = new Parser();

  describe("the binding keyword") {

    it("allows the binding operator in a binding") {
      val source = "binding Something { " +
              "BankService -> BankServiceImpl; " +
              "Port -> 9876; " +
              "Max -> firstThing;" +
              "}";
      // TODO(alex): figure out a good syntax for this
      // parser.parseFile(source).toStringTree() should be("(BINDING Something)");
    }

    it("can appear as an anonymous binding block") {
      val source = "class Foo() { Int thing() { binding(A -> B) {} } }";
      parser.parseFile(source).toStringTree() should be(
          "(CLASS Foo (METHOD Int thing (BINDING (BIND A B))))");
      val file = parser.buildTreeParser(parser.parseFile(source)).file;
      val method = file.classDef.methods.first;
      method.block.anonymousBindings should have length(1);
      method.block.namedBinding should be(None);
      val firstBinding = method.block.anonymousBindings.first;
      firstBinding.noopType should be("A");
      firstBinding.binding.getClass() should be(classOf[IdentifierExpression]);
      firstBinding.binding.asInstanceOf[IdentifierExpression].identifier should be ("B");

    }

    it("can appear as a named binding block") {
      val source = "class Foo() { Int thing() { binding MyBinding {} } }";
      parser.parseFile(source).toStringTree() should be(
          "(CLASS Foo (METHOD Int thing (BINDING MyBinding)))");
      val file = parser.buildTreeParser(parser.parseFile(source)).file;
      val method = file.classDef.methods.first;
      method.block.anonymousBindings should be('empty);
      method.block.namedBinding should be(Some("MyBinding"));
    }

    it("can appear in a method declaration with a name") {
      val source = "class Foo() { Int thing() binding MyBinding {} }";
      parser.parseFile(source).toStringTree() should be(
          "(CLASS Foo (METHOD Int thing (BINDING MyBinding)))");
      val file = parser.buildTreeParser(parser.parseFile(source)).file;
      val method = file.classDef.methods.first;
      method.block.anonymousBindings should be('empty);
      method.block.namedBinding should be(Some("MyBinding"));
    }

    it("can appear anonymously in a method declaration") {
      val source = "class Foo() { Int thing() binding(This -> that) {} }";
      parser.parseFile(source).toStringTree() should be(
          "(CLASS Foo (METHOD Int thing (BINDING (BIND This that))))");
      val file = parser.buildTreeParser(parser.parseFile(source)).file;
      val method = file.classDef.methods.first;
      method.block.anonymousBindings should have length(1);
      val firstBinding = method.block.anonymousBindings.first;
      firstBinding.noopType should be("This");
      firstBinding.binding.getClass() should be(classOf[IdentifierExpression]);
      firstBinding.binding.asInstanceOf[IdentifierExpression].identifier should be ("that");
    }

    it("can appear in a unittest declaration with a name") {
      val source = "class Foo() { unittest \"test this\" binding MyBinding {} }";
      parser.parseFile(source).toStringTree() should be(
          "(CLASS Foo (UNITTEST \"test this\" (BINDING MyBinding)))");
      val file = parser.buildTreeParser(parser.parseFile(source)).file;
      val method = file.classDef.unittests.first;
      method.block.anonymousBindings should be('empty);
      method.block.namedBinding should be(Some("MyBinding"));
    }

    it("can appear anonymously in a unittest declaration") {
      val source = "class Foo() { unittest \"test this\" binding(String -> \"foo\") {} }";
      parser.parseFile(source).toStringTree() should be(
          "(CLASS Foo (UNITTEST \"test this\" (BINDING (BIND String \"foo\"))))");
      val file = parser.buildTreeParser(parser.parseFile(source)).file;
      val method = file.classDef.unittests.first;
      method.block.anonymousBindings should have length(1);
      val firstBinding = method.block.anonymousBindings.first;
      firstBinding.noopType should be("String");
      firstBinding.binding.getClass() should be(classOf[StringLiteralExpression]);
      firstBinding.binding.asInstanceOf[StringLiteralExpression].value should be ("foo");
    }
  }
}