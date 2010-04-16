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

package noop.model;

import noop.graph.ModelVisitor;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class Block extends LanguageElement<Block> {
  public final String name;
  public final Clazz returnType;
  public final List<Parameter> parameters;

  public Block(String name, Clazz returnType, Parameter... parameters) {
    this.name = name;
    this.returnType = returnType;
    this.parameters = asList(parameters);
  }

  public Block() {
    this.name = null;
    this.returnType = null;
    this.parameters = emptyList();
  }

  @Override
  public void accept(ModelVisitor v) {
    v.visit(this);
  }

  public boolean isMethod() {
    return name != null;
  }
}
