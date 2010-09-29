package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;

import java.util.List;

/** @author Matthieu Casanova */
public final class LabeledStatement extends Statement {
  private final String label;

  private final Statement statement;

  public LabeledStatement(String label,
                          Statement statement,
                          int sourceStart,
                          int sourceEnd,
                          int beginLine,
                          int endLine,
                          int beginColumn,
                          int endColumn) {
    super(sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
    this.label = label;
    this.statement = statement;
  }

  /**
   * Return the object into String. It should be overriden
   *
   * @return a String
   */
  public String toString() {
    if (statement != null) {
      return label + statement.toString();
    }
    return label;
  }

  /**
   * Return the object into String.
   *
   * @param tab how many tabs (not used here
   *
   * @return a String
   */
  public String toString(int tab) {
    return tabString(tab) + toString();
  }

  /**
   * Get the variables from outside (parameters, globals ...)
   *
   * @param list the list where we will put variables
   */
  public void getOutsideVariable(List list) {
    if (statement != null) {
      statement.getOutsideVariable(list);
    }
  }

  /**
   * get the modified variables.
   *
   * @param list the list where we will put variables
   */
  public void getModifiedVariable(List list) {
    if (statement != null) {
      statement.getModifiedVariable(list);
    }
  }

  /**
   * Get the variables used.
   *
   * @param list the list where we will put variables
   */
  public void getUsedVariable(List list) {
    if (statement != null) {
      statement.getUsedVariable(list);
    }
  }

  public Expression expressionAt(int line, int column) {
    return statement != null && statement.isAt(line, column) ? statement.expressionAt(line, column) : null;
  }

  public void analyzeCode(PHPParser parser) {
    if (statement != null) {
      statement.analyzeCode(parser);
    }
  }
}
