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

import com.google.common.collect.Lists;
import noop.graph.ModelVisitor;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class Block extends LanguageElement<Block> {
  public final String name;
  public final Clazz returnType;
  public final List<Parameter> parameters = Lists.newArrayList();
  private final List<Expression> statements = Lists.newArrayList();
  public final boolean test;
  public final boolean instance;

  private Block(String name, Clazz returnType, boolean isTest, boolean instance) {
    this.name = name;
    this.returnType = returnType;
    this.test = isTest;
    this.instance = instance;
  }

  @Override
  public boolean adoptChild(LanguageElement child) {
    if (child instanceof Parameter) {
      parameters.add((Parameter) child);
      return true;
    }
    if (child instanceof Expression) {
      statements.add((Expression) child);
      return true;
    }
    return super.adoptChild(child);
  }

  public static Block unitTest(String name) {
    return new Block(name, null, true, false);
  }

  public static Block function(String name, Clazz returnType) {
    return new Block(name, returnType, false, false);
  }

  public static Block method(String name, Clazz returnType) {
    // TODO: should hold a reference to the instance clazz?
    return new Block(name, returnType, false, true);
  }

  @Override
  public void accept(ModelVisitor v) {
    v.visit(this);
    for (Parameter parameter : parameters) {
      v.enter(parameter);
      parameter.accept(v);
      v.leave(parameter);
    }
    for (Expression statement : statements) {
      v.enter(statement);
      statement.accept(v);
      v.leave(statement);
    }
  }

  @Override
  public String toString() {
    return "Block " + name;
  }

  public boolean isFunction() {
    return !instance;
  }

  public boolean isTest() {
    return test;
  }
}
