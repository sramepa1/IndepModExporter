package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;

import java.util.List;

/**
 * A do statement.
 *
 * @author Matthieu Casanova
 */
public final class DoStatement extends Statement {
  /** The condition expression. */
  private final Expression condition;
  /** The action of the while. (it could be a block) */
  private final Statement action;

  public DoStatement(Expression condition,
                     Statement action,
                     int sourceStart,
                     int sourceEnd,
                     int beginLine,
                     int endLine,
                     int beginColumn,
                     int endColumn) {
    super(sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
    this.condition = condition;
    this.action = action;
  }

  /**
   * Return the object into String.
   *
   * @param tab how many tabs (not used here
   *
   * @return a String
   */
  public String toString(int tab) {
    String conditionString = condition.toStringExpression();
    StringBuffer buff;
    if (action == null) {
      buff = new StringBuffer(17 + tab + conditionString.length());
      buff.append("do ");
      buff.append(" {} ;");
    } else {
      String actionString = action.toString(tab + 1);
      buff = new StringBuffer(13 + conditionString.length() + actionString.length());
      buff.append("do ");
      buff.append('\n');
      buff.append(actionString);
    }
    buff.append(tabString(tab));
    buff.append(" while (");
    buff.append(conditionString);
    buff.append(')');
    return buff.toString();
  }

  /**
   * Get the variables from outside (parameters, globals ...)
   *
   * @param list the list where we will put variables
   */
  public void getOutsideVariable(List list) {
    condition.getOutsideVariable(list); // todo: check if unuseful
    action.getOutsideVariable(list);
  }

  /**
   * get the modified variables.
   *
   * @param list the list where we will put variables
   */
  public void getModifiedVariable(List list) {
    condition.getModifiedVariable(list);
    action.getModifiedVariable(list);
  }

  /**
   * Get the variables used.
   *
   * @param list the list where we will put variables
   */
  public void getUsedVariable(List list) {
    condition.getUsedVariable(list);
    action.getUsedVariable(list);
  }

  public Expression expressionAt(int line, int column) {
    if (condition.isAt(line, column)) return condition;
    if (action.isAt(line, column)) return action.expressionAt(line, column);
    return null;
  }

  public void analyzeCode(PHPParser parser) {
    condition.analyzeCode(parser);
    action.analyzeCode(parser);
  }

}
