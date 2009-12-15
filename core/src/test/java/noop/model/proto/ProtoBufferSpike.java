package noop.model.proto;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.TextFormat;
import com.google.protobuf.TextFormat.ParseException;
import noop.model.proto.Noop.*;

import static noop.model.proto.Noop.Expression.Type.IDENTIFIER;
import static noop.model.proto.Noop.Expression.Type.RETURN;
import static noop.model.proto.Noop.Expression.Type.STRING_LITERAL;

/**
 * Utility class used for experimenting with serializing the Noop AST to protocol buffer.
 * TODO(alexeagle): remove when/if it's no longer useful
 * @author alexeagle@google.com (Alex Eagle)
 */
public class ProtoBufferSpike {
  public static void main(String[] args) throws InvalidProtocolBufferException, ParseException {
    Module myModule = Module.newBuilder()
        .setName("CmdLineExample")
        .setDocumentation(Documentation.newBuilder()
            .setSummary("used to read cmd line args"))
        .addBinding(Binding.newBuilder()
            .setName("example.AppBinding")
            .setDocumentation(Documentation.newBuilder()
                .setSummary("Contains bindings"))
            .setBlock(BindingBlock.newBuilder()
            .addBind(BindOperator.newBuilder()
            .setType("noop.Application")
            .setBoundTo(Expression.newBuilder().setType(IDENTIFIER).setStringVal("example.CmdLineArgs")))))
        .addConcreteClass(ConcreteClass.newBuilder()
            .setName("example.CmdLineArgs")
            .setDocumentation(Documentation.newBuilder()
                .setSummary("Demonstrates reading command line arguments in Noop."))
            .addSuperType("noop.Application")
            .addProperty(Property.newBuilder().setName("console").setType("noop.system.Console"))
            .addProperty(Property.newBuilder().setName("args").setType("noop.system.RawCommandLineArgs"))
            .addMethod(Method.newBuilder()
                .setSignature(MethodSignature.newBuilder()
                    .setName("main")
                    .setDocumentation(Documentation.newBuilder()
                        .setSummary("the entry point of the application"))
                    .addReturnType("noop.Int"))
                .setBlock(Block.newBuilder()
                .addExpression(Expression.newBuilder()
                    .setMethodInvocation(MethodInvocation.newBuilder()
                    .setLhs(Expression.newBuilder().setType(IDENTIFIER).setStringVal("console"))
                    .setMethod("println")
                    .addArgument(Expression.newBuilder().setType(STRING_LITERAL).setStringVal("hello"))))
                .addExpression(Expression.newBuilder()
                .setType(RETURN)
                .setRhs(Expression.newBuilder().setNumberVal(0)))))
            .addUnittest(Unittest.newBuilder().setDescription("should print the first argument"))
            .addUnittest(Unittest.newBuilder().setDescription("should return zero"))
        ).build();
    String serialized = myModule.toString();
    System.out.println("myModule = " + serialized);
    Noop.Module.Builder builder = Module.newBuilder();
    TextFormat.merge(serialized, builder);
    System.out.println("builder.build().getName() = " + builder.build().getName());
  }
}
