package noop.model


import collection.mutable.{ArrayBuffer, Buffer}

/**
 * Represents the declaration of a method in source code.
 */
class Method(nm: String, rtnTp: String, blk: Block) {
  val name: String = nm;
  val returnType: String = rtnTp;
  val block: Block = blk;
  var parameters: Buffer[Parameter] = new ArrayBuffer[Parameter]();
}
