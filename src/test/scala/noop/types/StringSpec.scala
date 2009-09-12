package noop.types

import grammar.Parser
import interpreter.ClassLoader
import java.io.File
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class StringSpec extends Spec with ShouldMatchers {

  describe("a Noop String") {
    it("should have a valid class definition parsed from Noop source") {
      val stringSourcePath = new File(getClass().getResource("/stdlib").toURI).getAbsolutePath();
      println(stringSourcePath);
      val classLoader = new ClassLoader(new Parser(), List(stringSourcePath));
      val classDef = classLoader.findClass("String");
      classDef.name should be("String");
    }
  }
}