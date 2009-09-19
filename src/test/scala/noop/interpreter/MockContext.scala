package noop.interpreter


import collection.mutable.Stack
import model.{Method, Modifier, ClassDefinition}

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

trait MockContext {
  def fixture = {
    val stack = new Stack[Frame];
    val classLoader = new MockClassLoader();
    val classDefinition = new ClassDefinition("String", "");
    val method = new Method("length", "Int", null, "");
    method.modifiers += Modifier.native;
    classDefinition.methods += method;

    classLoader.classes += Pair("String", classDefinition);
    classLoader.classes += Pair("Int", new ClassDefinition("Int", ""));
    val context = new Context(stack, classLoader);
    context;
  }
}