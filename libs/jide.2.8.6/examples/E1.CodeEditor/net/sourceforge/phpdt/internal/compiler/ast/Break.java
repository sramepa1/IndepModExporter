package net.sourceforge.phpdt.internal.compiler.ast;

/**
 * A break statement.
 *
 * @author Matthieu Casanova
 */
public final class Break extends BranchStatement {

  public Break(final Expression expression, final int sourceStart, final int sourceEnd,
                    final int beginLine,
                    final int endLine,
                    final int beginColumn,
                    final int endColumn) {
    super(expression, sourceStart, sourceEnd,beginLine,endLine,beginColumn,endColumn);
  }

  public String toString(final int tab) {
    final String s = tabString(tab);
    if (expression != null) {
      return s + "break " + expression.toString();
    }
    return s + "break";
  }
}
