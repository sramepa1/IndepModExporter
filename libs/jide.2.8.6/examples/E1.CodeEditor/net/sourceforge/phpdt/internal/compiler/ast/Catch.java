package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;

import java.util.List;

/** @author Matthieu Casanova */
public class Catch extends AstNode {
  private String catchedClass;

  private Variable variable;

  private Block block;

  public Catch(String catchedClass,
               Variable variable,
               Block block,
               int sourceStart,
               int sourceEnd,
               int beginLine,
               int endLine,
               int beginColumn,
               int endColumn) {
    super(sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
    this.catchedClass = catchedClass;
    this.variable = variable;
    this.block = block;
  }

  public String toString(int tab) {
    StringBuffer buff = new StringBuffer(200);
    buff.append(tabString(tab));
    buff.append("catch (").append(catchedClass).append(' ').append(variable.toStringExpression());
    buff.append(")\n");
    buff.append(block.toString(tab + 1));
    return buff.toString();
  }

  public void getOutsideVariable(List list) {
    block.getOutsideVariable(list);
  }

  public void getModifiedVariable(List list) {
    variable.getUsedVariable(list);
    block.getModifiedVariable(list);
  }

  public void getUsedVariable(List list) {
    block.getUsedVariable(list);
  }

  public void analyzeCode(PHPParser parser) {
    variable.analyzeCode(parser);
    block.analyzeCode(parser);
  }
}
