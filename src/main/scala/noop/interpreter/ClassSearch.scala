package noop.interpreter


import model.ClassDefinition

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

trait ClassSearch {
  def eachClass(f: ClassDefinition => Unit);
}