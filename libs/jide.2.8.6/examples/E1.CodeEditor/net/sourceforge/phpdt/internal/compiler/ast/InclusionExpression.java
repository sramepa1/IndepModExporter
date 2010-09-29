package net.sourceforge.phpdt.internal.compiler.ast;

import net.sourceforge.phpdt.internal.compiler.parser.Outlineable;
import net.sourceforge.phpdt.internal.compiler.parser.OutlineableWithChildren;

import java.util.List;

import gatchan.phpparser.project.itemfinder.PHPItem;
import gatchan.phpparser.parser.PHPParserConstants;
import gatchan.phpparser.parser.PHPParser;
import sidekick.IAsset;

import javax.swing.text.Position;
import javax.swing.*;

/** @author Matthieu Casanova */
public final class InclusionExpression extends Expression implements Outlineable, IAsset {
  private boolean silent;
  /** The kind of include. */
  private final int keyword;
  private final Expression expression;

  private final transient OutlineableWithChildren parent;

  private transient Position start;
  private transient Position end;
  private transient static Icon icon;
  private String cachedToString;

  public InclusionExpression(OutlineableWithChildren parent,
                             int keyword,
                             Expression expression,
                             int sourceStart,
                             int sourceEnd,
                             int beginLine,
                             int endLine,
                             int beginColumn,
                             int endColumn) {
    super(Type.INTEGER, sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
    this.keyword = keyword;
    this.expression = expression;
    this.parent = parent;
  }

  private String keywordToString() {
    return PHPParserConstants.tokenImage[keyword];
  }

  public String toStringExpression() {
    return toString();
  }

  public String toString() {
    if (cachedToString == null) {
      String keyword = keywordToString();
      String expressionString = expression.toStringExpression();
      StringBuffer buffer = new StringBuffer(keyword.length() +
                                             expressionString.length() + 2);
      if (silent) {
        buffer.append('@');
      }
      buffer.append(keyword);
      buffer.append(' ');
      buffer.append(expressionString);
      cachedToString = buffer.toString();
    }
    return cachedToString;
  }

  public OutlineableWithChildren getParent() {
    return parent;
  }

  /**
   * Get the variables from outside (parameters, globals ...)
   *
   * @param list the list where we will put variables
   */
  public void getOutsideVariable(List list) {
    expression.getOutsideVariable(list);
  }

  /**
   * get the modified variables.
   *
   * @param list the list where we will put variables
   */
  public void getModifiedVariable(List list) {
    expression.getModifiedVariable(list);
  }

  /**
   * Get the variables used.
   *
   * @param list the list where we will put variables
   */
  public void getUsedVariable(List list) {
    expression.getUsedVariable(list);
  }

  public String getName() {
    //todo : change this
    return null;
  }

  public int getItemType() {
    return PHPItem.INCLUDE;
  }

  public Position getEnd() {
    return end;
  }

  public void setEnd(Position end) {
    this.end = end;
  }

  public Position getStart() {
    return start;
  }

  public void setStart(Position start) {
    this.start = start;
  }

  public Icon getIcon() {
    if (icon == null) {
      icon = new ImageIcon(InclusionExpression.class.getResource("/gatchan/phpparser/icons/require.png"));
    }
    return icon;
  }

  public String getShortString() {
    return toString();
  }

  public String getLongString() {
    return toString();
  }

  public void setName(String name) {
  }

  public Expression expressionAt(int line, int column) {
    return expression.isAt(line, column) ? expression : null;
  }

  public void analyzeCode(PHPParser parser) {
    expression.analyzeCode(parser);
  }
}
