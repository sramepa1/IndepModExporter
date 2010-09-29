package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;

import java.util.List;
import java.io.Serializable;

/** @author Matthieu Casanova */
public final class FormalParameter extends Expression implements Serializable {

  private String name;

  private boolean reference;

  private String defaultValue;

  public FormalParameter() {
  }

  public FormalParameter(String name,
                         boolean reference,
                         String defaultValue,
                         int sourceStart,
                         int sourceEnd,
                         int beginLine,
                         int endLine,
                         int beginColumn,
                         int endColumn) {
    super(Type.UNKNOWN, sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
    this.name = name;
    this.reference = reference;
    this.defaultValue = defaultValue;
  }

  public FormalParameter(String name,
                         boolean reference,
                         int sourceStart,
                         int sourceEnd,
                         int beginLine,
                         int endLine,
                         int beginColumn,
                         int endColumn) {
    this(name, reference, null, sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
  }

  public String toStringExpression() {
    StringBuffer buff = new StringBuffer(200);
    if (reference) {
      buff.append('&');
    }
    buff.append('$').append(name);
    if (defaultValue != null) {
      buff.append('=');
      buff.append(defaultValue);
    }
    return buff.toString();
  }

  public final String toString() {
    return toStringExpression();
  }

  public void getOutsideVariable(List list) {
  }

  public void getModifiedVariable(List list) {
  }

  public void getUsedVariable(List list) {
  }

  public String getName() {
    return name;
  }

  public Expression expressionAt(int line, int column) {
    return null;
  }

  public void analyzeCode(PHPParser parser) {
  }
}
