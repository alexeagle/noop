package noop.types;

import model.ClassDefinition;

import scala.collection.mutable.Map;

/**
 * @author alexeagle@google.com (Alex Eagle)
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
class NoopObject(val classDef: ClassDefinition, val parameterInstances: Map[String, NoopObject]) {
}
