package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParseMessageEvent;
import gatchan.phpparser.parser.PHPParser;
import gatchan.phpparser.project.itemfinder.PHPItem;
import net.sourceforge.phpdt.internal.compiler.ast.declarations.VariableUsage;
import net.sourceforge.phpdt.internal.compiler.parser.Outlineable;
import net.sourceforge.phpdt.internal.compiler.parser.OutlineableWithChildren;
import sidekick.IAsset;

import javax.swing.*;
import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A Method declaration.
 *
 * @author Matthieu Casanova
 */
public class MethodDeclaration extends Statement implements OutlineableWithChildren, IAsset {
  private MethodHeader methodHeader;

  private Statement[] statements;

  private int bodyLineStart;
  private int bodyColumnStart;
  private int bodyLineEnd;
  private int bodyColumnEnd;

  /** Tell if the method is a class constructor. */
  private boolean isConstructor;

  /** The parent object. */
  private transient OutlineableWithChildren parent;
  /** The outlineable children (those will be in the node array too. */
  private final List children = new ArrayList();

  private transient Position start;
  private transient Position end;
  private static Icon icon;

  /** The variables assigned in code. This is used during code completion. */
  private List assignedVariablesInCode;

  public MethodDeclaration(OutlineableWithChildren parent, MethodHeader methodHeader) {
    sourceStart = methodHeader.getSourceStart();
    beginLine = methodHeader.getBeginLine();
    beginColumn = methodHeader.getBeginColumn();
    this.parent = parent;
    this.methodHeader = methodHeader;
  }

  /**
   * Return method into String, with a number of tabs
   *
   * @param tab the number of tabs
   *
   * @return the String containing the method
   */
  public String toString(int tab) {
    StringBuffer buff = new StringBuffer(200);
    buff.append(methodHeader.toString(tab));
    buff.append(toStringStatements(tab + 1));
    return buff.toString();
  }

  public String toString() {
    return methodHeader.toString();
  }

  /**
   * Return the statements of the method into Strings
   *
   * @param tab the number of tabs
   *
   * @return the String containing the statements
   */
  private String toStringStatements(int tab) {
    StringBuffer buff = new StringBuffer(" {");
    if (statements != null) {
      for (int i = 0; i < statements.length; i++) {
        buff.append('\n').append(statements[i].toString(tab));
        if (!(statements[i] instanceof Block)) {
          buff.append(';');
        }
      }
    }
    buff.append('\n').append(tabString(tab == 0 ? 0 : tab - 1)).append('}');
    return buff.toString();
  }

  public void setParent(OutlineableWithChildren parent) {
    this.parent = parent;
  }

  public OutlineableWithChildren getParent() {
    return parent;
  }

  public boolean add(Outlineable o) {
    return children.add(o);
  }

  public Outlineable get(int index) {
    return (Outlineable) children.get(index);
  }

