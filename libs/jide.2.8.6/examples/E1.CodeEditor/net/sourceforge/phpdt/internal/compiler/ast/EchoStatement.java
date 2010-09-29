package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;

import java.util.List;

/**
 * an echo statement. echo something;
 *
 * @author Matthieu Casanova
 */
public final class EchoStatement extends Statement {
  /** An array of expressions in this echo statement. */
  private final Expression[] expressions;

  public EchoStatement(Expression[] expressions,
                       int sourceStart,
                       int sourceEnd,
                       int beginLine,
                       int endLine,
                       int beginColumn,
                       int endColumn) {
    super(sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
    this.expressions = expressions;
  }

  public String toString() {
    StringBuffer buff = new StringBuffer("echo ");//$NON-NLS-1$
    for (int i = 0; i < expressions.length; i++) {
      if (i != 0) {
        buff.append(", ");//$NON-NLS-1$
      }
      buff.append(expressions[i].toStringExpression());
    }
    return buff.toString();
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
    String str = toString();
    return tabs + str;
  }

  /**
   * Get the variables from outside (parameters, globals ...)
   *
   * @param list the list where we will put variables
   */
  public void getOutsideVariable(List list) {
    for (int i = 0; i < expressions.length; i++) {
      expressions[i].getOutsideVariable(list);
    }
  }

  /**
   * get the modified variables.
   *
   * @param list the list where we will put variables
   */
  public void getModifiedVariable(List list) {
    for (int i = 0; i < expressions.length; i++) {
      expressions[i].getModifiedVariable(list);
    }
  }

  /**
   * Get the variables used.
   *
   * @param list the list where we will put variables
   */
  public void getUsedVariable(List list) {
    for (int i = 0; i < expressions.length; i++) {
      expressions[i].getUsedVariable(list);
    }
  }

  public Expression expressionAt(int line, int column) {
    for (int i = 0; i < expressions.length; i++) {
      Expression expression = expressions[i];
      if (expression.isAt(line, column)) return expression;
    }
    return null;
  }

  public void analyzeCode(PHPParser parser) {
    for (int i = 0; i < expressions.length; i++) {
      expressions[i].analyzeCode(parser);
    }
  }
}
