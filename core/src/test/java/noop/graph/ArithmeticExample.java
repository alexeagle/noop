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
public class ArithmeticExample extends Example {

  public ArithmeticExample(StandardLibraryBuilder stdLib) {
    super(stdLib);
  }

  @Override
  public void createProgram(Controller controller) {
    Project project = new Project("Arithmetic", "com.example", "Copyright 2010\nExample Co.");
    controller.apply(new NewNodeOperation(project));

    Library library = new Library("adding stuff");
    controller.apply(new NewNodeOperation(library, project));

    Parameter consoleDep = new Parameter("console");

    Block entryPoint = new Function("start here");
    controller.applyAll(asList(
        new NewNodeOperation(entryPoint, library),
        new NewEdgeOperation(entryPoint, TYPEOF, stdLib.intClazz),
        new NewNodeOperation(consoleDep, entryPoint),
        new NewEdgeOperation(consoleDep, TYPEOF, stdLib.consoleClazz)));

    IdentifierDeclaration i = new IdentifierDeclaration("i");
    controller.apply(new NewNodeOperation(i, entryPoint));

    IntegerLiteral one = new IntegerLiteral(1);
    controller.apply(new NewNodeOperation(one, i));

    IdentifierDeclaration j = new IdentifierDeclaration("j");
    controller.apply(new NewNodeOperation(j, entryPoint));

    IntegerLiteral two = new IntegerLiteral(2);
    controller.apply(new NewNodeOperation(two, j));

    IdentifierDeclaration k = new IdentifierDeclaration("k");
    controller.apply(new NewNodeOperation(k, entryPoint));

    Expression sum = new MethodInvocation();
    controller.applyAll(asList(
        new NewNodeOperation(sum, k),
        new NewEdgeOperation(sum, INVOKE, stdLib.integerPlus),
        new NewEdgeOperation(sum, TARGET, i),
        new NewEdgeOperation(sum, ARG, j)
    ));

    Expression printResult = new MethodInvocation();
    controller.applyAll(asList(
        new NewNodeOperation(printResult, entryPoint),
        new NewEdgeOperation(printResult, INVOKE, stdLib.printMethod),
        new NewEdgeOperation(printResult, TARGET, consoleDep),
        new NewEdgeOperation(printResult, ARG, k)
    ));

    IntegerLiteral zero = new IntegerLiteral(0);
    controller.apply(new NewNodeOperation(zero, entryPoint));

    Expression returnVal = new Return();
    controller.applyAll(asList(new NewNodeOperation(returnVal, entryPoint),
        new NewEdgeOperation(returnVal, ARG, zero)));
  }
}
