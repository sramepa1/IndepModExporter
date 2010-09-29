package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;

import java.util.List;

/**
 * This is a cast expression.
 *
 * @author Matthieu Casanova
 */
public final class CastExpression extends Expression {
  /** The castTarget in which we cast the expression. */
  private final ConstantIdentifier castTarget;

  /** The expression to be casted. */
  private final Expression expression;

  /**
   * Create a cast expression.
   *
   * @param type        the castTarget
   * @param expression  the expression
   * @param sourceStart starting offset
   * @param sourceEnd   ending offset
   */
  public CastExpression(ConstantIdentifier type,
                        Expression expression,
                        int sourceStart,
                        int sourceEnd,
                        int beginLine,
                        int endLine,
                        int beginColumn,
                        int endColumn) {
    //todo find good type
    super(Type.UNKNOWN,sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
    castTarget = type;
    this.expression = expression;
  }

  /**
   * Return the expression as String.
   *
   * @return the expression
   */
  public String toStringExpression() {
    StringBuffer buff = new StringBuffer("(");
    buff.append(castTarget.toStringExpression());
    buff.append(") ");
    buff.append(expression.toStringExpression());
    return buff.toString();
  }

  /**
   * Get the variables from outside (parameters, globals ...)
   *
   * @param list the list where we will put variables
   */
  public void getOutsideVariable(List list) {
  }

  /**
   * get the modified variables.
   *
   * @param list the list where we will put variables
   */
  public void getModifiedVariable(List list) {
    expression.getModifiedVariable(list);
  }

  /**
   * Get the variables used.
   *
   * @param list the list where we will put variables
   */
  public void getUsedVariable(List list) {
    expression.getUsedVariable(list);
  }

  public Expression expressionAt(int line, int column) {
    return expression.isAt(line, column) ? expression : null;
  }

  public void analyzeCode(PHPParser parser) {
    expression.analyzeCode(parser);
  }
}
