package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParseErrorEvent;
import gatchan.phpparser.parser.PHPParseMessageEvent;
import gatchan.phpparser.parser.PHPParser;
import gatchan.phpparser.project.itemfinder.PHPItem;
import net.sourceforge.phpdt.internal.compiler.parser.Outlineable;
import net.sourceforge.phpdt.internal.compiler.parser.OutlineableWithChildren;
import sidekick.IAsset;

import javax.swing.*;
import javax.swing.text.Position;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * This class is my ClassDeclaration declaration for php. It is similar to org.eclipse.jdt.internal.compiler.ast.TypeDeclaration
 * It directly extends AstNode because a class cannot appear anywhere in php
 *
 * @author Matthieu Casanova
 * @version $Id: ClassDeclaration.java,v 1.16 2006/05/18 14:23:03 kpouer Exp $
 */
public final class ClassDeclaration extends Statement implements OutlineableWithChildren, IAsset {

    private ClassHeader classHeader;

    private int bodyLineStart;
    private int bodyColumnStart;
    private int bodyLineEnd;
    private int bodyColumnEnd;

    /**
     * The constructor of the class.
     */
    private MethodDeclaration constructor;

    private List methods = new ArrayList();
    private final transient OutlineableWithChildren parent;
    /**
     * The outlineable children (those will be in the node array too.
     */
    private final List children = new ArrayList();

    private transient Position start;
    private transient Position end;
    private static Icon icon;

    /**
     * Create a class giving starting and ending offset.
     *
     * @param sourceStart starting offset
     * @param sourceEnd   ending offset
     */
    public ClassDeclaration(OutlineableWithChildren parent,
                            ClassHeader classHeader,
                            int sourceStart,
                            int sourceEnd,
                            int beginLine,
                            int endLine,
                            int beginColumn,
                            int endColumn) {
        super(sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
        this.parent = parent;
        this.classHeader = classHeader;
    }

    /**
     * Add a method to the class.
     *
     * @param method the method declaration
     */
    public void addMethod(MethodDeclaration method) {
        classHeader.addMethod(method.getMethodHeader());
        methods.add(method);
        add(method);
        if (method.getName().equals(classHeader.getName())) {
            constructor = method;
        }
    }

    /**
     * Add a method to the class.
     *
     * @param field the method declaration
     */
    public void addField(FieldDeclaration field) {
        VariableDeclaration c = field.getVariable();
        children.add(c);
        classHeader.addField(field);
    }

    /**
     * Add a new constant to the class.
     *
     * @param constant the constant
     */
    public void addConstant(ClassConstant constant) {
        children.add(constant);
        classHeader.addConstant(constant);
    }

    public boolean add(Outlineable o) {
        return children.add(o);
    }

    /**
     * Tell if the class has a constructor.
     *
     * @return a boolean
     */
    public boolean hasConstructor() {
        return constructor != null;
    }

    /**
     * Return the class as String.
     *
     * @param tab how many tabs before the class
     * @return the code of this class into String
     */
    public String toString(int tab) {
        return classHeader.toString(tab) + toStringBody(tab);
    }

    public String toString() {
        return classHeader.toString();
    }

    /**
     * Return the body of the class as String.
     *
     * @param tab how many tabs before the body of the class
     * @return the body as String
     */
    private String toStringBody(int tab) {
        StringBuffer buff = new StringBuffer(" {");//$NON-NLS-1$
        List fields = classHeader.getFields();
        if (fields != null) {
            for (int i = 0; i < fields.size(); i++) {
                FieldDeclaration field = (FieldDeclaration) fields.get(i);
                buff.append('\n'); //$NON-NLS-1$
                buff.append(field.toString(tab + 1));
                buff.append(';');//$NON-NLS-1$
            }
        }
        for (int i = 0; i < methods.size(); i++) {
            MethodDeclaration o = (MethodDeclaration) methods.get(i);
            buff.append('\n');//$NON-NLS-1$
            buff.append(o.toString(tab + 1));
        }
        buff.append('\n').append(tabString(tab)).append('}'); //$NON-NLS-2$ //$NON-NLS-1$
        return buff.toString();
    }

    public OutlineableWithChildren getParent() {
        return parent;
    }

    public Outlineable get(int index) {
        return (Outlineable) children.get(index);
    }

    public int size() {
        return children.size();
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
        return classHeader.getName();
    }

    public int getItemType() {
        return PHPItem.CLASS;
    }

    public MethodDeclaration insideWichMethodIsThisOffset(int line, int column) {
        for (int i = 0; i < methods.size(); i++) {
            MethodDeclaration methodDeclaration = (MethodDeclaration) methods.get(i);
            if (line == methodDeclaration.getBodyLineStart() && column > methodDeclaration.getBodyColumnStart())
                return methodDeclaration;
            if (line == methodDeclaration.getBodyLineEnd() && column < methodDeclaration.getBodyColumnEnd())
                return methodDeclaration;
            if (line > methodDeclaration.getBodyLineStart() && line < methodDeclaration.getBodyLineEnd())
                return methodDeclaration;
        }
        return null;
    }

    public ClassHeader getClassHeader() {
        return classHeader;
    }

    public int getBodyLineStart() {
        return bodyLineStart;
    }

    public void setBodyLineStart(int bodyLineStart) {
        this.bodyLineStart = bodyLineStart;
    }

    public int getBodyColumnStart() {
        return bodyColumnStart;
    }

    public void setBodyColumnStart(int bodyColumnStart) {
        this.bodyColumnStart = bodyColumnStart;
    }

    public int getBodyLineEnd() {
        return bodyLineEnd;
    }

    public void setBodyLineEnd(int bodyLineEnd) {
        this.bodyLineEnd = bodyLineEnd;
        setEndLine(bodyLineEnd);
    }

    public int getBodyColumnEnd() {
        return bodyColumnEnd;
    }

    public void setBodyColumnEnd(int bodyColumnEnd) {
        this.bodyColumnEnd = bodyColumnEnd;
        setEndColumn(bodyColumnEnd);
    }

    public Icon getIcon() {
        if (icon == null) {
            icon = ImageReader.createImageIcon("/gatchan/phpparser/icons/class.png");
        }
        return icon;
    }

    public String getShortString() {
        return toString();
    }

    public String getLongString() {
        return toString();
    }

    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }

