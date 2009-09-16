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

import interpreter.Context
import types.{NoopObject, NoopType}


/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class IdentifierDeclarationExpression(val noopType: String, val name: String) extends Expression {
  var initialValue: Option[Expression] = None;

  def evaluate(c: Context): Option[NoopObject] = {
    val frame = c.stack.top;

    val obj = initialValue match {
      case Some(v) => v.evaluate(c) match {
        case Some(e) => e;
        case None => throw new RuntimeException("The right handside didn't evaluate to a proper value");
      }
      case None => null;
    }
    frame.addIdentifier(name,  new Tuple2[NoopType, NoopObject](null, obj));
    return None;
  }
}
