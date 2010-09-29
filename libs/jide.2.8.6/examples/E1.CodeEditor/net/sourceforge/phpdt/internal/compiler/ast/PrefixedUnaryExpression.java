package net.sourceforge.phpdt.internal.compiler.ast;

/**
 * @author Matthieu Casanova
 */
public class PrefixedUnaryExpression extends UnaryExpression {


  public PrefixedUnaryExpression(Expression expression,
                                 int operator,
                                 int sourceStart,
                                 int beginLine,
                                 int beginColumn) {
    super(expression, operator, sourceStart, expression.getSourceEnd(),beginLine,expression.getEndLine(),beginColumn,expression.getEndColumn());
  }

  public String toStringExpression() {
    return operatorToString() + expression.toStringExpression();
  }
}
