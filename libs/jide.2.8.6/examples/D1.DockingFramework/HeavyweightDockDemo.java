/*
 * @(#)HeavyweightDockDemo.java 10/27/2005
 *
 * Copyright 2002 - 2005 JIDE Software Inc. All rights reserved.
 */

import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.plaf.LookAndFeelFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Demoed Component: {@link com.jidesoft.wizard.WizardDialog} <br> Required jar files: jide-common.jar,
 * jide-components.jar, jide-dialogs.jar <br> Required L&F: Any L&F
 */
public class HeavyweightDockDemo extends AbstractDemo {
    public String getName() {
        return "Docking Framework (Heavyweight Component) Demo";
    }

    public String getProduct() {
        return PRODUCT_NAME_DOCK;
    }

    @Override
    public String getDescription() {
        return "JIDE Docking Framework provides a very powerful yet easy-to-use dockable window solution as you can find in Visual Studio .NET IDE or Eclipse IDE. Dockable window is proved to be the most effective solution to the limited screen space problem and complex window layout. Since its first release at the end of 2002, JIDE Docking Framework has been chosen by hundreds of companies all over the world. It is no doubt the best dockable window solution currently available in the market.\n" +
                "\nBy taking advantage of Swing pluggable look and feel technology, we made several flavors of JIDE Docking Framework. In current release, we include Office 2003-like LookAndFeel, Visual Studio .NET-like LookAndFeel, Eclipse-like LookAndFeel, Aqua-like LookAndFeel (Mac OS X) and the default Swing Metal LookAndFeel. You can always find a style you like the most.\n" +
                "\n" +
                "Demoed classes:\n" +
                "com.jidesoft.dock.DockableFrame\n" +
                "com.jidesoft.dock.DefaultDockingManager\n" +
                "and a lot of more.\n";
    }

    public Component getDemoPanel() {
        ButtonPanel buttonPanel = new ButtonPanel(SwingConstants.TOP);
        buttonPanel.addButton(new JButton(new AbstractAction("Launch Heavyweight Panel and Canvas Demo") {
            public void actionPerformed(ActionEvent e) {
                HeavyweightDockingFrameworkDemo.showDemo(false);
            }
        }));

        return buttonPanel;
    }

    @Override
    public Component getOptionsPanel() {
        return null;
    }

    @Override
    public String getDemoFolder() {
        return "D1.DockingFramework";
    }

    @Override
    public String[] getDemoSource() {
        return new String[]{"HeavyweightDockingFrameworkDemo.java"};
    }

    public static void main(String[] args) {
        LookAndFeelFactory.installDefaultLookAndFeelAndExtension();
        showAsFrame(new HeavyweightDockDemo());
    }
}
