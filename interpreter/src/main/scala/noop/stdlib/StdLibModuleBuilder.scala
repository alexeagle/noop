package noop.stdlib

import noop.model.proto.Noop._
import noop.model.ConcreteClassDefinition

/**
 * Temporary home for the standard libraries, so they don't need to be parsed from source.
 * @author alexeagle@google.com (Alex Eagle)
 */
class StdLibModuleBuilder {
  def build: Library = {
    Library.newBuilder
            .setName("noop.StdLib")
            .addConcreteClass(booleanClass)
            .addConcreteClass(consoleClass)
            .addConcreteClass(intClass)
            .addConcreteClass(objectClass)
            .addConcreteClass(stringClass)
            .build();
  }

  def objectClass = ConcreteClass.newBuilder.setName("noop.Object").build();

  def stringClass = ConcreteClass.newBuilder
          .setName("noop.String")
          .addMethod(toStringMethod)
          .addMethod(Method.newBuilder
          .setSignature(MethodSignature.newBuilder
          .setName("length")
          .addReturnType("noop.Int")
          .addModifier(Modifier.NATIVE))
          .setBlock(Block.newBuilder));
  
  def consoleClass = ConcreteClass.newBuilder
            .setName("noop.system.Console")
            .addMethod(Method.newBuilder
            .setSignature(MethodSignature.newBuilder
            .setName("println")
            .addArgument(Property.newBuilder
            .setName("str")
            .setType("noop.String")))
            .setBlock(Block.newBuilder));

  def intClass: ConcreteClass.Builder = {
    val intClass = ConcreteClass.newBuilder.setName("noop.Int");

    for (arithmetic <- List("plus", "minus", "multiply", "divide", "modulo")) {
      intClass.addMethod(Method.newBuilder
              .setSignature(MethodSignature.newBuilder
              .setName(arithmetic)
              .addReturnType("noop.Int")
              .addModifier(Modifier.NATIVE)
              .addArgument(Property.newBuilder
              .setName("other")
              .setType("noop.Int")))
              .setBlock(Block.newBuilder));
    }

    for (logic <- List("equals", "doesNotEqual", "greaterThan", "lesserThan", "greaterOrEqualThan", "lesserOrEqualThan")) {
      intClass.addMethod(Method.newBuilder
              .setSignature(MethodSignature.newBuilder
              .setName(logic)
              .addReturnType("noop.Boolean")
              .addModifier(Modifier.NATIVE)
              .addArgument(Property.newBuilder
              .setName("other")
              .setType("noop.Int")))
              .setBlock(Block.newBuilder));
    }

    intClass.addMethod(toStringMethod);

    return intClass;
  }

  def toStringMethod = Method.newBuilder
          .setSignature(MethodSignature.newBuilder
          .setName("toString")
          .addReturnType("noop.String")
          .addModifier(Modifier.NATIVE))
          .setBlock(Block.newBuilder);

  def booleanClass: ConcreteClass.Builder = {
    val booleanClass = ConcreteClass.newBuilder.setName("noop.Boolean");

    for (logic <- List("and", "or", "xor")) {
      booleanClass.addMethod(Method.newBuilder
            .setSignature(MethodSignature.newBuilder
            .setName(logic)
            .addModifier(Modifier.NATIVE)
            .addReturnType("noop.Boolean")
            .addArgument(Property.newBuilder
            .setName("other")
            .setType("noop.Boolean")))
            .setBlock(Block.newBuilder));
    }

    booleanClass.addMethod(Method.newBuilder
          .setSignature(MethodSignature.newBuilder
          .setName("not")
          .addModifier(Modifier.NATIVE)
          .addReturnType("noop.Boolean"))
          .setBlock(Block.newBuilder));

    booleanClass.addMethod(toStringMethod);
    return booleanClass;
  }
}
