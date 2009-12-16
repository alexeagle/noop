package noop.model

import proto.Noop

/**
 * TODO(alexeagle): evil?
 * @author alexeagle@google.com (Alex Eagle)
 */
object ExpressionConverter {
  def fromData(expr: Noop.Expression): Expression = expr.getType match {
    case Noop.Expression.Type.IDENTIFIER => new IdentifierExpression(expr.getStringVal);
    case Noop.Expression.Type.STRING_LITERAL => new StringLiteralExpression(expr.getStringVal);
    case Noop.Expression.Type.NUMBER_LITERAL => new IntLiteralExpression(expr.getNumberVal);
    case Noop.Expression.Type.BOOLEAN_LITERAL => new BooleanLiteralExpression(expr.getBooleanVal);
    case Noop.Expression.Type.DEREFERENCE => new DereferenceExpression(fromData(expr.getLhs), fromData(expr.getRhs));
    case Noop.Expression.Type.CONDITIONAL => new ConditionalAndExpression(fromData(expr.getLhs), fromData(expr.getRhs));
    case _ => throw new IllegalArgumentException("Cannot convert proto expression with type " + expr.getType);
  }
}