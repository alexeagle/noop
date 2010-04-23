package noop.model;

import noop.graph.ModelVisitor;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public interface Visitable {
  void accept(ModelVisitor v);
}
