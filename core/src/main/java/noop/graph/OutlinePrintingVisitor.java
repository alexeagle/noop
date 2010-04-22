package noop.graph;

import noop.model.*;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.google.common.collect.Iterables.filter;
import static noop.graph.Edge.notContain;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class OutlinePrintingVisitor extends PrintingVisitor {
  private final PrintStream out;

  public OutlinePrintingVisitor(PrintStream out) {
    this.out = out;
  }

  @Override
  public void visit(Workspace workspace) {
    this.workspace = workspace;
    print(workspace, "Contents of workspace %s at %s",
        System.getProperty("user.name"), new SimpleDateFormat().format(new Date()));
  }

  @Override
  public void visit(Project project) {
    print(project, "Project \"%s\"->\"%s\" (copyright: \"%s\")",
        project.getNamespace(), project.getName(), escape(project.getCopyright()));
  }

  @Override
  public void visit(Library library) {
    print(library, "Library \"%s\"", library.name);
  }

  @Override
  public void visit(Clazz clazz) {
    print(clazz, "Class \"%s\"", clazz.name);
  }

  @Override
  public void visit(Method method) {
    print(method, "Method %s{}", method.name);
  }

  @Override
  public void visit(Function function) {
    print(function, "Function %s{}", function.name);
  }

  @Override
  public void visit(UnitTest unitTest) {
    print(unitTest, "Unit test %s", unitTest.name);
  }

  @Override
  public void visit(MethodInvocation methodInvocation) {
    print(methodInvocation, "invocation");
  }

  @Override
  public void visit(Parameter parameter) {
    print(parameter, "parameter %s", parameter.name);
  }

  @Override
  public void visit(Documentation documentation) {
    print(documentation, "Documentation: %s", documentation.summary);
  }

  @Override
  public void visit(IdentifierDeclaration identifierDeclaration) {
    print(identifierDeclaration, "Declare %s", identifierDeclaration.name);
  }

  @Override
  public void visit(Loop loop) {
    print(loop, "Loop until");
  }

  @Override
  public void visit(AnonymousBlock block) {
    print(block, "{}");
  }

  @Override
  public void visit(Assignment assignment) {
    print(assignment, "Assign");
  }

  @Override
  public void visit(IntegerLiteral integerLiteral) {
    print(integerLiteral, "literal %s", String.valueOf(integerLiteral.value));
  }

  @Override
  public void visit(StringLiteral stringLiteral) {
    print(stringLiteral, "literal \"%s\"", stringLiteral.value);
  }

  @Override
  public void visit(Return aReturn) {
    print(aReturn, "return");
  }

  @Override
  public void visit(Comment comment) {
    print(comment, "// %s --%s %s", comment.text, comment.user, comment.timestamp.toString());
  }

  private void print(LanguageElement element, String message, String... params) {
    out.format("%s%s [#%d]", indent(), String.format(message, params), idFor(element));
    for (Edge edge : filter(workspace.edgesFrom(idFor(element)), notContain())) {
      out.format(" %s -> #%d", edge.type.name(), edge.dest);
    }
    out.println();
  }

  private String indent() {
    StringBuilder builder = new StringBuilder((currentDepth - 1) * 2);
    for (int i = 0; i < currentDepth - 1; i++) {
      builder.append("  ");
    }
    return builder.toString();
  }


}
