package noop.interpreter;

import scala.collection.mutable.Map;

import java.io.{FileInputStream, File};
import noop.grammar.Parser;
import noop.model.ClassDefinition;
import org.antlr.runtime.RecognitionException;

/**
 * @author jeremiele@google.com (Jeremie Lenfant-Engelmann)
 */
class ClassLoader(parser: Parser, srcPaths: List[String]) {

  val cache = Map.empty[String, ClassDefinition];

  def getClassDefinition(file: File): ClassDefinition = {
    try {
      return parser.file(new FileInputStream(file)).classDef;
    } catch {
      // ANTLR will print a message about what failed to parse.
      case ex: RecognitionException =>
          throw new RuntimeException("Failed to parse " + file.getAbsolutePath());
    }
  }

  def findClass(className: String): ClassDefinition = {
    val parts = className.split("\\.");
    val expectedFile = parts.last + ".noop";
    val relativePath = parts.take(parts.size - 1).mkString(File.separator);

    if (cache.contains(className)) {
      return cache(className);
    }
    for (path <- srcPaths) {
      val dir = new File(path, relativePath);
      if (!dir.isDirectory()) {
        throw new RuntimeException("Wrong srcPath given: " + dir + " is not a directory");
      }
      val files = dir.listFiles();

      files.find(f => f.getName() == expectedFile) match {
        case Some(file) => return getClassDefinition(file);
        case None => // will try in next directory
      }
    }
    throw new ClassNotFoundException("Could not find class: " + className);
  }

  def addNativeClass(name: String, classDef: ClassDefinition) = {
    cache += Pair(name, classDef);
  }
}
