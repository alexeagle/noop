package noop.model.persistence;

import junit.framework.TestCase
import org.scalatest.matchers.ShouldMatchers;
import noop.model.AstRoot;
import org.scalatest.Spec;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class YamlAstSerializerSpec extends Spec with ShouldMatchers {
  val serializer = new YamlAstSerializer();

  describe("deserializing an AST from YAML") {
    it ("should deserialize a whole AST") {
      val ast: AstRoot = serializer.deserialize(getClass().getResourceAsStream("ast-structure.yaml"));
      ast.modules(0).name should be("CmdLineModule");
    }
  }
}
