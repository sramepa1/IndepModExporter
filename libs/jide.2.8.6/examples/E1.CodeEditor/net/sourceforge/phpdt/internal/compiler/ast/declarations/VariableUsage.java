package net.sourceforge.phpdt.internal.compiler.ast.declarations;

import net.sourceforge.phpdt.internal.compiler.ast.Type;

/**
 * A variable usage. This describe a variable declaration in a php document and his starting offset
 *
 * @author Matthieu Casanova
 */
public final class VariableUsage {
  /** the variable name. */
  private final String name;

  /** where the variable is declared. */
  private final int sourceStart;
  private final int sourceEnd;

  private final int beginLine;
  private final int endLine;
  private final int beginColumn;
  private final int endColumn;

  private final Type type;

  /**
   * create a VariableUsage.
   *
   * @param name        the name of the variable
   * @param sourceStart the starting offset
   * @param sourceEnd   the ending offset
   * @param beginLine   begin line
   * @param endLine     end line
   * @param beginColumn begin column
   * @param endColumn   end column
   */
  public VariableUsage(Type type,
                       String name,
                       int sourceStart,
                       int sourceEnd,
                       int beginLine,
                       int endLine,
                       int beginColumn,
                       int endColumn) {
    this.type = type;
    this.name = name;
    this.sourceStart = sourceStart;
    this.sourceEnd = sourceEnd;
    this.beginLine = beginLine;
    this.endLine = endLine;
    this.beginColumn = beginColumn;
    this.endColumn = endColumn;
  }

  public String toString() {
    return name + ' ' + sourceStart;
  }

  /**
   * Get the name of the variable.
   *
   * @return the name if the variable
   */
  public String getName() {
    return name;
  }

  /**
   * Get the starting offset.
   *
   * @return the starting offset
   */
  public int getSourceStart() {
    return sourceStart;
  }

  public int getBeginLine() {
    return beginLine;
  }

  public int getBeginColumn() {
    return beginColumn;
  }

  public int getSourceEnd() {
    return sourceEnd;
  }

  public int getEndLine() {
    return endLine;
  }

  public int getEndColumn() {
    return endColumn;
  }

  public boolean equals(Object object) {
    return name.equals(object);
  }

  public int hashCode() {
    return name.hashCode();
  }

  public Type getType() {
    return type;
  }

  public boolean isDeclaredBefore(VariableUsage variableUsage) {
    return (endLine < variableUsage.getEndLine()) || (endLine == variableUsage.getEndLine() && endColumn < variableUsage.getEndColumn());
  }
}
