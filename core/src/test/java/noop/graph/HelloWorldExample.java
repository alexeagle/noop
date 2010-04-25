package noop.graph;

import noop.model.*;
import noop.operations.NewEdgeOperation;
import noop.operations.NewProjectOperation;
import noop.stdlib.StandardLibraryBuilder;

import java.util.UUID;

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
    controller.apply(new NewProjectOperation(project));

    Library library = new Library(UUID.randomUUID(), "hello");
    project.addLibrary(library);

    Function sayHello = new Function("Say hello");
    library.addFunction(sayHello);

    Parameter consoleDep = new Parameter("console");
    sayHello.addParameter(consoleDep);

    controller.apply(
        new NewEdgeOperation(sayHello, TYPEOF, stdLib.intClazz),
        new NewEdgeOperation(consoleDep, TYPEOF, stdLib.consoleClazz));

    sayHello.setDocumentation(new Documentation("This is the entry point for the Hello World app",
        "alexeagle@google.com (Alex Eagle)"));

    StringLiteral helloWorld = new StringLiteral("Hello, World!");
    sayHello.addStatement(helloWorld);
    controller.apply(new NewEdgeOperation(helloWorld, TYPEOF, stdLib.stringClazz));

    Expression printHello = new MethodInvocation();
    sayHello.addStatement(printHello);
    controller.apply(
        new NewEdgeOperation(printHello, TARGET, consoleDep),
        new NewEdgeOperation(printHello, INVOKE, stdLib.printMethod),
        new NewEdgeOperation(printHello, ARG, helloWorld));

    IntegerLiteral zero = new IntegerLiteral(0);
    sayHello.addStatement(zero);

    Return aReturn = new Return();
    sayHello.addStatement(aReturn);
    controller.apply(
        new NewEdgeOperation(zero, TYPEOF, stdLib.intClazz),
        new NewEdgeOperation(aReturn, ARG, zero));

    UnitTest unitTest = new UnitTest("Should say hello");
    sayHello.addUnitTest(unitTest);

    IdentifierDeclaration resultDecl = new IdentifierDeclaration("result");
    unitTest.addStatement(resultDecl);
    controller.apply(new NewEdgeOperation(resultDecl, TYPEOF, stdLib.intClazz));

    Expression callMain = new MethodInvocation();
    unitTest.addStatement(callMain);
    controller.apply(new NewEdgeOperation(callMain, INVOKE, sayHello));

    Expression assertion = new MethodInvocation();
    unitTest.addStatement(assertion);
    // TODO: fill in assertion
  }
}
