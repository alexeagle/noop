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
package noop.inject

import noop.interpreter.InterpreterModule
import noop.types.{NoopObject, NoopConsole, NoopTypesModule}

import com.google.inject.Guice
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.Spec
import noop.model.{ConcreteClassDefinition, ClassDefinition}
import noop.model.proto.Noop.ConcreteClass

/**
 * @author alexeagle@google.com (Alex Eagle)
 */

class GuiceBackedInjectorSpec extends Spec with ShouldMatchers {
  def fixture = {
     val guiceInjector = Guice.createInjector(new NoopTypesModule(), new InterpreterModule(List()));
     guiceInjector.getInstance(classOf[Injector]);
  }
  describe("the injector") {
    it("should create a NoopConsole instance") {
      val noopInjector: Injector = fixture;
      val instance = noopInjector.getInstance(new ConcreteClassDefinition(
          ConcreteClass.newBuilder.setName("noop.Console").build));
      instance.getClass() should be(classOf[NoopConsole]);

    }
    
    it("should create an instance of a user-defined type") {
      val noopInjector: Injector = fixture;
      val userClass = new ConcreteClassDefinition(ConcreteClass.newBuilder
              .setName("A").setDocumentation("A class named A").build);
      val instance = noopInjector.getInstance(userClass);
      instance.getClass() should be(classOf[NoopObject]);
      instance.classDef should be(userClass);
    }
  }
}
