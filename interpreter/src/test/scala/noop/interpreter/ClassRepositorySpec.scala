package noop.interpreter

import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import noop.model.proto.Noop.{ConcreteClass, Library}

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class ClassRepositorySpec extends Spec with ShouldMatchers {
  def fixture = List(Library.newBuilder
          .setName("lib1")
          .addConcreteClass(ConcreteClass.newBuilder
          .setName("test.One")).build, Library.newBuilder
          .setName("lib2")
          .addConcreteClass(ConcreteClass.newBuilder
          .setName("test.Two")).build);

  describe("the class repository") {
    it("should locate a class among the modules") {
      val libs: Seq[Library] = fixture;
      val foundClass = new ClassRepository(libs).getConcreteClassDefinition("test.One");
      foundClass.name should be("test.One");
    }

    it("should provide a list of all classes") {
      val libs: Seq[Library] = fixture;
      new ClassRepository(libs).concreteClasses should have length(2);
    }
  }
}