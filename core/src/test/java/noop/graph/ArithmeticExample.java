package noop.graph;

import noop.model.*;
import noop.operations.NewEdgeOperation;
import noop.operations.NewProjectOperation;
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
    controller.apply(new NewProjectOperation(project));

    Library library = new Library("adding stuff");
    project.addLibrary(library);

    Function entryPoint = new Function("start here");
    library.addFunction(entryPoint);
    Parameter consoleDep = new Parameter("console");
    entryPoint.addParameter(consoleDep);
    controller.applyAll(asList(
        new NewEdgeOperation(entryPoint, TYPEOF, stdLib.intClazz),
        new NewEdgeOperation(consoleDep, TYPEOF, stdLib.consoleClazz)));

    IdentifierDeclaration i = new IdentifierDeclaration("i");
    entryPoint.addStatement(i);

    IntegerLiteral one = new IntegerLiteral(1);
    i.setInitialValue(one);

    IdentifierDeclaration j = new IdentifierDeclaration("j");
    entryPoint.addStatement(j);

    IntegerLiteral two = new IntegerLiteral(2);
    entryPoint.addStatement(two);

    IdentifierDeclaration k = new IdentifierDeclaration("k");
    entryPoint.addStatement(k);

    Expression sum = new MethodInvocation();
    controller.applyAll(asList(
        new NewEdgeOperation(sum, INVOKE, stdLib.integerPlus),
        new NewEdgeOperation(sum, TARGET, i),
        new NewEdgeOperation(sum, ARG, j)
    ));
    k.setInitialValue(sum);

    Expression printResult = new MethodInvocation();
    entryPoint.addStatement(printResult);
    controller.applyAll(asList(
        new NewEdgeOperation(printResult, INVOKE, stdLib.printMethod),
        new NewEdgeOperation(printResult, TARGET, consoleDep),
        new NewEdgeOperation(printResult, ARG, k)
    ));

    IntegerLiteral zero = new IntegerLiteral(0);
    entryPoint.addStatement(zero);

    Expression returnVal = new Return();
    entryPoint.addStatement(returnVal);
    controller.apply(new NewEdgeOperation(returnVal, ARG, zero));
  }
}
