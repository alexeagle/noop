package noop.model

import proto.NoopAst.Expr;
import noop.model.proto.NoopAst.Expr.Type;

/**
 * Converts proto buffer "inheritance", which is done by enum, into typed expressions.
 * @author alexeagle@google.com (Alex Eagle)
 */
class ExpressionWrapper(val data: Expr) extends Expression {
  def accept(visitor: Visitor): Unit = {
    getTypedExpression.accept(visitor);
  }

  def getTypedExpression: Expression = {
    data.getType match {
      case Type.STRING_LITERAL => {
        new StringLiteralExpression(data.getStringLiteral)
      }
      case Type.INT_LITERAL => {
        new IntLiteralExpression(data.getIntLiteral)
      }
      case Type.BOOLEAN_LITERAL => {
        new BooleanLiteralExpression(data.getBooleanLiteral)
      }
      case Type.ASSIGNMENT => {
        new AssignmentExpression(data.getAssignment)
      }
      case Type.IDENTIFIER => {
        new IdentifierExpression(data.getIdentifier)
      }
      case Type.CONDITIONAL => {
        data.getConditional.getOperator match {
          case "||" => new ConditionalOrExpression(data.getConditional)
          case "&&" => new ConditionalAndExpression(data.getConditional)
        }
      }
      case Type.DEREFERENCE => {
        new DereferenceExpression(data.getDeref)
      }
      case Type.METHOD_INVOCATION => {
        new MethodInvocationExpression(data.getMethodInvocation)
      }
      case Type.OPERATION => {
        new OperatorExpression(data.getOperation)
      }
    }
  }
}