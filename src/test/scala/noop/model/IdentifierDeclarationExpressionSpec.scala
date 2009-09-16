package noop.model


import collection.mutable.Stack
import interpreter.{MockContext, MockClassLoader, Frame, Context}
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import types.NoopString

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class IdentifierDeclarationExpressionSpec extends Spec with ShouldMatchers with MockContext {

  describe("an assignment expression") {

    it("should add an identifier to the map of identifiers on the current stack frame") {
      val identifierDeclaration = new IdentifierDeclarationExpression("type", "myString");

      identifierDeclaration.initialValue = Some(new StringLiteralExpression("hello world"));

      val context = fixture;
      context.stack.push(new Frame(null, null));

      identifierDeclaration.evaluate(context);
      context.stack.top.identifiers should have size (1);
      context.stack.top.identifiers("myString")._2.asInstanceOf[NoopString].value should equal ("hello world");
    }

  }
}