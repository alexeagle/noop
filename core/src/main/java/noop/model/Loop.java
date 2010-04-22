package noop.model;

import noop.graph.ModelVisitor;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class Loop extends Expression<Loop> {
  public Expression terminationCondition;
  public Block body;
  
  @Override
  public boolean adoptChild(LanguageElement child) {
    if (child instanceof Expression) {
      terminationCondition = (Expression) child;
      return true;
    }
    if (child instanceof Block) {
      body = (Block) child;
      return true;
    }
    return false;
  }

  @Override
  public void accept(ModelVisitor v) {
    v.visit(this);
    v.enter(terminationCondition);
    terminationCondition.accept(v);
    v.leave(terminationCondition);

    v.enter(body);
    body.accept(v);
    v.leave(body);
  }
}
