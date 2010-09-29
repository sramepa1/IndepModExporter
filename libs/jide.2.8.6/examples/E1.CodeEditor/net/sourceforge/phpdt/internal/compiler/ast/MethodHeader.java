package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;
import gatchan.phpparser.project.itemfinder.PHPItem;
import net.sourceforge.phpdt.internal.compiler.ast.declarations.VariableUsage;

import javax.swing.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author Matthieu Casanova
 * @version $Id: MethodHeader.java,v 1.10 2006/04/21 10:24:10 kpouer Exp $
 */
public class MethodHeader extends Statement implements PHPItem, Serializable {
    private final List modifiers;
    /**
     * The path of the file containing this class.
     */
    private String path;

    /**
     * The name of the method.
     */
    private String name;

    /**
     * Indicate if the method returns a reference.
     */
    private boolean reference;

    /**
     * The arguments.
     */
    private List arguments;

    private String cachedToString;

    private transient Icon icon;

    private String nameLowerCase;

    public MethodHeader(String path,
                        List modifiers,
                        String name,
                        boolean reference,
                        List arguments,
                        int sourceStart,
                        int sourceEnd,
                        int beginLine,
                        int endLine,
                        int beginColumn,
                        int endColumn) {
        super(sourceStart, sourceEnd, beginLine, endLine, beginColumn, endColumn);
        this.modifiers = modifiers;
        this.path = path;
        this.name = name;
        this.reference = reference;
        this.arguments = arguments;
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

    public String toString() {
        if (cachedToString == null) {
            StringBuffer buff = new StringBuffer(100);
            if (reference) buff.append('&');
            buff.append(name);
            buff.append('(');
            if (arguments != null) {
                for (int i = 0; i < arguments.size(); i++) {
                    FormalParameter o = (FormalParameter) arguments.get(i);
                    buff.append(o.toStringExpression());
                    if (i != (arguments.size() - 1)) {
                        buff.append(", ");
                    }
                }
            }
            buff.append(')');
            cachedToString = buff.toString();
        }
        return cachedToString;
    }

    public String toString(int tab) {
        return tabString(tab) + toString();
    }

    public void getOutsideVariable(List list) {
    }

    public void getModifiedVariable(List list) {
    }

    public void getUsedVariable(List list) {
    }

    public int getArgumentsCount() {
        return arguments.size();
    }

    public void getParameters(List list) {
        if (arguments != null) {
            for (int i = 0; i < arguments.size(); i++) {
                FormalParameter variable = (FormalParameter) arguments.get(i);
                VariableUsage variableUsage = new VariableUsage(Type.UNKNOWN,
                        variable.getName(),
                        variable.getSourceStart(),
                        variable.getSourceEnd(),
                        variable.getBeginLine(),
                        variable.getEndLine(),
                        variable.getBeginColumn(),
                        variable.getEndColumn());
                list.add(variableUsage);
            }
        }
    }

    public String getPath() {
        return path;
    }

    public int getItemType() {
        return METHOD;
    }

    public Icon getIcon() {
        if (icon == null) {
            icon = ImageReader.createImageIcon("/gatchan/phpparser/icons/method.png");
        }
        return icon;
    }

    public Expression expressionAt(int line, int column) {
        if (arguments != null) {
            for (int i = 0; i < arguments.size(); i++) {
                FormalParameter formalParameter = (FormalParameter) arguments.get(i);
                if (formalParameter.isAt(line, column)) return formalParameter;
            }
        }
        return null;
    }

    public void analyzeCode(PHPParser parser) {
        checkModifiers(parser);
    }

    private void checkModifiers(PHPParser parser) {
        if (modifiers == null)
            return;

        Set modifierKinds = new HashSet(5);
        for (int i = 0; i < modifiers.size(); i++) {
            Modifier modifier = (Modifier) modifiers.get(i);
            if (modifier.isVisibilityModifier()) {
                if (!modifierKinds.add(Integer.toString(-1))) {
                    // il y avait déjà un modifier de visibility
                    parser.fireParseError("You already have a visibility modifier", modifier);
                }
            }
            else if (!modifierKinds.add(modifier.toString())) {
                parser.fireParseError("Duplicate modifier " + modifier.toString(), modifier);
            }
            else
                modifier.checkCompatibility(parser, modifiers);
        }
    }
}
