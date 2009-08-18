package noop.grammar

import org.antlr.runtime.RecognitionException
import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers


class ClassTest extends FunSuite with ShouldMatchers {
  val parser = new OurParser();

  test("Should fail parsing a class with no parenthesis") {
    val source = "class Bar {}";
    try {
      val commonTree = parser.parse(source);
      fail();
    } catch {
      case e:RecognitionException => ;
    }
  }

  test("Should parse a class with no parameters") {
    val source = "class Bar() {}";
    val commonTree = parser.parse(source);

    commonTree.toStringTree() should equal ("(CLASS Bar)");
  }

  test("Should parse a class with one parameter") {
    val source = "class Bar(A a) {}";
    val commonTree = parser.parse(source);

    commonTree.toStringTree() should equal ("(CLASS Bar (PARAMS (PARAM A a)))");
  }

  test("Should parse a class with multiple parameters") {
    val source = "class Bar(A a, B b, C c) {}";
    val commonTree = parser.parse(source);

    commonTree.toStringTree() should equal ("(CLASS Bar (PARAMS (PARAM A a) (PARAM B b) (PARAM C c)))");
  }
}