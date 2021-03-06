/**
 * Copyright 2009 Google Inc.
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

/**
 * An integer literal, like 4.
 * @author alexeagle@google.com (Alex Eagle)
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
class IntLiteralExpression(val value: Int) extends Expression {

  override def accept(visitor: Visitor) = {
    visitor.visit(this);
  }

  override def hashCode() = value.hashCode;
  override def equals(other: Any) = other match {
    case that: IntLiteralExpression => that.value.equals(value);
    case _ => false;
  }
}
