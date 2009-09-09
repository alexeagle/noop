package noop.model


import collection.mutable.{ArrayBuffer, Buffer}

/**
 * Represents the declaration of a method in source code.
 */
class Method(val name: String, val returnType: String, val block: Block) {
  var parameters: Buffer[Parameter] = new ArrayBuffer[Parameter]();
}
