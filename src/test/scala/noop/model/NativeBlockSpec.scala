package noop.model

import collection.mutable.Stack
import interpreter.{Frame, MockClassLoader, Context}
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import types.NoopString

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class NativeBlockSpec extends Spec with ShouldMatchers {

  describe("a native block") {
    it("should execute the provided function when evaluating") {
      val context = new Context(new Stack[Frame], new MockClassLoader());
      val aString = new NoopString(null, null, "hello");
      val evaluator = (c: Context) => aString;
      val block = new NativeBlock(evaluator);
      block.evaluate(context).get() should equal(aString);
    }
  }
}