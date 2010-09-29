package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;
import gatchan.phpparser.parser.PHPParserConstants;

import java.util.List;

/**
 * Any class access.
 *
 * @author Matthieu Casanova
 * @version $Id: ClassAccess.java,v 1.8 2005/12/03 20:28:58 kpouer Exp $
 */
public final class ClassAccess extends AbstractVariable {

  private final Expression prefix;

  /** the suffix. */
  private final Expression suffix;

  /** the accessType of access. */
  private final int accessType;
  private static final long serialVersionUID = -7498661310867405981L;


  /**
   * Instantiate a class access.
   *
   * @param prefix    usualy the class name
   * @param suffix    the field or method called (it can be null in case of parse error)
   * @param type      the accessType of access
   * @param sourceEnd the end offset
   * @param endLine   the end line
   * @param endColumn the end column
   */
  public ClassAccess(Expression prefix,
                     Expression suffix,
                     int type,
                     int sourceEnd,
                     int endLine,
                     int endColumn) {
    super(Type.UNKNOWN,prefix.getSourceStart(), sourceEnd, prefix.getBeginLine(), endLine, prefix.getBeginColumn(), endColumn);
    this.prefix = prefix;
    this.suffix = suffix;
    accessType = type;
  }

  private String toStringOperator() {
    return PHPParserConstants.tokenImage[accessType];
  }

  /**
   * Return the expression as String.
   *
   * @return the expression
   */
  public String toStringExpression() {
    String prefixString = prefix.toStringExpression();
    String operatorString = toStringOperator();
    StringBuffer buff = new StringBuffer(prefixString.length() + operatorString.length() + 100);
    buff.append(prefixString);
    buff.append(operatorString);
    if (suffix != null) {
      String suffixString = suffix.toStringExpression();
      buff.append(suffixString);
    }
    return buff.toString();
  }

  /**
   * Returns the name of the class. todo: find a better way to handle this
   *
   * @return the name of the variable
   */
  public String getName() {
    if (prefix instanceof AbstractVariable) {
      return ((AbstractVariable) prefix).getName();
    }
    return prefix.toStringExpression();
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
    prefix.getUsedVariable(list);
    if (suffix != null) {
      suffix.getUsedVariable(list);
    }
  }

  public Expression expressionAt(int line, int column) {
    if (prefix.isAt(line, column)) return prefix;
    if (suffix != null && suffix.isAt(line, column)) return suffix;
    return null;
  }

  public void analyzeCode(PHPParser parser) {
    prefix.analyzeCode(parser);
    if (suffix != null) suffix.analyzeCode(parser);
  }

  public Expression getPrefix() {
    return prefix;
  }
}
