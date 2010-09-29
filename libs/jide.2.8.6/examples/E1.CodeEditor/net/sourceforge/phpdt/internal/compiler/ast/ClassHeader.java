package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;
import gatchan.phpparser.project.itemfinder.PHPItem;

import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The ClassHeader is that : class ClassName [extends SuperClassName].
 *
 * @author Matthieu Casanova
 * @version $Id: ClassHeader.java,v 1.12 2006/05/18 14:23:03 kpouer Exp $
 */
public class ClassHeader extends AstNode implements PHPItem, Serializable {
    /**
     * The path of the file containing this class.
     */
    private String path;

    /**
     * The name of the class.
     */
    private String className;

    private String nameLowerCase;

    /**
     * The name of the superclass.
     */
    private String superClassName;

    /**
     * The implemented interfaces. It could be null.
     */
    private List interfaceNames;

    /**
     * The methodsHeaders of the class.
     */
    private final List methodsHeaders = new ArrayList();

    /**
     * The constants of the class (for php5).
     */
    private final List constants = new ArrayList();

    private List modifiers = new ArrayList(3);
    /**
     * The fields of the class.
     * It contains {@link FieldDeclaration}
     */
    private final List fields = new ArrayList();

    private static transient Icon icon;

    private transient String cachedToString;
    private static final long serialVersionUID = 8213003151739601011L;

    public ClassHeader() {
    }

    public ClassHeader(String path,
                       String className,
                       String superClassName,
                       List interfaceNames,
                       int sourceStart,
                       int sourceEnd,
                       int beginLine,
                       int endLine,
                       int beginColumn,
                       int endColumn) {
        super(sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
        this.path = path;
        this.className = className;
        this.superClassName = superClassName;
        this.interfaceNames = interfaceNames;
    }

    public String toString(int tab) {
        StringBuffer buff = new StringBuffer(200);
        buff.append(tabString(tab));
        buff.append("class ");
        buff.append(className);
        if (superClassName != null) {
            buff.append(" extends ");
            buff.append(superClassName);
        }
        if (interfaceNames != null) {
            buff.append(" implements ");
            for (int i = 0; i < interfaceNames.size(); i++) {
                if (i != 0)
                    buff.append(", ");
                buff.append(interfaceNames.get(i));
            }
        }
        return buff.toString();
    }

    public String toString() {
        if (cachedToString == null) {
            StringBuffer buff = new StringBuffer(200);
            buff.append(className);
            if (superClassName != null) {
                buff.append(':');
                buff.append(superClassName);
            }
            cachedToString = buff.toString();
        }
        return cachedToString;
    }

    public void getOutsideVariable(List list) {
    }

    public void getModifiedVariable(List list) {
    }

    public void getUsedVariable(List list) {
    }

    public void addModifier(Modifier modifier) {
        modifiers.add(modifier);
    }

    /**
     * Returns the name of the class.
     *
     * @return the name of the class
     */
    public String getName() {
        return className;
    }

    public String getNameLowerCase() {
        if (nameLowerCase == null) {
            nameLowerCase = className.toLowerCase();
        }
        return nameLowerCase;
    }

    /**
     * Returns the name of the superclass.
     *
     * @return the name of the superclass
     */
    public String getSuperClassName() {
        return superClassName;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ClassHeader)) return false;
        return ((ClassHeader) obj).getName().equals(className);
    }

    public String getPath() {
        return path;
    }

    public Icon getIcon() {
        if (icon == null) {
            icon = ImageReader.createImageIcon("/gatchan/phpparser/icons/class.png");
        }
        return icon;
    }

    /**
     * Add a method to the class.
     *
     * @param method the method declaration
     */
    public void addMethod(MethodHeader method) {
        methodsHeaders.add(method);
    }

    /**
     * Add a method to the class.
     *
     * @param field the method declaration
     */
    public void addField(FieldDeclaration field) {
        fields.add(field);
    }

    /**
     * Add a constant to the class.
     *
     * @param constant the constant
     */
    public void addConstant(ClassConstant constant) {
        constants.add(constant);
    }

    public List getMethodsHeaders() {
        return methodsHeaders;
    }

    /**
     * Returns the list of the field of this class.
     * It contains {@link FieldDeclaration}
     *
     * @return the list of fields of the class
     */
    public List getFields() {
        return fields;
    }

    public int getItemType() {
        return CLASS;
    }

    public void analyzeCode(PHPParser parser) {
    }

    public List getInterfaceNames() {
        return interfaceNames;
    }
}
