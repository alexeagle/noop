package noop.grammar

import org.antlr.runtime.RecognitionException
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

class InterfaceSpec extends Spec with ShouldMatchers {

  val parser = new Parser();

  describe("interface") {

    it("should parse an interface with no methods") {
      val source = "interface MyInterface {}";
      val commonTree = parser.parseFile(source);

      commonTree.toStringTree() should equal ("(INTERFACE MyInterface)");
    }

    it("should parse an interface with one method") {
      val source = "interface MyInterface { Int helloWorld();  }";
      val commonTree = parser.parseFile(source);

      commonTree.toStringTree() should equal ("(INTERFACE MyInterface (METHOD Int helloWorld))");
    }

    it("should not parse an interface with a method having a body") {
      val source = "interface MyInterface { Int helloWorld() { Int i = 0; }  }";
      intercept[RecognitionException] {
    	  parser.parseFile(source);
      }
    }
  }
}
