package noop.model;

import noop.graph.ModelVisitor;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class Function extends Block<Function> {

  public Function(String name, Clazz returnType) {
    super(name, returnType);
  }
  
  @Override
  public void accept(ModelVisitor v) {
    v.visit(this);
    super.accept(v);
  }
}
