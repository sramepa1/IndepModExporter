package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;
import gatchan.phpparser.parser.PHPParseMessageEvent;

import java.util.List;

/**
 * A ConditionalExpression is like that : booleanExpression ? trueValue : falseValue;.
 *
 * @author Matthieu Casanova
 */
public final class ConditionalExpression extends OperatorExpression {
  private final Expression condition;
  private final Expression valueIfTrue;
  private final Expression valueIfFalse;

  public ConditionalExpression(Expression condition,
                               Expression valueIfTrue,
                               Expression valueIfFalse) {
    super(valueIfTrue.getType(), // we use the valueIfTrue type
          -1,
          condition.getSourceStart(),
          valueIfFalse.getSourceEnd(),
          condition.getBeginLine(),
          valueIfFalse.getEndLine(),
          condition.getBeginColumn(),
          valueIfFalse.getEndColumn());
    this.condition = condition;
    this.valueIfTrue = valueIfTrue;
    this.valueIfFalse = valueIfFalse;
  }

  public String toStringExpression() {
    String conditionString = condition.toStringExpression();
    String valueIfTrueString = valueIfTrue.toStringExpression();
    String valueIfFalse = this.valueIfFalse.toStringExpression();
    StringBuffer buff = new StringBuffer(8 +
                                         conditionString.length() +
                                         valueIfTrueString.length() +
                                         valueIfFalse.length());
    buff.append('(');
    buff.append(conditionString);
    buff.append(") ? ");
    buff.append(valueIfTrueString);
    buff.append(" : ");
    buff.append(valueIfFalse);
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
    condition.getModifiedVariable(list);
    valueIfTrue.getModifiedVariable(list);
    valueIfFalse.getModifiedVariable(list);
  }

  /**
   * Get the variables used.
   *
   * @param list the list where we will put variables
   */
  public void getUsedVariable(List list) {
    condition.getUsedVariable(list);
    valueIfTrue.getUsedVariable(list);
    valueIfFalse.getUsedVariable(list);
  }

  public Expression expressionAt(int line, int column) {
    if (condition.isAt(line, column)) return condition;
    if (valueIfTrue.isAt(line, column)) return valueIfTrue;
    if (valueIfFalse.isAt(line, column)) return valueIfFalse;
    return null;
  }

  public void analyzeCode(PHPParser parser) {
    Type typeFalse = valueIfFalse.getType();
    Type typeTrue = valueIfTrue.getType();
    if (typeFalse != typeTrue && !typeFalse.isEmpty() && !typeTrue.isEmpty()) {
      parser.fireParseMessage(new PHPParseMessageEvent(PHPParser.WARNING,
                                                       PHPParseMessageEvent.MESSAGE_CONDITIONAL_EXPRESSION_CHECK,
                                                       parser.getPath(),
                                                       "Conditional expression : warning, the true value is type "+typeTrue+" and the false value is "+typeFalse,
                                                       sourceStart,
                                                       sourceEnd,
                                                       beginLine,
                                                       endLine,
                                                       beginColumn,
                                                       endColumn));
    }
  }
}
