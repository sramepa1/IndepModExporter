package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;

import java.util.List;

/**
 * A Foreach statement.
 *
 * @author Matthieu Casanova
 */
public final class ForeachStatement extends Statement {
  private final Expression expression;
  private final Expression variable;
  private final Statement statement;

  /**
   * Create a new Foreach statement.
   *
   * @param expression  the Array that will be read. It could be null if there was a parse error
   * @param variable    the value (it could be a value or a key => value, or null if there was a parse error)
   * @param statement   the statement that will be executed
   * @param sourceStart the start of the foreach
   * @param sourceEnd   the end of the foreach
   */
  public ForeachStatement(Expression expression,
                          Expression variable,
                          Statement statement,
                          int sourceStart,
                          int sourceEnd,
                          int beginLine,
                          int endLine,
                          int beginColumn,
                          int endColumn) {
    super(sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
    this.expression = expression;
    this.variable = variable;
    this.statement = statement;
  }

  /**
   * Return the object into String.
   *
   * @param tab how many tabs (not used here
   *
   * @return a String
   */
  public String toString(int tab) {
    String expressionString;
    if (expression == null) {
      expressionString = "__EXPRESSION__";
    } else {
      expressionString = expression.toStringExpression();
    }
    String variableString;
    if (variable == null) {
      variableString = "__VARIABLE__";
    } else {
      variableString = variable.toStringExpression();
    }

    String statementString;
    if (statement == null) {
      statementString = "__STATEMENT__";
    } else {
      statementString = statement.toString(tab + 1);
    }

    StringBuffer buff = new StringBuffer(tab +
                                         expressionString.length() +
                                         variableString.length() +
                                         statementString.length() + 18);
    buff.append(AstNode.tabString(tab));
    buff.append("foreach (");
    buff.append(expressionString);
    buff.append(" as ");
    buff.append(variableString);
    buff.append(" {\n");
    buff.append(statementString);
    buff.append("\n}");
    return buff.toString();
  }

  /**
   * Get the variables from outside (parameters, globals ...).
   *
   * @param list the list where we will put variables
   */
  public void getOutsideVariable(List list) {
    if (expression != null) expression.getOutsideVariable(list);
    if (variable != null) variable.getOutsideVariable(list);
    if (statement != null) statement.getOutsideVariable(list);
  }

  /**
   * get the modified variables.
   *
   * @param list the list where we will put variables
   */
  public void getModifiedVariable(List list) {
    if (expression != null) expression.getModifiedVariable(list);
    if (variable != null) variable.getUsedVariable(list);
    if (statement != null) statement.getModifiedVariable(list);
  }

  /**
   * Get the variables used.
   *
   * @param list the list where we will put variables
   */
  public void getUsedVariable(List list) {
    if (expression != null) expression.getUsedVariable(list);
    if (statement != null) statement.getUsedVariable(list);
  }

  public Expression expressionAt(int line, int column) {
    if (expression != null && expression.isAt(line, column)) return expression;
    if (variable != null && variable.isAt(line, column)) return variable;
    if (statement != null && statement.isAt(line, column)) return statement.expressionAt(line, column);
    return null;
  }

  public void analyzeCode(PHPParser parser) {
    if (expression != null) expression.analyzeCode(parser);
    if (variable != null) variable.analyzeCode(parser);
    if (statement != null) statement.analyzeCode(parser);
  }
}
