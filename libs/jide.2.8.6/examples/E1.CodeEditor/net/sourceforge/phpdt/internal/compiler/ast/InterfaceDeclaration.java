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
 * An interface declaration.
 *
 * @author Matthieu Casanova
 * @version $Id: InterfaceDeclaration.java,v 1.10 2006/05/18 14:23:03 kpouer Exp $
 */
public class InterfaceDeclaration extends Statement implements OutlineableWithChildren, PHPItem, IAsset {
    private final String path;
    private final transient OutlineableWithChildren parent;
    private final String name;

    private List children = new ArrayList();

    /**
     * The constants of the class (for php5).
     */
    private final List constants = new ArrayList();

    private static transient Icon icon;
    private String nameLowerCase;

    private transient Position start;
    private transient Position end;

    /**
     * The list of the super interfaces names. This list could be null
     */
    private List superInterfaces;

    /**
     * The methodsHeaders of the class.
     */
    private final List methodsHeaders = new ArrayList();

    private static final long serialVersionUID = -6768547707320365598L;

    public InterfaceDeclaration(String path,
                                OutlineableWithChildren parent,
                                String name,
                                List superInterfaces,
                                int sourceStart,
                                int beginLine,
                                int beginColumn) {
        this.path = path;
        this.parent = parent;
        this.name = name;
        this.superInterfaces = superInterfaces;
        this.sourceStart = sourceStart;
        this.beginLine = beginLine;
        this.beginColumn = beginColumn;
    }

    public String toString(int tab) {
        return null;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("interface ");
        buf.append(name);
        if (superInterfaces != null) {
            buf.append(" extends ");
            for (int i = 0; i < superInterfaces.size(); i++) {
                if (i != 0)
                    buf.append(", ");
                buf.append(superInterfaces.get(i));
            }
        }
        return buf.toString();
    }

    public void getOutsideVariable(List list) {
    }

    public void getModifiedVariable(List list) {
    }

    public void getUsedVariable(List list) {
    }

    public boolean add(Outlineable o) {
        return children.add(o);
    }

    /**
     * Add a constant to the class.
     *
     * @param constant the constant
     */
    public void addConstant(ClassConstant constant) {
        constants.add(constant);
    }

    /**
     * Add a method to the interface.
     *
     * @param method the method declaration
     */
    public void addMethod(MethodDeclaration method) {
        methodsHeaders.add(method.getMethodHeader());
        add(method);
    }

    public Outlineable get(int index) {
        return (Outlineable) children.get(index);
    }

    public int size() {
        return children.size();
    }

    public OutlineableWithChildren getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public String getNameLowerCase() {
        if (nameLowerCase == null) {
            nameLowerCase = name.toLowerCase();
        }
        return nameLowerCase;
    }

    public int getItemType() {
        return INTERFACE;
    }

    public String getPath() {
        return path;
    }

    public Icon getIcon() {
        // todo an interface icon
        if (icon == null) {
            icon = ImageReader.createImageIcon("/gatchan/phpparser/icons/class.png");
        }
        return icon;
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
        return name;
    }

    public String getLongString() {
        return name;
    }

    public void setName(String name) {
    }

    public Expression expressionAt(int line, int column) {
        //todo : fix interface declaration
        return null;
    }

    public void analyzeCode(PHPParser parser) {
        // todo : analyze the interface
    }

    public List getMethodsHeaders() {
        return methodsHeaders;
    }

    public List getSuperInterfaces() {
        return superInterfaces;
    }
}
