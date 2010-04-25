package noop.graph;

import noop.model.LanguageElement;
import noop.model.Library;

/**
 * Creates the vertex in the element graph for each element encountered which doesn't have one already.
 * @author alexeagle@google.com (Alex Eagle)
 */
public class VertexCreatingVisitor extends ModelVisitor {
  private Library currentLibrary;

  @Override
  public void enter(LanguageElement element) {
    if (element instanceof Library) {
      currentLibrary = (Library) element;
    }
    if (currentLibrary != null) {
      if (element.vertex == Vertex.NONE) {
        int nextIndex = currentLibrary.add(element);
        element.vertex = new Vertex(currentLibrary.uid, nextIndex);
      }
    }
  }
}
