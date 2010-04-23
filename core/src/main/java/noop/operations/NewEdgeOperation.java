package noop.operations;

import noop.graph.Controller;
import noop.graph.Edge.EdgeType;
import noop.model.LanguageElement;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class NewEdgeOperation implements MutationOperation {
  public final LanguageElement src;
  public final EdgeType type;
  public final LanguageElement dest;

  public NewEdgeOperation(LanguageElement src, EdgeType type, LanguageElement dest) {
    this.src = src;
    this.type = type;
    this.dest = dest;
  }

  @Override
  public void execute(Controller controller) {
    controller.addEdge(this);
  }
}
