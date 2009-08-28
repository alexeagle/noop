package noop.grammar

import org.antlr.runtime.RecognitionException
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

class MethodsSpec extends Spec with ShouldMatchers {

  val parser = new OurParser();

  it("should parse a method with no parameters") {
      val source = "class Bar() { String helloWorld() { Int i = 1; } }";
      val commonTree = parser.parseFile(source);

      commonTree.toStringTree() should equal ("(CLASS Bar (METHOD String helloWorld (VAR Int (= i 1))))");
    }

  it("should parse a method with parameters") {
      val source = "class Bar() { String helloWorld(String s, Int n) { Int i = 1; } }";
      val commonTree = parser.parseFile(source);

      commonTree.toStringTree() should equal ("(CLASS Bar (METHOD String helloWorld " +
          "(PARAMS (PARAM String s) (PARAM Int n)) (VAR Int (= i 1))))");
    }
  
  it("should parse a method invocation on a parameter reference") {
    val source = """
      class HelloWorld(Console console) {
        Int hello(String args) {
          console.println("Hello, World!");
          return 5.next;
        }
      }
    """;
   parser.parseFile(source).toStringTree() should equal(
     """(CLASS HelloWorld (PARAMS (PARAM Console console)) (METHOD Int hello (PARAMS (PARAM String args)) (. console println (ARGS "Hello, World!")) (return (. 5 next))))""");
  }
}
