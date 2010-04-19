package noop.graph;

import noop.graph.HelloWorldExampleMain.Output;
import org.junit.Test;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class HelloWorldExampleTest {

  @Test
  public void shouldCreateDotSuccessfully() {
    new HelloWorldExampleMain(Output.DOT, System.out).run();
  }

  @Test
  public void shouldCreateOutlineSuccessfully() {
    new HelloWorldExampleMain(Output.OUTLINE, System.out).run();
  }
}
