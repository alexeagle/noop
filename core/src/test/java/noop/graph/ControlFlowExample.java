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
public class ControlFlowExample extends Example {
  public ControlFlowExample(StandardLibraryBuilder stdLib) {
    super(stdLib);
  }

  @Override
  public void createProgram(Controller controller) {
    Project project = new Project("Control Flow", "com.example", "Copyright 2010\nExample Co.");
    controller.apply(new NewNodeOperation(project));

    Library library = new Library("Testing loops");
    project.addLibrary(library);

    Clazz clazz = new Clazz("Iterating Printer");
    library.addClazz(clazz);

    Method method = new Method("Print 1 through 10");
    clazz.addBlock(method);

    Parameter consoleDep = new Parameter("console");
    method.addParameter(consoleDep);

    IdentifierDeclaration b = new IdentifierDeclaration("count");
    method.addStatement(b);
    controller.apply(new NewEdgeOperation(b, TYPEOF, stdLib.booleanClazz));

    b.setInitialValue(new IntegerLiteral(0));

    Loop loop = new Loop();
    method.addStatement(loop);

    IntegerLiteral ten = new IntegerLiteral(10);
    method.addStatement(ten);

    Expression terminateWhen = new MethodInvocation();
    loop.setTerminationCondition(terminateWhen);
    controller.applyAll(asList(
        new NewEdgeOperation(terminateWhen, TARGET, b),
        new NewEdgeOperation(terminateWhen, INVOKE, stdLib.integerEquals),
        new NewEdgeOperation(terminateWhen, ARG, ten)
    ));

    Block body = new AnonymousBlock();
    loop.setBody(body);

    Expression printValue = new MethodInvocation();
    body.addStatement(printValue);
    controller.applyAll(asList(
        new NewEdgeOperation(printValue, TARGET, consoleDep),
        new NewEdgeOperation(printValue, INVOKE, stdLib.printMethod),
        new NewEdgeOperation(printValue, ARG, b)
    ));
  }
}
