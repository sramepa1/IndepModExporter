package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParserConstants;

/**
 * Any expression that have an operator.
 *
 * @author Matthieu Casanova
 */
public abstract class OperatorExpression extends Expression {

  private final int operator;

  protected OperatorExpression(Type type,
                               int operator,
                               int sourceStart,
                               int sourceEnd,
                               int beginLine,
                               int endLine,
                               int beginColumn,
                               int endColumn) {
    super(type, sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
    this.operator = operator;
  }

  public String operatorToString() {
    return PHPParserConstants.tokenImage[operator];
  }
}
