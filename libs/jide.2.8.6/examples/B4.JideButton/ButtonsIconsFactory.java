/*
 * @(#)ButtonsIconsFactory.java
 *
 * Copyright 2002-2003 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.icons.IconsFactory;

import javax.swing.*;

/**
 * A helper class to contain icons for demo of JIDE products. Those icons are copyrighted by JIDE Software, Inc.
 */
public class ButtonsIconsFactory {

    public static class Buttons {
        public static final String RENAME = "icons/e_rename.png";
        public static final String MOVE = "icons/e_move.png";
        public static final String COPY = "icons/e_copy.png";
        public static final String PUBLISH = "icons/e_publish.png";
        public static final String EMAIL = "icons/e_email.png";
        public static final String DELET = "icons/e_delete.png";
    }

    public static ImageIcon getImageIcon(String name) {
        if (name != null)
            return IconsFactory.getImageIcon(ButtonsIconsFactory.class, name);
        else
            return null;
    }

    public static void main(String[] argv) {
        IconsFactory.generateHTML(ButtonsIconsFactory.class);
    }


}
