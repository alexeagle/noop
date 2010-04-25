package noop.graph;

import noop.model.LanguageElement;

import java.util.Stack;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class PrintingVisitor extends ModelVisitor {
  protected Workspace workspace;
  protected int currentDepth;
  protected Stack<LanguageElement> parents = new Stack<LanguageElement>();

  protected LanguageElement getParent() {
    if (parents.size() < 2) {
      return null;
    }
    return parents.elementAt(parents.size() - 2);
  }

  @Override
  public void enter(LanguageElement element) {
    parents.push(element);
    currentDepth++;
  }

  @Override
  public void leave(LanguageElement element) {
    parents.pop();
    currentDepth--;
  }

  protected String escape(String value) {
    return value.replaceAll("\n", "\\\\n");
  }
}
