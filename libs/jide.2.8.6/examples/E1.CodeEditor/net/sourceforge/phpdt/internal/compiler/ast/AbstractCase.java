package net.sourceforge.phpdt.internal.compiler.ast;

import java.util.List;

/**
 * Superclass of case statement that we can find in a switch.
 *
 * @author Matthieu Casanova
 */
public abstract class AbstractCase extends Statement {

  /** The statements in the case. */
  protected final Statement[] statements;

  /**
   * Create a case statement.
   *
   * @param statements  the statements array
   * @param sourceStart the beginning source offset
   * @param sourceEnd   the ending offset
   * @param beginLine   begin line
   * @param endLine     end line
   * @param beginColumn begin column
   * @param endColumn   end column
   */
  protected AbstractCase(final Statement[] statements,
                         final int sourceStart,
                         final int sourceEnd,
                         final int beginLine,
                         final int endLine,
                         final int beginColumn,
                         final int endColumn) {
    super(sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
    this.statements = statements;
  }

  /**
   * Get the variables from outside (parameters, globals ...).
   *
   * @param list the list where we will put variables
   */
  public final void getOutsideVariable(final List list) {
    for (int i = 0; i < statements.length; i++) {
      statements[i].getOutsideVariable(list);
    }
  }

  /**
   * get the modified variables.
   *
   * @param list the list where we will put variables
   */
  public void getModifiedVariable(final List list) {
    for (int i = 0; i < statements.length; i++) {
      statements[i].getModifiedVariable(list);
    }
  }

  /**
   * Get the variables used.
   *
   * @param list the list where we will put variables
   */
  public void getUsedVariable(final List list) {
    for (int i = 0; i < statements.length; i++) {
      statements[i].getUsedVariable(list);
    }
  }

  public Expression expressionAt(int line, int column) {
    if (statements != null) {
      for (int i = 0; i < statements.length; i++) {
        Statement statement = statements[i];
        if (statement.isAt(line, column)) return statement.expressionAt(line, column);
      }
    }
    return null;
  }
}
