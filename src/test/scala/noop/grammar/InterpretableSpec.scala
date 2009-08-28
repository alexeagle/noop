package noop.grammar

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

class InterpretableSpec extends Spec with ShouldMatchers {
  val parser = new Parser();
  
  describe("the parser") {
    it("should parse an import as interpretable") {
      val source = "import noop.Foo;";
      parser.parseInterpretable(source).toStringTree() should equal (
        "(import noop Foo)");
    }
    
    it("should parse a class definition as interpretable") {
      val source = "class F() {}";
      parser.parseInterpretable(source).toStringTree() should equal (
        "(CLASS F)");
    }
    
    it("should parse a variable declaratian as interpretable") {
      val source = "Int a = 3;";
      parser.parseInterpretable(source).toStringTree() should equal (
        "(VAR Int (= a 3))");
    }
    
    it("should parse several statements at once") {
      val source = """Int a = 3; import foo.Foo; String b = "poop"; class F() {}""";
      parser.parseInterpretable(source).toStringTree() should equal (
        """(VAR Int (= a 3)) (import foo Foo) (VAR String (= b "poop")) (CLASS F)""");
    }
  }
}
