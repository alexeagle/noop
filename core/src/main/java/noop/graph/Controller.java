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

import noop.graph.Edge;
import noop.graph.Edge.EdgeType;
import noop.model.Clazz;
import noop.model.LanguageElement;
import noop.graph.Workspace;
import noop.operations.EditNodeOperation;
import noop.operations.MutationOperation;
import noop.operations.NewNodeOperation;

import java.util.Map.Entry;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class Controller {
  private Workspace workspace;

  public Controller(Workspace workspace) {
    this.workspace = workspace;
  }

  public void apply(NewNodeOperation operation) {
    workspace.elements.add(operation.newElement);
    int newNodeId = workspace.elements.size() - 1;
    addEdge(newNodeId, operation.container, EdgeType.CONTAIN, true);
    for (Entry<EdgeType, LanguageElement> edgeTypeLanguageNodeEntry : operation.edges.entries()) {
      LanguageElement destElement = edgeTypeLanguageNodeEntry.getValue();
      EdgeType edgeType = edgeTypeLanguageNodeEntry.getKey();
      addEdge(newNodeId, destElement, edgeType, false);
    }
  }

  private void addEdge(int newNodeId, LanguageElement destElement, EdgeType edgeType, boolean backwards) {
    int destId = destElement == null ? 0 : workspace.elements.indexOf(destElement);
    if (destId < 0) {
      throw new IllegalStateException(String.format("Cannot add edge [%s -> %s] due to non-existant dest %s",
          newNodeId, destId, destElement));
    }
    Edge newEdge = backwards ? new Edge(destId, edgeType, newNodeId) : new Edge(newNodeId, edgeType, destId);
    workspace.edges.add(newEdge);
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
    }

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
}
