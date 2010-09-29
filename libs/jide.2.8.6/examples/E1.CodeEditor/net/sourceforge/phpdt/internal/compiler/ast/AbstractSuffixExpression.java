package net.sourceforge.phpdt.internal.compiler.ast;

/**
 * Variable suffix. class access or [something] Should it be an expression ?
 *
 * @author Matthieu Casanova
 */
public abstract class AbstractSuffixExpression extends Expression {
  protected AbstractSuffixExpression(Type type,
                                     int sourceStart,
                                     int sourceEnd,
                                     int beginLine,
                                     int endLine,
                                     int beginColumn,
                                     int endColumn) {
    super(type, sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
  }
}
