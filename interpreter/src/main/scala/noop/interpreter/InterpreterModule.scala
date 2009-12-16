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

import com.google.protobuf.TextFormat
import noop.stdlib.StdLibModuleBuilder;
import noop.model.{Visitor, CompositeVisitor, LoggingAstVisitor}
import noop.inject.{Injector, GuiceBackedInjector}
import noop.model.proto.Noop.Library
import java.io.InputStreamReader
import com.google.inject._;


import scala.collection.mutable;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class InterpreterModule(srcRoots: Seq[String]) extends AbstractModule {
  override def configure() = {
    bind(classOf[ClassSearch]).to(classOf[RepositoryClassLoader]);
    bind(classOf[ClassLoader]).to(classOf[RepositoryClassLoader]);
  }

  @Provides @Singleton def getClassRepository(): ClassRepository = {
    new ClassRepository(List(new StdLibModuleBuilder().build));
  }

  @Provides def getInjector(classLoader: ClassLoader): Injector =
      new GuiceBackedInjector(classLoader, Guice.createInjector());

  @Provides @Singleton def getContext(classLoader: ClassLoader) =
      new Context(new mutable.Stack[Frame], classLoader);

  /**
   * Chain a logging visitor with the one that interprets the code.
   */
  @Provides def getVisitor(context: Context, injector: Injector, interpreterVisitor: InterpreterVisitor): Visitor =
      new CompositeVisitor(List(new LoggingAstVisitor(), interpreterVisitor));
}