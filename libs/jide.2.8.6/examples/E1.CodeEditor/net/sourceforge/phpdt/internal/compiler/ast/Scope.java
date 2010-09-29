package net.sourceforge.phpdt.internal.compiler.ast;

import net.sourceforge.phpdt.internal.compiler.ast.declarations.VariableUsage;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * This class will define the variables declarations in current scope.
 *
 * @author Matthieu Casanova
 */
public class Scope {
  private final Map variableUsages = new HashMap();

  public Scope() {
  }

  public void addVariable(VariableUsage variableUsage) {
    String name = variableUsage.getName();
    Object o = variableUsages.get(name);
    if (o == null) {
      variableUsages.put(name, variableUsage);
    } else if (o instanceof List) {
      ((List) o).add(variableUsage);
    } else {
      List list = new ArrayList();
      variableUsages.put(name, list);
      list.add(o);
      list.add(variableUsage);
    }
  }

  public void addVariablesList(List list) {
    for (int i = 0; i < list.size(); i++) {
      addVariable((VariableUsage) list.get(i));
    }
  }

  public VariableUsage getVariable(String name,int line,int column) {
    Object o = variableUsages.get(name);
    VariableUsage found = null;
    if (o == null) {
      found = null;
    } else if (o instanceof VariableUsage) {
      VariableUsage variableUsage = (VariableUsage) o;
      if (variableUsage.getName().equals(name) &&
          variableUsage.getEndLine() < line || (variableUsage.getEndLine() == line && variableUsage.getBeginColumn() <= column)) {
        found = variableUsage;
      }
    } else {
      List list = (List) o;
      for (int i = 0; i < list.size(); i++) {
        VariableUsage variableUsage = (VariableUsage) list.get(i);
        if (variableUsage.getEndLine() > line || (variableUsage.getEndLine() == line && variableUsage.getBeginColumn() > column)) {
          // We do not need variables declared after the given line
          break;
        }
        if (variableUsage.getName().equals(name) && (found == null || found.isDeclaredBefore(variableUsage))) {
          found = variableUsage;
        }
      }
    }
    return found;
  }
}
