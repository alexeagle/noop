package noop.model.persistence;

import noop.model.ClassDefinition;
import org.jvyaml.YAML;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: eva
 * Date: Dec 8, 2009
 * Time: 10:17:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class YamlAstSerializer {
    

    public static void main(String[] args) throws FileNotFoundException {
        List modules = (List) YAML.load(new FileReader(new File("/Users/eva/noop/ast-structure.yaml")));
        for (Object module : modules) {
            Map<String, Object> moduleMap = (Map<String, Object>) module;
            System.out.println("module " + moduleMap.get("name"));
            System.out.println("  bindings " + moduleMap.get("bindings"));
            System.out.println("  interfaces " + moduleMap.get("interfaces"));
            System.out.println("  concrete classes " + moduleMap.get("concrete classes"));
        }
  }
}
