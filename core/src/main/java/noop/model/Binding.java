package noop.model;

import noop.graph.ModelVisitor;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class Binding extends LanguageElement<Binding> {
  private Clazz type;
  private Expression boundTo;

  @Override
  public boolean adoptChild(LanguageElement child) {
    if (child instanceof Clazz) {
      type = (Clazz) child;
      return true;
    }
    if (child instanceof Expression) {
      boundTo = (Expression) child;
      return true;
    }
    return super.adoptChild(child);
  }

  @Override
  public void accept(ModelVisitor v) {
    v.visit(this);
  }
}
