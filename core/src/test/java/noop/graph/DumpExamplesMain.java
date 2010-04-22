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

import noop.graph.WorkspaceDumper.Output;
import noop.stdlib.StandardLibraryBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class DumpExamplesMain {
  private final Output output;
  private final File outDir;

  public DumpExamplesMain(Output output, File outDir) {
    this.output = output;
    this.outDir = outDir;
  }

  public static void main(String[] args) throws FileNotFoundException {
    new DumpExamplesMain(Output.DOT, new File(args[0])).run();
    new DumpExamplesMain(Output.TXT, new File(args[0])).run();
  }

  public void run() throws FileNotFoundException {
    StandardLibraryBuilder stdLib = new StandardLibraryBuilder();

    for (Example example : Arrays.asList(
        new ArithmeticExample(stdLib),
        new HelloWorldExample(stdLib),
        new ControlFlowExample(stdLib))) {
      Workspace workspace = new Workspace();

      Controller controller = new Controller(workspace);
      controller.applyAll(stdLib.build());

      example.createProgram(controller);
      File outFile = new File(outDir, example.getClass().getSimpleName() + "." + output.name().toLowerCase());
      new WorkspaceDumper(output, new PrintStream(new FileOutputStream(outFile))).dump(workspace);
    }
  }
}
