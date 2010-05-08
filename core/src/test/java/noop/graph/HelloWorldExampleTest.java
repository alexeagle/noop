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
