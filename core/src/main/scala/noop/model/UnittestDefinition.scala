package noop.model

import proto.Noop.{Property, Unittest}

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class UnittestDefinition(val data: Unittest) extends Invokable {
  def parameters: Seq[Property] = List();
  def name: String = data.getDescription;
}