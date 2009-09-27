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

package noop.types

import collection.mutable.Stack
import grammar.Parser
import interpreter.{Frame, Context, SourceFileClassLoader}
import java.io.File
import model.Modifier
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class StringSpec extends Spec with ShouldMatchers {
  def createFixture = {
    val stdlibSourcePath = new File(getClass().getResource("/stdlib").toURI).getAbsolutePath();
    new SourceFileClassLoader(new Parser(), List(stdlibSourcePath))
  }

  describe("a Noop String") {
    it("should have a valid class definition parsed from Noop source") {
      val classLoader = createFixture;
      val classDef = classLoader.findClass("String");
      classDef.name should be("String");
    }

    it("should have a native implementation of the length method") {
      val classLoader = createFixture;
      val stringClass = classLoader.findClass("String");
      val aString = new NoopString(stringClass, Map.empty[String, NoopObject], "hello");
      val method = stringClass.findMethod("length");
      method.modifiers should contain(Modifier.native);
      val stack = new Stack[Frame]();
      stack.push(new Frame(aString, null));
      val context = new Context(stack, classLoader);

      method.execute(context) match {
        case Some(i) => i.asInstanceOf[NoopInteger].value should be(5);
        case None => fail();
      }
    }
  }
}