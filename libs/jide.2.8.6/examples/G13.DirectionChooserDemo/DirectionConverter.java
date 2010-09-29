/*
 * @(#)DirectionConverter.java 3/27/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.converter.ConverterContext;
import com.jidesoft.converter.ObjectConverter;

import javax.swing.*;

/**
 */
public class DirectionConverter implements ObjectConverter {

    public static final ConverterContext CONTEXT = new ConverterContext("Direction");
    static final String[] DIRECTIONS = new String[]{
            "From Top",
            "From Top-Right",
            "From Right",
            "From Bottom-Right",
            "From Bottom",
            "From Bottom-Left",
            "From Left",
            "From Top-Left",
    };

    public String toString(Object object, ConverterContext context) {
        if (object instanceof Integer) {
            int d = (Integer) object;
            if (d >= 1 && d <= 8) {
                return DIRECTIONS[d - 1];
            }
        }
        return "";
    }

    public boolean supportToString(Object object, ConverterContext context) {
        return true;
    }

    public Object fromString(String string, ConverterContext context) {
        for (int i = 0; i < DIRECTIONS.length; i++) {
            String direction = DIRECTIONS[i];
            if (direction.equals(string)) {
                return i + 1;
            }
        }
        return SwingConstants.SOUTH;
    }

    public boolean supportFromString(String string, ConverterContext context) {
        return true;
    }
}
