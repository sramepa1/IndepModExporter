package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;

import java.util.List;

/**
 * An access to a key of an array.
 *
 * @author Matthieu Casanova
 * @version $Id: ArrayDeclarator.java,v 1.7 2005/12/03 20:28:13 kpouer Exp $
 */
public final class ArrayDeclarator extends AbstractVariable {
  /** The name of the array. */
  private final Expression prefix;

  /** The key. */
  private final Expression key;
  private static final long serialVersionUID = 4303473470051707778L;


  /**
   * Create an ArrayDeclarator.
   *
   * @param prefix    the prefix, it could be a variable.
   * @param key       the key
   * @param sourceEnd the end of the expression
   * @param endLine   the end of the line
   * @param endColumn the end of the column
   */
  public ArrayDeclarator(Expression prefix,
                         Expression key,
                         int sourceEnd,
                         int endLine,
                         int endColumn) {
    super(Type.UNKNOWN,
          prefix.getSourceStart(),
          sourceEnd,
          prefix.getBeginLine(),
          endLine,
          prefix.getBeginColumn(),
          endColumn);
    this.prefix = prefix;
    this.key = key;
  }

  /**
   * Return the expression as String.
   *
   * @return the expression
   */
  public String toStringExpression() {
    StringBuffer buff = new StringBuffer(prefix.toStringExpression());
    buff.append('[');
    if (key != null) {
      buff.append(key.toStringExpression());
    }
    buff.append(']');
    return buff.toString();
  }

  /**
   * Return the name of the variable.
   *
   * @return the name of the functionName variable
   */
  public String getName() {
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
    prefix.getModifiedVariable(list);
    if (key != null) {
      key.getModifiedVariable(list);
    }
  }

  /**
   * Get the variables used.
   *
   * @param list the list where we will put variables
   */
  public void getUsedVariable(List list) {
    if (key != null) {
      key.getUsedVariable(list);
    }
  }

  public Expression expressionAt(int line, int column) {
    if (prefix.isAt(line, column)) return prefix;
    if (key != null && key.isAt(line, column)) return key;
    return null;
  }

  public void analyzeCode(PHPParser parser) {
    prefix.analyzeCode(parser);
    if (key != null) {
      key.analyzeCode(parser);
    }
  }
}
