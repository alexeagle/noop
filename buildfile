# Copyright 2009 Google Inc.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

require 'buildr/antlr'
require 'buildr/scala'

# Version number for this release
VERSION_NUMBER = "0.1.0-SNAPSHOT"
# Group identifier for your projects
GROUP = "com.google"
COPYRIGHT = "Apache 2.0"

repositories.remote << "http://www.ibiblio.org/maven2"

ANTLR = ["org.antlr:antlr:jar:3.1.1"]
ANTLR_RUNTIME = ["org.antlr:antlr-runtime:jar:3.1.1"]
SLF4J = ["org.slf4j:slf4j-api:jar:1.5.6", "org.slf4j:slf4j-simple:jar:1.5.6"]
Buildr::ANTLR::REQUIRES.clear
Buildr::ANTLR::REQUIRES.concat(ANTLR)

desc "The Noop language"
define "noop" do

  project.version = VERSION_NUMBER
  project.group = GROUP
  manifest["Implementation-Vendor"] = COPYRIGHT

  antlr = antlr([_('src/main/antlr/Doc.g'), _('src/main/antlr/Noop.g'), _('src/main/antlr/NoopAST.g')], :in_package=>'noop.grammar.antlr')

  resources.from [_('src/main/noop'), _('examples/noop')]

  compile.from antlr
  compile.with [ANTLR, ANTLR_RUNTIME, SLF4J]

  package(:jar).
      with(:manifest=>{'Main-Class' => 'noop.interpreter.InterpreterMain'})

  compile.dependencies.each do |c|
    if c.to_s.index("antlr-runtime") or c.to_s.index("scala-library")
      package(:jar).merge(c).include('*.class')
    end
  end
  package :sources
end
