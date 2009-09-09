package noop.types;

import model.ClassDefinition;
import scala.collection.mutable.Map;

class NoopInteger(classDef: ClassDefinition, parameterInstances: Map[String, NoopObject],
    val value: Int) extends NoopObject(classDef, parameterInstances) {  
}
