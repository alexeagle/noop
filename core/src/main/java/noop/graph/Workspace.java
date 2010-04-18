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

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import noop.model.LanguageElement;

import java.util.List;
import java.util.Set;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class Workspace extends LanguageElement<Workspace> {
  public final Set<Edge> edges = Sets.newHashSet();
  public final List<LanguageElement> elements = Lists.<LanguageElement>newArrayList(this);

  @Override
  public void accept(ModelVisitor v) {
    v.visit(this);
    for (LanguageElement element : elements) {
      if (element != this) {
        element.accept(v);
      }
    }
    for (Edge edge : edges) {
      edge.accept(v);
    }
    v.leave(this);
  }
}
