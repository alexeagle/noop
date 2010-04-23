package noop.graph;

import noop.model.Visitable;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class PrintingVisitor extends ModelVisitor {
  protected Workspace workspace;
  protected int currentDepth;

  @Override
  public void enter(Visitable element) {
    currentDepth++;
  }

  @Override
  public void leave(Visitable element) {
    currentDepth--;
  }

  protected String escape(String value) {
    return value.replaceAll("\n", "\\\\n");
  }
}
