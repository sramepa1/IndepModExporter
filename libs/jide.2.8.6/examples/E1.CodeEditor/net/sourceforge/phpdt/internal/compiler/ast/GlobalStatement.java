package net.sourceforge.phpdt.internal.compiler.ast;

import java.util.List;

import net.sourceforge.phpdt.internal.compiler.parser.Outlineable;
import net.sourceforge.phpdt.internal.compiler.parser.OutlineableWithChildren;
import gatchan.phpparser.parser.PHPParser;
import gatchan.phpparser.parser.PHPParseMessageEvent;
import gatchan.phpparser.project.itemfinder.PHPItem;
import sidekick.IAsset;

import javax.swing.text.Position;
import javax.swing.*;

/**
 * A GlobalStatement statement in php.
 *
 * @author Matthieu Casanova
 */
public final class GlobalStatement extends Statement implements Outlineable, IAsset {

  /** An array of the variables called by this global statement. */
  private final AbstractVariable[] variables;

  private final transient OutlineableWithChildren parent;

  private transient Position start;
  private transient Position end;
  private String cachedToString;
  
  public GlobalStatement(OutlineableWithChildren parent,
                         AbstractVariable[] variables,
                         int sourceStart,
                         int sourceEnd,
                         int beginLine,
                         int endLine,
                         int beginColumn,
                         int endColumn) {
    super(sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
    this.variables = variables;
    this.parent = parent;
  }

  public String toString() {
    if (cachedToString == null) {
      StringBuffer buff = new StringBuffer("global ");
      for (int i = 0; i < variables.length; i++) {
        if (i != 0) {
          buff.append(", ");
        }
        buff.append(variables[i].toStringExpression());
      }
      cachedToString = buff.toString();
    }
    return cachedToString;
  }

  public String toString(int tab) {
    return tabString(tab) + toString();
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
    for (int i = 0; i < variables.length; i++) {
      variables[i].getUsedVariable(list);
    }
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

  /**
   * We will analyse the code. if we have in globals a special variable it will be reported as a warning.
   *
   * @see Variable#SPECIAL_VARS
   */
  public void analyzeCode(PHPParser parser) {
    for (int i = 0; i < variables.length; i++) {
      if (arrayContains(Variable.SPECIAL_VARS, variables[i].getName())) {
        parser.fireParseMessage(new PHPParseMessageEvent(PHPParser.WARNING,
                                                         PHPParseMessageEvent.MESSAGE_UNNECESSARY_GLOBAL,
                                                         parser.getPath(),
                                                         "warning, you shouldn't request " + variables[i].getName() + " as global",
                                                         variables[i].sourceStart,
                                                         variables[i].sourceEnd,
                                                         variables[i].getBeginLine(),
                                                         variables[i].getEndLine(),
                                                         variables[i].getBeginColumn(),
                                                         variables[i].getEndColumn()));
      }
    }
  }

  public String getName() {
    //todo : change this
    return null;
  }

  public int getItemType() {
    return PHPItem.GLOBAL;
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
    for (int i = 0; i < variables.length; i++) {
      AbstractVariable variable = variables[i];
      if (variable.isAt(line, column)) return variable;
    }
    return null;
  }

  public void propagateType(List list) {
  }
}
