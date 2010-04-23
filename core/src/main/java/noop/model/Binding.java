package noop.model;

import noop.graph.ModelVisitor;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class Binding extends LanguageElement<Binding> {

  @Override
  public void accept(ModelVisitor v) {
    v.visit(this);
  }
}
