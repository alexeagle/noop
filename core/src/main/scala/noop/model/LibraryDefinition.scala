package noop.model

import collection.mutable.{Buffer, ArrayBuffer}
import collection.jcl.Buffer
import proto.Noop.{Library, ConcreteClass}

/**
 * A library is a collection of classes. It might be useful to have it correspond with a subproject/module/component.
 * It would typically be built into its own jar, and is the entity that makes dependencies on other libraries like
 * google collections.
 * @author alexeagle@google.com (Alex Eagle)
 */
class LibraryDefinition(data: Library) {
  def findClass(name: String) = {
    Buffer(data.getConcreteClassList).find((c: ConcreteClass) => c.getName == name) match {
      case Some(c) => new ConcreteClassDefinition(c);
      case None => throw new IllegalStateException("Class " + name + " not found in library " + data.getName);
    }
  }
}
