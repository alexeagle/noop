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

package noop.model

import interpreter.Context
import types.{NoopBoolean, NoopObject}
/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class WhileLoop(val continueCondition: Expression, val body: Block) extends Expression {
  def evaluate(c: Context): Option[NoopObject] = {
    while (shouldContinue(c)) {
      body.evaluate(c);
    }
    return None;
  }

  def shouldContinue(c: Context): Boolean = {
    continueCondition.evaluate(c) match {
      case Some(result) => result.asInstanceOf[NoopBoolean].value;
      case None => false;
    };
  }
}