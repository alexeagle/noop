package noop.model

import scala.collection.mutable.Buffer
import scala.collection.mutable.ArrayBuffer

class ClassDefinition {
  var name:String = "";
  var parameters:Buffer[Parameter] = new ArrayBuffer[Parameter];
}
