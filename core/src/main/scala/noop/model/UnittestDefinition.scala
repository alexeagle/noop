package noop.model

import proto.Noop.{Modifier, Property, Unittest}

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class UnittestDefinition(val data: Unittest) extends Invokable {
  def parameters: Seq[Property] = List();
  def name: String = data.getDescription;
  def modifiers: Seq[Modifier] = List();
  // TODO: wire in real unittest block
  def block: Block = new Block(null);
}