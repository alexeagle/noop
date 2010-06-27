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

package noop.interpreter;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class InterpreterSystemTest {
  @Before public void setUp() {
    InterpreterMain.disableSystemExitForTesting();
  }

  @Test public void shouldRunTheHelloWorldProgram() throws Exception {
    InterpreterMain.main(new String[] {
        // TODO: fix absolute paths on my machine!
        "-lib", "/Users/alexeagle/IdeaProjects/noop/dumps/com.google.noop/Noop/io.xml",
        "-lib", "/Users/alexeagle/IdeaProjects/noop/dumps/com.google.noop/Noop/lang.xml",
        "-lib", "/Users/alexeagle/IdeaProjects/noop/dumps/com.example/Hello World/hello.xml",
        "-v",
        "dcf83cb3-9457-4695-b4b6-94389fef5a5b", "1"
        });
    assertEquals(0, InterpreterMain.exitCodeForTesting);
  }
}
