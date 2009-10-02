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

import interpreter.testing.TestFailedException;
import model.{AssignmentExpression, Block, BooleanLiteralExpression, DereferenceExpression,
    EvaluatedExpression, Expression, IdentifierDeclarationExpression, IdentifierExpression,
    IntLiteralExpression, MethodInvocationExpression, OperatorExpression, ReturnExpression,
    ShouldExpression, StringLiteralExpression, Visitor, WhileLoop};
import types.{NoopBoolean, NoopInteger, NoopObject, NoopString, NoopType};

/**
 * @author alexeagle@google.com (Alex Eagle)
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
class InterpreterVisitor(val context: Context) extends Visitor {

  def visit(assignmentExpression: AssignmentExpression) = {
    val currentFrame = context.stack.top;
    val identifier = assignmentExpression.lhs.asInstanceOf[IdentifierExpression].identifier;
    val obj = currentFrame.lastEvaluated(1);

    currentFrame.lastEvaluated.clear();
    if (obj == null) {
      throw new RuntimeException("cannot assign Void");
    }
    if (currentFrame.identifiers.contains(identifier)) {
      currentFrame.identifiers(identifier) = Tuple(null, obj);
    } else {
      throw new IllegalStateException("No identifier " + identifier);
    }
  };

  def visit(block: Block) = {
    context.stack.top.lastEvaluated.clear();
  };

  def visit(booleanLiteralExpression: BooleanLiteralExpression) = {
    val noopBooleanClassDef = context.classLoader.findClass("Boolean");

    context.stack.top.lastEvaluated += new NoopBoolean(noopBooleanClassDef,
        Map.empty[String, NoopObject], booleanLiteralExpression.value);
  };

  def visit(dereferenceExpression: DereferenceExpression) = {
  };

  def visit(evaluatedExpression: EvaluatedExpression) = {
    context.stack.top.lastEvaluated += evaluatedExpression.value;
  };

  def visit(identifierDeclarationExpression: IdentifierDeclarationExpression) = {
    val currentFrame = context.stack.top;

    if (currentFrame.lastEvaluated.isEmpty) {
      throw new RuntimeException("The right handside didn't evaluate to a proper value");
    }
    val obj = currentFrame.lastEvaluated(0);

    currentFrame.lastEvaluated.clear();
    currentFrame.addIdentifier(identifierDeclarationExpression.name,
        new Tuple2[NoopType, NoopObject](null, obj)); 
  };

  def visit(identifierExpression: IdentifierExpression) = {
    val currentFrame = context.stack.top;
    val identifier = identifierExpression.identifier;

    if (identifier == "this") {
      currentFrame.lastEvaluated += currentFrame.thisRef;
    } else if (currentFrame.identifiers.contains(identifier)) {
      currentFrame.lastEvaluated += currentFrame.identifiers(identifier)._2;
    } else if (currentFrame.thisRef.parameterInstances.contains(identifier)) {
      currentFrame.lastEvaluated += currentFrame.thisRef.parameterInstances(identifier);
    } else {
      throw new RuntimeException(
          "Not an IdentifierExpression: " + identifier);
    }
  };

  def visit(intLiteralExpression: IntLiteralExpression) = {
    val noopIntegerClassDef = context.classLoader.findClass("Int");

    context.stack.top.lastEvaluated += new NoopInteger(noopIntegerClassDef,
        Map.empty[String, NoopObject], intLiteralExpression.value);
  };

  def visit(methodInvocationExpression: MethodInvocationExpression) = {
    val methodInvocationEvaluator = new MethodInvocationEvaluator(methodInvocationExpression);

    methodInvocationEvaluator.execute(context, this);
  };

  def visit(operatorExpression: OperatorExpression) = {
  };

  def visit(returnExpression: ReturnExpression) = {
  };

  def visit(shouldExpression: ShouldExpression) = {
    val lastEvaluated = context.stack.top.lastEvaluated;
    val actual = lastEvaluated(0);
    val expected = lastEvaluated(1);

    if (actual != expected) {
      throw new TestFailedException("expected " + actual + " to equal " + expected);
    }
  };

  def visit(stringLiteralExpression: StringLiteralExpression) = {
    val noopStringClassDef = context.classLoader.findClass("String");

    context.stack.top.lastEvaluated += new NoopString(noopStringClassDef,
        Map.empty[String, NoopObject], stringLiteralExpression.value);
  };

  def visit(whileLoop: WhileLoop) = {
    if (context.stack.top.lastEvaluated(0).asInstanceOf[NoopBoolean].value) {
      whileLoop.body.accept(this);
      whileLoop.accept(this);
    }
    context.stack.top.lastEvaluated.clear();
  };
}
