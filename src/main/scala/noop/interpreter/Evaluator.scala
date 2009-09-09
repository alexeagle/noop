package noop.interpreter;

import model.Expression
import scala.collection.mutable.Stack;

class Evaluator(classLoader: ClassLoader, stack: Stack[Frame]) {

  def evaluateSomeStuffBitch(e: Expression, c: Context) = {
    e.evaluate(c);
    
  }
}