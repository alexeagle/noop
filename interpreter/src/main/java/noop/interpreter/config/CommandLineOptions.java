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

package noop.interpreter.config;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class CommandLineOptions implements InterpreterOptions {

  @Argument(usage = "Entry point of the application to execute", metaVar = "entryPoint", required = true)
  public String entryPoint;

  public static CommandLineOptions fromCmdLineArgs(String[] args) throws CmdLineException {
    CommandLineOptions options = new CommandLineOptions();
    CmdLineParser parser = new CmdLineParser(options);
    try {
      parser.parseArgument(args);
    } catch (CmdLineException e) {
      System.err.println("Invalid command line: " + e.getMessage());
      parser.printUsage(System.err);
      throw e;
    }
    return options;
  }

}
