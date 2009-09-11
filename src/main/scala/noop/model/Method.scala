package noop.model;

import collection.mutable.{ArrayBuffer, Buffer};

/**
 * Represents the declaration of a method in source code.
 */
class Method(val name: String, val returnType: String, val block: Block) {
  val parameters: Buffer[Parameter] = new ArrayBuffer[Parameter]();
  val modifiers: Buffer[Modifier.Value] = new ArrayBuffer[Modifier.Value]();
}
