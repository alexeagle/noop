package noop.model

import collection.mutable.{ArrayBuffer, Buffer}

/**
 * Created by IntelliJ IDEA.
 * User: eva
 * Date: Dec 9, 2009
 * Time: 12:23:14 AM
 * To change this template use File | Settings | File Templates.
 */

class AstRoot {
  val modules: Buffer[Module] = new ArrayBuffer[Module];
}