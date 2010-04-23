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

import noop.model.LanguageElement;
import noop.operations.*;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class Controller {
  private Workspace workspace;
  private final ModelVisitor addVertices;

  public Controller(Workspace workspace, ModelVisitor addVertices) {
    this.workspace = workspace;
    this.addVertices = addVertices;
  }

  public void addNode(NewNodeOperation operation) {
    int nextNodeId = workspace.elements.size();
    LanguageElement container = operation.container == null ? workspace : operation.container;

    int destId = workspace.elements.indexOf(container);
    if (destId < 0) {
      throw new IllegalStateException(String.format("Cannot add edge [%s -> %s] due to non-existant dest %s",
          nextNodeId, destId, container));
    }
    workspace.elements.add(operation.newElement);
  }

  public void editNode(EditNodeOperation operation) {
    LanguageElement currentValue = workspace.elements.get(operation.id);
    if (currentValue.getClass() != operation.newValue.getClass()) {
      throw new IllegalArgumentException(String.format("Cannot edit node %d with %s because the current type is %s",
          operation.id, operation.newValue, currentValue.getClass()));
    }

    operation.newValue.setPreviousVersion(currentValue);
    workspace.elements.set(operation.id, operation.newValue);
  }

  public void addEdge(NewEdgeOperation operation) {
    if (operation.src.vertex == Vertex.NONE) {
      throw new IllegalArgumentException("src element has no vertex");
    }
    if (operation.dest.vertex == Vertex.NONE) {
      throw new IllegalArgumentException("dest element has no vertex");
    }
    workspace.edges.add(new Edge(operation.src.vertex, operation.type, operation.dest.vertex));
  }

  public void addProject(NewProjectOperation operation) {
    workspace.addProject(operation.project);
    operation.project.accept(addVertices);
  }

  public void apply(MutationOperation operation) {
    operation.execute(this);    
  }

  public void applyAll(Iterable<? extends MutationOperation> operations) {
    for (MutationOperation operation : operations) {
      apply(operation);
    }
  }
}
