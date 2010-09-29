package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;

import java.util.List;

/**
 * Here is a branchstatement : break or continue.
 *
 * @author Matthieu Casanova
 */
public abstract class BranchStatement extends Statement {
  /** The label (if there is one). */
  protected final Expression expression;

  protected BranchStatement(Expression expression, int sourceStart, int sourceEnd,
                            int beginLine,
                            int endLine,
                            int beginColumn,
                            int endColumn) {
    super(sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
    this.expression = expression;
  }


  /**
   * Get the variables from outside (parameters, globals ...)
   *
   * @param list the list where we will put variables
   */
  public final void getOutsideVariable(List list) {
    if (expression != null) {
      expression.getOutsideVariable(list);
    }
  }

  /**
   * get the modified variables.
   *
   * @param list the list where we will put variables
   */
  public final void getModifiedVariable(List list) {
    if (expression != null) {
      expression.getModifiedVariable(list);
    }
  }

  /**
   * Get the variables used.
   *
   * @param list the list where we will put variables
   */
  public final void getUsedVariable(List list) {
    if (expression != null) {
      expression.getUsedVariable(list);
    }
  }

  public Expression expressionAt(int line, int column) {
    return expression.isAt(line, column) ? expression : null;
  }

  public void analyzeCode(PHPParser parser) {
    if (expression != null) {
      expression.analyzeCode(parser);
    }
  }
}
