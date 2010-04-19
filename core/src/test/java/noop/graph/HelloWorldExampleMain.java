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

import noop.model.*;
import noop.operations.NewNodeOperation;
import noop.stdlib.StandardLibraryBuilder;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import static noop.graph.Edge.EdgeType.*;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class HelloWorldExampleMain {
  private Controller controller;
  private Workspace workspace;
  private StandardLibraryBuilder stdLib;
  private final PrintStream out;

  public HelloWorldExampleMain(PrintStream out) {
    this.out = out;
  }

  public static void main(String[] args) throws FileNotFoundException {
    new HelloWorldExampleMain(new PrintStream(new FileOutputStream(new File(args[0])))).run();
  }

  private void run() {
    workspace = new Workspace();
    controller = new Controller(workspace);
    stdLib = new StandardLibraryBuilder();
    controller.applyAll(stdLib.build());
    createHelloWorldProgram();
    workspace.accept(new DotGraphPrintingVisitor(workspace, out));
  }

  public void createHelloWorldProgram() {
    Project project = new Project("Hello World", "com.example", "Copyright 2010\nExample Co.");
    controller.apply(new NewNodeOperation(project));

    Library library = new Library("hello");
    controller.apply(new NewNodeOperation(library, project));

    Parameter consoleDep = new Parameter("console");

    Block sayHello = new Block("say hello", stdLib.intClazz, consoleDep);
    controller.applyAll(Arrays.asList(new NewNodeOperation(sayHello, library),
                        new NewNodeOperation(consoleDep, sayHello, TYPEOF, stdLib.consoleClazz)));

    Documentation sayHelloDoc = new Documentation("This is the entry point for the Hello World app");
    controller.apply(new NewNodeOperation(sayHelloDoc, sayHello));

    StringLiteral helloWorld = new StringLiteral("Hello, World!");
    controller.apply(new NewNodeOperation(helloWorld, sayHello, TYPEOF, stdLib.stringClazz));

    Expression printHello = new MethodInvocation(helloWorld);
    controller.apply(new NewNodeOperation(printHello, sayHello,  TARGET, consoleDep, INVOKE, stdLib.printMethod));

    IntegerLiteral zero = new IntegerLiteral(0);
    controller.applyAll(Arrays.asList(new NewNodeOperation(zero, sayHello, TYPEOF, stdLib.intClazz),
                        new NewNodeOperation(new Return(zero), sayHello)));
  }
}
