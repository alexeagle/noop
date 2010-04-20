package noop.model;

import noop.graph.ModelVisitor;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class UnitTest extends Block<UnitTest> {
  public UnitTest(String name) {
    super(name, null);
  }

  @Override
  public boolean adoptChild(LanguageElement child) {
    if (child instanceof Parameter) {
      return false;
    }
    return super.adoptChild(child);
  }

  @Override
  public void accept(ModelVisitor v) {
    v.visit(this);
    super.accept(v);
  }
}
