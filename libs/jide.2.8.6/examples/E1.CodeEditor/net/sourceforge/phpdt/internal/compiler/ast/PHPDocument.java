package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;
import gatchan.phpparser.project.itemfinder.PHPItem;
import net.sourceforge.phpdt.internal.compiler.parser.Outlineable;
import net.sourceforge.phpdt.internal.compiler.parser.OutlineableWithChildren;
import sidekick.IAsset;

import javax.swing.*;
import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.List;

/**
 * It's a php document. This class is an outlineable object It will contains html and php
 *
 * @author Matthieu Casanova
 */
public final class PHPDocument implements OutlineableWithChildren, IAsset {
  /** The nodes. It will include html nodes or php nodes */
  private AstNode[] nodes;

  private final String name;

  /** The outlineable children (those will be in the node array too. */
  private final List children = new ArrayList();

  private transient Position start;
  private transient Position end;

  /**
   * Create the PHPDocument.
   *
   * @param name the nale f the document
   */
  public PHPDocument(String name) {
    this.name = name;
  }

  /**
   * Return the php document as String.
   *
   * @return a string representation of the object.
   */
  public String toString() {
    StringBuffer buff = new StringBuffer();
    AstNode node;
    if (nodes != null) {
      int i;
      for (i = 0; i < nodes.length; i++) {
        node = nodes[i];
        if (node == null) {
          break;
        }
        buff.append(node.toString(0));
        if (node instanceof HTMLCode) {
          buff.append('\n');
        } else {
          buff.append(";\n");
        }
      }
    }
    return buff.toString();
  }

  /**
   * Add an outlineable object.
   *
   * @param o the new outlineable
   *
   * @return does the addition worked ?
   */
  public boolean add(Outlineable o) {
    return children.add(o);
  }

  /**
   * Return the outlineable at the index.
   *
   * @param index the index
   *
   * @return an outlineable object
   */
  public Outlineable get(int index) {
    return (Outlineable) children.get(index);
  }

  /**
   * The number of outlineable children.
   *
   * @return the number of children that are outlineable
   */
  public int size() {
    return children.size();
  }


  /**
   * Get the parent of the object.
   *
   * @return null
   */
  public OutlineableWithChildren getParent() {
    return null;
  }

  /**
   * Analyze the code of a php document.
   *
   * @param phpParser the php parser
   */
  public void analyzeCode(PHPParser phpParser) {
    if (nodes != null) {
      for (int i = 0; i < nodes.length; i++) {
        AstNode node = nodes[i];
        if (node == null) break;
        node.analyzeCode(phpParser);
      }
    }
  }

  public String getName() {
    return name;
  }

  /**
   * Give the method at the line and column given. It will returns null if no method can be found at the offset.
   *
   * @param line the line
   * @param column the offset
   * @return the method at the offset or null
   */
  public ClassDeclaration classAtOffset(int line, int column) {
    for (int i = 0; i < children.size(); i++) {
      Outlineable outlineable = (Outlineable) children.get(i);
      if (outlineable.getItemType() == PHPItem.CLASS) {
        ClassDeclaration classDeclaration = (ClassDeclaration) outlineable;
        if (line == classDeclaration.getBodyLineStart() && column > classDeclaration.getBodyColumnStart()) return classDeclaration;
        if (line == classDeclaration.getBodyLineEnd() && column < classDeclaration.getBodyColumnEnd()) return classDeclaration;
        if (line > classDeclaration.getBodyLineStart() && line < classDeclaration.getBodyLineEnd()) return classDeclaration;
      }
    }
    return null;
  }

  /**
   * Give the method at the line and column given. It will returns null if no method can be found at the offset.
   *
   * @param line the line
   * @param column the offset
   * @return the method at the offset or null
   */
  public MethodDeclaration methodAtOffset(int line, int column) {
    for (int i = 0; i < children.size(); i++) {
      Outlineable outlineable = (Outlineable) children.get(i);
      if (outlineable.getItemType() == PHPItem.METHOD) {
        MethodDeclaration methodDeclaration = (MethodDeclaration) outlineable;
        if (line == methodDeclaration.getBodyLineStart() && column > methodDeclaration.getBodyColumnStart()) return methodDeclaration;
        if (line == methodDeclaration.getBodyLineEnd() && column < methodDeclaration.getBodyColumnEnd()) return methodDeclaration;
        if (line > methodDeclaration.getBodyLineStart() && line < methodDeclaration.getBodyLineEnd()) return methodDeclaration;
      }
    }
    return null;
  }

  /**
   * Returns the statement at the given position.
   *
   * @param line the line
   * @param column the column
   * @return the statement at the position
   */
  public Statement getStatementAt(int line, int column) {
    Statement statement = null;
    for (int i = 0; i < nodes.length; i++) {
      statement = (Statement) nodes[i];
      if (statement == null) break;

      if (line == statement.getBeginLine() && column > statement.getBeginColumn()) return statement;
      if (line == statement.getEndLine() && column < statement.getEndColumn()) return statement;
      if (line > statement.getBeginLine() && line < statement.getEndLine()) return statement;
    }
    return statement;
  }

  /**
   * Set the nodes of the document.
   *
   * @param nodes the nodes
   */
  public void setNodes(AstNode[] nodes) {
    this.nodes = nodes;
  }

  public int getItemType() {
    return PHPItem.DOCUMENT;
  }

  public Position getEnd() {
    return end;
  }

  public void setEnd(Position end) {
    this.end = end;
  }

  public Position getStart() {
    return start;
  }

  public void setStart(Position start) {
    this.start = start;
  }

  public Icon getIcon() {
    return null;
  }

  public String getShortString() {
    return "/";
  }

  public String getLongString() {
    return "/";
  }

  public void setName(String name) {
  }

}