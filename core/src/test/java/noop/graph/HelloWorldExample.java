package noop.graph;

import noop.model.*;
import noop.operations.NewEdgeOperation;
import noop.operations.NewNodeOperation;
import noop.stdlib.StandardLibraryBuilder;

import static java.util.Arrays.asList;
import static noop.graph.Edge.EdgeType.*;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class HelloWorldExample extends Example {
  public HelloWorldExample(StandardLibraryBuilder stdLib) {
    super(stdLib);
  }

  @Override
  public void createProgram(Controller controller) {
    Project project = new Project("Hello World", "com.example", "Copyright 2010\nExample Co.");
    controller.apply(new NewNodeOperation(project));

    Library library = new Library("hello");
    controller.apply(new NewNodeOperation(library, project));

    Parameter consoleDep = new Parameter("console");

    Block sayHello = new Function("Say hello");
    controller.applyAll(asList(
        new NewNodeOperation(sayHello, library),
        new NewEdgeOperation(sayHello, TYPEOF, stdLib.intClazz),
        new NewNodeOperation(consoleDep, sayHello),
        new NewEdgeOperation(consoleDep, TYPEOF, stdLib.consoleClazz)));

    Documentation sayHelloDoc = new Documentation("This is the entry point for the Hello World app");
    controller.apply(new NewNodeOperation(sayHelloDoc, sayHello));

    StringLiteral helloWorld = new StringLiteral("Hello, World!");
    controller.applyAll(asList(
        new NewNodeOperation(helloWorld, sayHello),
        new NewEdgeOperation(helloWorld, TYPEOF, stdLib.stringClazz)));

    Expression printHello = new MethodInvocation();
    controller.applyAll(asList(
        new NewNodeOperation(printHello, sayHello),
        new NewEdgeOperation(printHello, TARGET, consoleDep),
        new NewEdgeOperation(printHello, INVOKE, stdLib.printMethod)));
    controller.apply(new NewEdgeOperation(printHello, ARG, helloWorld));

    IntegerLiteral zero = new IntegerLiteral(0);
    Return aReturn = new Return();
    controller.applyAll(asList(
        new NewNodeOperation(zero, sayHello),
        new NewEdgeOperation(zero, TYPEOF, stdLib.intClazz),
        new NewNodeOperation(aReturn, sayHello),
        new NewEdgeOperation(aReturn, ARG, zero)));

    Block unitTest = new UnitTest("Should say hello");
    controller.apply(new NewNodeOperation(unitTest, sayHello));

    IdentifierDeclaration resultDecl = new IdentifierDeclaration("result");
    controller.applyAll(asList(
        new NewNodeOperation(resultDecl, unitTest),
        new NewEdgeOperation(resultDecl, TYPEOF, stdLib.intClazz)));

    Expression callMain = new MethodInvocation();
    controller.applyAll(asList(
        new NewNodeOperation(callMain, resultDecl),
        new NewEdgeOperation(callMain, INVOKE, sayHello)));

    Expression assertion = new MethodInvocation();
    controller.apply(new NewNodeOperation(assertion, unitTest));
  }
}
