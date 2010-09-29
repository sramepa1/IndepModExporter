package net.sourceforge.phpdt.internal.compiler.ast;

/**
 * @author Matthieu Casanova
 */
public abstract class MagicLiteral extends Literal {

  protected MagicLiteral(Type type, int sourceStart, int sourceEnd, int beginLine, int endLine, int beginColumn, int endColumn) {
    super(type, sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
  }
}
