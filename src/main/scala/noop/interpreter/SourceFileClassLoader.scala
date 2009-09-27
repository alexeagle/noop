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

import grammar.{ParseException, Parser}
import scala.collection.mutable.Map;

import java.io.{FileInputStream, File};

import noop.model.ClassDefinition;

/**
 * @author jeremiele@google.com (Jeremie Lenfant-Engelmann)
 */
class SourceFileClassLoader(parser: Parser, srcPaths: List[String]) extends ClassLoader with ClassSearch {

  val cache = Map.empty[String, ClassDefinition];

  def getClassDefinition(file: File): ClassDefinition = {
    try {
      return parser.file(new FileInputStream(file)).classDef;
    } catch {
      case ex: ParseException =>
          throw new ParseException("Failed to parse " + file.getAbsolutePath());
    }
  }

  def findClass(className: String): ClassDefinition = {
    val parts = className.split("\\.");
    val expectedFile = parts.last + ".noop";
    val relativePath = parts.take(parts.size - 1).mkString(File.separator);

    if (cache.contains(className)) {
      return cache(className);
    }
    for (path <- srcPaths) {
      val dir = new File(path, relativePath);
      if (!dir.isDirectory()) {
        throw new RuntimeException("Wrong srcPath given: " + dir + " is not a directory");
      }
      val files = dir.listFiles();

      files.find(f => f.getName() == expectedFile) match {
        case Some(file) => return getClassDefinition(file);
        case None => // will try in next directory
      }
    }
    throw new ClassNotFoundException("Could not find class: " + className);
  }

  def addNativeClass(name: String, classDef: ClassDefinition) = {
    cache += Pair(name, classDef);
  }

  def eachClass(f: ClassDefinition => Unit) = {
    for (path <- srcPaths) {
      var srcRoot = new File(path);
      eachClassInPath(srcRoot, f);
    }
  }

  def eachClassInPath(dir: File, f: ClassDefinition => Unit): Unit = {
    for(file <- dir.listFiles()) {
      if (file.isDirectory()) {
        eachClassInPath(file, f);
      } else {
        f.apply(getClassDefinition(file));
      }
    }
  }
}
