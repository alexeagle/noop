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
    controller.apply(new NewNodeOperation(library, project));

    Clazz clazz = new Clazz("Iterating Printer");
    controller.apply(new NewNodeOperation(clazz, library));

    Method method = new Method("Print 1 through 10");
    controller.apply(new NewNodeOperation(method, clazz));

    Parameter consoleDep = new Parameter("console");
    controller.apply(new NewNodeOperation(consoleDep, method));
    
    IdentifierDeclaration b = new IdentifierDeclaration("count");
    controller.applyAll(asList(
        new NewNodeOperation(b, method),
        new NewEdgeOperation(b, TYPEOF, stdLib.booleanClazz)));
    IntegerLiteral initValue = new IntegerLiteral(0);
    controller.apply(new NewNodeOperation(initValue, b));

    Loop loop = new Loop();
    controller.apply(new NewNodeOperation(loop, method));

    IntegerLiteral ten = new IntegerLiteral(10);
    controller.apply(new NewNodeOperation(ten, method));

    Expression terminateWhen = new MethodInvocation();
    controller.applyAll(asList(
        new NewNodeOperation(terminateWhen, loop),
        new NewEdgeOperation(terminateWhen, TARGET, b),
        new NewEdgeOperation(terminateWhen, INVOKE, stdLib.integerEquals),
        new NewEdgeOperation(terminateWhen, ARG, ten)
    ));

    Block body = new AnonymousBlock();
    controller.apply(new NewNodeOperation(body, loop));

    Expression printValue = new MethodInvocation();
    controller.applyAll(asList(
        new NewNodeOperation(printValue, body),
        new NewEdgeOperation(printValue, TARGET, consoleDep),
        new NewEdgeOperation(printValue, INVOKE, stdLib.printMethod),
        new NewEdgeOperation(printValue, ARG, b)
    ));
  }
}
