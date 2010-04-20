package noop.graph;

import noop.model.LanguageElement;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class PrintingVisitor extends ModelVisitor {
  protected Workspace workspace;
  protected int currentDepth;

  @Override
  public void enter(LanguageElement element) {
    currentDepth++;
  }

  @Override
  public void leave(LanguageElement element) {
    currentDepth--;
  }

  protected int idFor(LanguageElement element) {
    return workspace.elements.indexOf(element);
  }

  protected String escape(String value) {
    return value.replaceAll("\n", "\\\\n");
  }
}
