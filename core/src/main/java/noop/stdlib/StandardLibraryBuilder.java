package noop.stdlib;

import com.google.common.collect.Lists;
import noop.model.*;
import noop.operations.MutationOperation;
import noop.operations.NewEdgeOperation;
import noop.operations.NewNodeOperation;

import java.util.List;

import static noop.graph.Edge.EdgeType.TYPEOF;

/**
 * TODO: when we have a way to share serialized noop code, remove this class
 * @author alexeagle@google.com (Alex Eagle)
 */
public class StandardLibraryBuilder {
  public Clazz intClazz;
  public Clazz consoleClazz;
  public Clazz stringClazz;
  public Clazz voidClazz;
  public Clazz booleanClazz;
  public Method printMethod;
  public Method integerPlus;
  public Method integerEquals;

  public List<MutationOperation> build() {
    List<MutationOperation> result = Lists.newArrayList();

    Project project = new Project("Noop", "com.google.noop", "Apache 2");
    result.add(new NewNodeOperation(project));

    Library lang = new Library("lang");
    project.addLibrary(lang);

    stringClazz = new Clazz("String");
    lang.addClazz(stringClazz);

    voidClazz = new Clazz("Void");
    lang.addClazz(voidClazz);

    Library io = new Library("io");
    project.addLibrary(io);

    consoleClazz = new Clazz("Console");
    io.addClazz(consoleClazz);

    printMethod = new Method("print");
    consoleClazz.addBlock(printMethod);
    result.add(new NewEdgeOperation(printMethod, TYPEOF, voidClazz));

    Parameter printArg = new Parameter("s");
    printMethod.addParameter(printArg);
    result.add(new NewEdgeOperation(printArg, TYPEOF, stringClazz));

    booleanClazz = new Clazz("Boolean");
    lang.addClazz(booleanClazz);

    intClazz = new Clazz("Integer");
    lang.addClazz(intClazz);

    integerPlus = new Method("+");
    intClazz.addBlock(integerPlus);
    intClazz.addComment(new Comment("Elements may have symbols in their names." +
        " Tools may choose to render this as infix",
        System.getProperty("user.name")));
    result.add(new NewEdgeOperation(integerPlus, TYPEOF, intClazz));

    integerEquals = new Method("==");
    intClazz.addBlock(integerEquals);
    result.add(new NewEdgeOperation(integerEquals, TYPEOF, booleanClazz));

    Parameter integerPlusArg = new Parameter("i");
    integerPlus.addParameter(integerPlusArg);
    result.add(new NewEdgeOperation(integerPlusArg, TYPEOF, intClazz));

    return result;
  }
}
