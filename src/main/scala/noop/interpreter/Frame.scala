package noop.interpreter;
 
import model.Block;
import types.{NoopType, NoopObject};
import scala.collection.mutable;

 /**
  * @author alexeagle@google.com (Alex Eagle)
  * @author jeremiele@google.com (Jeremie Lenfant-Engelmann)
  */
class Frame(val thisRef: NoopObject, val block: Block) {
  val identifiers = mutable.Map.empty[String, Tuple2[NoopType, NoopObject]];
  var returnValue: Option[Int] = None;

  def addIdentifier(name: String, arg: Tuple2[NoopType, NoopObject]) = {
    if (identifiers.contains(name)) {
      throw new RuntimeException("Identifier " + name + " shadowing existing identifier.");
    }
    identifiers += Pair(name, arg);
  }
}
