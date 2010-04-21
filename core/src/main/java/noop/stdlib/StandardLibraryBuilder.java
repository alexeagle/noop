package noop.stdlib;

import com.google.common.collect.Lists;
import noop.model.*;
import noop.operations.MutationOperation;
import noop.operations.NewEdgeOperation;
import noop.operations.NewNodeOperation;

import java.util.List;

import static noop.graph.Edge.EdgeType.TYPEOF;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class StandardLibraryBuilder {
  public Clazz intClazz;
  public Clazz consoleClazz;
  public Clazz stringClazz;
  public Clazz voidClazz;
  public Block printMethod;
  public Method integerPlus;

  public List<MutationOperation> build() {
    List<MutationOperation> result = Lists.newArrayList();

    Project project = new Project("Noop", "com.google.noop", "Apache 2");
    result.add(new NewNodeOperation(project));

    Library lang = new Library("lang");
    result.add(new NewNodeOperation(lang, project));

    stringClazz = new Clazz("String");
    result.add(new NewNodeOperation(stringClazz, lang));

    voidClazz = new Clazz("Void");
    result.add(new NewNodeOperation(voidClazz, lang));

    Library io = new Library("io");
    result.add(new NewNodeOperation(io, project));

    consoleClazz = new Clazz("Console");
    result.add(new NewNodeOperation(consoleClazz, io));

    printMethod = new Method("print");
    result.add(new NewNodeOperation(printMethod, consoleClazz));
    result.add(new NewEdgeOperation(printMethod, TYPEOF, voidClazz));

    Parameter printArg = new Parameter("s");
    result.add(new NewNodeOperation(printArg, printMethod));
    result.add(new NewEdgeOperation(printArg, TYPEOF, stringClazz));

    intClazz = new Clazz("Integer");
    result.add(new NewNodeOperation(intClazz, lang));

    integerPlus = new Method("+");
    result.add(new NewNodeOperation(integerPlus, intClazz));
    result.add(new NewEdgeOperation(integerPlus, TYPEOF, intClazz));

    Parameter integerPlusArg = new Parameter("i");
    result.add(new NewNodeOperation(integerPlusArg, integerPlus));
    result.add(new NewEdgeOperation(integerPlusArg, TYPEOF, intClazz));

    return result;
  }
}