    public void setStart(Position start) {
        this.start = start;
    }

    public void setEnd(Position end) {
        this.end = end;
    }

    public void setName(String name) {
    }

    public Expression expressionAt(int line, int column) {
        for (int i = 0; i < methods.size(); i++) {
            MethodDeclaration methodDeclaration = (MethodDeclaration) methods.get(i);
            if (methodDeclaration.isAt(line, column)) return methodDeclaration.expressionAt(line, column);
        }
        List fields = classHeader.getFields();
        for (int i = 0; i < fields.size(); i++) {
            FieldDeclaration field = (FieldDeclaration) fields.get(i);
            if (field.isAt(line, column)) return field.expressionAt(line, column);
        }
        return null;
    }

    public void analyzeCode(PHPParser parser) {
        List fields = classHeader.getFields();
        Set methodsNames = new HashSet(methods.size());
        Set fieldNames = new HashSet(fields.size());
        for (int i = 0; i < methods.size(); i++) {
            MethodDeclaration methodDeclaration = (MethodDeclaration) methods.get(i);
            methodDeclaration.analyzeCode(parser);
            String name = methodDeclaration.getName();
            checkMethod(methodsNames, name, methodDeclaration.getMethodHeader(), parser,
                    "this method name is already used by another field or method in the class : ");
        }

        for (int i = 0; i < fields.size(); i++) {
            FieldDeclaration fieldDeclaration = (FieldDeclaration) fields.get(i);
            String name = fieldDeclaration.getName();
            checkField(methodsNames, fieldNames, name, fieldDeclaration, parser);
        }


    }

    private void checkMethod(Set itemNames, String name, AstNode node, PHPParser parser, String msg) {
        if (!itemNames.add(name)) {
            // the method name already exists in this class this is an error
            parser.fireParseError(new PHPParseErrorEvent(PHPParser.ERROR,
                    parser.getPath(),
                    msg + name,
                    node.getSourceStart(),
                    node.getSourceEnd(),
                    node.getBeginLine(),
                    node.getEndLine(),
                    node.getBeginColumn(),
                    node.getEndColumn()));
        }
    }

    private void checkField(Set methodNames, Set fieldNames, String name, AstNode node, PHPParser parser) {
        if (!fieldNames.add(name)) {
            // the method name already exists in this class this is an error
            parser.fireParseError(new PHPParseErrorEvent(PHPParser.ERROR,
                    parser.getPath(),
                    "this field name is already used by another field or method in the class : " + name,
                    node.getSourceStart(),
                    node.getSourceEnd(),
                    node.getBeginLine(),
                    node.getEndLine(),
                    node.getBeginColumn(),
                    node.getEndColumn()));
        }
        else if (methodNames.contains(name)) {
            parser.fireParseMessage(new PHPParseMessageEvent(PHPParser.WARNING,
                    PHPParseMessageEvent.MESSAGE_METHOD_FIELD_WITH_SAME_NAME,
                    parser.getPath(),
                    "a method is defined with the same name " + name,
                    node.getSourceStart(),
                    node.getSourceEnd(),
                    node.getBeginLine(),
                    node.getEndLine(),
                    node.getBeginColumn(),
                    node.getEndColumn()));
        }
    }

}
