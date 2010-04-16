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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import static noop.graph.Edge.EdgeType.*;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class HelloWorldExampleMain {
  private static Clazz stringClazz;
  private static Clazz consoleClazz;
  private static Clazz intClazz;
  private static Block printMethod;

  public static void main(String[] args) throws FileNotFoundException {
    Workspace workspace = new Workspace();
    Controller controller = new Controller(workspace);
    createNoopStdLib(workspace, controller);
    createHelloWorldProgram(workspace, controller);
    PrintStream out = new PrintStream(new FileOutputStream(new File(args[0])));
    workspace.accept(new DotGraphPrintingVisitor(workspace, out));
  }

  public static void createNoopStdLib(Workspace workspace, Controller controller) {
    Project project = new Project("Noop", "com.google.noop", "Apache 2");
    controller.apply(new NewNodeOperation(project, workspace));

    Library lang = new Library("lang");
    controller.apply(new NewNodeOperation(lang, project));

    stringClazz = new Clazz("String");
    controller.apply(new NewNodeOperation(stringClazz, lang));

    Library io = new Library("io");
    controller.apply(new NewNodeOperation(io, project));

    consoleClazz = new Clazz("Console");
    controller.apply(new NewNodeOperation(consoleClazz, io));

    printMethod = new Block("print", null);
    controller.apply(new NewNodeOperation(printMethod, consoleClazz));

    Parameter printArg = new Parameter("s");
    controller.apply(new NewNodeOperation(printArg, printMethod, TYPEOF, stringClazz));

    intClazz = new Clazz("Integer");
    controller.apply(new NewNodeOperation(intClazz, lang));
  }

  public static void createHelloWorldProgram(Workspace workspace, Controller controller) {
    Project project = new Project("Hello World", "com.example", "Copyright 2010\nExample Co.");
    controller.apply(new NewNodeOperation(project, workspace));

    Library library = new Library("hello");
    controller.apply(new NewNodeOperation(library, project));

    Parameter consoleDep = new Parameter("console");

    Block sayHello = new Block("say hello", intClazz, consoleDep);
    controller.applyAll(new NewNodeOperation(sayHello, library),
                        new NewNodeOperation(consoleDep, sayHello, TYPEOF, consoleClazz));

    Documentation sayHelloDoc = new Documentation("This is the entry point for the Hello World app");
    controller.apply(new NewNodeOperation(sayHelloDoc, sayHello));

    StringLiteral helloWorld = new StringLiteral("Hello, World!");
    controller.apply(new NewNodeOperation(helloWorld, sayHello, TYPEOF, stringClazz));

    Expression printHello = new MethodInvocation(helloWorld);
    controller.apply(new NewNodeOperation(printHello, sayHello,  TARGET, consoleDep, INVOKE, printMethod));

    IntegerLiteral zero = new IntegerLiteral(0);
    controller.applyAll(new NewNodeOperation(zero, sayHello, TYPEOF, intClazz),
                        new NewNodeOperation(new Return(zero), sayHello));
  }

}
