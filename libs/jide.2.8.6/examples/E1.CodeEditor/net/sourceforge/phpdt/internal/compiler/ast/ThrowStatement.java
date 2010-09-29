package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;

import java.util.List;

/**
 * @author Matthieu Casanova
 */
public final class ThrowStatement extends Statement {
  private final Expression throwed;

  public ThrowStatement(Expression throwed,
                        int sourceStart,
                        int sourceEnd,
                        int beginLine,
                        int endLine,
                        int beginColumn,
                        int endColumn) {
    super(sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
    this.throwed = throwed;
  }

  public String toString(int tab) {
    return tabString(tab) + "throw "+throwed.toStringExpression();
  }

  public void getOutsideVariable(List list) {
    throwed.getOutsideVariable(list);
  }

  public void getModifiedVariable(List list) {
    throwed.getOutsideVariable(list);
  }

  public void getUsedVariable(List list) {
    throwed.getOutsideVariable(list);
  }

  public Expression expressionAt(int line, int column) {
    return throwed.isAt(line, column)?throwed : null;
  }

  public void analyzeCode(PHPParser parser) {
    throwed.analyzeCode(parser);
  }
}
