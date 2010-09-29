/*
 * @(#)YesNoConverter.java 1/8/2007
 *
 * Copyright 2002 - 2007 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.ObjectConverter;

/**
 * Converter which converts Boolean to String Yes or No and converts it back.
 */
public class YesNoConverter implements ObjectConverter {

    public static final ConverterContext CONTEXT = new ConverterContext("YesNo");

    YesNoConverter() {
    }

    public String toString(Object object, ConverterContext context) {
        if (Boolean.FALSE.equals(object)) {
            return "No";
        }
        else if (Boolean.TRUE.equals(object)) {
            return "Yes";
        }
        else {
            return "";
        }
    }

    public boolean supportToString(Object object, ConverterContext context) {
        return true;
    }

    public Object fromString(String string, ConverterContext context) {
        if (string.equalsIgnoreCase("Yes")) {
            return Boolean.TRUE;
        }
        else if (string.equalsIgnoreCase("Y")) { // in case the application runs under different locale, we still condier "true" is true.
            return Boolean.TRUE;
        }
        else if (string.equalsIgnoreCase("No")) {
            return Boolean.FALSE;
        }
        else if (string.equalsIgnoreCase("N")) { // in case the application runs under different locale, we still condier "false" is false.
            return Boolean.FALSE;
        }
        else {
            return null;
        }
    }

    public boolean supportFromString(String string, ConverterContext context) {
        return true;
    }
}
