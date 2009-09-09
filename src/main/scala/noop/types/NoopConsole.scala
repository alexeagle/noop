package noop.types;

import model.ClassDefinition;
import scala.collection.mutable.Map;

class NoopConsole(classDef: ClassDefinition, parameterInstances: Map[String, NoopObject])
    extends NoopObject(classDef, parameterInstances) {

  def println(s: String) = {
    Console.println(s);
  }
}
