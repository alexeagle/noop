package noop.grammar

import org.antlr.runtime.RecognitionException
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

class MethodsSpec extends Spec with ShouldMatchers {

  val parser = new OurParser();

  it("should parse a method with no parameters") {
      val source = "class Bar() { String helloWorld() { Int i = 1; } }";
      val commonTree = parser.parseFile(source);

      commonTree.toStringTree() should equal ("(CLASS Bar (METHOD String helloWorld (PROP Int (= i 1))))");
    }

  it("should parse a method with parameters") {
      val source = "class Bar() { String helloWorld(String s, Int n) { Int i = 1; } }";
      val commonTree = parser.parseFile(source);

      commonTree.toStringTree() should equal ("(CLASS Bar (METHOD String helloWorld " +
          "(PARAMS (PARAM String s) (PARAM Int n)) (PROP Int (= i 1))))");
    }
}
