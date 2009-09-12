package noop.interpreter;

import scala.collection.mutable.Stack;

import types.NoopObject;

/**
 * @author alexeagle@google.com (Alex Eagle)
 * @author jeremiele@google.com (Jeremie Lenfant-Engelmann)
 */
class Context(val stack: Stack[Frame], val classLoader: ClassLoader) {
}
