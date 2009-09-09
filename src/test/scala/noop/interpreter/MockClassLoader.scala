package noop.interpreter

import grammar.Parser
import scala.collection.mutable
import model.ClassDefinition

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class MockClassLoader(p: Parser, s: List[String]) extends ClassLoader {
  val classes = mutable.Map.empty[String, ClassDefinition];

  override def findClass(className: String): ClassDefinition = {
    return classes(className);
  }
}