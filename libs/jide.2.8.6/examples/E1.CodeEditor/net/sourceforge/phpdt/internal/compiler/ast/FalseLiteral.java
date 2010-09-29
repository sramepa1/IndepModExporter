package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.Token;
import gatchan.phpparser.parser.PHPParser;

/**
 * @author Matthieu Casanova
 */
public final class FalseLiteral extends MagicLiteral {

  public FalseLiteral(Token token) {
    super(Type.BOOLEAN, token.sourceStart, token.sourceEnd, token.beginLine,token.endLine,token.beginColumn,token.endColumn);
  }

  /**
   * Return the expression as String.
   * @return the expression
   */
  public String toStringExpression() {
    return "false";//$NON-NLS-1$
  }

  public String toString() {
    return "false";//$NON-NLS-1$
  }

  public void analyzeCode(PHPParser parser) {
  }
}