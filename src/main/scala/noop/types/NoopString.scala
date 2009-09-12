package noop.types;

import model.ClassDefinition;
import scala.collection.mutable.Map;

class NoopString(classDef: ClassDefinition, parameterInstances: Map[String, NoopObject],
    val value: String) extends NoopObject(classDef, parameterInstances) {
}
