package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;

import java.util.List;

/**
 * @author Matthieu Casanova
 */
public abstract class UnaryExpression extends OperatorExpression {

  protected final Expression expression;

  protected UnaryExpression(Expression expression,
                            int operator,
                            int sourceStart,
                            int sourceEnd,
                            int beginLine,
                            int endLine,
                            int beginColumn,
                            int endColumn) {
    super(expression.getType(), operator, sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
    this.expression = expression;
  }

  /**
   * Get the variables from outside (parameters, globals ...)
   *
   * @param list the list where we will put variables
   */
  public final void getOutsideVariable(List list) {
  }

  /**
   * get the modified variables.
   *
   * @param list the list where we will put variables
   */
  public final void getModifiedVariable(List list) {
    expression.getModifiedVariable(list);
  }

  /**
   * Get the variables used.
   *
   * @param list the list where we will put variables
   */
  public final void getUsedVariable(List list) {
    expression.getUsedVariable(list);
  }

  public Expression expressionAt(int line, int column) {
    return expression.isAt(line, column) ? expression : this;
  }

  public void analyzeCode(PHPParser parser) {
    expression.analyzeCode(parser);
  }
}
