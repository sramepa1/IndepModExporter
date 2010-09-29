package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.Token;
import gatchan.phpparser.parser.Token;
import gatchan.phpparser.parser.PHPParser;

/**
 * Literal for numbers.
 *
 * @author Matthieu Casanova
 */
public final class NumberLiteral extends Literal {
  private final String source;

  public NumberLiteral(Type type,Token token) {
    super(type,token.sourceStart, token.sourceEnd, token.beginLine, token.endLine, token.beginColumn, token.endColumn);
    source = token.image;
  }

  /**
   * Return the expression as String.
   *
   * @return the expression
   */
  public String toStringExpression() {
    return source;
  }

  public void analyzeCode(PHPParser parser) {}

}
