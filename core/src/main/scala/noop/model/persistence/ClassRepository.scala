package noop.model.persistence;

import noop.model.proto.Noop.ConcreteClass
import com.google.protobuf.TextFormat
import java.io.{InputStreamReader, InputStream}
import noop.model.{ConcreteClassDefinition, ClassDefinition}


/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class ClassRepository {
  def getClassDefinition(stream: InputStream): ConcreteClassDefinition = {
    val builder = ConcreteClass.newBuilder();
    TextFormat.merge(new InputStreamReader(stream), builder);
    new ConcreteClassDefinition(builder.build());
  }
}
