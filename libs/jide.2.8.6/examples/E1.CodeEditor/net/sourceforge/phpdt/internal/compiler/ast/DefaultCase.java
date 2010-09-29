package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;

/**
 * A default case for a switch.
 * it's default : .....;
 *
 * @author Matthieu Casanova
 */
public final class DefaultCase extends AbstractCase {

  /**
   * Create a default case.
   *
   * @param statements  the statements
   * @param sourceStart the starting offset
   * @param sourceEnd   the ending offset
   */
  public DefaultCase(final Statement[] statements,
                     final int sourceStart,
                     final int sourceEnd,
                     final int beginLine,
                     final int endLine,
                     final int beginColumn,
                     final int endColumn) {
    super(statements, sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
  }

  /**
   * Return the object into String.
   *
   * @param tab how many tabs (not used here
   * @return a String
   */
  public String toString(final int tab) {
    final StringBuffer buff = new StringBuffer(tabString(tab));
    buff.append("default : \n"); //$NON-NLS-1$
    for (int i = 0; i < statements.length; i++) {
      final Statement statement = statements[i];
      buff.append(statement.toString(tab + 9));
    }
    return buff.toString();
  }

  public void analyzeCode(PHPParser parser) {
    for (int i = 0; i < statements.length; i++) {
      statements[i].analyzeCode(parser);
    }
  }
}
