package noop.interpreter

import model.IdentifierDeclarationExpression;
import org.scalatest.matchers.ShouldMatchers;
import org.scalatest.Spec;

import types.{NoopString, Injector};

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class InterpreterVisitorSpec extends Spec with ShouldMatchers with MockContext {
  describe("the interpreter visitor") {
    it("should visit an identifier declaration and get the initial value from the eval stack") {
      val context = fixture;
      val injector = new Injector(context.classLoader)
      val visitor = new InterpreterVisitor(context, injector);
      val identifierDeclaration = new IdentifierDeclarationExpression("String", "s");

      context.stack.top.lastEvaluated += injector.create(true);
      context.stack.top.lastEvaluated += injector.create("hello");

      visitor.visit(identifierDeclaration);

      context.stack.top.identifiers("s")._2.getClass() should be(classOf[NoopString]);
    }
  }
}