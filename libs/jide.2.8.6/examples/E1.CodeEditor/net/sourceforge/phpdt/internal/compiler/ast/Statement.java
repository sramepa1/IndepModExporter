package net.sourceforge.phpdt.internal.compiler.ast;

/**
 * A Statement.
 *
 * @author Matthieu Casanova
 */
public abstract class Statement extends AstNode {
  protected Statement() {
  }

  /**
   * Create a node.
   *
   * @param sourceStart starting offset
   * @param sourceEnd   ending offset
   * @param beginLine   begin line
   * @param endLine     ending line
   * @param beginColumn begin column
   * @param endColumn   ending column
   */
  protected Statement(int sourceStart,
					  int sourceEnd,
					  int beginLine,
					  int endLine,
					  int beginColumn,
					  int endColumn) {
	super(sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
  }

  /**
   * Tell if the block is empty.
   *
   * @return a statement is not empty by default
   */
  public boolean isEmptyBlock() {
	return false;
  }

  public abstract Expression expressionAt(int line, int column);

  //public abstract void propagateType(List list);
}
