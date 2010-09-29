package net.sourceforge.phpdt.internal.compiler.ast;

/**
 * The variable superclass.
 *
 * @author Matthieu Casanova
 */
public abstract class AbstractVariable extends Expression {

  protected AbstractVariable(Type type,
                             int sourceStart,
                             int sourceEnd,
                             int beginLine,
                             int endLine,
                             int beginColumn,
                             int endColumn) {
    super(type, sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
  }

  /**
   * This method will return the name of the variable.
   *
   * @return a string containing the name of the variable.
   */
  public abstract String getName();
}
