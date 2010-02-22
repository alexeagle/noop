package noop.model

import proto.NoopAst.Stmt
import proto.NoopAst.Stmt.Type

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
class StatementWrapper(data: Stmt) extends Expression {
  def accept(visitor: Visitor): Unit = {
    getTypedExpression.accept(visitor);
  }

  def getTypedExpression: Expression = {
    data.getType match {
      case Type.SHOULD => {
        new ShouldExpression(data.getShould)
      }
    }
  }
}
