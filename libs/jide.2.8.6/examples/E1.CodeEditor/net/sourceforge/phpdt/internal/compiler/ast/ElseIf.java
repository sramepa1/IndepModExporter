package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;

import java.util.List;

/**
 * An elseif statement.
 *
 * @author Matthieu Casanova
 */
public final class ElseIf extends Statement {
  /** The condition. */
  private final Expression condition;

  /** The statements. */
  private final Statement[] statements;

  public ElseIf(Expression condition, Statement[] statements, int sourceStart, int sourceEnd,
                int beginLine,
                int endLine,
                int beginColumn,
                int endColumn) {
    super(sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
    this.condition = condition;
    this.statements = statements;
  }

  /**
   * Return the object into String.
   *
   * @param tab how many tabs (not used here
   *
   * @return a String
   */
  public String toString(int tab) {
    StringBuffer buff = new StringBuffer(tabString(tab));
    buff.append("elseif (");
    buff.append(condition.toStringExpression());
    buff.append(") \n");
    for (int i = 0; i < statements.length; i++) {
      Statement statement = statements[i];
      buff.append(statement.toString(tab + 1)).append('\n');
    }
    return buff.toString();
  }

  /**
   * Get the variables from outside (parameters, globals ...)
   *
   * @param list the list where we will put variables
   */
  public void getOutsideVariable(List list) {
    for (int i = 0; i < statements.length; i++) {
      statements[i].getModifiedVariable(list);
    }
  }

  /**
   * get the modified variables.
   *
   * @param list the list where we will put variables
   */
  public void getModifiedVariable(List list) {
    for (int i = 0; i < statements.length; i++) {
      statements[i].getModifiedVariable(list);
    }
    condition.getModifiedVariable(list);
  }

  /**
   * Get the variables used.
   *
   * @param list the list where we will put variables
   */
  public void getUsedVariable(List list) {
    for (int i = 0; i < statements.length; i++) {
      statements[i].getUsedVariable(list);
    }
    condition.getUsedVariable(list);
  }

  public Expression expressionAt(int line, int column) {
    if (condition.isAt(line, column)) return condition;
    for (int i = 0; i < statements.length; i++) {
      Statement statement = statements[i];
      if (statement.isAt(line, column)) return null;
    }
    return null;
  }

  public void analyzeCode(PHPParser parser) {
    condition.analyzeCode(parser);
    for (int i = 0; i < statements.length; i++) {
      statements[i].analyzeCode(parser);
    }
  }

}
