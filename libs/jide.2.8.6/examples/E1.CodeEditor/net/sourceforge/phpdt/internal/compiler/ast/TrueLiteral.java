package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.Token;
import gatchan.phpparser.parser.PHPParser;

/**
 * the true literal.
 * @author Matthieu Casanova
 */
public final class TrueLiteral extends MagicLiteral {

  public TrueLiteral(Token token) {
    super(Type.BOOLEAN, token.sourceStart, token.sourceEnd, token.beginLine,token.endLine,token.beginColumn,token.endColumn);
  }

  /**
   * Return the expression as String.
   * @return the expression
   */
  public String toStringExpression() {
    return "true";
  }

  public String toString() {
    return "true";
  }

  public void analyzeCode(PHPParser parser) {
  }

}
