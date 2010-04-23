package noop.model;

import noop.graph.ModelVisitor;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class Loop extends Expression<Loop> {
  public Expression terminationCondition;
  public Block body;

  public void setTerminationCondition(Expression condition) {
    this.terminationCondition = condition;
  }

  public void setBody(Block body) {
    this.body = body;
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
