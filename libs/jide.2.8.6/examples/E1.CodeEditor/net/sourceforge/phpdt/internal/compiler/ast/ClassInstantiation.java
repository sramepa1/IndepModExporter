package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParserConstants;

/**
 * a class instantiation.
 *
 * @author Matthieu Casanova
 */
public final class ClassInstantiation extends PrefixedUnaryExpression {

  private final boolean reference;

  public ClassInstantiation(Expression expression,
                            boolean reference,
                            int sourceStart,
                            int beginLine,
                            int beginColumn) {
    super(expression, PHPParserConstants.NEW, sourceStart,beginLine,beginColumn);
    this.reference = reference;
  }
  

  public String toStringExpression() {
    if (!reference) {
      return super.toStringExpression();
    }
    return '&' + super.toStringExpression();
  }
}
