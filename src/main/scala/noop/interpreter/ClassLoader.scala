package noop.interpreter


import java.io.File
import model.ClassDefinition

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

trait ClassLoader {
  def findClass(className: String): ClassDefinition;
  def getClassDefinition(file: File): ClassDefinition;
}