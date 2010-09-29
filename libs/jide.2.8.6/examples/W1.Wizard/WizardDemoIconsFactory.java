/*
 * @(#)WizardIconsFactory.java
 *
 * Copyright 2002-2003 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.icons.IconsFactory;

import javax.swing.*;

/**
 * A helper class to contain icons for demo of JIDE products. Those icons are copyrighted by JIDE Software, Inc.
 */
public class WizardDemoIconsFactory {

    public static class Wizard97 {
        public static final String SAMPLE_IMAGE_SMALL = "icons/welcome_wizard97.png";
        public static final String SAMPLE_IMAGE_LARGE = "icons/welcome_wizard97_big.png";
    }

    public static class Metal {
        public static final String SAMPLE_IMAGE_SMALL = "icons/welcome_metal.png";
        public static final String SAMPLE_IMAGE_LARGE = "icons/welcome_wizard97_big.png";
    }

    public static ImageIcon getImageIcon(String name) {
        if (name != null)
            return IconsFactory.getImageIcon(WizardDemoIconsFactory.class, name);
        else
            return null;
    }

    public static void main(String[] argv) {
        IconsFactory.generateHTML(WizardDemoIconsFactory.class);
    }


}
