package noop.graph;

import noop.graph.WorkspaceDumper.Output;
import noop.stdlib.StandardLibraryBuilder;
import org.junit.Before;
import org.junit.Test;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class DumpExamplesTest {

  Workspace workspace;
  StandardLibraryBuilder stdLib;
  Controller controller;
  
  @Before
  public void setUp() {
    workspace = new Workspace();
    stdLib = new StandardLibraryBuilder();
    controller = new Controller(workspace);
    controller.applyAll(stdLib.build());
  }

  @Test
  public void shouldCreateArithmeticDot() {
    new ArithmeticExample(stdLib).createProgram(controller);
    new WorkspaceDumper(Output.DOT, System.out).dump(workspace);
  }

  @Test
  public void shouldCreateArithmeticOutline() {
    new ArithmeticExample(stdLib).createProgram(controller);
    new WorkspaceDumper(Output.TXT, System.out).dump(workspace);
  }

  @Test
  public void shouldCreateHelloWorldDot() {
    new HelloWorldExample(stdLib).createProgram(controller);
    new WorkspaceDumper(Output.DOT, System.out).dump(workspace);
  }

  @Test
  public void shouldCreateHelloWorldOutline() {
    new HelloWorldExample(stdLib).createProgram(controller);
    new WorkspaceDumper(Output.TXT, System.out).dump(workspace);
  }

  @Test
  public void shouldCreateControlFlowDot() {
    new ControlFlowExample(stdLib).createProgram(controller);
    new WorkspaceDumper(Output.DOT, System.out).dump(workspace);
  }

  @Test
  public void shouldCreateControlFlowOutline() {
    new ControlFlowExample(stdLib).createProgram(controller);
    new WorkspaceDumper(Output.TXT, System.out).dump(workspace);
  }
}
