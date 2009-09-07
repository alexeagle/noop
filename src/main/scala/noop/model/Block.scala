package noop.model


import collection.mutable.{Buffer, ArrayBuffer}

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class Block {
  var statements: Buffer[Expression] = new ArrayBuffer[Expression]();
}