  public int size() {
    return children.size();
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
   * This method will analyze the code.
   *
   * @param list the list where we will put variables
   */
  public void getUsedVariable(List list) {
  }

  /**
   * Get global variables (not parameters).
   *
   * @param list the list where I will put the variables
   */
  private void getGlobalVariable(List list) {
    if (statements != null) {
      for (int i = 0; i < statements.length; i++) {
        statements[i].getOutsideVariable(list);
      }
    }
  }

  /**
   * get the modified variables.
   *
   * @return the assigned variables in code.
   */
  private List getAssignedVariableInCode() {
    if (assignedVariablesInCode == null) {
      assignedVariablesInCode = new ArrayList(50);
      if (statements != null) {
        for (int i = 0; i < statements.length; i++) {
          statements[i].getModifiedVariable(assignedVariablesInCode);
        }
      }
    }
    return assignedVariablesInCode;
  }

  /**
   * Get the variables used.
   *
   * @param list the list where I will put the variables
   */
  private void getUsedVariableInCode(List list) {
    if (statements != null) {
      for (int i = 0; i < statements.length; i++) {
        statements[i].getUsedVariable(list);
      }
    }
  }

  /**
   * Returns the last variable assignation with the given name before the line and column.
   *
   * @param name   the name of the variable
   * @param line   the line
   * @param column the column
   *
   * @return a variable usage or null
   */
  public VariableUsage getAssignedVariableInCode(String name, int line, int column) {
    List assignedVariablesInCode = getAssignedVariableInCode();
    VariableUsage found = null;
    for (int i = 0; i < assignedVariablesInCode.size(); i++) {
      VariableUsage variableUsage = (VariableUsage) assignedVariablesInCode.get(i);
      if (variableUsage.getEndLine() > line || (variableUsage.getEndLine() == line && variableUsage.getBeginColumn() > column)) {
        // We do not need variables declared after the given line
        break;
      }
      if (variableUsage.getName().equals(name) && (found == null || found.isDeclaredBefore(variableUsage))) {
        found = variableUsage;
      }
    }
    return found;
  }

  private static boolean isVariableDeclaredBefore(List list, VariableUsage var) {
    String name = var.getName();
    int pos = var.getSourceStart();
    for (int i = 0; i < list.size(); i++) {
      VariableUsage variableUsage = (VariableUsage) list.get(i);
      if (variableUsage.getName().equals(name) && variableUsage.getSourceStart() < pos) {
        return true;
      }
    }
    return false;
  }

  /** This method will analyze the code. */
  public void analyzeCode(PHPParser parser) {
    methodHeader.analyzeCode(parser);
    if (statements != null) {
      for (int i = 0; i < statements.length; i++) {
        statements[i].analyzeCode(parser);
      }
    }

    List globalsVars = new ArrayList();
    getGlobalVariable(globalsVars);
    List modifiedVars = getAssignedVariableInCode();
    List parameters = new ArrayList(methodHeader.getArgumentsCount());
    methodHeader.getParameters(parameters);

    List declaredVars = new ArrayList(globalsVars.size() + modifiedVars.size() + parameters.size());
    declaredVars.addAll(globalsVars);
    declaredVars.addAll(modifiedVars);
    declaredVars.addAll(parameters);

    List usedVars = new ArrayList();
    getUsedVariableInCode(usedVars);
    List readOrWriteVars = new ArrayList(modifiedVars.size() + usedVars.size());
    readOrWriteVars.addAll(modifiedVars);
    readOrWriteVars.addAll(usedVars);

    //look for used variables that were not declared before
    findUnusedParameters(parser, readOrWriteVars, parameters);
    findUnknownUsedVars(parser, usedVars, declaredVars);
  }

  /**
   * This method will add a warning on all unused parameters.
   *
   * @param parser     the php parser
   * @param vars       the used variable list
   * @param parameters the declared variable list
   */
  private static void findUnusedParameters(PHPParser parser, List vars, List parameters) {
    for (int i = 0; i < parameters.size(); i++) {
      VariableUsage param = (VariableUsage) parameters.get(i);
      if (!isVariableInList(param.getName(), vars)) {
        parser.fireParseMessage(new PHPParseMessageEvent(PHPParser.WARNING,
                                                         PHPParseMessageEvent.MESSAGE_UNUSED_PARAMETERS,
                                                         parser.getPath(),
                                                         "warning, the parameter " + param.getName() + " seems to be never used in your method",
                                                         param.getSourceStart(),
                                                         param.getSourceEnd(),
                                                         param.getBeginLine(),
                                                         param.getEndLine(),
                                                         param.getBeginColumn(),
                                                         param.getEndColumn()));
        /* fireParseMessage(new PHPParseMessageEvent(PHPParser.WARNING,
                                               parser.getPath(),
                                               "You should use '<?php' instead of '<?' it will avoid some problems with XML",
                                               param.getSourceStart(),
                                               param.getSourceStart() + param.getName().length(),
                                               token.beginLine,
                                               token.endLine,
                                               token.beginColumn,
                                               token.endColumn));*/
      }
    }
  }

  /**
   * Tell if the list of VariableUsage contains a variable named by the name given.
   *
   * @param name the variable name
   * @param list the list of VariableUsage
   *
   * @return true if the variable is in the list false otherwise
   */
  private static boolean isVariableInList(String name, List list) {
    for (int i = 0; i < list.size(); i++) {
      if (((VariableUsage) list.get(i)).getName().equals(name)) {
        return true;
      }
    }
    return false;
  }

  /**
   * This method will add a warning on all used variables in a method that aren't declared before.
   *
   * @param parser       the php parser
   * @param usedVars     the used variable list
   * @param declaredVars the declared variable list
   */
  private static void findUnknownUsedVars(PHPParser parser, List usedVars, List declaredVars) {
    Set list = new HashSet(usedVars.size());
    for (int i = 0; i < usedVars.size(); i++) {
      VariableUsage variableUsage = (VariableUsage) usedVars.get(i);
      if ("this".equals(variableUsage.getName())) continue; // this is a special variable
      if (!list.contains(variableUsage.getName()) && !isVariableDeclaredBefore(declaredVars, variableUsage)) {
        list.add(variableUsage.getName());
        parser.fireParseMessage(new PHPParseMessageEvent(PHPParser.WARNING,
                                                         PHPParseMessageEvent.MESSAGE_VARIABLE_MAY_BE_UNASSIGNED,
                                                         parser.getPath(),
                                                         "warning, usage of a variable that seems to be unassigned yet : " + variableUsage.getName(),
                                                         variableUsage.getSourceStart(),
                                                         variableUsage.getSourceEnd(),
                                                         variableUsage.getBeginLine(),
                                                         variableUsage.getEndLine(),
                                                         variableUsage.getBeginColumn(),
                                                         variableUsage.getEndColumn()));
      }
    }
  }

  public String getName() {
    return methodHeader.getName();
  }

  public MethodHeader getMethodHeader() {
    return methodHeader;
  }

  public void setStatements(Statement[] statements) {
    this.statements = statements;
  }

  public int getBodyLineStart() {
    return bodyLineStart;
  }

  public void setBodyLineStart(int bodyLineStart) {
    this.bodyLineStart = bodyLineStart;
  }

  public int getBodyColumnStart() {
    return bodyColumnStart;
  }

  public void setBodyColumnStart(int bodyColumnStart) {
    this.bodyColumnStart = bodyColumnStart;
  }

  public int getBodyLineEnd() {
    return bodyLineEnd;
  }

  public void setBodyLineEnd(int bodyLineEnd) {
    this.bodyLineEnd = bodyLineEnd;
    setEndLine(bodyLineEnd);
  }

  public int getBodyColumnEnd() {
    return bodyColumnEnd;
  }

  public void setBodyColumnEnd(int bodyColumnEnd) {
    this.bodyColumnEnd = bodyColumnEnd;
    setEndColumn(bodyColumnEnd);
  }

  public int getItemType() {
    return PHPItem.METHOD;
  }

  public Icon getIcon() {
    if (icon == null) {
      icon = new ImageIcon(MethodDeclaration.class.getResource("/gatchan/phpparser/icons/method.png"));
    }
    return icon;
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

  public String getShortString() {
    return toString();
  }

  public String getLongString() {
    return toString();
  }

  public void setName(String name) {
  }

  public Expression expressionAt(int line, int column) {
    if (methodHeader.isAt(line, column)) return methodHeader.expressionAt(line, column);
    for (int i = 0; i < statements.length; i++) {
      Statement statement = statements[i];
      if (statement.isAt(line, column)) return statement.expressionAt(line, column);
    }
    return null;
  }
}
