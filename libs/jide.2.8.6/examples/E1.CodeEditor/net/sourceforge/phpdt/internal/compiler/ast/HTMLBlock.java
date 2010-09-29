package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;

import java.util.List;

/**
 * @author Matthieu Casanova
 */
public final class HTMLBlock extends Statement {

  private final AstNode[] nodes;

  public HTMLBlock(AstNode[] nodes) {
    super(nodes[0].getSourceStart(),
            nodes[(nodes.length > 0) ? nodes.length - 1 : 0].getSourceEnd(),
            nodes[0].getBeginLine(),
            nodes[(nodes.length > 0) ? nodes.length - 1 : 0].getEndLine(),
            nodes[0].getBeginColumn(),
            nodes[(nodes.length > 0) ? nodes.length - 1 : 0].getEndColumn());
    this.nodes = nodes;
  }

  /**
   * Return the object into String.
   *
   * @param tab how many tabs (not used here
   * @return a String
   */
  public String toString(int tab) {
    StringBuffer buff = new StringBuffer(tabString(tab));
    buff.append("?>");
    for (int i = 0; i < nodes.length; i++) {
      buff.append(nodes[i].toString(tab + 1));
    }
    buff.append("<?php\n");
    return buff.toString();
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
