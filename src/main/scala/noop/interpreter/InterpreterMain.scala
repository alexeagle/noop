package noop.interpreter;

import scala.collection.mutable.{ArrayBuffer, Stack, Map};
import grammar.Parser;
import model.{Block, ClassDefinition, Expression, MethodInvocationExpression, NativeExpression, Parameter, StringLiteralExpression};
import model.Method;
import org.antlr.runtime.MismatchedTokenException;
import types.{NoopConsole, NoopObject};

/**
 * The static entry point into the Noop interpreter, for use from the command-line.
 *
 * @author alexeagle@google.com (Alex Eagle)
 */
object InterpreterMain {
  def main(args: Array[String]) {
    //TODO: a proper command line parser, like scalax.io.CommandLineParser
    if (args.size != 2) {
      println("Usage: InterpreterMain main-class path/to/sources");
      System.exit(1);
    }
    //TODO: guice? other injector?
    val classLoader = new ClassLoader(new Parser(), List(args(1)));
    val mainClass = classLoader.findClass(args(0));

    new Interpreter(classLoader).runApplication(mainClass);

    System.exit(0);
  }
}
