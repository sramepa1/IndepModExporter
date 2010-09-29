package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;

import java.util.List;

/**
 * A Function call.
 *
 * @author Matthieu Casanova
 */
public final class FunctionCall extends AbstractSuffixExpression {
  /** the function name. */
  private final Expression functionName;

  /** the arguments. */
  private final Expression[] args;

  public FunctionCall(Expression functionName,
                      Expression[] args,
                      int sourceEnd,
                      int endLine,
                      int endColumn) {
    super(functionName.getType(),
          functionName.getSourceStart(),
          sourceEnd,
          functionName.getBeginLine(),
          endLine,
          functionName.getBeginColumn(),
          endColumn);
    this.functionName = functionName;
    this.args = args;
  }

  /**
   * Return the expression as String.
   *
   * @return the expression
   */
  public String toStringExpression() {
    StringBuffer buff = new StringBuffer(functionName.toStringExpression());
    buff.append('(');
    if (args != null) {
      for (int i = 0; i < args.length; i++) {
        Expression arg = args[i];
        if (i != 0) {
          buff.append(',');
        }
        buff.append(arg.toStringExpression());
      }
    }
    buff.append(')');
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
    if (args != null) {
      for (int i = 0; i < args.length; i++) {
        args[i].getModifiedVariable(list);
      }
    }
  }

  /**
   * Get the variables used.
   *
   * @param list the list where we will put variables
   */
  public void getUsedVariable(List list) {
    functionName.getUsedVariable(list);
    if (args != null) {
      for (int i = 0; i < args.length; i++) {
        args[i].getUsedVariable(list);
      }
    }
  }

  public Expression expressionAt(int line, int column) {
    if (functionName.isAt(line, column)) return functionName;
    if (args != null) {
      for (int i = 0; i < args.length; i++) {
        Expression arg = args[i];
        if (arg.isAt(line, column)) return arg;
      }
    }
    return null;
  }

  public void analyzeCode(PHPParser parser) {
  }
}
