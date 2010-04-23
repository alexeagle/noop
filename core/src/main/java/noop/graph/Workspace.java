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

import com.google.common.base.Nullable;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import noop.model.LanguageElement;
import noop.model.Project;

import java.util.List;
import java.util.Set;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class Workspace extends LanguageElement<Workspace> {
  public final Set<Edge> edges = Sets.newHashSet();
  public final List<LanguageElement> elements = Lists.<LanguageElement>newArrayList(this);
  private List<Project> projects = Lists.newArrayList();
  private List<LanguageElement> orphans = Lists.newArrayList();

  public void addProject(Project project) {
    projects.add(project);
  }

  public void addOrphan(LanguageElement orphan) {
    orphans.add(orphan);
  }

  @Override
  public void accept(ModelVisitor v) {
    v.enter(this);
    v.visit(this);
    for (Project project : projects) {
      v.enter(project);
      project.accept(v);
      v.leave(project);
    }
    for (Edge edge : edges) {
      edge.accept(v);
    }
    v.leave(this);
  }

  public Iterable<Edge> edgesFrom(final int id) {
    return Iterables.filter(edges, new Predicate<Edge>() {
      @Override
      public boolean apply(@Nullable Edge input) {
        return input.src == id;
      }
    });
  }
}
