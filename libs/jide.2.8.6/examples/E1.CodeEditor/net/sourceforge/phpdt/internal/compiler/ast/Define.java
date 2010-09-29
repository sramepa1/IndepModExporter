package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.project.itemfinder.PHPItem;
import gatchan.phpparser.parser.PHPParser;
import net.sourceforge.phpdt.internal.compiler.ast.declarations.VariableUsage;
import net.sourceforge.phpdt.internal.compiler.parser.Outlineable;
import net.sourceforge.phpdt.internal.compiler.parser.OutlineableWithChildren;
import sidekick.IAsset;

import javax.swing.*;
import javax.swing.text.Position;
import java.util.List;

/**
 * a Define. define(expression,expression)
 *
 * @author Matthieu Casanova
 */
public final class Define extends Statement implements Outlineable, IAsset {
  private final Expression defineName;
  private final Expression defineValue;

  private final transient OutlineableWithChildren parent;

  private transient Position start;
  private transient Position end;
  private String cachedToString;

  public Define(OutlineableWithChildren parent,
                Expression defineName,
                Expression defineValue,
                int sourceStart,
                int sourceEnd,
                int beginLine,
                int endLine,
                int beginColumn,
                int endColumn) {
    super(sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
    this.parent = parent;
    this.defineName = defineName;
    this.defineValue = defineValue;
  }

  public String toString(int tab) {
    String nameString = defineName.toStringExpression();
    String valueString = defineValue.toStringExpression();
    StringBuffer buff = new StringBuffer(tab + 10 + nameString.length() + valueString.length());
    buff.append(tabString(tab));
    buff.append("define(");
    buff.append(nameString);
    buff.append(", ");
    buff.append(valueString);
    buff.append(')');
    return buff.toString();
  }

  public String toString() {
    if (cachedToString == null) {
      String nameString = defineName.toStringExpression();
      String valueString = defineValue.toStringExpression();
      cachedToString = nameString + " = " + valueString;
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
    list.add(new VariableUsage(Type.UNKNOWN,
                               defineName.toStringExpression(),
                               sourceStart,
                               sourceEnd,
                               beginLine,
                               endLine,
                               beginColumn,
                               endColumn));//todo: someday : evaluate the defineName
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

  public String getName() {
    //todo : change this
    return defineName.toString();
  }

  public int getItemType() {
    return PHPItem.DEFINE;
  }

  public Position getStart() {
    return start;
  }

  public void setStart(Position start) {
    this.start = start;
  }

  public Position getEnd() {
    return end;
  }

  public void setEnd(Position end) {
    this.end = end;
  }

  public Icon getIcon() {
    return null;
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
    if (defineName.isAt(line, column)) return defineName;
    if (defineValue.isAt(line, column)) return defineValue;
    return null;
  }

  public void analyzeCode(PHPParser parser) {
    // todo analyze define
  }
}
