package noop.model;

import noop.graph.ModelVisitor;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class AnonymousBlock extends Block<AnonymousBlock> {

  public AnonymousBlock() {
    super("[anonymous]");
  }

  @Override
  public void accept(ModelVisitor v) {
    v.visit(this);
    super.accept(v);
  }
}
