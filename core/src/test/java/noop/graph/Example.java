package noop.graph;

import noop.stdlib.StandardLibraryBuilder;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public abstract class Example {
  protected final StandardLibraryBuilder stdLib;

  public Example(StandardLibraryBuilder stdLib) {
    this.stdLib = stdLib;
  }

  public abstract void createProgram(Controller controller);
}
