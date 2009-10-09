package noop.types

import model.{ClassDefinition, Parameter}
import scala.collection.mutable;
import interpreter.ClassLoader;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class Injector(classLoader: ClassLoader) {

  def boolClass = classLoader.findClass("Boolean");
  def create(value: Boolean) = new NoopBoolean(boolClass, Map.empty[String, NoopObject], value, this);
  def intClass = classLoader.findClass("Int");
  def create(value: Int) = new NoopInteger(intClass, Map.empty[String, NoopObject], value, this);
  def stringClass = classLoader.findClass("String");
  def create(value: String) = new NoopString(stringClass, Map.empty[String, NoopObject], value, this);

  def getInstance(classDef: ClassDefinition): NoopObject = {
    val parameterInstances = mutable.Map.empty[String, NoopObject];

    for (param <- classDef.parameters) {
      val paramClassDef = classLoader.findClass(param.noopType);

      parameterInstances += Pair(param.name, getInstance(paramClassDef));
    }

    //TODO(alexeagle): Injectables really needs work
    classDef.name match {
      case "Console" => new NoopConsole(classLoader.findClass("Console"), parameterInstances);
      case _ => new NoopObject(classDef, parameterInstances);
    }
  }
}