/*
 * Copyright 2010 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package noop.graph;

import noop.model.*;

import java.io.PrintStream;

import static com.google.common.base.Join.join;
import static java.lang.System.identityHashCode;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class DotGraphPrintingVisitor extends PrintingVisitor {
  private final PrintStream out;

  public DotGraphPrintingVisitor(PrintStream out) {
    this.out = out;
  }

  @Override
  public void visit(Workspace workspace) {
    this.workspace = workspace;
    out.format("digraph workspace\n{\n");
    out.format("workspace [label=\"%s\" %s]\n", "Workspace", "shape=house");
    for (Project project : workspace.getProjects()) {
      out.format("workspace -> %s\n", identityHashCode(project));
    }
  }

  private void print(LanguageElement element, String label, String... additional) {
    String attrs = join(",", additional);
    if (additional.length > 0) {
      attrs = ", " + attrs;
    }
    out.format("%s [label=\"%s\"%s]\n", element.vertex.hashCode(), label, attrs);
    for (Edge edge : workspace.edgesFrom(element.vertex)) {
      out.format("%s -> %s ", edge.src.hashCode(), edge.dest.hashCode());
      out.println("[label=\"" + edge.type.name().toLowerCase() + "\", style=dashed]");
    }
    if (getParent().vertex != Vertex.NONE) {
      out.format("%s -> %s\n", getParent().vertex.hashCode(), element.vertex.hashCode());
    }
  }

  @Override
  public void visit(Project project) {
    out.format("%s [label=\"%s\"%s]\n", identityHashCode(project),
        String.format("%s -> %s", project.getNamespace(), project.getName()), "shape=box");
    for (Library library : project.getLibraries()) {
      out.format("%s -> %s\n", identityHashCode(project), library.vertex.hashCode());
    }
    out.format("%s [label=\"%s\", shape=none]\n", identityHashCode(project.getCopyright()),
        escape(project.getCopyright()));
    out.format("%s -> %s\n", identityHashCode(project), identityHashCode(project.getCopyright()));
  }

  @Override
  public void visit(Library library) {
    print(library, library.name, "shape=hexagon");

  }

  @Override
  public void visit(Method method) {
    print(method, method.name + "{}");
  }

  @Override
  public void visit(Function function) {
    print(function, function.name + "{}");
  }

  @Override
  public void visit(UnitTest unitTest) {
    print(unitTest, "test: " + unitTest.name);
  }

  @Override
  public void visit(Assignment assignment) {
    print(assignment, "assign");
  }

  @Override
  public void visit(IdentifierDeclaration identifierDeclaration) {
    print(identifierDeclaration, identifierDeclaration.name);
  }

  @Override
  public void visit(Loop loop) {
    print(loop, loop.toString());
  }

  @Override
  public void visit(AnonymousBlock block) {
    print(block, "{}");
  }

  @Override
  public void visit(Return aReturn) {
    print(aReturn, "[return]");
  }

  @Override
  public void visit(IntegerLiteral integerLiteral) {
    print(integerLiteral, String.valueOf(integerLiteral.value));
  }

  @Override
  public void visit(Parameter parameter) {
    print(parameter, parameter.name);
  }

  @Override
  public void visit(MethodInvocation methodInvocation) {
    print(methodInvocation, "[invoke]");
  }

  @Override
  public void visit(Documentation documentation) {
    if (documentation != Documentation.NONE) {
      out.format("%s [label=\"%s\"%s]\n", identityHashCode(documentation), escape(documentation.summary), "shape=none");
    }
  }

  protected String escape(String value) {
    String escaped = super.escape(value);
    if (escaped.length() > 15) {
      escaped = escaped.substring(0, 12) + "...";
    }
    return escaped;
  }

  @Override
  public void visit(StringLiteral stringLiteral) {
    print(stringLiteral, "\\\"" + stringLiteral.value + "\\\"");
  }

  @Override
  public void visit(Clazz clazz) {
    print(clazz, clazz.name);
  }

  @Override
  public void leave(LanguageElement element) {
    super.leave(element);
    if (currentDepth == 0) {
      out.println("}");
    }
  }
}
