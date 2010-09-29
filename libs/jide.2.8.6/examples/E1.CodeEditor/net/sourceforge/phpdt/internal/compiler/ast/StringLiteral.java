/*******************************************************************************
 * Copyright (c) 2000, 2001, 2002 International Business Machines Corp. and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v0.5
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v05.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/
package net.sourceforge.phpdt.internal.compiler.ast;

import java.util.List;

import gatchan.phpparser.parser.Token;
import gatchan.phpparser.parser.Token;
import gatchan.phpparser.parser.PHPParser;

public final class StringLiteral extends Literal {
  private String source;

  private AbstractVariable[] variablesInside;

  public StringLiteral(Token token) {
    this(token.image,
         token.sourceStart,
         token.sourceEnd,
         token.beginLine,
         token.endLine,
         token.beginColumn,
         token.endColumn,
         null);
  }

  public StringLiteral(String source,
                       int sourceStart,
                       int sourceEnd,
                       int beginLine,
                       int endLine,
                       int beginColumn,
                       int endColumn) {
    this(source, sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn, null);
  }

  public StringLiteral(String source,
                       int sourceStart,
                       int sourceEnd,
                       int beginLine,
                       int endLine,
                       int beginColumn,
                       int endColumn,
                       AbstractVariable[] variablesInside) {
    super(Type.STRING, sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
    this.source = source;
    this.variablesInside = variablesInside;
  }

  /**
   * Return the expression as String.
   *
   * @return the expression
   */
  public String toStringExpression() {
    return source;
  }

  public void getUsedVariable(List list) {
    if (variablesInside != null) {
      for (int i = 0; i < variablesInside.length; i++) {
        variablesInside[i].getUsedVariable(list);
      }
    }
  }

  public void analyzeCode(PHPParser parser) {
    if (variablesInside != null) {
      for (int i = 0; i < variablesInside.length; i++) {
        variablesInside[i].analyzeCode(parser);
      }
    }
  }

}
