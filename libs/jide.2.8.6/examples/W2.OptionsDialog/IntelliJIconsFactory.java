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
public class IntelliJIconsFactory {

    public static class ProjectOptions {
        public static final String PATHS = "idea/general/configurableProjectPaths.png";
        public static final String COMPILER = "idea/general/configurableCompiler.png";
        public static final String RUNDEBUG = "idea/general/configurableRunDebug.png";
        public static final String DEBUGGER = "idea/general/configurableDebugger.png";
        public static final String LOCALVCS = "idea/general/configurableLocalVCS.png";
        public static final String VCSSUPPORT = "idea/general/configurableVcs.png";
        public static final String WEB = "idea/general/configurableWeb.png";
        public static final String EJB = "idea/general/configurableEjb.png";
        public static final String JAVADOC = "idea/general/configurableJavadoc.png";
        public static final String MISCELLANEOUS = "idea/general/configurableMisc.png";
    }

    public static ImageIcon getImageIcon(String name) {
        if (name != null)
            return IconsFactory.getImageIcon(IntelliJIconsFactory.class, name);
        else
            return null;
    }

    public static void main(String[] argv) {
        IconsFactory.generateHTML(IntelliJIconsFactory.class);
    }


}
