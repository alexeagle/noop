package noop.interpreter;

import model.Block
import types.{NoopType, NoopObject}
import scala.collection.mutable

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class Frame(val thisRef: NoopObject, val block: Block) {
  val identifiers = mutable.Map.empty[String, Tuple2[NoopType, NoopObject]];
  var returnValue: Option[Int] = None;
}
