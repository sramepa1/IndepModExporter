package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;

import java.util.List;

/**
 * An empty statement.
 * @author Matthieu Casanova
 */
public final class EmptyStatement extends Statement {

  public EmptyStatement(int sourceStart, int sourceEnd, int beginLine, int endLine, int beginColumn, int endColumn) {
    super(sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
  }

  public String toString(int tab) {
    return tabString(tab) + ';';
  }

  /**
   * Get the variables from outside (parameters, globals ...)
   *
   * @param list the list where we will put variables
   */
  public void getOutsideVariable(List list) {}

  /**
   * get the modified variables.
   *
   * @param list the list where we will put variables
   */
  public void getModifiedVariable(List list) {}

  /**
   * Get the variables used.
   *
   * @param list the list where we will put variables
   */
  public void getUsedVariable(List list) {}

  public Expression expressionAt(int line, int column) {
    return null;
  }

  public void analyzeCode(PHPParser parser) {
  }
}
