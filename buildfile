require 'buildr/antlr'
require 'buildr/scala'

# Version number for this release
VERSION_NUMBER = "1.0.0"
# Group identifier for your projects
GROUP = "com.google"
COPYRIGHT = "Apache 2.0"

repositories.remote << "http://www.ibiblio.org/maven2"

ANTLR = ["org.antlr:antlr:jar:3.1.1", "org.antlr:stringtemplate:jar:3.2"]
Buildr::ANTLR::REQUIRES = ANTLR
Java.classpath.unshift ANTLR

desc "The Noop language"
define "noop" do

  project.version = VERSION_NUMBER
  project.group = GROUP
  manifest["Implementation-Vendor"] = COPYRIGHT

  antlr = antlr(_('src/main/antlr'), :in_package=>'noop.grammar')

  compile.from antlr
  compile.with ANTLR
end
