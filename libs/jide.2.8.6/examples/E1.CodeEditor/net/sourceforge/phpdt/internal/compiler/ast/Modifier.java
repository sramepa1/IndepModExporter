package net.sourceforge.phpdt.internal.compiler.ast;

import gatchan.phpparser.parser.PHPParser;
import gatchan.phpparser.parser.Token;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * @author Matthieu Casanova
 * @version $Id: Modifier.java,v 1.1 2006/08/15 15:49:54 pago Exp $
 */
public class Modifier extends AstNode
{
    public static final int FINAL = 1;
    public static final int STATIC = 2;
    public static final int PUBLIC = 4;
    public static final int PRIVATE = 8;
    public static final int PROTECTED = 16;
    public static final int CONST = 32;
    public static final int ABSTRACT = 64;

    private final int kind;
    private static final long serialVersionUID = -6959851555310749803L;


    private static final transient Map incompatibilityMap = new HashMap(2);

    static
    {
        Map finalIncompatibilities = new HashMap(1);
        finalIncompatibilities.put(toStringModifier(ABSTRACT), "Incompatible modifiers final / abstract");
        Map abstractIncompatibilities = new HashMap(1);
        abstractIncompatibilities.put(toStringModifier(FINAL), "Incompatible modifiers final / abstract");
        

        incompatibilityMap.put(toStringModifier(FINAL), finalIncompatibilities);
        incompatibilityMap.put(toStringModifier(ABSTRACT), abstractIncompatibilities);
    }

    public Modifier(Token token)
    {
        super(token.sourceStart,
              token.sourceEnd,
              token.beginLine,
              token.endLine,
              token.beginColumn,
              token.endColumn);
        if ("final".equalsIgnoreCase(token.image))
            kind = FINAL;
        else if ("static".equalsIgnoreCase(token.image))
            kind = STATIC;
        else if ("public".equalsIgnoreCase(token.image))
            kind = PUBLIC;
        else if ("private".equalsIgnoreCase(token.image))
            kind = PRIVATE;
        else if ("protected".equalsIgnoreCase(token.image))
            kind = PROTECTED;
        else if ("const".equalsIgnoreCase(token.image))
            kind = CONST;
        else if ("abstract".equalsIgnoreCase(token.image))
            kind = ABSTRACT;
        else
            throw new IllegalArgumentException("Invalid modifier token " + token.image);
    }


    public String toString(int tab)
    {
        return tabString(tab) + toStringModifier(kind);
    }


    public String toString()
    {
        return toStringModifier(kind);
    }

    private static String toStringModifier(int kind)
    {
        switch (kind)
        {
            case FINAL :
                return "final";
            case STATIC :
                return "static";
            case PUBLIC :
                return "public";
            case PRIVATE :
                return "private";
            case PROTECTED :
                return "protected";
            case CONST :
                return "const";
            case ABSTRACT :
                return "abstract";
            default:
                throw new IllegalArgumentException("Invalid modifier kind " + kind);
        }
    }

    public void getOutsideVariable(List list)
    {
    }

    public void getModifiedVariable(List list)
    {
    }

    public void getUsedVariable(List list)
    {
    }

    public void analyzeCode(PHPParser parser)
    {
    }

    public boolean isVisibilityModifier()
    {
        return kind == PUBLIC || kind == PRIVATE || kind == PROTECTED;
    }

    public int getKind()
    {
        return kind;
    }

    public void checkCompatibility(PHPParser parser, List modifiers)
    {
        Map incompatibilities = (Map) incompatibilityMap.get(toString());
        if (incompatibilities != null)
        {
            for (int i = 0; i < modifiers.size(); i++)
            {
                Modifier modifier = (Modifier) modifiers.get(i);
                String message = (String) incompatibilities.get(modifier.toString());
                if (message != null)
                    parser.fireParseError(message, modifier);
            }
        }
    }
}
