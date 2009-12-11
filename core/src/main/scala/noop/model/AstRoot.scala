package noop.model

import collection.mutable.{ArrayBuffer, Buffer}

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class AstRoot {
  val modules: Buffer[Module] = new ArrayBuffer[Module];
}