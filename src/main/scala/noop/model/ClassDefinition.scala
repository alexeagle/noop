package noop.model;

import scala.collection.mutable.{ArrayBuffer,Buffer,Map};

import interpreter.ClassLoader;
import interpreter.Context;
import types.NoopObject;

class ClassDefinition {
  var name:String = "";
  val parameters:Buffer[Parameter] = new ArrayBuffer[Parameter];
  val interfaces:Buffer[String] = new ArrayBuffer[String];
  val methods:Buffer[Method] = new ArrayBuffer[Method];
  val modifiers:Buffer[Modifier.Value] = new ArrayBuffer[Modifier.Value];

  def findMethod(methodName: String): Method = {
    methods.find(method => method.name == methodName) match {
      case Some(method) => return method;
      case None => throw new NoSuchMethodException(
          "Method " + methodName + " is not defined on class " + name);
    }
  }

  def getInstance(classLoader: ClassLoader): NoopObject = {
    val parameterInstances = Map.empty[String, NoopObject];

    for (param <- parameters) {
      val classDef = classLoader.findClass(param.noopType);

      parameterInstances += Pair(param.name, classDef.getInstance(classLoader));
    }
    return new NoopObject(this, parameterInstances);
  }
}
