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

package noop.grammar;

import java.io.InputStream;
import noop.model.{File};
import noop.grammar.antlr.NoopAST;
import noop.grammar.antlr.NoopParser;
import noop.grammar.antlr.NoopLexer;
import noop.grammar.antlr.DocParser;
import noop.grammar.antlr.DocLexer;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;

import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeNodeStream;

class Parser() {
  def buildParser(input: ANTLRStringStream): NoopParser = {
    return new NoopParser(new CommonTokenStream(new NoopLexer(input)));
  }

  def buildDocParser(input: ANTLRStringStream): DocParser = {
    return new DocParser(new CommonTokenStream(new DocLexer(input)));
  }

  def parseFile(source: InputStream): CommonTree = {
    val file = buildParser(new ANTLRInputStream(source)).file();
    return file.getTree().asInstanceOf[CommonTree];
  }

  def parseFile(source: String): CommonTree = {
    val file = buildParser(new ANTLRStringStream(source)).file();
    return file.getTree().asInstanceOf[CommonTree];
  }

  def parseInterpretable(source: String): CommonTree = {
    val interpretable = buildParser(new ANTLRStringStream(source)).interpretable();
    return interpretable.getTree().asInstanceOf[CommonTree];
  }

  def parseBlock(source: String): CommonTree = {
    val block = buildParser(new ANTLRStringStream(source)).block();
    return block.getTree().asInstanceOf[CommonTree];
  }

  def parseDoc(source: String): CommonTree = {
    val doc = buildDocParser(new ANTLRStringStream(source)).doc();
    return doc.getTree().asInstanceOf[CommonTree];
  }

  def buildTreeParser(ast: CommonTree): NoopAST = {
    return new NoopAST(new CommonTreeNodeStream(ast));
  }

  def file(source: String): File = {
    return buildTreeParser(parseFile(source)).file();
  }

  def file(source: InputStream): File = {
    return buildTreeParser(parseFile(source)).file();
  }
}
