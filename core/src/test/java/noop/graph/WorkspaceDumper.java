package noop.graph;

import com.thoughtworks.xstream.XStream;

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
    TXT, XML, DOT
  }

  public void dump(Workspace workspace) {
    switch (output) {
      case DOT:
        workspace.accept(new DotGraphPrintingVisitor(out));
        break;
      case TXT:
        workspace.accept(new OutlinePrintingVisitor(out));
        break;
      case XML:
        XStream xStream = new XStream();
        out.append(xStream.toXML(workspace));
        break;
      default:
        throw new RuntimeException("unknown output type " + output);
    }
  }
}
