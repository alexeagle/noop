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

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public abstract class ModelVisitor {
  public void enter(LanguageElement element) {}
  
  public void leave(LanguageElement element) {}

  public void visit(Edge edge) {}

  public void visit(Workspace workspace) {}

  public void visit(Method method) {}

  public void visit(Function function) {}

  public void visit(UnitTest unitTest) {}

  public void visit(Project project) {}

  public void visit(MethodInvocation methodInvocation) {}

  public void visit(Parameter parameter) {}

  public void visit(Library library) {}

  public void visit(Clazz clazz) {}

  public void visit(StringLiteral stringLiteral) {}

  public void visit(Return aReturn) {}

  public void visit(IntegerLiteral integerLiteral) {}

  public void visit(Documentation documentation) {}

  public void visit(Assignment assignment) {}

  public void visit(IdentifierDeclaration identifierDeclaration) {}
}
