package noop.model.persistence;

import com.google.protobuf.TextFormat
import java.io.{InputStreamReader, InputStream}
import noop.model.proto.Noop.{Module, ConcreteClass}
import noop.model.{ModuleDefinition, ConcreteClassDefinition, ClassDefinition}
import collection.jcl.Buffer


/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class ClassRepository(modules: Seq[Module]) {
  def getConcreteClassDefinition(name: String): ConcreteClassDefinition = {
    return modules.find((m: Module) =>
      Buffer(m.getConcreteClassList).find((c: ConcreteClass) =>
        c.getName == name).isDefined) match {
      case Some(module) => new ModuleDefinition(module).findClass(name);
      case None => throw new ClassNotFoundException("Class " + name + " not found in any module");
    }
  }
}
