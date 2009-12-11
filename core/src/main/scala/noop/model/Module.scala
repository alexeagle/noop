package noop.model

import collection.mutable.{ArrayBuffer, Buffer}

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class Module(val name: String, val documentation: String) {
  val concreteClasses: Buffer[ConcreteClassDefinition] = new ArrayBuffer[ConcreteClassDefinition];
  val aliases: Buffer[AliasDefinition] = new ArrayBuffer[AliasDefinition];
  val bindings: Buffer[BindingDefinition] = new ArrayBuffer[BindingDefinition];
  val interfaces: Buffer[InterfaceDefinition] = new ArrayBuffer[InterfaceDefinition];
}