package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;

import java.util.List;

/**
 * a php echo block. <?= someexpression ?>
 *
 * @author Matthieu Casanova
 */
public final class PHPEchoBlock extends Statement {
  /** the expression. */
  private final Expression expr;

  /**
   * @param sourceStart starting offset
   * @param sourceEnd   ending offset
   * @param beginLine   begin line
   * @param endLine     ending line
   * @param beginColumn begin column
   * @param endColumn   ending column
   */
  public PHPEchoBlock(Expression expr,
                      int sourceStart,
                      int sourceEnd,
                      int beginLine,
                      int endLine,
                      int beginColumn,
                      int endColumn) {
    super(sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
    this.expr = expr;
  }

  /**
   * Return the object into String.
   *
   * @param tab how many tabs (not used here
   *
   * @return a String
   */
  public String toString(int tab) {
    String tabs = tabString(tab);
    String expression = expr.toStringExpression();
    StringBuffer buff = new StringBuffer(tabs.length() +
                                         expression.length() +
                                         5);
    buff.append(tabs);
    buff.append("<?=");
    buff.append(expression);
    buff.append("?>");
    return buff.toString();
  }

  /**
   * Get the variables from outside (parameters, globals ...)
   *
   * @param list the list where we will put variables
   */
  public void getOutsideVariable(List list) {
  }

  /**
   * get the modified variables.
   *
   * @param list the list where we will put variables
   */
  public void getModifiedVariable(List list) {
  }

  /**
   * Get the variables used.
   *
   * @param list the list where we will put variables
   */
  public void getUsedVariable(List list) {
    expr.getUsedVariable(list);
  }

  public Expression expressionAt(int line, int column) {
    return null;
  }

  public void analyzeCode(PHPParser parser) {
  }
}
