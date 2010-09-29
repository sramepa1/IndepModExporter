package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;

import java.util.List;

/**
 * an else statement.
 * it's else
 * @author Matthieu Casanova
 */
public final class Else extends Statement {

  /** the statements. */
  private final Statement[] statements;

  /**
   * An else statement bad version ( : endif).
   * @param statements the statements
   * @param sourceStart the starting offset
   * @param sourceEnd the ending offset
   */
  public Else(Statement[] statements,
              int sourceStart,
              int sourceEnd,
                    int beginLine,
                    int endLine,
                    int beginColumn,
                    int endColumn) {
    super(sourceStart, sourceEnd,beginLine,endLine,beginColumn,endColumn);
    this.statements = statements;
  }

  /**
   * An else statement good version
   * @param statement the statement (it could be a block)
   * @param sourceStart the starting offset
   * @param sourceEnd the ending offset
   */
  public Else(Statement statement,
              int sourceStart,
              int sourceEnd,
                    int beginLine,
                    int endLine,
                    int beginColumn,
                    int endColumn) {
    super(sourceStart, sourceEnd,beginLine,endLine,beginColumn,endColumn);
    statements = new Statement[1];
    statements[0] = statement;
  }

  /**
   * Return the object into String.
   * @param tab how many tabs (not used here
   * @return a String
   */
  public String toString(int tab) {
    StringBuffer buff = new StringBuffer(tabString(tab));
    buff.append("else \n");
    Statement statement;
    for (int i = 0; i < statements.length; i++) {
      statement = statements[i];
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
      statements[i].getOutsideVariable(list);
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
  }

  public Expression expressionAt(int line, int column) {
    for (int i = 0; i < statements.length; i++) {
      Statement statement = statements[i];
      if (statement.isAt(line, column)) return statement.expressionAt(line, column);
    }
    return null;
  }

  public void analyzeCode(PHPParser parser) {
    for (int i = 0; i < statements.length; i++) {
      statements[i].analyzeCode(parser);
    }
  }
}
