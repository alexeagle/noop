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

package noop.operations;

import com.google.common.base.Supplier;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import noop.model.LanguageElement;

import java.util.Collection;
import java.util.List;

import static noop.graph.Edge.EdgeType;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class NewNodeOperation implements MutationOperation {
  public final LanguageElement newElement;
  public final LanguageElement container;
  public final Multimap<EdgeType, LanguageElement> edges = Multimaps.newListMultimap(
      Maps.<EdgeType, Collection<LanguageElement>>newHashMap(), new Supplier<List<LanguageElement>>() {
    public List<LanguageElement> get() {
      return Lists.newArrayList();
    }
  });

  public NewNodeOperation(LanguageElement newElement, LanguageElement container) {
    this.newElement = newElement;
    this.container = container;
  }

  public NewNodeOperation(LanguageElement newElement, LanguageElement container,
                          EdgeType edgeType, LanguageElement dest) {
    this(newElement, container);
    edges.put(edgeType, dest);
  }

  public NewNodeOperation(LanguageElement newElement, LanguageElement container,
                          EdgeType edgeType, LanguageElement dest,
                          EdgeType edge2Type, LanguageElement dest2) {
    this(newElement, container, edgeType, dest);
    edges.put(edge2Type, dest2);
  }
}
