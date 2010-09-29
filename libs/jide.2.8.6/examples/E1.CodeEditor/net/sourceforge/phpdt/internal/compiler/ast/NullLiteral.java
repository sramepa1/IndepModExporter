package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.Token;
import gatchan.phpparser.parser.PHPParser;

/**
 * @author Matthieu Casanova
 */
public final class NullLiteral extends MagicLiteral {

  public NullLiteral(Token token) {
    super(Type.NULL,token.sourceStart, token.sourceEnd, token.beginLine,token.endLine,token.beginColumn,token.endColumn);
  }

  /**
   * Return the expression as String.
   * @return the expression
   */
  public String toStringExpression() {
    return "null";
  }

  public void analyzeCode(PHPParser parser) {}
}
