package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;

import java.util.List;

/**
 * This is a if statement.
 * if (condition)
 *  statement
 * (elseif statement)*
 * else statement
 * @author Matthieu Casanova
 */
public final class IfStatement extends Statement {

  private final Expression condition;
  private final Statement statement;
  private final ElseIf[] elseifs;
  private final Else els;

  /**
   * Create a new If statement.
   * @param condition the condition
   * @param statement a statement or a block of statements
   * @param elseifs the elseifs
   * @param els the else (or null)
   * @param sourceStart the starting position
   * @param sourceEnd the ending offset
   */
  public IfStatement(Expression condition,
                     Statement statement,
                     ElseIf[] elseifs,
                     Else els,
                     int sourceStart,
                     int sourceEnd,
                     int beginLine,
                     int endLine,
                     int beginColumn,
                     int endColumn) {
    super(sourceStart, sourceEnd,beginLine, endLine, beginColumn, endColumn);
    this.condition = condition;
    this.statement = statement;
    this.elseifs = elseifs;
    this.els = els;
  }

  /**
   * Return the object into String.
   * @param tab how many tabs (not used here
   * @return a String
   */
  public String toString(int tab) {
    StringBuffer buff = new StringBuffer(tabString(tab));
    buff.append("if (");
    buff.append(condition.toStringExpression()).append(") ");
    if (statement != null) {
      buff.append(statement.toString(tab + 1));
    }
    for (int i = 0; i < elseifs.length; i++) {
      buff.append(elseifs[i].toString(tab + 1));
      buff.append('\n');
    }
    if (els != null) {
      buff.append(els.toString(tab + 1));
      buff.append('\n');
    }
    return buff.toString();
  }

  /**
   * Get the variables from outside (parameters, globals ...)
   *
   * @param list the list where we will put variables
   */
  public void getOutsideVariable(List list) {
    condition.getOutsideVariable(list); // todo: check if unuseful
    if (statement != null) {
      statement.getOutsideVariable(list);
    }
    for (int i = 0; i < elseifs.length; i++) {
      elseifs[i].getOutsideVariable(list);
    }
    if (els != null) {
      els.getOutsideVariable(list);
    }
  }

  /**
   * get the modified variables.
   *
   * @param list the list where we will put variables
   */
  public void getModifiedVariable(List list) {
    condition.getModifiedVariable(list);
    if (statement != null) {
      statement.getModifiedVariable(list);
    }
    for (int i = 0; i < elseifs.length; i++) {
      elseifs[i].getModifiedVariable(list);
    }
    if (els != null) {
      els.getModifiedVariable(list);
    }
  }

  public void analyzeCode(PHPParser parser) {
    condition.analyzeCode(parser);
    if (statement != null) {
      statement.analyzeCode(parser);
    }
    for (int i = 0; i < elseifs.length; i++) {
      elseifs[i].analyzeCode(parser);
    }
    if (els != null) {
      els.analyzeCode(parser);
    }
  }
  /**
   * Get the variables used.
   *
   * @param list the list where we will put variables
   */
  public void getUsedVariable(List list) {
    condition.getUsedVariable(list);
    if (statement != null) {
      statement.getUsedVariable(list);
    }
    for (int i = 0; i < elseifs.length; i++) {
      elseifs[i].getUsedVariable(list);
    }
    if (els != null) {
      els.getUsedVariable(list);
    }
  }

  public Expression expressionAt(int line, int column) {
    if (condition.isAt(line,column)) return condition;
    if (statement != null && statement.isAt(line,column)) return statement.expressionAt(line,column);
    for (int i = 0; i < elseifs.length; i++) {
      ElseIf elseif = elseifs[i];
      if (elseif.isAt(line, column)) return elseif.expressionAt(line, column);
    }
    if (els != null && els.isAt(line,column)) return els.expressionAt(line, column);
    return null;
  }

}
