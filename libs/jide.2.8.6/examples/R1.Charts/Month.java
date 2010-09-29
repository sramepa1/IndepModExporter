/*
 * @(#)Month.java 8/22/2009
 *
 * 2002 - 2010 JIDE Software Inc. All rights reserved.
 * Copyright 2005 - 2010 Catalysoft Ltd. All rights reserved.
 */

import java.awt.*;

public enum Month {
    January("Jan", Color.green.darker().darker()),
    February("Feb", Color.blue),
    March("Mar", Color.cyan),
    April("Apr", Color.darkGray),
    May("May", Color.gray),
    June("Jun", Color.green),
    July("Jul", Color.magenta),
    August("Aug", Color.orange),
    September("Sep", Color.pink),
    October("Oct", Color.red),
    November("Nov", Color.yellow),
    December("Dec", Color.lightGray);

    private String name;
    private Color color;

    private Month(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public static Month getMonth(int index) {
        return values()[index - 1];
    }

    public int index() {
        return 1 + ordinal();
    }

    @Override
    public String toString() {
        return name;
    }
}
