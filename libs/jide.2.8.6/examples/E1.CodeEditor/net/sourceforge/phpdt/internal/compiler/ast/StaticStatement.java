package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;

import java.util.List;

/**
 * A GlobalStatement statement in php.
 * @author Matthieu Casanova
 */
public final class StaticStatement extends Statement {

  /** An array of the variables called by this global statement. */
  private final VariableDeclaration[] variables;

  public StaticStatement(VariableDeclaration[] variables, int sourceStart, int sourceEnd,
                    int beginLine,
                    int endLine,
                    int beginColumn,
                    int endColumn) {
    super(sourceStart, sourceEnd,beginLine,endLine,beginColumn,endColumn);
    this.variables = variables;
  }

  public String toString() {
    StringBuffer buff = new StringBuffer("static ");
    for (int i = 0; i < variables.length; i++) {
      if (i != 0) {
        buff.append(", ");
      }
      buff.append(variables[i]);
    }
    return buff.toString();
  }

  public String toString(int tab) {
    return tabString(tab) + toString();
  }

  /**
   * Get the variables from outside (parameters, globals ...)
   *
   * @param list the list where we will put variables
   */
  public void getOutsideVariable(List list) {
    for (int i = 0; i < variables.length; i++) {
      variables[i].getModifiedVariable(list);
    }
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
  }

  public Expression expressionAt(int line, int column) {
    for (int i = 0; i < variables.length; i++) {
      VariableDeclaration variable = variables[i];
      if (variable.isAt(line, column)) return variable;
    }
    return null;
  }

  public void analyzeCode(PHPParser parser) {
    for (int i = 0; i < variables.length; i++) {
      variables[i].analyzeCode(parser);
    }
  }
}
