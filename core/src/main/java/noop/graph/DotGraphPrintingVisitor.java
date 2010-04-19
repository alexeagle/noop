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
    out.format("%s [label=\"%s\", shape=house]\n", idFor(workspace), "Workspace");
  }

  @Override
  public void visit(Project project) {
    out.format("%s [label=\"%s (%s)\", shape=box]\n",
        idFor(project), project.getName(), project.getNamespace());

    out.format("%s [label=\"%s\", shape=none]\n", identityHashCode(project.getCopyright()),
        escape(project.getCopyright()));
    out.format("%s -> %s\n", idFor(project), identityHashCode(project.getCopyright()));
  }

  @Override
  public void visit(Library library) {
    out.format("%s [label=\"%s\", shape=hexagon]\n", idFor(library), library.name);
  }

  @Override
  public void visit(Block block) {
    out.format("%s [label=\"%s {}\"]\n", idFor(block), block.name);
    for (Parameter parameter : block.parameters) {
      out.format("%s -> %s [label=param, style=dotted]\n",
          idFor(block), idFor(parameter));
    }
    if (block.returnType != null) {
      out.format("%s -> %s [label=return, style=dotted]\n",
          idFor(block), idFor(block.returnType));
    }
  }

  @Override
  public void visit(IdentifierDeclaration identifierDeclaration) {
    out.format("%s [label=\"%s\"]\n", idFor(identifierDeclaration), identifierDeclaration.name);

  }

  @Override
  public void visit(Return aReturn) {
    out.format("%s [label=\"%s\"]\n", idFor(aReturn), "[return]");
    out.format("%s -> %s [label=arg, style=dotted]\n",
          idFor(aReturn), idFor(aReturn.returned));
  }

  @Override
  public void visit(IntegerLiteral integerLiteral) {
    out.format("%s [label=\"%s\"]\n", idFor(integerLiteral), integerLiteral.value);        
  }

  @Override
  public void visit(Parameter parameter) {
    out.format("%s [label=\"%s\"]\n", idFor(parameter), parameter.name);    
  }

  @Override
  public void visit(MethodInvocation methodInvocation) {
    out.format("%s [label=\"%s\"]\n", idFor(methodInvocation), "[invoke]");
    for (Expression argument : methodInvocation.arguments) {
      out.format("%s -> %s [label=arg, style=dotted]\n",
          idFor(methodInvocation), idFor(argument));
    }
  }

  @Override
  public void visit(Documentation documentation) {
    out.format("%s [label=\"%s\", shape=none]\n", idFor(documentation), escape(documentation.summary));
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
    out.format("%s [label=\"\\\"%s\\\"\"]\n", idFor(stringLiteral), stringLiteral.value);
  }

  @Override
  public void visit(Clazz clazz) {
    out.format("%s [label=\"%s\"]\n", idFor(clazz), clazz.name);
  }

  @Override
  public void leave(LanguageElement element) {
    super.leave(element);
    if (currentDepth == 0) {
      out.println("}");
    }
  }

  @Override
  public void visit(Edge edge) {
    out.format("%d -> %d ", edge.src, edge.dest);
    switch (edge.type) {
      case CONTAIN:
        out.print("\n");
        break;
      default:
        out.println("[label=\"" + edge.type.name().toLowerCase() + "\", style=dashed]");
        break;
    }
  }
}
