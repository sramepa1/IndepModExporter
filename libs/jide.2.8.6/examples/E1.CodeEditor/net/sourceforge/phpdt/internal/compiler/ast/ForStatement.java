package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;

import java.util.List;

/**
 * A For statement.
 * for(initializations;condition;increments) action
 * @author Matthieu Casanova
 */
public final class ForStatement extends Statement {
  /** the initializations. */
  private final Expression[] initializations;

  /** the condition. */
  private final Expression condition;
  /** the increments. */
  private final Expression[] increments;

  private final Statement action;

  public ForStatement(Expression[] initializations,
                      Expression condition,
                      Expression[] increments,
                      Statement action,
                      int sourceStart,
                      int sourceEnd,
                      int beginLine,
                      int endLine,
                      int beginColumn,
                      int endColumn) {
    super(sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
    this.initializations = initializations;
    this.condition = condition;
    this.increments = increments;
    this.action = action;
  }

  public String toString(int tab) {
    final StringBuffer buff = new StringBuffer(tabString(tab));
    buff.append("for (");  //$NON-NLS-1$
    //inits
    if (initializations != null) {
      for (int i = 0; i < initializations.length; i++) {
        buff.append(initializations[i].toStringExpression());
        if (i != (initializations.length - 1))
          buff.append(" , ");
      }
    }
    buff.append("; ");
    //cond
    if (condition != null) {
      buff.append(condition.toStringExpression());
    }
    buff.append("; ");
    //updates
    if (increments != null) {
      for (int i = 0; i < increments.length; i++) {
        //nice only with expressions
        buff.append(increments[i].toStringExpression());
        if (i != (increments.length - 1))
          buff.append(" , ");
      }
    }
    buff.append(") ");
    //block
    if (action == null)
      buff.append("{}");
    else
      buff.append(action.toString(tab + 1));
    return buff.toString();
  }

  /**
   * Get the variables from outside (parameters, globals ...)
   *
   * @param list the list where we will put variables
   */
  public void getOutsideVariable(List list) {
    if (condition != null) {
      condition.getOutsideVariable(list);
    }
    if (action != null) {
      action.getOutsideVariable(list);
    }
    if (initializations != null) {
      for (int i = 0; i < initializations.length; i++) {
        initializations[i].getOutsideVariable(list);
      }
    }
    if (increments != null) {
      for (int i = 0; i < increments.length; i++) {
        increments[i].getOutsideVariable(list);
      }
    }
  }

  /**
   * get the modified variables.
   *
   * @param list the list where we will put variables
   */
  public void getModifiedVariable(List list) {
    if (condition != null) {
      condition.getModifiedVariable(list);
    }
    if (action != null) {
      action.getModifiedVariable(list);
    }
    if (initializations != null) {
      for (int i = 0; i < initializations.length; i++) {
        initializations[i].getModifiedVariable(list);
      }
    }
    if (increments != null) {
      for (int i = 0; i < increments.length; i++) {
        increments[i].getModifiedVariable(list);
      }
    }
  }

  /**
   * Get the variables used.
   *
   * @param list the list where we will put variables
   */
  public void getUsedVariable(List list) {
    if (condition != null) {
      condition.getUsedVariable(list);
    }
    if (action != null) {
      action.getUsedVariable(list);
    }
    if (initializations != null) {
      for (int i = 0; i < initializations.length; i++) {
        initializations[i].getUsedVariable(list);
      }
    }
    if (increments != null) {
      for (int i = 0; i < increments.length; i++) {
        increments[i].getUsedVariable(list);
      }
    }
  }

  public Expression expressionAt(int line, int column) {
    if (condition != null && condition.isAt(line, column)) return condition;
    if (action != null && action.isAt(line, column)) return action.expressionAt(line, column);
    if (initializations != null) {
      for (int i = 0; i < initializations.length; i++) {
        Expression initialization = initializations[i];
        if (initialization.isAt(line, column)) return initialization;
      }
    }
    if (increments != null) {
      for (int i = 0; i < increments.length; i++) {
        Expression increment = increments[i];
        if (increment.isAt(line, column)) return increment;
      }
    }
    return null;
  }

  public void analyzeCode(PHPParser parser) {
    if (initializations != null) {
      for (int i = 0; i < initializations.length; i++) {
        initializations[i].analyzeCode(parser);
      }
    }
    if (condition != null) condition.analyzeCode(parser);
    if (increments != null) {
      for (int i = 0; i < increments.length; i++) {
        increments[i].analyzeCode(parser);
      }
    }
    action.analyzeCode(parser);
  }
}
