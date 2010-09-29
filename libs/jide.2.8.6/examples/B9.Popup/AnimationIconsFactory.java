/*
 * @(#)AnimationIconsFactory.java 3/26/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.icons.IconsFactory;

import javax.swing.*;

/**
 * A helper class to contain icons for demo of JIDE products. Those icons are copyrighted by JIDE Software, Inc.
 */
public class AnimationIconsFactory {

    public static class Location {
        public static final String PIC = "icons/picture.png";
    }

    public static class Direction {
        public static final String N = "icons/n.png";
        public static final String E = "icons/e.png";
        public static final String S = "icons/s.png";
        public static final String W = "icons/w.png";
        public static final String NW = "icons/nw.png";
        public static final String NE = "icons/ne.png";
        public static final String SW = "icons/sw.png";
        public static final String SE = "icons/se.png";
    }

    public static ImageIcon getImageIcon(String name) {
        if (name != null)
            return IconsFactory.getImageIcon(AnimationIconsFactory.class, name);
        else
            return null;
    }

    public static void main(String[] argv) {
        IconsFactory.generateHTML(AnimationIconsFactory.class);
    }
}
