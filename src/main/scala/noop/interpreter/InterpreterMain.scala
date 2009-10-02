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
package noop.interpreter;

import grammar.Parser;

/**
 * The static entry point into the Noop interpreter, for use from the command-line.
 *
 * @author alexeagle@google.com (Alex Eagle)
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
object InterpreterMain {

  def main(args: Array[String]) {

    //TODO: a proper command line parser, like scalax.io.CommandLineParser
    if (args.size < 2) {
      println("Usage: InterpreterMain main-class paths/to/sources ...");
      System.exit(1);
    }

    //TODO: guice? other injector?
    val classLoader = new SourceFileClassLoader(new Parser(), args.toList.tail);
    val mainClass = classLoader.findClass(args(0));

    new Interpreter(classLoader).runApplication(mainClass);

    System.exit(0);
  };
}
