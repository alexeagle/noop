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

package noop.interpreter

import grammar.Parser
import java.io.{ByteArrayOutputStream, File}
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers

class InterpreterSpec extends Spec with ShouldMatchers {
  def createFixture = {
    val exampleSourcePath = new File(getClass().getResource("/helloworld").toURI).getAbsolutePath();
    new ClassLoader(new Parser(), List(exampleSourcePath));
  }

  describe("the interpreter") {
    it("should run a HelloWorld program") {
      val classLoader = createFixture;
      val output = new ByteArrayOutputStream();
      val originalOut = Console.out;
      try {
        Console.setOut(output);
        val mainClass = classLoader.findClass("HelloWorld");
        new Interpreter(classLoader).runApplication(mainClass);
        output.toString() should include("Hello World!");
      } finally {
        Console.setOut(originalOut);
      }
    }

    it("should register Console as a native type") {
      val classLoader = new MockClassLoader();
      val interpreter = new Interpreter(classLoader);
      interpreter.registerNativeTypes;
      classLoader.cache should contain key("Console");
    }
  }
}