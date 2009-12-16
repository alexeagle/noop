package noop.interpreter;

import noop.model.proto.Noop.{Library, ConcreteClass}
import noop.model.{LibraryDefinition, ConcreteClassDefinition, ClassDefinition}
import collection.jcl.Buffer

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class ClassRepository(libraries: Seq[Library]) {
  def getConcreteClassDefinition(name: String): ConcreteClassDefinition = {
    return libraries.find((l: Library) =>
      Buffer(l.getConcreteClassList).find((c: ConcreteClass) =>
        c.getName == name).isDefined) match {
      case Some(library) => new LibraryDefinition(library).findClass(name);
      case None => throw new ClassNotFoundException("Class " + name + " not found in any library");
    }
  }

  def concreteClasses: Seq[ConcreteClassDefinition] = libraries.flatMap((l: Library) => Buffer(l.getConcreteClassList))
      .map((c: ConcreteClass) => new ConcreteClassDefinition(c));
}
