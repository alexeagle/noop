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
package noop.to.java;

import org.antlr.stringtemplate.CommonGroupLoader;
import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.antlr.tool.ErrorManager;

import org.scalatest.matchers.ShouldMatchers;
import org.scalatest.Spec;

/**
 * @author rdionne@google.com (Robert Dionne)
 *
 * TODO(rdionne): Write the code generation classes that use noop.model.* and test those,
 *     not String Template.
 */
class JavaSpec extends Spec with ShouldMatchers {

  StringTemplateGroup.registerGroupLoader(
      new CommonGroupLoader(
          "noop/to/java",
          ErrorManager.getStringTemplateErrorListener()));
  val group = StringTemplateGroup.loadGroup("Java");

  describe("A file template") {
    val file = group.getInstanceOf("file");

    it("should evaluate correctly") {
      file.setAttribute("namespace", "com.google");
      file.setAttribute("imports", "org.json.JSONObject");
      file.setAttribute("class", "class Empty {}");

      file.toString() should be ("package com.google;\n\nimport org.json.JSONObject;\n\nclass Empty {}");
    }
  }

  describe("A class template") {
    val clazz = group.getInstanceOf("class");

    it("should evaluate correctly") {
      clazz.setAttribute("documentation", "/**\n * @author\n */");
      clazz.setAttribute("modifiers", "public final");
      clazz.setAttribute("name", "MyClass");
      clazz.setAttribute("interfaces", "List");
      clazz.setAttribute("methods", "void add(int a, int b) {}");

      clazz.toString() should be ("/**\n * @author\n */\npublic final class MyClass implements List {\n  void add(int a, int b) {}\n}");
    }
  }
}
