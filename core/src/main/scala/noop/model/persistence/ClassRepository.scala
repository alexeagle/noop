package noop.model.persistence;

import noop.model.AstRoot;
import noop.model.ClassDefinition;

import java.io.InputStream;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class ClassRepository {
    def getClassDefinition(stream: InputStream): ClassDefinition = {
        val astSerializer = new YamlAstSerializer();
        val root: AstRoot = astSerializer.deserialize(stream);
        return null;
    }
}
