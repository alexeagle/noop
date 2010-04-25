package noop.graph;

import com.thoughtworks.xstream.XStream;
import noop.model.LanguageElement;

import java.io.PrintStream;

/**
 * Dumps the content of a workspace in one of a few possible formats.
 * @author alexeagle@google.com (Alex Eagle)
 */
public class ModelSerializer {
  private final Output output;
  private final PrintStream out;

  public ModelSerializer(Output output, PrintStream out) {
    this.output = output;
    this.out = out;
  }

  public enum Output {
    TXT, XML, DOT
  }

  public void dump(LanguageElement element) {
    switch (output) {
      case DOT:
        element.accept(new DotGraphPrintingVisitor(out));
        break;
      case TXT:
        element.accept(new OutlinePrintingVisitor(out));
        break;
      case XML:
        XStream xStream = new XStream();
        out.append(xStream.toXML(element));
        break;
      default:
        throw new RuntimeException("unknown output type " + output);
    }
  }
}
