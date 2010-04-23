package noop.graph;

import noop.model.LanguageElement;
import noop.model.Library;
import noop.model.Visitable;

/**
 * Creates the vertex in the element graph for each element encountered which doesn't have one already.
 * @author alexeagle@google.com (Alex Eagle)
 */
public class VertexCreatingVisitor extends ModelVisitor {
  private Library currentLibrary;

  @Override
  public void visit(Library library) {
    currentLibrary = library;
  }

  @Override
  public void enter(Visitable v) {
    if (currentLibrary != null && v instanceof LanguageElement) {
      LanguageElement element = (LanguageElement) v;
      if (element.vertex == Vertex.NONE) {
        int nextIndex = currentLibrary.add(element);
        element.vertex = new Vertex(currentLibrary.uid, nextIndex);
      }
    }
  }
}
