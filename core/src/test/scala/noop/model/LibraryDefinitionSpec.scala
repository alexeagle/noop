package noop.model

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import proto.Noop.{ConcreteClass, Library}

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class LibraryDefinitionSpec extends Spec with ShouldMatchers {
  describe("a LibraryDefinition") {
    it("should find a class by name") {
      val library = new LibraryDefinition(Library.newBuilder
              .setName("test")
              .addConcreteClass(ConcreteClass.newBuilder
              .setName("namespace.Class"))
              .addConcreteClass(ConcreteClass.newBuilder
              .setName("nope.not.this.One")).build);
      val foundClass = library.findClass("namespace.Class");
      foundClass.name should be("namespace.Class");
    }
  }
}