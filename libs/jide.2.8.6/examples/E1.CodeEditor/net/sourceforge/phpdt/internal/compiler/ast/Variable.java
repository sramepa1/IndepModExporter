package net.sourceforge.phpdt.internal.compiler.ast;

import java.util.List;

import net.sourceforge.phpdt.internal.compiler.ast.declarations.VariableUsage;
import gatchan.phpparser.parser.PHPParser;

/**
 * A variable. It could be a simple variable, or contains another variable.
 *
 * @author Matthieu Casanova
 */
public final class Variable extends AbstractVariable {
  /** The name of the variable. */
  private String name;

  /** A variable inside ($$varname). */
  private AbstractVariable variable;

  /** the variable is defined like this ${expression}. */
  private Expression expression;

  private static final String _GET = "_GET";
  private static final String _POST = "_POST";
  private static final String _REQUEST = "_REQUEST";
  private static final String _SERVER = "_SERVER";
  private static final String _SESSION = "_SESSION";
  private static final String _this = "this";
  private static final String GLOBALS = "GLOBALS";
  private static final String _COOKIE = "_COOKIE";
  private static final String _FILES = "_FILES";
  private static final String _ENV = "_ENV";

  /** Here is an array of all superglobals variables and the special "this". */
  public static final String[] SPECIAL_VARS = {_GET,
    _POST,
    _REQUEST,
    _SERVER,
    _SESSION,
    _this,
    GLOBALS,
    _COOKIE,
    _FILES,
    _ENV};

  /**
   * Create a new simple variable.
   *
   * @param name        the name
   * @param sourceStart the starting position
   * @param sourceEnd   the ending position
   */
  public Variable(String name,
                  int sourceStart,
                  int sourceEnd,
                  int beginLine,
                  int endLine,
                  int beginColumn,
                  int endColumn) {
    super(Type.UNKNOWN, sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
    this.name = name;
  }

  /**
   * Create a special variable ($$toto for example).
   *
   * @param variable    the variable contained
   * @param sourceStart the starting position
   * @param sourceEnd   the ending position
   */
  public Variable(AbstractVariable variable,
                  int sourceStart,
                  int sourceEnd,
                  int beginLine,
                  int endLine,
                  int beginColumn,
                  int endColumn) {
    super(Type.UNKNOWN, sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
    this.variable = variable;
  }

  /**
   * Create a special variable ($$toto for example).
   *
   * @param expression  the variable contained
   * @param sourceStart the starting position
   * @param sourceEnd   the ending position
   */
  public Variable(Expression expression,
                  int sourceStart,
                  int sourceEnd,
                  int beginLine,
                  int endLine,
                  int beginColumn,
                  int endColumn) {
    super(Type.UNKNOWN, sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
    this.expression = expression;
  }

  /**
   * Return the expression as String.
   *
   * @return the expression
   */
  public String toStringExpression() {
    return '$' + getName();
  }

  public String getName() {
    if (name != null) {
      return name;
    }
    if (variable != null) {
      return variable.toStringExpression();
    }
    return '{' + expression.toStringExpression() + '}';
  }

  /**
   * This method will return the current variable.
   *
   * @param list we will add the current method to the list
   */
  public void getOutsideVariable(List list) {
    getUsedVariable(list);
  }

  /**
   * This method will return the current variable.
   *
   * @param list we will add the current method to the list
   */
  public void getModifiedVariable(List list) {
    getUsedVariable(list);
  }

  /**
   * This method will return the current variable.
   *
   * @param list we will add the current method to the list
   */
  public void getUsedVariable(List list) {
    String varName;
    if (name != null) {
      varName = name;
    } else if (variable != null) {
      varName = variable.getName();
    } else {
      varName = expression.toStringExpression();//todo : do a better thing like evaluate this ??
    }
    if (!arrayContains(SPECIAL_VARS, name)) {
      list.add(new VariableUsage(type,
                                 varName,
                                 sourceStart,
                                 sourceEnd,
                                 beginLine,
                                 endLine,
                                 beginColumn,
                                 endColumn));
    }
  }

  public Expression expressionAt(int line, int column) {
    if (variable != null && variable.isAt(line, column)) return variable;
    if (expression != null && expression.isAt(line, column)) return expression;
    return null;
  }

  public void analyzeCode(PHPParser parser) {
    if (name == null) {
      if (variable != null) {
        variable.analyzeCode(parser);
      } else {
        expression.analyzeCode(parser);
      }
    }
  }
}
