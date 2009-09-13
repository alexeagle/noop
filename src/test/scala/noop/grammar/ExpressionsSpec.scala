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

package noop.grammar

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

class ExpressionsSpec extends Spec with ShouldMatchers {
  val parser = new Parser();

  describe("the parser") {
    it("should parse a variable assignment") {
      val source = "{ Int a = 3; }";
      parser.parseBlock(source).toStringTree() should equal ("(VAR Int (= a 3))");
    }
  }
}
