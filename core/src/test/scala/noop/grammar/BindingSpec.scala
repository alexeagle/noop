package noop.grammar


import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec

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
      file.classDef.methods.first.anonymousBindings should have length(1);
    }

    it("can appear as a named binding block") {
      val source = "class Foo() { Int thing() { binding MyBinding {} } }";
      parser.parseFile(source).toStringTree() should be(
          "(CLASS Foo (METHOD Int thing (BINDING MyBinding)))");
    }

    it("can appear in a method declaration with a name") {
      val source = "class Foo() { Int thing() binding MyBinding {} }";
      parser.parseFile(source).toStringTree() should be(
          "(CLASS Foo (METHOD Int thing (BINDING MyBinding)))");
    }

    it("can appear anonymously in a method declaration") {
      val source = "class Foo() { Int thing() binding(This -> that) {} }";
      parser.parseFile(source).toStringTree() should be(
          "(CLASS Foo (METHOD Int thing (BINDING (BIND This that))))");
    }

    it("can appear in a unittest declaration with a name") {
      val source = "class Foo() { unittest \"test this\" binding MyBinding {} }";
      parser.parseFile(source).toStringTree() should be(
          "(CLASS Foo (UNITTEST \"test this\" (BINDING MyBinding)))");
    }

    it("can appear anonymously in a unittest declaration") {
      val source = "class Foo() { unittest \"test this\" binding(String -> \"foo\") {} }";
      parser.parseFile(source).toStringTree() should be(
          "(CLASS Foo (UNITTEST \"test this\" (BINDING (BIND String \"foo\"))))");
    }
  }
}