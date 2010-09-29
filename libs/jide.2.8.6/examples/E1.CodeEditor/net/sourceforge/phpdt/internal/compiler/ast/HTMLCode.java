package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;

import java.util.List;


/**
 * It's html code. It will contains some html, javascript, css ...
 *
 * @author Matthieu Casanova
 */
public final class HTMLCode extends Statement {
  /** The html Code. */
  private final String htmlCode;

  /**
   * @param sourceStart starting offset
   * @param sourceEnd   ending offset
   * @param beginLine   begin line
   * @param endLine     ending line
   * @param beginColumn begin column
   * @param endColumn   ending column
   */
  public HTMLCode(String htmlCode,
                  int sourceStart,
                  int sourceEnd,
                  int beginLine,
                  int endLine,
                  int beginColumn,
                  int endColumn) {
    super(sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
    this.htmlCode = htmlCode;
  }

  /**
   * I don't process tabs, it will only return the html inside.
   *
   * @return the text of the block
   */
  public String toString() {
    return htmlCode;
  }

  /**
   * I don't process tabs, it will only return the html inside.
   *
   * @param tab how many tabs before this html
   *
   * @return the text of the block
   */
  public String toString(int tab) {
    return htmlCode + ' ';
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
  }

  /**
   * Get the variables used.
   *
   * @param list the list where we will put variables
   */
  public void getUsedVariable(List list) {
  }

  public Expression expressionAt(int line, int column) {
    return null;
  }

  public void analyzeCode(PHPParser parser) {
  }
}
