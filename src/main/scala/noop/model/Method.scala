package noop.model


import collection.mutable.{ArrayBuffer, Buffer}

/**
 * Represents the declaration of a method in source code.
 */
class Method(nm: String, rtnTp: String) {
  val name: String = nm;
  val returnType: String = rtnTp;
  var parameters: Buffer[Parameter] = new ArrayBuffer[Parameter]();
}
