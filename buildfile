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
define 'noop', :version=>VERSION_NUMBER do

  manifest["Implementation-Vendor"] = COPYRIGHT

  define "core" do
    antlr = antlr([_('src/main/antlr3/noop/grammar/antlr/Doc.g'), \
                   _('src/main/antlr3/noop/grammar/antlr/Noop.g'), \
                   _('src/main/antlr3/noop/grammar/antlr/NoopAST.g')],
        :in_package=>'noop.grammar.antlr')
    compile.from antlr
    compile.with [ANTLR, SLF4J]
    package :jar
  end

  define "compiler" do
    resources.from [_('src/main/stringtemplate')]
    compile.with [project("core"), ANTLR, SLF4J]
    package :jar
  end

  define "interpreter" do
    # TODO - only want examples as a test resource
    resources.from [_('src/main/noop'), project("noop")._('examples/noop')]
    package(:jar).with(:manifest=>{'Main-Class' => 'noop.interpreter.InterpreterMain'})
    compile.with [project("core"), ANTLR_RUNTIME, SLF4J]
    compile.dependencies.each do |c|
      unless c.to_s.index("scala-compiler")
        package(:jar).merge(c).include('*.class')
      end
    end
    package :sources
  end

end
