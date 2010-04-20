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

import com.google.common.collect.Sets;
import noop.graph.ModelVisitor;

import java.io.Serializable;
import java.util.Set;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public abstract class LanguageElement<T> implements Serializable {
  protected Documentation documentation;
  protected Set<UnitTest> unitTests = Sets.newHashSet();
  protected T previousVersion;

  public void accept(ModelVisitor v) {
    if (documentation != null) {
      v.enter(documentation);
      documentation.accept(v);
      v.leave(documentation);
    }
  }

  public T getPreviousVersion() {
    return previousVersion;
  }
  public void setPreviousVersion(T previousVersion) {
    this.previousVersion = previousVersion;
  }
  public boolean adoptChild(LanguageElement child) {
    if (child instanceof Documentation) {
      this.documentation = (Documentation) child;
      return true;
    }
    if (child instanceof UnitTest) {
      UnitTest block = (UnitTest) child;
      unitTests.add(block);
      return true;
    }
    return false;
  }
}
