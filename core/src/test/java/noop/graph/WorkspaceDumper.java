package noop.graph;

import java.io.PrintStream;

/**
 * Dumps the content of a workspace in one of a few possible formats.
 * @author alexeagle@google.com (Alex Eagle)
 */
public class WorkspaceDumper {
  private final Output output;
  private final PrintStream out;

  public WorkspaceDumper(Output output, PrintStream out) {
    this.output = output;
    this.out = out;
  }

  public enum Output {
    TXT, DOT
  }

  public void dump(Workspace workspace) {
    PrintingVisitor graphPrintingVisitor;
    switch (output) {
      case DOT:
        graphPrintingVisitor = new DotGraphPrintingVisitor(out);
        break;
      case TXT:
        graphPrintingVisitor = new OutlinePrintingVisitor(out);
        break;
      default:
        throw new RuntimeException("unknown output type " + output);
    }
    workspace.accept(graphPrintingVisitor);
  }
}
