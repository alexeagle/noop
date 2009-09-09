package noop.interpreter;

import model.Expression;
import scala.collection.mutable.Stack;

/**
 * @author alexeagle@google.com (Alex Eagle)
 * @author jeremiele@google.com (Jeremie Lenfant-Engelmann)
 */
class Evaluator(classLoader: ClassLoader) {

  def evaluateSomeStuffBitch(e: Expression, context: Context) = {
    e.evaluate(context);
  }
}
