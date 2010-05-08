/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
        DotGraphPrintingVisitor visitor = new DotGraphPrintingVisitor(out);
        visitor.enter(element);
        element.accept(visitor);
        visitor.leave(element);
        break;
      case TXT:
        OutlinePrintingVisitor v = new OutlinePrintingVisitor(out);
        v.enter(element);
        element.accept(v);
        v.leave(element);
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
