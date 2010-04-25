package noop.graph;

import noop.graph.ModelSerializer.Output;
import noop.model.Library;
import noop.stdlib.StandardLibraryBuilder;
import org.junit.Before;
import org.junit.Test;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class HelloWorldExampleTest {
  Workspace workspace;
  StandardLibraryBuilder stdLib;
  Controller controller;
  private HelloWorldExample helloWorldExample;
  private Library library;

  @Before
  public void setUp() {
    workspace = new Workspace();
    stdLib = new StandardLibraryBuilder();
    controller = new Controller(workspace, new VertexCreatingVisitor());
    stdLib.build(controller);
    helloWorldExample = new HelloWorldExample(stdLib);
    helloWorldExample.createProgram(controller);
    library = workspace.lookupLibrary(helloWorldExample.uid);
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
