package noop.model

import collection.mutable.{Buffer, ArrayBuffer}
import collection.jcl.Buffer
import proto.Noop.{ConcreteClass, Module}

/**
 * A module is a collection of classes. It might be useful to have it correspond with a subproject/module/component
 * @author alexeagle@google.com (Alex Eagle)
 */

class ModuleDefinition(data: Module) {
  def findClass(name: String) = {
    Buffer(data.getConcreteClassList).find((c: ConcreteClass) => c.getName == name) match {
      case Some(c) => new ConcreteClassDefinition(c);
      case None => throw new IllegalStateException("Class " + name + " not found in module " + data.getName);
    }
  }
}
