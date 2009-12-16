package noop.interpreter

import com.google.inject.Inject
import noop.model.{ConcreteClassDefinition, ClassDefinition}

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class RepositoryClassLoader @Inject() (repository: ClassRepository) extends ClassLoader with ClassSearch {
  def findClass(className: String): ClassDefinition = repository.getConcreteClassDefinition(className);
  def eachClass(f: ConcreteClassDefinition => Unit) = {
    repository.concreteClasses.foreach(c => f.apply(c));
  }
}