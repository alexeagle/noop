package noop.grammar

import model.{LiteralExpression, IdentifierDeclaration, AssignmentExpression}
import org.antlr.runtime.RecognitionException
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

class MethodsSpec extends Spec with ShouldMatchers {

  val parser = new Parser();
  describe("the parser") {
    it("should parse a method with no parameters") {
      val source = "class Bar() { String helloWorld() { Int i = 1; } }";
      val commonTree = parser.parseFile(source);

      commonTree.toStringTree() should equal ("(CLASS Bar (METHOD String helloWorld " +
          "(VAR Int (= i 1))))");
    }

    it("should parse a method with parameters") {
      val source = "class Bar() { String helloWorld(String s, Int n) { Int i = 1; } }";
      val commonTree = parser.parseFile(source);

      commonTree.toStringTree() should equal ("(CLASS Bar (METHOD String helloWorld " +
          "(PARAMS (PARAM String s) (PARAM Int n)) (VAR Int (= i 1))))");

      val file = parser.file(source);
      file.classDef.methods.size should be (1);
      val firstMethod = file.classDef.methods(0)
      firstMethod.name should be ("helloWorld");
      firstMethod.returnType should be ("String");
      firstMethod.parameters.size should be (2);
      firstMethod.parameters(0).name should be ("s");
      firstMethod.parameters(0).noopType should be ("String");
      firstMethod.parameters(1).name should be ("n");
      firstMethod.parameters(1).noopType should be ("Int");
      firstMethod.block.statements.size should be (1);
      firstMethod.block.statements(0).getClass() should be (classOf[IdentifierDeclaration]);
      val firstStatement = firstMethod.block.statements(0).asInstanceOf[IdentifierDeclaration];
      firstStatement.noopType should be ("Int");
      firstStatement.name should be ("i");
      // firstStatement.initialValue should be (Some(new LiteralExpression[Integer](1)));
    }

    it("should allow variable declaration without an initial value") {
      val source = "class Bar() { String helloWorld() { Int i; } }";
      val commonTree = parser.parseFile(source);

      commonTree.toStringTree() should equal ("(CLASS Bar (METHOD String helloWorld " +
          "(VAR Int i)))");

      val file = parser.file(source);
      file.classDef.methods.size should be (1);
      val firstMethod = file.classDef.methods(0)
      val firstStatement = firstMethod.block.statements(0).asInstanceOf[IdentifierDeclaration];

      firstStatement.noopType should be ("Int");
      firstStatement.name should be ("i");
      firstStatement.initialValue should be (None);
    }

    it("should parse a method invocation on a parameter reference") {
      val source = """class HelloWorld() { Int hello() { console.println("Hello, World!"); }}""";
      parser.parseFile(source).toStringTree() should equal(
          """(CLASS HelloWorld (METHOD Int hello (. console println (ARGS "Hello, World!"))))""");
    }

    it("should parse a method with a return statement") {
      val source = "class Foo() { Void do() { return 4; } }";
      parser.parseFile(source).toStringTree() should equal (
          "(CLASS Foo (METHOD Void do (return 4)))");
    }
  }
}
