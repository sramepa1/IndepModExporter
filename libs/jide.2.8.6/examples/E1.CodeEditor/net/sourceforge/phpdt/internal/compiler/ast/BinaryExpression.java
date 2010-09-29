package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParserConstants;
import gatchan.phpparser.parser.PHPParser;

import java.util.List;

/**
 * a binary expression is a combination of two expressions with an operator.
 *
 * @author Matthieu Casanova
 * @version $Id: BinaryExpression.java,v 1.10 2005/10/27 07:15:16 kpouer Exp $
 */
public final class BinaryExpression extends OperatorExpression {
  /** The left expression. */
  private final Expression left;
  /** The right expression. */
  private final Expression right;

  public BinaryExpression(Expression left,
                          Expression right,
                          int operator,
                          int sourceStart,
                          int sourceEnd,
                          int beginLine,
                          int endLine,
                          int beginColumn,
                          int endColumn) {
    super(Type.UNKNOWN, operator, sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
    this.left = left;
    this.right = right;
    switch (operator) {
      case PHPParserConstants.OR_OR :
      case PHPParserConstants.AND_AND :
      case PHPParserConstants._ORL :
      case PHPParserConstants.XOR :
      case PHPParserConstants._ANDL:
      case PHPParserConstants.EQUAL_EQUAL:
      case PHPParserConstants.GT:
      case PHPParserConstants.LT:
      case PHPParserConstants.LE:
      case PHPParserConstants.GE:
      case PHPParserConstants.NOT_EQUAL:
      case PHPParserConstants.DIF:
      case PHPParserConstants.BANGDOUBLEEQUAL:
      case PHPParserConstants.TRIPLEEQUAL:
        type = Type.BOOLEAN;
      break;
      case PHPParserConstants.DOT:
        type = Type.STRING;
      break;
      case PHPParserConstants.BIT_AND:
      case PHPParserConstants.BIT_OR:
      case PHPParserConstants.BIT_XOR:
      case PHPParserConstants.LSHIFT:
      case PHPParserConstants.RSIGNEDSHIFT:
      case PHPParserConstants.RUNSIGNEDSHIFT:
      case PHPParserConstants.PLUS:
      case PHPParserConstants.MINUS:
      case PHPParserConstants.STAR:
      case PHPParserConstants.SLASH:
      case PHPParserConstants.REMAINDER:
        type = Type.INTEGER;
      break;
      case PHPParserConstants.INSTANCEOF:
        type = Type.BOOLEAN;
      break;
    }
  }

  public String toStringExpression() {
    String leftString = left.toStringExpression();
    String operatorString = operatorToString();
    String rightString = right.toStringExpression();
    StringBuffer buff = new StringBuffer(leftString.length() + operatorString.length() + rightString.length());
    buff.append(leftString);
    buff.append(operatorString);
    buff.append(rightString);
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
    left.getModifiedVariable(list);
    if (right != null) {
      right.getModifiedVariable(list);
    }
  }

  /**
   * Get the variables used.
   *
   * @param list the list where we will put variables
   */
  public void getUsedVariable(List list) {
    left.getUsedVariable(list);
    if (right != null) {
      right.getUsedVariable(list);
    }
  }

  public Expression expressionAt(int line, int column) {
    if (left.isAt(line, column)) return left;
    if (right != null && right.isAt(line, column)) return right;
    return null;
  }

  public void analyzeCode(PHPParser parser) {
    left.analyzeCode(parser);
    if (right != null) right.analyzeCode(parser);
    // todo analyze binary expression
  }
}
