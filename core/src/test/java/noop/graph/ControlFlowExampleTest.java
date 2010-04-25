package noop.graph;

import noop.graph.ModelSerializer.Output;
import noop.model.Library;
import noop.stdlib.StandardLibraryBuilder;
import org.junit.Before;
import org.junit.Test;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class ControlFlowExampleTest {
  Workspace workspace;
  StandardLibraryBuilder stdLib;
  Controller controller;
  private ControlFlowExample controlFlowExample;
  private Library library;

  @Before
  public void setUp() {
    workspace = new Workspace();
    stdLib = new StandardLibraryBuilder();
    controller = new Controller(workspace, new VertexCreatingVisitor());
    stdLib.build(controller);
    controlFlowExample = new ControlFlowExample(stdLib);
    controlFlowExample.createProgram(controller);
    library = workspace.lookupLibrary(controlFlowExample.uid);
  }

  @Test
  public void shouldCreateArithmeticDot() {
    new ModelSerializer(Output.DOT, System.out).dump(library);
  }

  @Test
  public void shouldCreateArithmeticOutline() {
    new ModelSerializer(Output.TXT, System.out).dump(library);
  }
}
