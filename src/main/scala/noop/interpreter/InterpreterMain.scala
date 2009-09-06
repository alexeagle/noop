package noop.interpreter


import grammar.Parser
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
    //TODO: actually interpret it!
    println("Interpreting: " + mainClass.name);

    System.exit(0);
  }
}