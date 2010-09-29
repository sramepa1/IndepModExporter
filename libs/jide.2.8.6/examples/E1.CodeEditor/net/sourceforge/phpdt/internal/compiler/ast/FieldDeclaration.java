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
 * A Field declaration. This is a variable declaration for a php class In fact it's an array of VariableUsage, since a
 * field could contains several var : var $toto,$tata;
 *
 * @author Matthieu Casanova
 */
public final class FieldDeclaration extends Statement implements Outlineable, PHPItem, IAsset {
    private List modifiers;
    /**
     * The path of the file containing this field.
     */
    private String path;

    /**
     * The variables.
     */
    private final VariableDeclaration variable;

    private String nameLowerCase;
    /**
     * The parent do not need to be serialized.
     */
    private final transient OutlineableWithChildren parent;

    private static transient Icon icon;

    private transient Position start;
    private transient Position end;

    private transient String cachedToString;

    /**
     * Create a field. with public visibility
     *
     * @param path        the path
     * @param variable    the variable of the field
     * @param parent      the parent class
     * @param sourceStart the sourceStart
     * @param sourceEnd   source end
     * @param beginLine   begin line
     * @param endLine     end line
     * @param beginColumn begin column
     * @param endColumn   end column
     */
    public FieldDeclaration(String path,
                            VariableDeclaration variable,
                            OutlineableWithChildren parent,
                            int sourceStart,
                            int sourceEnd,
                            int beginLine,
                            int endLine,
                            int beginColumn,
                            int endColumn) {
        this(null,
                path,
                variable,
                parent,
                sourceStart,
                sourceEnd,
                beginLine,
                endLine,
                beginColumn,
                endColumn);
    }

    /**
     * Create a field.
     *
     * @param modifiers   a list of {@link Modifier}
     * @param path        the path
     * @param variable    the variable of the field
     * @param parent      the parent class
     * @param sourceStart the sourceStart
     * @param sourceEnd   source end
     * @param beginLine   begin line
     * @param endLine     end line
     * @param beginColumn begin column
     * @param endColumn   end column
     */
    public FieldDeclaration(List modifiers,
                            String path,
                            VariableDeclaration variable,
                            OutlineableWithChildren parent,
                            int sourceStart,
                            int sourceEnd,
                            int beginLine,
                            int endLine,
                            int beginColumn,
                            int endColumn) {
        super(sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
        this.modifiers = modifiers;
        this.path = path;
        this.variable = variable;
        this.parent = parent;
    }

    /**
     * Return the object into String.
     *
     * @param tab how many tabs (not used here
     * @return a String
     */
    public String toString(int tab) {
        // todo : rewrite tostring method
        StringBuffer buff = new StringBuffer(tabString(tab));
        buff.append("var ");
        buff.append(variable.toStringExpression());
        return buff.toString();
    }


    public String toString() {
        return getName();
    }

    public OutlineableWithChildren getParent() {
        return parent;
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
    }

    public String getName() {
        if (cachedToString == null) {
            cachedToString = variable.getName();
        }
        return cachedToString;
    }

    public String getNameLowerCase() {
        if (nameLowerCase == null) {
            nameLowerCase = variable.getName().toLowerCase();
        }
        return nameLowerCase;
    }


    public int getItemType() {
        return FIELD;
    }

    public String getPath() {
        return path;
    }

    public Icon getIcon() {
        if (icon == null) {
            icon = ImageReader.createImageIcon("/gatchan/phpparser/icons/field.png");
        }
        return icon;
    }

    public VariableDeclaration getVariable() {
        return variable;
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

    public String getShortString() {
        return toString();
    }

    public String getLongString() {
        return toString();
    }

    public void setName(String name) {
    }

    public Expression expressionAt(int line, int column) {
        if (variable.isAt(line, column)) return variable.expressionAt(line, column);
        return null;
    }

    public void analyzeCode(PHPParser parser) {
        // variable.analyzeCode(parser); no need VariableDeclaration is not used
    }
}
