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

import noop.graph.Edge.EdgeType;
import noop.model.LanguageElement;
import noop.operations.EditNodeOperation;
import noop.operations.MutationOperation;
import noop.operations.NewEdgeOperation;
import noop.operations.NewNodeOperation;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class Controller {
  private Workspace workspace;

  public Controller(Workspace workspace) {
    this.workspace = workspace;
  }

  public void apply(NewNodeOperation operation) {
    int nextNodeId = workspace.elements.size();
    LanguageElement container = operation.container == null ? workspace : operation.container;

//    if (!container.adoptChild(operation.newElement)) {
//      throw new IllegalArgumentException("Element " + operation.newElement
//          + " not allowed as child of " + container);
//    }
    int destId = workspace.elements.indexOf(container);
    if (destId < 0) {
      throw new IllegalStateException(String.format("Cannot add edge [%s -> %s] due to non-existant dest %s",
          nextNodeId, destId, container));
    }
    Edge newEdge = new Edge(destId, EdgeType.CONTAIN, nextNodeId);
    workspace.edges.add(newEdge);
    workspace.elements.add(operation.newElement);
  }

  public void apply(EditNodeOperation operation) {
    LanguageElement currentValue = workspace.elements.get(operation.id);
    if (currentValue.getClass() != operation.newValue.getClass()) {
      throw new IllegalArgumentException(String.format("Cannot edit node %d with %s because the current type is %s",
          operation.id, operation.newValue, currentValue.getClass()));
    }

    operation.newValue.setPreviousVersion(currentValue);
    workspace.elements.set(operation.id, operation.newValue);
  }

  public void apply(NewEdgeOperation operation) {
    workspace.edges.add(new Edge(workspace.elements.indexOf(operation.src),
        operation.type, workspace.elements.indexOf(operation.dest)));
  }

  public void applyAll(Iterable<? extends MutationOperation> operations) {
    for (MutationOperation operation : operations) {
      apply(operation);
    }
  }

  private void apply(MutationOperation operation) {
    if (operation instanceof NewNodeOperation) {
      apply((NewNodeOperation)operation);
    } else if (operation instanceof EditNodeOperation) {
      apply((EditNodeOperation)operation);
    } else if (operation instanceof NewEdgeOperation) {
      apply((NewEdgeOperation)operation);
    }
  }
}
