package noop.grammar

import org.antlr.runtime.RecognitionException
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers


class ClassSpec extends Spec with ShouldMatchers {
  val parser = new OurParser();

  describe("parser") {
    it("should fail to parse a class with no parenthesis") {
      val source = "class Bar {}";
      intercept[RecognitionException] {
        parser.parseFile(source);
      }
    }

    it("should parse a class with no parameters") {
      val source = "class Bar() {}";
      val commonTree = parser.parseFile(source);

      commonTree.toStringTree() should equal ("(CLASS Bar)");
    }

    it("should parse a class with one parameter") {
      val source = "class Bar(A a) {}";
      val commonTree = parser.parseFile(source);

      commonTree.toStringTree() should equal ("(CLASS Bar (PARAMS (PARAM A a)))");
    }

    it("should parse a class with multiple parameters") {
      val source = "class Bar(A a, B b, C c) {}";
      val commonTree = parser.parseFile(source);

      commonTree.toStringTree() should equal (
          "(CLASS Bar (PARAMS (PARAM A a) (PARAM B b) (PARAM C c)))");
    }
    
    it("should allow modifiers on the parameters") {
      val source = "class Bar(mutable A a, delegate B b, mutable delegate C c) {}";
      parser.parseFile(source).toStringTree() should equal (
          "(CLASS Bar (PARAMS (PARAM (MOD mutable) A a) (PARAM (MOD delegate) B b) " +
          "(PARAM (MOD mutable delegate) C c)))");
    }
  }
}