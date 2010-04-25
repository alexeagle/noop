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
public class ControlFlowExample extends Example {
  public ControlFlowExample(StandardLibraryBuilder stdLib) {
    super(stdLib);
  }

  @Override
  public void createProgram(Controller controller) {
    Project project = new Project("Control Flow", "com.example", "Copyright 2010\nExample Co.");
    controller.apply(new NewProjectOperation(project));

    Library library = new Library(UUID.randomUUID(), "Testing loops");
    project.addLibrary(library);

    Clazz clazz = new Clazz("Iterating Printer");
    library.addClazz(clazz);

    Method method = new Method("Print 1 through 10");
    clazz.addBlock(method);

    Parameter consoleDep = new Parameter("console");
    method.addParameter(consoleDep);

    IdentifierDeclaration i = new IdentifierDeclaration("count");
    method.addStatement(i);
    controller.apply(new NewEdgeOperation(i, TYPEOF, stdLib.intClazz));

    i.setInitialValue(new IntegerLiteral(0));

    Loop loop = new Loop();
    method.addStatement(loop);

    IntegerLiteral ten = new IntegerLiteral(10);
    method.addStatement(ten);

    Expression terminateWhen = new MethodInvocation();
    loop.setTerminationCondition(terminateWhen);
    controller.apply(
        new NewEdgeOperation(terminateWhen, TARGET, i),
        new NewEdgeOperation(terminateWhen, INVOKE, stdLib.integerEquals),
        new NewEdgeOperation(terminateWhen, ARG, ten));

    Block body = new AnonymousBlock();
    loop.setBody(body);

    Expression printValue = new MethodInvocation();
    body.addStatement(printValue);
    controller.apply(
        new NewEdgeOperation(printValue, TARGET, consoleDep),
        new NewEdgeOperation(printValue, INVOKE, stdLib.printMethod),
        new NewEdgeOperation(printValue, ARG, i));
  }
}
