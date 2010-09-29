package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;

import java.util.List;

/**
 * a variable declaration in an array().
 * it could take Expression as key.
 *
 * @author Matthieu Casanova
 */
public final class ArrayVariableDeclaration extends Expression {

  /** the array key. */
  private final Expression key;

  /** the array value. */
  private Expression value;

  public ArrayVariableDeclaration(Expression key,
                                  Expression value,
                                  int sourceStart,
                                  int sourceEnd,
                                  int beginLine,
                                  int endLine,
                                  int beginColumn,
                                  int endColumn) {
    super(Type.UNKNOWN, sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
    this.key = key;
    this.value = value;
  }

  /**
   * Create a new array variable declaration.
   *
   * @param key       the key
   * @param sourceEnd the end position
   */
  public ArrayVariableDeclaration(Expression key,
                                  int sourceEnd,
                                  int beginLine,
                                  int endLine,
                                  int beginColumn,
                                  int endColumn) {
    super(Type.UNKNOWN, key.getSourceStart(), sourceEnd, beginLine, endLine, beginColumn, endColumn);
    this.key = key;
  }

  /**
   * Return the expression as String.
   *
   * @return the expression
   */
  public String toStringExpression() {
    if (value == null) {
      return key.toStringExpression();
    } else {
      String keyString = key.toStringExpression();
      String valueString = value.toStringExpression();
      StringBuffer buff = new StringBuffer(keyString.length() + valueString.length() + 3);
      buff.append(keyString);
      buff.append(" => ");
      buff.append(valueString);
      return buff.toString();
    }
  }


  /**
   * Get the variables from outside (parameters, globals ...)
   *
   * @param list the list where we will put variables
   */
  public void getOutsideVariable(List list) {
  }

  /**
   * get the modified variables.
   *
   * @param list the list where we will put variables
   */
  public void getModifiedVariable(List list) {
    key.getModifiedVariable(list);
    if (value != null) {
      value.getModifiedVariable(list);
    }
  }

  /**
   * Get the variables used.
   *
   * @param list the list where we will put variables
   */
  public void getUsedVariable(List list) {
    key.getUsedVariable(list);
    if (value != null) {
      value.getUsedVariable(list);
    }
  }

  public Expression expressionAt(int line, int column) {
    if (key.isAt(line, column)) return key;
    if (value != null && value.isAt(line, column)) return value;
    return null;
  }

  public void analyzeCode(PHPParser parser) {
    key.analyzeCode(parser);
    if (value != null) {
      value.analyzeCode(parser);
    }
  }
}
