package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;

import java.util.List;

/**
 * A Block. { statements }.
 *
 * @author Matthieu Casanova
 */
public final class Block extends Statement {
  /** An array of statements inside the block. */
  private final Statement[] statements;

  /**
   * Create a block.
   *
   * @param statements  the statements
   * @param sourceStart starting offset
   * @param sourceEnd   ending offset
   */
  public Block(Statement[] statements,
               int sourceStart,
               int sourceEnd,
               int beginLine,
               int endLine,
               int beginColumn,
               int endColumn) {
    super(sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
    this.statements = statements;
  }

  /**
   * tell if the block is empty.
   *
   * @return the block is empty if there are no statements in it
   */
  public boolean isEmptyBlock() {
    return statements == null;
  }

  /**
   * Return the block as String.
   *
   * @param tab how many tabs
   *
   * @return the string representation of the block
   */
  public String toString(int tab) {
    final String s = AstNode.tabString(tab);
    final StringBuffer buff = new StringBuffer(s);
    buff.append("{\n");
    if (statements != null) {
      for (int i = 0; i < statements.length; i++) {
        buff.append(statements[i].toString(tab + 1)).append(";\n");
      }
    }
    buff.append("}\n"); //$NON-NLS-1$
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

  public Statement[] getStatements() {
    return statements;
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
