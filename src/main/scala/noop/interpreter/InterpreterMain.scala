package noop.interpreter


import scala.collection.mutable
import grammar.Parser
import model.Method
import org.antlr.runtime.MismatchedTokenException

/**
 * The entry point into the Noop interpreter.
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

    val evaluator = new Evaluator(classLoader, new mutable.Stack[Frame]);
    val context = new Context();
    mainClass.methods.find(m => m.name == "main") match {
      case Some(mainMethod) => evaluator.evaluateSomeStuffBitch(null, context);
      case None => println("Main method not found in class " + mainClass.name);
    }

    System.exit(0);
  }
}