/*
 * @(#)SpecialCustomTypeConverter.java 5/12/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.ObjectConverter;

/**
 */
public class SpecialCustomTypeConverter implements ObjectConverter {
    public static ConverterContext CONTEXT = new ConverterContext("SpecialCustomType");

    public String toString(Object object, ConverterContext context) {
        if (object instanceof CustomType) {
            CustomType customType = (CustomType) object;
            return "(" + customType.getIntValue() + " ==> " + customType.getStringValue() + ")";
        }
        return null;
    }

    public boolean supportToString(Object object, ConverterContext context) {
        return true;
    }

    public Object fromString(String string, ConverterContext context) {
        if (string == null || string.trim().length() == 0) {
            return null;
        }
        if (string.startsWith("(")) {
            string = string.substring(1);
        }
        if (string.endsWith(")")) {
            string = string.substring(0, string.length() - 1);
        }
        int colon = string.indexOf(" ==> ");
        if (colon == -1) {
            return new CustomType(0, string);
        }
        else {
            String intValue = string.substring(0, colon);
            String stringValue = string.substring(colon + " ==> ".length());
            return new CustomType(Integer.parseInt(intValue), stringValue);
        }
    }

    public boolean supportFromString(String string, ConverterContext context) {
        return true;
    }
}
