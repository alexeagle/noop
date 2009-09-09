package noop.interpreter

import collection.mutable.{ArrayBuffer, Stack}
import model.{Expression, MethodInvocationExpression, ClassDefinition}
import scala.Console
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import noop.grammar.Parser
import java.io.ByteArrayOutputStream

class EvaluatorSpec extends Spec with ShouldMatchers {
  val parser = new Parser();

  describe("the evaluator") {
    it("should evaluate a simple method") {
      val source = """
import noop.Application;
import noop.Console;

class HelloWorld(Console console) implements Application {

  Int main(List args) {

    console.println("Hello World!");
    return 0;
  }
}
""";
      val classLoader = new MockClassLoader(null, null);
      val classDef = parser.file(source).classDef;

      classLoader.classes += ("noop.HelloWorld" -> classDef);
      classLoader.classes += ("noop.Console" -> new ClassDefinition());
      val evaluator = new Evaluator(classLoader, new Stack[Frame]);
      val context = new Context();
      evaluator.evaluateSomeStuffBitch(new MethodInvocationExpression("main", new ArrayBuffer[Expression]), context);

    }
  }
}