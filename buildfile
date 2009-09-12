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
Buildr::ANTLR::REQUIRES.clear
Buildr::ANTLR::REQUIRES.concat(ANTLR)

desc "The Noop language"
define "noop" do

  project.version = VERSION_NUMBER
  project.group = GROUP
  manifest["Implementation-Vendor"] = COPYRIGHT

  antlr = antlr([_('src/main/antlr/Noop.g'), _('src/main/antlr/NoopAST.g')], :in_package=>'noop.grammar.antlr')

  resources.from _('src/main/noop')  

  compile.from antlr
  compile.with [ANTLR, ANTLR_RUNTIME]

  package(:jar).
      with(:manifest=>{'Main-Class' => 'noop.interpreter.InterpreterMain'})

  compile.dependencies.each do |c|
    if c.to_s.index("antlr-runtime") or c.to_s.index("scala-library")
      package(:jar).merge(c).include('*.class')
    end
  end
  package :sources
end
