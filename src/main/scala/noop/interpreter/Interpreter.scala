package noop.interpreter;

import collection.mutable.{Map, Stack, ArrayBuffer}
import model.{IdentifierExpression, Expression, MethodInvocationExpression, StringLiteralExpression, ClassDefinition, Block, NativeExpression, Parameter, Method};

import types.{NoopConsole, NoopObject};
import noop.grammar.Parser;

/**
 * This class bootstraps the interpretation process, by setting up the ClassLoader with
 * native Scala-implemented Noop types, and starting off the first method invocation.
 *
 * @author alexeagle@google.com (Alex Eagle)
 * @author jeremiele@google.com (Jeremie Lenfant-Engelmann)
 */
class Interpreter(classLoader: ClassLoader) {

  def runApplication(mainClass: ClassDefinition) = {
    registerNativeTypes;

    val evaluator = new Evaluator(classLoader);
    val context = new Context(new Stack[Frame], classLoader);

    var args = new ArrayBuffer[Expression];

    //TODO: pass the list of command line arguments to main() instead
    args += (new StringLiteralExpression("something"));
    val mainInstance = new IdentifierExpression(mainClass.name);
    evaluator.evaluateSomeStuffBitch(
        new MethodInvocationExpression(mainInstance, "main", args), context);
  }

  def registerNativeTypes = {
    classLoader.addNativeClass("Console", consoleClassDef);
  }

  def consoleClassDef: ClassDefinition = {
    val consoleClassDef = new ClassDefinition();

    consoleClassDef.name = "Console";
    val block = new Block();

    block.statements += new NativeExpression(new NoopConsole(consoleClassDef,
        Map.empty[String, NoopObject]));
    val printlnMethod = new Method("println", "Void", block);
    printlnMethod.parameters += new Parameter("s", "String");
    consoleClassDef.methods += printlnMethod;
    return consoleClassDef;
  }
}