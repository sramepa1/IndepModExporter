package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;
import gatchan.phpparser.project.itemfinder.PHPItem;
import net.sourceforge.phpdt.internal.compiler.parser.Outlineable;
import net.sourceforge.phpdt.internal.compiler.parser.OutlineableWithChildren;
import sidekick.IAsset;

import javax.swing.*;
import javax.swing.text.Position;
import java.util.List;

/**
 * A variable declaration.
 *
 * @author Matthieu Casanova
 */
public final class VariableDeclaration extends Expression implements Outlineable, IAsset {
    private final AbstractVariable variable;

    /**
     * The value for variable initialization.
     */
    private Expression initialization;

    private transient OutlineableWithChildren parent;
    private boolean reference;


    private String operator;

    private transient Position start;
    private transient Position end;
    private Icon icon;

    private String cachedToString;

    /**
     * Create a variable.
     *
     * @param variable       the name of the variable
     * @param initialization the initialization (it could be null when you have a parse error)
     * @param operator       the assign operator
     * @param sourceStart    the start point
     * @param sourceEnd      the end point
     */
    public VariableDeclaration(OutlineableWithChildren parent,
                               AbstractVariable variable,
                               Expression initialization,
                               String operator,
                               int sourceStart,
                               int sourceEnd,
                               int beginLine,
                               int endLine,
                               int beginColumn,
                               int endColumn) {
        super(initialization == null ? Type.UNKNOWN : initialization.getType(),
                sourceStart,
                sourceEnd,
                beginLine,
                endLine,
                beginColumn,
                endColumn);
        this.initialization = initialization;
        this.variable = variable;
        variable.setType(type);
        this.operator = operator;
        this.parent = parent;
    }

    /**
     * Create a variable.
     *
     * @param variable    a variable (in case of $$variablename)
     * @param sourceStart the start point
     */
    public VariableDeclaration(OutlineableWithChildren parent,
                               AbstractVariable variable,
                               int sourceStart,
                               int sourceEnd,
                               int beginLine,
                               int endLine,
                               int beginColumn,
                               int endColumn) {
        super(Type.NULL, sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
        this.variable = variable;
        this.parent = parent;
    }

    public void setReference(boolean reference, int sourceStart, int beginLine, int beginColumn) {
        this.reference = reference;
        this.sourceStart = sourceStart;
        this.beginLine = beginLine;
        this.beginColumn = beginColumn;
    }

    /**
     * Return the variable into String.
     *
     * @return a String
     */
    public String toStringExpression() {
        if (cachedToString == null) {
            String variableString = variable.toStringExpression();
            if (initialization == null) {
                if (reference) return '&' + variableString;
                else return variableString;
            }
            else {
                //  final String operatorString = operatorToString();
                String initString = initialization.toStringExpression();
                StringBuffer buff = new StringBuffer(variableString.length() +
                        operator.length() +
                        initString.length() +
                        1);
                buff.append(variableString);
                buff.append(operator);
                buff.append(initString);
                cachedToString = buff.toString();
            }
        }
        return cachedToString;
    }

    public OutlineableWithChildren getParent() {
        return parent;
    }

    public String toString() {
        return toStringExpression();
    }


    /**
     * Get the variables from outside (parameters, globals ...)
     */
    public void getOutsideVariable(List list) {
    }

    /**
     * get the modified variables.
     */
    public void getModifiedVariable(List list) {
        variable.getUsedVariable(list);
        if (initialization != null) {
            initialization.getModifiedVariable(list);
        }
    }

    /**
     * Get the variables used.
     */
    public void getUsedVariable(List list) {
        if (initialization != null) {
            initialization.getUsedVariable(list);
        }
    }

    public String getName() {
        return variable.getName();
    }

    public Expression getInitialization() {
        return initialization;
    }

    public int getItemType() {
        return PHPItem.VARIABLE;
    }

    public Position getStart() {
        return start;
    }

    public void setStart(Position start) {
        this.start = start;
    }

    public Position getEnd() {
        return end;
    }

    public void setEnd(Position end) {
        this.end = end;
    }

    public Icon getIcon() {
        if (icon == null) {
            icon = ImageReader.createImageIcon("/gatchan/phpparser/icons/field.png");
        }
        return icon;
    }

    public String getShortString() {
        return toString();
    }

    public String getLongString() {
        return toString();
    }

    public void setName(String name) {
    }

    public Expression expressionAt(int line, int column) {
        if (variable.isAt(line, column)) return variable;
        if (initialization != null && initialization.isAt(line, column)) return initialization;
        return null;
    }

    public void analyzeCode(PHPParser parser) {
    }
}
