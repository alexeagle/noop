package noop.model

import scala.collection.mutable.Buffer
import scala.collection.mutable.ArrayBuffer

/**
 * A Noop source file.
 * 
 * @author alexeagle@google.com (Alex Eagle)
 */
class File {
  var namespace:String = "";
  var imports:Buffer[String] = new ArrayBuffer[String];
}
