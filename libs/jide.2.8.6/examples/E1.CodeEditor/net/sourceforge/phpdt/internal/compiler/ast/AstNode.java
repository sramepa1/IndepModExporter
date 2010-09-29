package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;

import java.util.List;
import java.io.Serializable;

/**
 * It will be the mother of our own ast tree for php just like the ast tree of Eclipse.
 *
 * @author Matthieu Casanova
 */
public abstract class AstNode implements Serializable {
  /** Starting and ending position of the node in the sources. */
  protected int sourceStart;
  protected int sourceEnd;

  protected int beginLine;
  protected int endLine;
  protected int beginColumn;
  protected int endColumn;

  protected AstNode() {
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
  protected AstNode(int sourceStart,
                    int sourceEnd,
                    int beginLine,
                    int endLine,
                    int beginColumn,
                    int endColumn) {
    this.sourceStart = sourceStart;
    this.sourceEnd = sourceEnd;
    this.beginLine = beginLine;
    this.endLine = endLine;
    this.beginColumn = beginColumn;
    this.endColumn = endColumn;
  }

  /**
   * Add some tabulations.
   *
   * @param tab the number of tabulations
   *
   * @return a String containing some spaces
   */
  public static String tabString(int tab) {
    if (tab == 0) return "";
    StringBuffer s = new StringBuffer(2 * tab);
    for (int i = tab; i > 0; i--) {
      s.append("  ");
    }
    return s.toString();
  }

  /**
   * Return the object into String. It should be overriden
   *
   * @return a String
   */
  public String toString() {
    return "****" + super.toString() + "****";
  }

  /**
   * Return the object into String.
   *
   * @param tab how many tabs (not used here
   *
   * @return a String
   */
  public abstract String toString(int tab);

  /**
   * Get the variables from outside (parameters, globals ...)
   *
   * @param list the list where we will put variables
   */
  public abstract void getOutsideVariable(List list);

  /**
   * get the modified variables.
   *
   * @param list the list where we will put variables
   */
  public abstract void getModifiedVariable(List list);

  /**
   * Get the variables used.
   *
   * @param list the list where we will put variables
   */
  public abstract void getUsedVariable(List list);

  /** This method will analyze the code. by default it will do nothing */
  public abstract void analyzeCode(PHPParser parser);

  /**
   * Check if the array array contains the object o.
   *
   * @param array an array
   * @param o     an obejct
   *
   * @return true if the array contained the object o
   */
  public final boolean arrayContains(Object[] array, Object o) {
    for (int i = 0; i < array.length; i++) {
      if (array[i].equals(o)) {
        return true;
      }
    }
    return false;
  }

  public int getSourceStart() {
    return sourceStart;
  }

  public int getSourceEnd() {
    return sourceEnd;
  }

  public int getBeginLine() {
    return beginLine;
  }

  public int getEndLine() {
    return endLine;
  }

  public int getBeginColumn() {
    return beginColumn;
  }

  public int getEndColumn() {
    return endColumn;
  }

  public void setSourceEnd(int sourceEnd) {
    this.sourceEnd = sourceEnd;
  }

  public void setEndLine(int endLine) {
    this.endLine = endLine;
  }

  public void setEndColumn(int endColumn) {
    this.endColumn = endColumn;
  }

  /**
   * Returns true if the line and column position are contained by this node.
   *
   * @param line   the line
   * @param column the column
   *
   * @return true if the line and column position are contained by this node.
   */
  public boolean isAt(int line, int column) {
    return isAt(this, line, column);
  }

  /**
   * Returns true if the line and column position are contained by the given node.
   *
   * @param node   the node
   * @param line   the line
   * @param column the column
   *
   * @return true if the line and column position are contained by the given node.
   */
  public static boolean isAt(AstNode node, int line, int column) {
    return (line == node.getBeginLine() && column > node.getBeginColumn()) ||
           (line == node.getEndLine() && column < node.getEndColumn()) ||
           (line > node.getBeginLine() && line < node.getEndLine());
  }
}
