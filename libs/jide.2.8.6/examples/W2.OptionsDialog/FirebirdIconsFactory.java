/*
 * @(#)SampleIconsFactory.java
 *
 * Copyright 2002-2003 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.icons.IconsFactory;

import javax.swing.*;

/**
 * A helper class to contain icons for demo of JIDE products.
 */
public class FirebirdIconsFactory {

    public static class Options {
        public static final String GENERAL = "firebird/general.png";
        public static final String PRIVACY = "firebird/privacy.png";
        public static final String WEB = "firebird/web.png";
        public static final String THEMES = "firebird/themes.png";
        public static final String EXTENSIONS = "firebird/extensions.png";
        public static final String FONTSCOLOR = "firebird/fontscolor.png";
        public static final String CONNECTION = "firebird/connection.png";
    }

    public static ImageIcon getImageIcon(String name) {
        if (name != null)
            return IconsFactory.getImageIcon(FirebirdIconsFactory.class, name);
        else
            return null;
    }

    public static void main(String[] argv) {
        IconsFactory.generateHTML(FirebirdIconsFactory.class);
    }


}
