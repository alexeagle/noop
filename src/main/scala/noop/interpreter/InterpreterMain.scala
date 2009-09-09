package noop.interpreter;

import scala.collection.mutable.{ArrayBuffer, Stack, Map};
import grammar.Parser;
import model.{Block, ClassDefinition, Expression, MethodInvocationExpression, NativeExpression, Parameter, StringLiteralExpression};
import model.Method;
import org.antlr.runtime.MismatchedTokenException;
import types.{NoopConsole, NoopObject};

/**
 * The entry point into the Noop interpreter.
 *
 * @author alexeagle@google.com (Alex Eagle)
 */
object InterpreterMain {

  def bootstrap(mainClass: ClassDefinition, classLoader: ClassLoader) = {
    val evaluator = new Evaluator(classLoader);
    val context = new Context(new Stack[Frame], classLoader);

    context.thisRef = mainClass.getInstance(classLoader);
    var args = new ArrayBuffer[Expression];

    args += (new StringLiteralExpression("something"));
    evaluator.evaluateSomeStuffBitch(new MethodInvocationExpression("main",
        args), context)
  }

  def main(args: Array[String]) {
    //TODO: a proper command line parser, like scalax.io.CommandLineParser
    if (args.size != 2) {
      println("Usage: InterpreterMain main-class path/to/sources");
      System.exit(1);
    }
    //TODO: guice? other injector?
    val classLoader = new ClassLoader(new Parser(), List(args(1)));
    val consoleClassDef = new ClassDefinition();

    consoleClassDef.name = "Console";
    val printlnStatements = new ArrayBuffer[Expression]();
    printlnStatements += new NativeExpression(new NoopConsole(consoleClassDef,
        Map.empty[String, NoopObject]));
    val block = new Block();

    block.statements ++= (printlnStatements);
    val printlnMethod = new Method("println", "Void", block);
    val parameterForMethod = new Parameter();
    parameterForMethod.name = "s";
    parameterForMethod.noopType = "String";
    printlnMethod.parameters += parameterForMethod;
    consoleClassDef.methods += printlnMethod;
    classLoader.addNativeClass("Console", consoleClassDef);
    val mainClass = classLoader.findClass(args(0));

    mainClass.methods.find(m => m.name == "main") match {
      case Some(mainMethod) => bootstrap(mainClass, classLoader);
      case None => println("Main method not found in class " + mainClass.name);
    }
    System.exit(0);
  }
}
