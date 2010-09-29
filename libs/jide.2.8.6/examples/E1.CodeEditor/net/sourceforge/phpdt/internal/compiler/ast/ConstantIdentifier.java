package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.Token;
import gatchan.phpparser.parser.PHPParser;

import java.util.List;

/** @author Matthieu Casanova */
public final class ConstantIdentifier extends Expression {

  private final String name;

  public ConstantIdentifier(String name,
                            int sourceStart,
                            int sourceEnd,
                            int beginLine,
                            int endLine,
                            int beginColumn,
                            int endColumn) {
    super(Type.STRING, sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
    this.name = name;
  }

  public ConstantIdentifier(Token token) {
    super(Type.STRING, token.sourceStart, token.sourceEnd, token.beginLine, token.endLine, token.beginColumn, token.endColumn);
    name = token.image;
  }

  /**
   * Return the expression as String.
   *
   * @return the expression
   */
  public String toStringExpression() {
    return name;
  }

  public String toString() {
    return name;
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
