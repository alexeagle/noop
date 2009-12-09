package noop.model.persistence;

import noop.model.AstRoot
import java.io.{InputStreamReader, InputStream}

import noop.model.Module;
import org.jvyaml.YAML;

import java.util.Map;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class YamlAstSerializer {
  def deserialize(stream: InputStream): AstRoot = {
    val modules = List(YAML.load(new InputStreamReader(stream)).asInstanceOf[java.util.List[Object]].toArray : _*);
    val result = new AstRoot();
    for (moduleMap <- modules) {
      val module: Module  = deserializeModule(moduleMap.asInstanceOf[Map[String, Object]]);
      result.modules += module;
    }
    return result;
  }

  def deserializeModule(map: Map[String, Object]):  Module = {
    new Module(map.get("name").asInstanceOf[String], map.get("documentation").asInstanceOf[String]);
  }
}
