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
 * A class constant.
 *
 * @author Matthieu Casanova
 * @version $Id: ClassConstant.java,v 1.1 2006/08/15 15:49:54 pago Exp $
 */
public class ClassConstant extends Statement implements Outlineable, PHPItem, IAsset {
    private final String path;

    private final String name;

    /**
     * The value can be null when there are parse errors.
     */
    private final Expression value;

    private final OutlineableWithChildren parent;

    private static transient Icon icon;

    private transient Position start;
    private transient Position end;

    private static final long serialVersionUID = 6115937167801653273L;


    /**
     * Create a node.
     *
     * @param path        the path
     * @param parent      the parent class
     * @param name        the name of the constant
     * @param value       the value (it could be null in case of parse error)
     * @param sourceStart starting offset
     * @param sourceEnd   ending offset
     * @param beginLine   begin line
     * @param endLine     ending line
     * @param beginColumn begin column
     * @param endColumn   ending column
     */
    public ClassConstant(String path,
                         OutlineableWithChildren parent,
                         String name,
                         Expression value,
                         int sourceStart,
                         int sourceEnd,
                         int beginLine,
                         int endLine,
                         int beginColumn,
                         int endColumn) {
        super(sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
        this.path = path;
        this.name = name;
        this.value = value;
        this.parent = parent;
    }


    public Expression expressionAt(int line, int column) {
        return null;
    }

    /**
     * Return the object into String.
     *
     * @param tab how many tabs (not used here
     * @return a String
     */
    public String toString(int tab) {
        return tabString(tab) + "const " + name + " = " + (value == null ? "?" : value.toStringExpression());
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

    /**
     * This method will analyze the code. by default it will do nothing
     */
    public void analyzeCode(PHPParser parser) {
    }

    /**
     * Returns the parent of the item.
     *
     * @return the parent
     */
    public OutlineableWithChildren getParent() {
        return parent;
    }

    /**
     * Give the name of the item.
     *
     * @return the name of the item
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the item type.
     * in {@link PHPItem#CLASS},{@link PHPItem#FIELD}, {@link PHPItem#INTERFACE}, {@link PHPItem#METHOD}
     *
     * @return the item type
     */
    public int getItemType() {
        return CLASS_CONSTANT;
    }

    public String getNameLowerCase() {
        return name.toLowerCase();
    }

    public String getPath() {
        return path;
    }

    public Icon getIcon() {
        if (icon == null) {
            icon = new ImageIcon(ClassDeclaration.class.getResource("/gatchan/phpparser/icons/field.png"));
        }
        return icon;
    }

    /**
     * Returns a brief description of the asset to be shown in the tree.
     */
    public String getShortString() {
        return toString();
    }

    /**
     * Returns a full description of the asset to be shown in the view's
     * status bar on when the mouse is over the asset in the tree.
     */
    public String getLongString() {
        return toString();
    }

    /**
     * Set the name of the asset
     */
    public void setName(String name) {
    }

    /**
     * Set the start position
     */
    public void setStart(Position start) {
        this.start = start;
    }

    /**
     * Returns the starting position.
     */
    public Position getStart() {
        return start;
    }

    /**
     * Set the end position
     */
    public void setEnd(Position end) {
        this.end = end;
    }

    /**
     * Returns the end position.
     */
    public Position getEnd() {
        return end;
    }

    public String toString() {
        return getName();
  }
}
