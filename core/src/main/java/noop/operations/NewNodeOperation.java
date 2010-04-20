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

import noop.model.LanguageElement;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class NewNodeOperation implements MutationOperation {
  public final LanguageElement newElement;
  public final LanguageElement container;

  /**
   * Create a new node with the given parent
   * @param newElement any language element
   * @param container the containing element
   */
  public NewNodeOperation(LanguageElement newElement, LanguageElement container) {
    this.newElement = newElement;
    this.container = container;
  }

  /**
   * Create a new node whose parent is the workspace.
   * This may create orphaned elements, but is appropriate for projects
   * @param newElement the element
   */
  public NewNodeOperation(LanguageElement newElement) {
    this(newElement, null);
  }
}
