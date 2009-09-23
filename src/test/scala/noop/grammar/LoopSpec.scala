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

import model.WhileLoop
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class LoopSpec extends Spec with ShouldMatchers {
  val parser = new Parser();

  describe("a while loop") {
    it("should be formed with a while statement") {
      val source = "{ while(true) {} }";
      parser.parseBlock(source).toStringTree() should be ("(WHILE true)");
      val block = parser.buildTreeParser(parser.parseBlock(source)).block();
      block.statements(0).getClass() should be (classOf[WhileLoop]);
      val whileLoop = block.statements(0).asInstanceOf[WhileLoop];
    }
  }
}