package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;

import java.util.List;

/** @author Matthieu Casanova */
public final class PrintExpression extends Expression {
  private final Expression expression;

  public PrintExpression(Expression expression,
                         int sourceStart,
                         int sourceEnd,
                         int beginLine,
                         int endLine,
                         int beginColumn,
                         int endColumn) {
    super(Type.INTEGER,sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
    this.expression = expression;
  }

  /**
   * Return the expression as String.
   *
   * @return the expression
   */
  public String toStringExpression() {
    return "print " + expression.toStringExpression();
  }

  /**
   * Get the variables from outside (parameters, globals ...)
   *
   * @param list the list where we will put variables
   */
  public void getOutsideVariable(List list) {
    expression.getOutsideVariable(list);
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
